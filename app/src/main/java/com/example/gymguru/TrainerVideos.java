package com.example.gymguru;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gymguru.databinding.FragmentTrainerVideosBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;


public class TrainerVideos extends Fragment {
    private FragmentTrainerVideosBinding bind;
    DatabaseReference trainerreference;
    String tuid;
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
        tuid = TrainerVideosArgs.fromBundle(getArguments()).getUid();
        bind = FragmentTrainerVideosBinding.bind(view);
        bind.recyclerTrainer.setHasFixedSize(true);
        bind.recyclerTrainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        trainerreference = FirebaseDatabase.getInstance().getReference("Users");
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<UploadMember> videos = new ArrayList<>();
        trainerreference = database.getReference("videos");
        trainerreference.get().addOnSuccessListener(dataSnapshot -> {
            videos.clear();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                value = child.getValue(UploadMember.class);
                if (value.uploaderId.equals(tuid)) {
                    videos.add(value);
                    Log.d("Info", value.getUploaderId());
                }
            }
        }).addOnFailureListener(e -> {
            Snackbar.make(bind.getRoot(), e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
        });
    }

    private void firebaseSearch(String searchtext) {
        String query = searchtext.toLowerCase();
        Query firebaseQuery = trainerreference.orderByChild("title").startAt(query).endAt(query + "\uf8ff");

        FirebaseRecyclerOptions<UploadMember> options =
                new FirebaseRecyclerOptions.Builder<UploadMember>()
                        .setQuery(firebaseQuery, UploadMember.class)
                        .build();

        FirebaseRecyclerAdapter<UploadMember, TrainerHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<UploadMember, TrainerHolder>(options) {
                    @NonNull
                    @Override
                    public TrainerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainer_design, parent, false);
                        return new TrainerHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull TrainerHolder trainerHolder, int i, @NonNull UploadMember uploadMember) {
                        if (uploadMember.uploaderId.equals(tuid)) {
                            trainerHolder.setTrainerVideo(requireActivity().getApplication(), uploadMember, i);
                        }
                    }
                };
        firebaseRecyclerAdapter.startListening();
        bind.recyclerTrainer.setAdapter(firebaseRecyclerAdapter);

    }
        //Implementing FirebaseRecyclerOptions to fetch the data from Firebase and put it into the FirebaseRecyclerAdapter
        FirebaseRecyclerOptions<UploadMember> options =
                new FirebaseRecyclerOptions.Builder<UploadMember>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("videos"), UploadMember.class)
                        .build();

        @Override
        public void onStart () {
            super.onStart();
            FirebaseRecyclerAdapter<UploadMember, TrainerHolder> firebaseRecyclerAdapter =
                    new FirebaseRecyclerAdapter<UploadMember, TrainerHolder>(options) {
                        @NonNull
                        @Override
                        public TrainerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainer_design, parent, false);
                            return new TrainerHolder(view);
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull TrainerHolder trainerHolder, int i, @NonNull UploadMember uploadMember) {
                            if (uploadMember.uploaderId.equals(tuid)) {
                                trainerHolder.setTrainerVideo(requireActivity().getApplication(), uploadMember, i);
                            }
                        }
                    };
            firebaseRecyclerAdapter.startListening();
            bind.recyclerTrainer.setAdapter(firebaseRecyclerAdapter);
        }

    //Enable options menu in this fragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
            setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search_firebase);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    //handle item clicks of menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
