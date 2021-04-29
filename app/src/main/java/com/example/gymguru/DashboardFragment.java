package com.example.gymguru;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.LoginFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gymguru.databinding.FragmentDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DashboardFragment extends Fragment {

    private FirebaseAuth auth;
    private FragmentDashboardBinding bind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentDashboardBinding.bind(view);
        if(auth.getCurrentUser() != null)
        {
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
        }
    }
}