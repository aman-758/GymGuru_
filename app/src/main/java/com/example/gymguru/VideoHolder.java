package com.example.gymguru;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class VideoHolder extends RecyclerView.ViewHolder {
    private static final int PERMISSION_STORAGE_CODE = 1000;
    View mView;
    SimpleExoPlayer exoPlayer;
    private PlayerView mExoplayerView;
    private ImageView like,dislike,follow,download,save;

    public VideoHolder( View itemView) {
        super(itemView);
        mView = itemView;
    }

    // This is the function where i control all the function of row.xml

    public void initui(final Application ctx, UploadMember model, int position, String videoId){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Like function(incomplete)
        like = mView.findViewById(R.id.like_btn);
        like.setOnClickListener(v -> {

            FirebaseDatabase.getInstance().getReference("video_likes").child(videoId).setValue
                    (new VideoLike(uid,"like"),(error, ref) -> {
                       if(error != null){
                           Snackbar.make(mView,"Error", BaseTransientBottomBar.LENGTH_LONG).show();
                       }else{
                           //replace color of like button
                           Snackbar.make(mView,"Added to Liked videos",BaseTransientBottomBar.LENGTH_LONG).show();
                       }
                    });
        });

        //Dislike function(incomplete)
        dislike = mView.findViewById(R.id.btnDislike);
        dislike.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference("video_dislikes").child(videoId).setValue
                    (new VideoDislike(uid,"Dislike"),(error, ref) -> {
                       if(error!= null){
                           Snackbar.make(mView,"Error",BaseTransientBottomBar.LENGTH_LONG).show();
                       }else{
                            Snackbar.make(mView,"You Disliked this video",BaseTransientBottomBar.LENGTH_LONG).show();
                       }
                    });
        });

        //Follow function(incomplete)
        follow = mView.findViewById(R.id.FollowUser);
        follow.setOnClickListener(v -> {

              FirebaseDatabase.getInstance().getReference("Followers").child(model.uploaderId).
                    setValue(new FollowModel(uid,model.uploaderId),(error, ref) -> {

                        if(error!= null){
                            Snackbar.make(mView,"Error",BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                        else{
                            Snackbar.make(mView,"Added to follow list",BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    });
        });

        //Download Function(complete)
        download = mView.findViewById(R.id.btnDownload);
        download.setOnClickListener(v -> {
            Download(mView.getContext(),model.url);

        });

    }

    private void Download(Context context, String url) {
        Toast.makeText(context, "Video Download Start", Toast.LENGTH_SHORT).show();
        downloadVideos(mView.getContext().getApplicationContext(),"GymVideos", ".mp4", DIRECTORY_DOWNLOADS, url);
    }

    private void downloadVideos(Context context, String videoName, String videoExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, videoName+videoExtension);

        downloadManager.enqueue(request);
    }

    public void setVideo(final Application ctx, UploadMember model, int position){

        TextView mTextView = mView.findViewById(R.id.titletv);
        mExoplayerView = mView.findViewById(R.id.exoplayer_view);


        mTextView.setText(model.getTitle());


        // Now i am using try and catch method for retrieving the videos

        try{
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(ctx).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(ctx);
            Uri video = Uri.parse(model.getUrl());
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null,
                    null);
            mExoplayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false); // false because if we do this then all video will run simultaneously
            exoPlayer.addListener(new EventListener() {
                @Override
                public void onIsPlayingChanged(boolean isPlaying) { // This function runs when user tap play button on the exoplayer

                    if(isPlaying){ // Check if user tap play button
                        Toast.makeText(ctx,""+model.title,Toast.LENGTH_LONG).show();  //model populates title

                    }
                }

            });
        } catch (Exception e){
            Log.e("VideoHolder", "exoplayer error" + e.toString());
        }
    }
    public boolean isPlaying(ExoPlayer exoPlayer) {
        return exoPlayer.getPlaybackState() == Player.STATE_READY && exoPlayer.getPlayWhenReady();
    }
}
