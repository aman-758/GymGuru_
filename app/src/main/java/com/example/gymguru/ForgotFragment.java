package com.example.gymguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gymguru.databinding.FragmentForgotBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotFragment extends Fragment {

    private FragmentForgotBinding bind;
    private FirebaseAuth auth;
    private Animation forgotAnim, forgotlogoAnim, forgotImgAnim;
    private Animation animationEditText;
    private Animation forgotButtin;
    private Animation forgotButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_forgot, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentForgotBinding.bind(view);
        //code for animation
        forgotAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down);
        forgotImgAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.sidelogo_anim);
        forgotlogoAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up);
        animationEditText = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up);
        forgotButton = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        //set animation on element
        bind.forgotShow.setAnimation(forgotAnim);
        bind.slideImg.setAnimation(forgotImgAnim);
        bind.forgotLogoShow.setAnimation(forgotlogoAnim);
        bind.forgotEmail.setAnimation(animationEditText);
        bind.cardForgot.setAnimation(forgotButton);

        bind.cardForgot.setOnClickListener(v -> {
            resetForgot();
        });
    }

    private void resetForgot() {
        String email = bind.forgotEmail.getText().toString();
        if(email.isEmpty()){
            bind.forgotEmail.setError("Email Field cannot be empty or provide valid email");
            bind.forgotEmail.requestFocus();
            return;
        }
        bind.progbar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snackbar.make(bind.getRoot(),"Please check your email to reset your password",BaseTransientBottomBar.LENGTH_LONG).show();
                bind.progbar.setVisibility(View.GONE);
                NavHostFragment.findNavController(ForgotFragment.this).navigate(R.id.action_forgotFragment_to_loginFragment);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Snackbar.make(bind.getRoot(), e.getMessage(),BaseTransientBottomBar.LENGTH_LONG).show();
                bind.progbar.setVisibility(View.GONE);
            }
        });
    }
}