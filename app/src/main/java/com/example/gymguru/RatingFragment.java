package com.example.gymguru;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.gymguru.databinding.RatingFragmentBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RatingFragment extends DialogFragment {

    private RatingFragmentBinding bind;
    private String videoId;
    private String uid;
    float rating;
    private DatabaseReference reference;
    private FirebaseUser user;
    long averagerating = 0;


    @NonNull

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rating_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = RatingFragmentBinding.bind(view);


        reference = FirebaseDatabase.getInstance().getReference("videos").child("rating");

        reference.get().addOnSuccessListener(dataSnapshot -> {
            long count = dataSnapshot.getChildrenCount(); // count = 2
            float totalrating = 0;
            for(DataSnapshot child : dataSnapshot.getChildren()){
                int rating = (int)child.child("rating").getValue(); // rate = 4,4,5,3,3,4
                totalrating += rating; // total rating = 23
            }
            averagerating = (long) (totalrating/count); // average rating = 23/6
            bind.ratingText.setText(String.valueOf(averagerating)); // average rating = 3.8

        });

        bind.btnSubmit.setOnClickListener(v -> {
            DatabaseReference push = FirebaseDatabase.getInstance().getReference("videos").child(videoId).child("ratings").push();
            HashMap<String,Object> data = new HashMap<>();
            data.put("uid",uid);
            data.put("rating",rating);
            push.updateChildren(data);
            Toast.makeText(getActivity(), "Thanks for rating me"+rating, Toast.LENGTH_SHORT).show();
        });
        bind.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            this.rating = rating;
        });

    }
    //Setters
    public void setVideoId(String videoId){
        this.videoId = videoId;
    }

    public void setUserId(String uid){
        this.uid = uid;
    }
}

