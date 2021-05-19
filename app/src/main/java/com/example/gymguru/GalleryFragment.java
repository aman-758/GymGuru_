package com.example.gymguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gymguru.databinding.FragmentGalleryBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding bind;
    FirebaseDatabase database;

    private FirebaseAuth mAuth;
    private DatabaseReference users,followers;
    ArrayList<RegistrationModel> trainerList,followList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth =FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        followers = database.getReference("Followers");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentGalleryBinding.bind(view);
        bind.recyclerViewImg.setHasFixedSize(true);
        bind.recyclerViewImg.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*FirebaseRecyclerOptions<RegistrationModel,FollowModel> options =
                new FirebaseRecyclerOptions.Builder<RegistrationModel,FollowModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), RegistrationModel.class,FollowModel.class)
                        .build();*/

        database = FirebaseDatabase.getInstance();

        trainerList = new ArrayList<>();
        users.get().addOnSuccessListener(dataSnapshot -> {
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                   RegistrationModel registrationModel = child.getValue(RegistrationModel.class);
                   if(registrationModel.userType.equals("Gym Trainer")){
                       trainerList.add(registrationModel);
                   }
            }
        }).addOnFailureListener(e -> {
            Snackbar.make(bind.getRoot(),e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
        });

        /*followList = new ArrayList<>();
        followers.get().addOnSuccessListener(dataSnapshot -> {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    FollowModel followModel = child.getValue(FollowModel.class);
                    if(.userType.equals("Viewer")){
                        followList.add(registrationModel);
                    }
                }
        }).addOnFailureListener(e -> {
           Snackbar.make(bind.getRoot(),e.getMessage(),BaseTransientBottomBar.LENGTH_LONG).show();

        });*/
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<RegistrationModel, GalleryHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<RegistrationModel, GalleryHolder>(
                        RegistrationModel.class,
                        R.layout.row_gallery,
                        GalleryHolder.class,
                        users
        )

        {

            @Override
            protected void populateViewHolder(GalleryHolder holder, RegistrationModel registrationModel, int i) {
                if(registrationModel.getUserType().equals("Gym Trainer")) {


                    holder.setTrainer(getActivity(), registrationModel, i);
                }
            }
        };
        bind.recyclerViewImg.setAdapter(firebaseRecyclerAdapter);
    }
}