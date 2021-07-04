package com.example.gymguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.gymguru.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding bind;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference reference = databaseReference.child("Users");
    private String userId;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    Animation profileAnim;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentProfileBinding.bind(view);
        //code for animation
        profileAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down);
        //set animation on elements
        bind.profileAnim.setAnimation(profileAnim);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RegistrationModel registrationModel = snapshot.getValue(RegistrationModel.class);
                if(registrationModel!=null) {
                    String username = registrationModel.username;
                    String usertype = registrationModel.userType;
                    if(registrationModel.imageUrl!=null) {
                        Glide.with(getActivity()).load(registrationModel.getImageUrl())
                                .centerCrop().placeholder(R.drawable.ic_baseline_account_circle_24).into(bind.img);
                    }

                    bind.profileUser.setText(username);
                    if(registrationModel.channelName!=null) {
                        bind.profileUserType.setText(usertype);
                    }else{
                        bind.profileUserType.setText("Viewer");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle the error
            }
        });

        bind.editProfile.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.action_profileFragment_to_editProfileFragment);
        });
        bind.storage.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "This feature under construction!", Toast.LENGTH_SHORT).show();
        });
        bind.notification.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "This feature under construction!", Toast.LENGTH_SHORT).show();
        });
        bind.watchHistory.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "This feature under construction!", Toast.LENGTH_SHORT).show();
        });
        bind.downloadCard.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "This feature under construction!", Toast.LENGTH_SHORT).show();
        });
        bind.watchLater.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "This feature under construction!", Toast.LENGTH_SHORT).show();
        });
    }
}
