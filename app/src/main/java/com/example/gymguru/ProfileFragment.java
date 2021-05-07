package com.example.gymguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gymguru.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding bind;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentProfileBinding.bind(view);
        bind.editProfile.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.action_profileFragment_to_editProfileFragment);
        });
    }

}