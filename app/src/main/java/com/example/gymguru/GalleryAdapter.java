package com.example.gymguru;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class GalleryAdapter extends RecyclerView.ViewHolder {
    View view;
    ImageView imgView;
    TextView channelName, numberVideos, numberFollowers, channelRatings;

    // Creating the constructor of the GalleryAdapter class
    public GalleryAdapter(View mView){
        super(mView);
        view = mView;

    }
    public void getTrainer(RegistrationModel model, int i){
        imgView = view.findViewById(R.id.profileImg);
        channelName = view.findViewById(R.id.channelText);
        numberVideos = view.findViewById(R.id.numberVideos);
        numberFollowers = view.findViewById(R.id.numberFollow);
        channelRatings = view.findViewById(R.id.channelRating);

        channelName.setText(model.username);

    }
}
