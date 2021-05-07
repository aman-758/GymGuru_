package com.example.gymguru;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
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

public class VideoHolder extends RecyclerView.ViewHolder {

    View mView;
    SimpleExoPlayer exoPlayer;
    private PlayerView mExoplayerView;


    public VideoHolder( View itemView) {
        super(itemView);
        mView = itemView;

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
                        Toast.makeText(ctx,""+model.title,Toast.LENGTH_LONG).show(); //model populates title

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
