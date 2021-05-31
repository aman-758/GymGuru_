package com.example.gymguru;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gymguru.databinding.FragmentSplashBinding;


public class SplashFragment extends Fragment {
    private FragmentSplashBinding bind;
    /*ImageView logoImage;
    TextView logoText;
    Button startButton;*/

    private Animation sidelogoanim, sidetextanim, buttonanim, greetLogoAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        bind = FragmentSplashBinding.bind(view);

        // code  for animaton

        sidelogoanim = AnimationUtils.loadAnimation( getActivity() ,R.anim.sidelogo_anim) ;
        sidetextanim= AnimationUtils.loadAnimation( getActivity() ,R.anim.sidetext_anim);
        buttonanim= AnimationUtils.loadAnimation( getActivity() ,R.anim.button_anim);
        greetLogoAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.sidelogo_anim);

        //set animation on elements
        bind.logoView.setAnimation(sidelogoanim);
        bind.GYMText.setAnimation(sidetextanim);
        bind.getstatedBtn.setAnimation(buttonanim);
        bind.greetLogo.setAnimation(sidetextanim);
        new Handler().postDelayed(() -> NavHostFragment.findNavController(SplashFragment.this).navigate(R.id.action_splashFragment_to_loginFragment),3000);
    }
}