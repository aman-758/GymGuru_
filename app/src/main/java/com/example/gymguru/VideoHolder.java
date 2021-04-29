package com.example.gymguru;

import android.app.Application;
import android.net.Uri;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
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
    public void setVideo(final Application ctx, String title, final String url){
        TextView mTextView = mView.findViewById(R.id.titletv);
        mExoplayerView = mView.findViewById(R.id.exoplayer_view);

        mTextView.setText(title);

        // Now i am using try and catch method for retrieving the videos

        try{
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(ctx).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(ctx);
            Uri video = Uri.parse(url);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null,
                    null);
            mExoplayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false); // false because if we do this then all video will run simultaneously

        } catch (Exception e){
            Log.e("VideoHolder", "exoplayer error" + e.toString());
        }
    }
}
