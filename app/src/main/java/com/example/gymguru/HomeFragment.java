package com.example.gymguru;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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
        reference = database.getReference("videos");

    }

    //This is for search text
    /*private void firebaseSearch(String searchtext){
        String query = searchtext.toLowerCase();
        Query firebaseQuery = database.getReference().orderByChild("search").startAt(query).endAt(query + "\uf8ff");
    }*/

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<UploadMember, VideoHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<UploadMember, VideoHolder>(
                        UploadMember.class,
                        R.layout.row,
                        VideoHolder.class,
                        reference

                ) {

                    @Override
                    protected void populateViewHolder(VideoHolder videoHolder, UploadMember model, int position) {

                        Log.d("Message",model.getUrl());
                        videoHolder.setVideo(requireActivity().getApplication(), model, position);
                        String videoId = this.getRef(position).getKey();
                        videoHolder.initui(requireActivity().getApplication(), model, position,videoId);

                    }
                };
        bind.recyclerviewVideo.setAdapter(firebaseRecyclerAdapter);

    }

}