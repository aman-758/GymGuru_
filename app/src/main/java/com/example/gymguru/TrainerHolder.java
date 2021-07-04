package com.example.gymguru;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
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
import com.google.firebase.database.DatabaseReference;

public class TrainerHolder extends RecyclerView.ViewHolder {

    View mView;
    SimpleExoPlayer exoPlayer;
    TextView mTextView,timeText;
    ProgressBar progressBar;
    private PlayerView mExoplayerView;
    PlayerView exoplayerGalView;
    DatabaseReference tuid;


    public TrainerHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setTrainerVideo(final Application ctx, UploadMember uploadMember, int i){

        mTextView = mView.findViewById(R.id.galTitle);
        timeText = mView.findViewById(R.id.textGalTime);
        mExoplayerView = mView.findViewById(R.id.exoplayerGal_view);
        progressBar = mView.findViewById(R.id.progressGal_bar);


        exoplayerGalView = mView.findViewById(R.id.exoplayerGal_view);

        mTextView.setText(uploadMember.getTitle());
        timeText.setText(uploadMember.getDate());

        try{
            LoadControl loadControl = new DefaultLoadControl();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(ctx).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(ctx,trackSelector,loadControl);
            Uri video = Uri.parse(uploadMember.getUrl());
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video"); //factory = dataSourceFactory
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null,
                    null);

            mExoplayerView.setPlayer(exoPlayer);
            mExoplayerView.setKeepScreenOn(true); //keep screen on
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);
        }catch (Exception e){
            Log.e("VideoHolder", "exoplayer error" + e.toString());
        }
    }
}
