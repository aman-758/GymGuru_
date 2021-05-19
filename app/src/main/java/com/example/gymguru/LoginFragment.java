package com.example.gymguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gymguru.databinding.FragmentLoginBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    private FirebaseAuth auth;
    private FragmentLoginBinding bind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentLoginBinding.bind(view);

        bind.btnLogin.setOnClickListener(v -> {
            String email = bind.editEmail.getText().toString();
            String password = bind.editPassword.getText().toString();
            if(email.length() > 10)
            {
                if(password.length() >= 8)
                {
                    bind.plogin.setVisibility(View.VISIBLE); // Show the progress bar
                    auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
                        updateUI(authResult.getUser());
                    }).addOnFailureListener(e -> {
                        Snackbar.make(bind.getRoot(),"error:" +e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                        updateUI(null);
                    });
                }
                else
                {
                    bind.editPassword.setError("Password Invalid!");
                    bind.editPassword.requestFocus(); // Cursor focus on password box
                    updateUI(null);
                }
            }
            else
            {
                bind.editEmail.setError("Email Invalid!");
                bind.editEmail.requestFocus();
                updateUI(null);
            }
        });
        bind.regUser.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_registerFragment2);
        });
        bind.forgot.setOnClickListener(view2 -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_forgotFragment);
        });
    }

    private void updateUI(FirebaseUser user)
    {
        bind.plogin.setVisibility(View.GONE);
        if(user!=null)
        {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_dashboardFragment);
        }
    }

    public void onStart(){

        super.onStart();
        // logic dhakke nikal ke bahr nikalne ka
        if(auth.getCurrentUser()!=null) {
            updateUI(auth.getCurrentUser());
        }else {
        }
    }
}