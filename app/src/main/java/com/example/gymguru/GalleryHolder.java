package com.example.gymguru;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class GalleryHolder extends RecyclerView.ViewHolder {
    View mView;
    TextView channelName, numberVideos, numberFollowers, channelRating;
    ImageView trainerImg;



    public GalleryHolder(View itemView){
        super(itemView);
        mView = itemView;
    }

    @SuppressLint("ResourceType")
    public void setTrainer(Context ct, RegistrationModel registrationModel, int i) {
        channelName = mView.findViewById(R.id.channelText);

        channelName.setText(registrationModel.getChannelName());

    }
}
