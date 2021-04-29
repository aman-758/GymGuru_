package com.example.gymguru;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gymguru.databinding.FragmentRegisterBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterFragment extends Fragment {

    private FirebaseAuth auth;
    private FragmentRegisterBinding bind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentRegisterBinding.bind(view);
        bind.btnReg.setOnClickListener(v -> {
            String name = bind.editName.getText().toString();
            String email = bind.editUemail.getText().toString();
            String password = bind.editUpass.getText().toString();
            String cPass = bind.cPass.getText().toString();
            String age = bind.editAge.getText().toString();
            bind.progReg.setVisibility(View.VISIBLE);
            if (!name.trim().isEmpty()) {
                if (!age.trim().isEmpty()) {
                    if (email.length() >= 10) {
                        if (password.length() >= 8) {
                            if (cPass.equals(password)) {
                                auth.createUserWithEmailAndPassword(email, password)
                                        .addOnSuccessListener(authResult -> {
                                            FirebaseUser user = authResult.getUser();
                                            UserProfileChangeRequest req = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                            user.updateProfile(req);
                                            updateUI(user);
                                        })
                                        .addOnFailureListener(e -> {
                                            Snackbar.make(bind.getRoot(), e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                                            updateUI(null);
                                        });
                            } else {
                                bind.cPass.setError("Password do not match!");
                                bind.cPass.requestFocus();
                                updateUI(null);
                            }
                        } else {
                            bind.editUpass.setError("Invalid Password!");
                            bind.editUpass.requestFocus();
                            updateUI(null);
                        }
                    } else {
                        bind.editUemail.setError("Invalid Email!");
                        bind.editUemail.requestFocus();
                        updateUI(null);
                    }
                } else {
                    bind.editAge.setError("Age must be provided!");
                    bind.editAge.requestFocus();
                    updateUI(null);
                }
            } else{
                bind.editName.setError("Name must be provided!");
                bind.editName.requestFocus();
                updateUI(null);
            }

        });

    }

    private void updateUI(FirebaseUser user)
    {
        if(user!= null)
        {
            NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_dashboardFragment);
        }
        bind.progReg.setVisibility(View.GONE);
    }

}