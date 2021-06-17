package com.example.gymguru;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.gymguru.databinding.RatingFragmentBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.HashMap;

public class RatingFragment extends DialogFragment {

    private RatingFragmentBinding bind;
    private String videoId;
    private String uid;
    float rating;
    private DatabaseReference reference;
    private FirebaseUser user;
    float averagerating = 0;
    private Animation showDialog;

    @NonNull

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        averagerating = arguments.getFloat("averagerating",0);
        videoId = arguments.getString("videoId",videoId);
        uid = arguments.getString("uid",uid);

        return super.onCreateDialog(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rating_fragment, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = RatingFragmentBinding.bind(view);
        //here i am using decimal format for showing 2 decimal places ratings

        //code for animation
        showDialog = AnimationUtils.loadAnimation(getActivity(),R.anim.fadein);
        //set element on animation
        bind.showDialog.setAnimation(showDialog);


        bind.btnSubmit.setOnClickListener(v -> {
            DatabaseReference push = FirebaseDatabase.getInstance().getReference("videos").child(videoId).child("ratings").push();
            HashMap<String,Object> data = new HashMap<>();
            data.put("uid",uid);
            data.put("rating",rating);
            push.updateChildren(data);
            Toast.makeText(getActivity(), "Thanks for rating me "+rating, Toast.LENGTH_SHORT).show();
        });
        bind.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            this.rating = rating;
        });

    }

}

