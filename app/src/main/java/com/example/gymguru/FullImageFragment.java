package com.example.gymguru;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.gymguru.databinding.FragmentFullImageBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FullImageFragment extends DialogFragment {

    private FragmentFullImageBinding bind;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference reference = databaseReference.child("Users");
    private String userId;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentFullImageBinding.bind(view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RegistrationModel registrationModel = snapshot.getValue(RegistrationModel.class);

                if(registrationModel != null) {
                    String userImage = registrationModel.username;

                    bind.userImage.setText(userImage);

                    Glide.with(getActivity()).load(registrationModel.getImageUrl()).fitCenter().centerCrop()
                            .placeholder(R.drawable.ic_baseline_account_circle_24).into(bind.fullImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(bind.getRoot(),"Something went wrong", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
        bind.backArrow.setOnClickListener(v -> {
            NavHostFragment.findNavController(FullImageFragment.this).navigate(R.id.action_fullImageFragment_to_editProfileFragment);
        });
    }
}