package com.example.gymguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gymguru.databinding.FragmentLiveBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;


public class LiveFragment extends Fragment {
    private FragmentLiveBinding bind;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentLiveBinding.bind(view);

    }

    //Explicit functions never implements inside the function
    private void loadMockVideoStream() {
         bind.mockLiveStreamView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
             @Override
             public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError error) {
                 super.onError(youTubePlayer, error);
                 Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
             }

             @Override
             public void onReady(YouTubePlayer youTubePlayer) {
                 super.onReady(youTubePlayer);
                 youTubePlayer.loadVideo("M_08pLUVUByLJu5_vbj",0);

             }
         });
    }
}