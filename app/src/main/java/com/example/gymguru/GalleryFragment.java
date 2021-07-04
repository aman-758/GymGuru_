package com.example.gymguru;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gymguru.databinding.FragmentGalleryBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding bind;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    private DatabaseReference users;
    ArrayList<RegistrationModel> trainerList, followList;
    private Animation galleryAnim;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentGalleryBinding.bind(view);

        //code for animation
        galleryAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.dash_sidetext);
        //set animation on element
        bind.galleryAnim.setAnimation(galleryAnim);

        bind.recyclerViewImg.setHasFixedSize(true);
        bind.recyclerViewImg.setLayoutManager(new LinearLayoutManager(getActivity()));
        trainerList = new ArrayList<>();
        GalleryAdapter galleryAdapter = new GalleryAdapter(getActivity(),trainerList,this);
        bind.recyclerViewImg.setAdapter(galleryAdapter);
        users.get().addOnSuccessListener(dataSnapshot -> {
            trainerList.clear();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                RegistrationModel registrationModel = child.getValue(RegistrationModel.class);
                if (registrationModel != null && registrationModel.userType.equals("Gym Trainer")) {
                    trainerList.add(registrationModel);
                    Log.d("Info",registrationModel.getUserType());
                }
                bind.recyclerViewImg.getAdapter().notifyDataSetChanged();

            }
        }).addOnFailureListener(e -> {
            Snackbar.make(bind.getRoot(), e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
        });
    }
}