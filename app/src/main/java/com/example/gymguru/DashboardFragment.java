package com.example.gymguru;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gymguru.databinding.FragmentDashboardBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DashboardFragment extends Fragment {

    private FirebaseAuth auth;
    private FragmentDashboardBinding bind;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String UserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind = FragmentDashboardBinding.bind(view);
        if (auth.getCurrentUser() != null) {
            bind.greet.setText(auth.getCurrentUser().getDisplayName());
            bind.cardLogout.setOnClickListener(v -> {
                auth.signOut();
                NavHostFragment.findNavController(this).navigateUp();
            });
            bind.userProfileCard.setOnClickListener(view1 -> {
                NavHostFragment.findNavController(DashboardFragment.this).navigate(R.id.action_dashboardFragment_to_profileFragment);
            });
            bind.cardUpload.setOnClickListener(v -> {
                NavHostFragment.findNavController(DashboardFragment.this).navigate(R.id.action_dashboardFragment_to_uploadFragment);
            });
            bind.cardHome.setOnClickListener(v -> {
                NavHostFragment.findNavController(DashboardFragment.this).navigate(R.id.action_dashboardFragment_to_homeFragment);
            });
            bind.cardGallery.setOnClickListener(v -> {
                NavHostFragment.findNavController(DashboardFragment.this).navigate(R.id.action_dashboardFragment_to_galleryFragment);
            });
            bind.cardExit.setOnClickListener(v -> {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Exit");
                builder.setMessage("Do you want to exit?");
                builder.setPositiveButton("Yes,Exit Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // dialog is the object of DialogInterface
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            });
            UserId = user.getUid();
            reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    RegistrationModel registrationModel = snapshot.getValue(RegistrationModel.class);
                    if(registrationModel != null){
                        String username = registrationModel.username;

                        bind.greet.setText(username);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Snackbar.make(bind.getRoot(),"Something went wrong", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            });

        }

    }
}