package com.example.gymguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gymguru.databinding.FragmentTrainerVideosBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class TrainerVideos extends Fragment {
    private FragmentTrainerVideosBinding bind;
    DatabaseReference trainerreference;
    String tuid;
    ArrayList<UploadMember> uploadMembers;
    FirebaseDatabase database;
    private UploadMember value;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trainer_videos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentTrainerVideosBinding.bind(view);
        bind.recyclerTrainer.setHasFixedSize(true);

        bind.recyclerTrainer.setLayoutManager(new LinearLayoutManager(getActivity()));

        tuid = TrainerVideosArgs.fromBundle(getArguments()).getUid();
        trainerreference = FirebaseDatabase.getInstance().getReference("Users");
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<UploadMember> videos = new ArrayList<>();
        trainerreference = database.getReference("videos");
        trainerreference.get().addOnSuccessListener(dataSnapshot -> {
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                value = child.getValue(UploadMember.class);
                if(value.uploaderId.equals(tuid)){
                    videos.add(value);
                }
            }
        }).addOnFailureListener(e -> {
            Snackbar.make(bind.getRoot(),e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<UploadMember,VideoHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<UploadMember, VideoHolder>(
                        UploadMember.class,
                        R.layout.row,
                        VideoHolder.class,
                        trainerreference
                ) {
                    @Override
                    protected void populateViewHolder(VideoHolder videoHolder, UploadMember member, int i) {
                        videoHolder.setVideo(requireActivity().getApplication(),member,i);
                        String videoId = this.getRef(i).getKey();
                        videoHolder.initui(getActivity(),member,i,videoId,getChildFragmentManager());
                    }
                };
        bind.recyclerTrainer.setAdapter(firebaseRecyclerAdapter);
    }

}