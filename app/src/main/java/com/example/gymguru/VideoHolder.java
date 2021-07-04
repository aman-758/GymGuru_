package com.example.gymguru;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class VideoHolder extends RecyclerView.ViewHolder {
    private static final int PERMISSION_STORAGE_CODE = 1000;
    private FirebaseAuth auth;
    ArrayList<RegistrationModel>trainerList;
    FirebaseDatabase database;
    private DatabaseReference users;
    View mView;
    Button rating;
    TextView textRating;
    TextView timeText,channelName;
    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btFullScreen;
    boolean flag = false;
    SimpleExoPlayer exoPlayer;
    private PlayerView mExoplayerView;
    ImageButton likeButton, dislikeButton, followButton;
    ImageView download,comment,channelImg;
    TextView likesdisplay, dislikesdisplay, followsdisplay;
    DatabaseReference likesref, dislikeref, followsref;
    int likesCount, dislikesCount, followCount;
    float averagerating = 0;

    public VideoHolder( View itemView) {
        super(itemView);
        mView = itemView;

        //this is for deleting the video
        itemView.setOnClickListener(v -> {
            mClickListener.onItemClick(v,getAdapterPosition());
        });
        itemView.setOnLongClickListener(v -> {
            mClickListener.onItemLongClick(v, getAdapterPosition());
            return false;
        });
    }

    // This is the function where i control all the object of row.xml
    public void initui(Context ctx, UploadMember model, int position, String videoId, FragmentManager fm){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        trainerList = new ArrayList<>();

        //Rating System
        rating = mView.findViewById(R.id.ratingBtn);
        textRating = mView.findViewById(R.id.ratingText);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("videos").child(videoId).child("ratings");

        reference.get().addOnSuccessListener(dataSnapshot -> {
            long count = dataSnapshot.getChildrenCount(); // count = 2
            float totalrating = 0;
            for(DataSnapshot child : dataSnapshot.getChildren()){
                double rating = Double.parseDouble(String.valueOf(child.child("rating").getValue())); // rate = 4,4,5,3,3,4
                totalrating += rating; // total rating = 23
            }
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);

            averagerating = (float) (totalrating/count); // average rating = 23/6
            textRating.setText(df.format(averagerating));

        });

        rating.setOnClickListener(v ->  {

            RatingFragment rating = new RatingFragment();
            Bundle args = new Bundle();
            args.putString("videoId",videoId);
            args.putString("uid",uid);
            args.putFloat("averagerating",averagerating);
            rating.setArguments(args);

            rating.show (fm,"Rating");

        });
        //Comment
        comment = mView.findViewById(R.id.comment_img); //Here i passing through the reference further for recycler view

        //Fetching trainer details on the video section
        channelName = mView.findViewById(R.id.channelName);
        channelImg = mView.findViewById(R.id.channelImg);

        users.child(model.uploaderId).get().addOnSuccessListener(dataSnapshot -> {
            RegistrationModel registrationModel = dataSnapshot.getValue(RegistrationModel.class);
            if (registrationModel != null && registrationModel.userType.equals("Gym Trainer")) {
                Glide.with(ctx).load(registrationModel.getImageUrl())
                        .fitCenter().centerCrop().placeholder(R.drawable.ic_baseline_account_circle_24).into(channelImg);
                channelName.setText(registrationModel.getChannelName());
                trainerList.add(registrationModel);
                Log.d("Info", registrationModel.getUserType());
                Log.d("Info",registrationModel.getUid());
                Log.d("Info",videoId);
            }

        }).addOnFailureListener(e -> {
            Snackbar.make(mView.getRootView(),e.getMessage(),BaseTransientBottomBar.LENGTH_LONG).show();
        });
        //Download Function
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



    @SuppressLint("ResourceType")
    public void setVideo(final Application ctx, UploadMember model, int position){
        TextView mTextView = mView.findViewById(R.id.titletv);
        timeText = mView.findViewById(R.id.textTime);
        mExoplayerView = mView.findViewById(R.id.exoplayer_view);
        progressBar = mView.findViewById(R.id.progress_bar);
        //btFullScreen = playerView.findViewById(R.id.bt_fullscreen);

        mTextView.setText(model.getTitle());
        timeText.setText(model.getDate());
        /*channelName.setText(registrationModel.getChannelName());*/

        // Now i am using try and catch method for retrieving the videos

        try{
            //getWindow().setFLags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); not working
            LoadControl loadControl = new DefaultLoadControl();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(ctx).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(ctx,trackSelector,loadControl);
            Uri video = Uri.parse(model.getUrl());
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video"); //factory = dataSourceFactory
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null,
                    null);
            mExoplayerView.setPlayer(exoPlayer);
            mExoplayerView.setKeepScreenOn(true); //keep screen on
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
            exoPlayer.addListener(new Player.EventListener(){
                @Override
                public void onTimelineChanged(Timeline timeline, @Nullable  Object manifest, int reason) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if(playbackState == Player.STATE_BUFFERING){
                        //When buffering jio ke net se check kr lenge
                        //Show progress bar
                        progressBar.setVisibility(View.VISIBLE);

                    }else if(playbackState == Player.STATE_READY){
                        //When ready
                        //Hide progress bar
                        progressBar.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onSeekProcessed() {

                }
            });

            btFullScreen.setOnClickListener(v -> {
                //Check condition
                if(flag){
                    //when flag is true
                    //set enter full screen Image
                    btFullScreen.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_baseline_fullscreen_24));
                    //Set portrait orientation
                    //
                    //Set flag value is false
                    flag = false;
                }else{
                    //when flag is false
                    //set exit full screen image
                    btFullScreen.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_baseline_fullscreen_exit_24));
                    //set landscape orientation
                    //
                    //set flag value is true
                    flag = true;
                }
            });

            //Initialize load control
            //Initialize bandwidth meter already initialized
            //Track selector already initialized

        } catch (Exception e){
            Log.e("VideoHolder", "exoplayer error" + e.toString());
        }
    }


    //For deleting the video
    private VideoHolder.Clicklistener mClickListener;
    public interface Clicklistener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(VideoHolder.Clicklistener clicklistener){
        mClickListener = clicklistener;
    }

    public boolean isPlaying(ExoPlayer exoPlayer) {
        return exoPlayer.getPlaybackState() == Player.STATE_READY && exoPlayer.getPlayWhenReady();

    }


    // Like Functionality
    public void setLikesButtonStatus(String postkey) {
        likeButton = mView.findViewById(R.id.like_btn);
        likesdisplay = mView.findViewById(R.id.like_text);
        likesref = FirebaseDatabase.getInstance().getReference("video_likes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        String likes = "";
        likesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                if(snapshot.child(postkey).hasChild(userId)){
                    likesCount = (int)snapshot.child(postkey).getChildrenCount();
                    likeButton.setImageResource(R.drawable.ic_baseline_favorite_24);
                    likesdisplay.setText(Integer.toString(likesCount)+likes); //It is for counting the likes
                }
                else{
                    likesCount = (int)snapshot.child(postkey).getChildrenCount();
                    likeButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    likesdisplay.setText(Integer.toString(likesCount)+likes); //It is for when viewer take back their like
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Snackbar.make(mView.getRootView(), error.getMessage(),BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

    }

    // Dislike Functionality
    public void setDislikeButtonStatus(String postkey) {
        dislikeButton = mView.findViewById(R.id.btnDislike);
        dislikesdisplay = mView.findViewById(R.id.dislikeText);
        dislikeref = FirebaseDatabase.getInstance().getReference("video_dislikes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        String dislikes = "";
        dislikeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(postkey).hasChild(userId)){
                    dislikesCount = (int)snapshot.child(postkey).getChildrenCount();
                    dislikeButton.setImageResource(R.drawable.ic_baseline_thumbdown_fill);
                    dislikesdisplay.setText(Integer.toString(dislikesCount)+dislikes); //It is for counting the dislikes
                }else{
                    dislikesCount = (int)snapshot.child(postkey).getChildrenCount();
                    dislikeButton.setImageResource(R.drawable.ic_baseline_thumb_down_off_alt_24);
                    dislikesdisplay.setText(Integer.toString(dislikesCount)+dislikes); //It is for take back their dislikes
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(mView.getRootView(),error.getMessage(),BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }

    //follow functionality

    public void setFollowButtonStatus(String postkey) {
        followButton = mView.findViewById(R.id.btnFollow);
        followsdisplay = mView.findViewById(R.id.followText);
        followsref = FirebaseDatabase.getInstance().getReference("Followers");
        DatabaseReference reference = database.getReference("videos");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userId = user.getUid();
        String follows = "";

        followsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                reference.child(postkey).get().addOnSuccessListener(dataSnapshot -> { //Here i got uploader id from the postkey
                    UploadMember model = dataSnapshot.getValue(UploadMember.class);
                    if(snapshot.child(model.uploaderId).hasChild(userId)){
                        followCount = (int)snapshot.child(model.uploaderId).getChildrenCount();
                        followButton.setImageResource(R.drawable.ic_baseline_person_add_alt_1_24);
                        followsdisplay.setText(Integer.toString(followCount)+follows); //It is for counting the follows
                    }else{
                        followCount = (int)snapshot.child(model.uploaderId).getChildrenCount();
                        followButton.setImageResource(R.drawable.ic_outline_person_add_alt_1_24);
                        followsdisplay.setText(Integer.toString(followCount)+follows); //It is for simply take back the follow, means user don't want to follow
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(mView.getRootView(),error.getMessage(),BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }
}