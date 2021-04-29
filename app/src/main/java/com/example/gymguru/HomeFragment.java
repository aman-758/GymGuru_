package com.example.gymguru;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gymguru.databinding.FragmentHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding bind;
    FirebaseDatabase database;
    DatabaseReference reference;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentHomeBinding.bind(view);
        bind.recyclerviewVideo.setHasFixedSize(true);
        bind.recyclerviewVideo.setLayoutManager(new LinearLayoutManager(getActivity()));
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("video");

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<HomeMember, VideoHolder>firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<HomeMember, VideoHolder>(
                        HomeMember.class,
                        R.layout.row,
                        VideoHolder.class,
                        reference
                ) {
                    @Override
                    protected void populateViewHolder(VideoHolder viewHolder, HomeMember model, int position) {
                        viewHolder.setVideo(requireActivity().getApplication(), model.getTitle(), model.getUrl());
                    }
                };
        bind.recyclerviewVideo.setAdapter(firebaseRecyclerAdapter);

    }
}