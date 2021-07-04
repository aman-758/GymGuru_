package com.example.gymguru;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private  ArrayList<RegistrationModel> trainers;
    private  Context ctx;
    Fragment fragment;
    FirebaseAuth auth;
    public GalleryAdapter(Context ctx, ArrayList<RegistrationModel> trainers, Fragment fragment){
        this.ctx = ctx;
        this.trainers = trainers;
        this.fragment = fragment;
    }
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        RegistrationModel registrationModel = trainers.get(position);
        viewHolder.bind(registrationModel, position);

    }

    @Override
    public int getItemCount() {
        return trainers.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(ctx).inflate(R.layout.row_gallery,
                parent, false);
        return new MyViewHolder(mItemView, this);

    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private final GalleryAdapter galleryAdapter;
        TextView trainer;
        ImageView img;
        public MyViewHolder(@NonNull View itemView, GalleryAdapter galleryAdapter) {
            super(itemView);
            this.galleryAdapter = galleryAdapter;
            trainer = itemView.findViewById(R.id.channelText);
            img = itemView.findViewById(R.id.trainerImg);
            itemView.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                RegistrationModel registrationModel = trainers.get(adapterPosition);
                GalleryFragmentDirections.ActionGalleryFragmentToTrainerVideos dir = GalleryFragmentDirections.actionGalleryFragmentToTrainerVideos(registrationModel.uid);
                NavHostFragment.findNavController(fragment).navigate(dir);

            });
        }

        public void bind(RegistrationModel registrationModel, int position) {
            trainer.setText(registrationModel.getChannelName());

            Glide.with(ctx).load(registrationModel.getImageUrl())
                    .fitCenter().centerCrop().placeholder(R.drawable.ic_baseline_account_circle_24).into(img);
        }
    }
}
