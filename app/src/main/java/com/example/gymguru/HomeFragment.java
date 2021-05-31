package com.example.gymguru;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gymguru.databinding.FragmentHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment{
    private FragmentHomeBinding bind;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference databaseReference, likesreference, dislikesreference, followsreference;
    Boolean likeChecker = false;
    Boolean dislikeChecker = false;
    Boolean followChecker = false;
    String title;
    private Animation videoAnim;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentHomeBinding.bind(view);
        //code for animation
        videoAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.dash_sidetext);
        //set animation on element
        bind.videoAnim.setAnimation(videoAnim);

        bind.recyclerviewVideo.setHasFixedSize(true);

        bind.recyclerviewVideo.setLayoutManager(new LinearLayoutManager(getActivity()));

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("videos");
        likesreference = database.getReference("video_likes");
        dislikesreference = database.getReference("video_dislikes");
        followsreference = database.getReference("Followers");

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
                new FirebaseRecyclerAdapter<UploadMember, VideoHolder /*RegistrationModel*/>(
                        UploadMember.class,
                        //RegistrationModel.class,
                        R.layout.row,
                        VideoHolder.class,
                        reference

                ) {


                    @Override
                    protected void populateViewHolder(VideoHolder videoHolder, UploadMember model, int position) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserId = user.getUid();
                        final String postkey = getRef(position).getKey(); //It will currently get the key of the post

                        Log.d("Message", model.getUrl());
                        videoHolder.setOnClickListener(new VideoHolder.Clicklistener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                title = getItem(position).getTitle();
                                showDeleteDialogName(model.title);
                            }
                        });
                        videoHolder.setVideo(requireActivity().getApplication(), model, position);
                        String videoId = this.getRef(position).getKey();

                        videoHolder.initui(getActivity(), model, position, videoId,getChildFragmentManager());

                        videoHolder.setLikesButtonStatus(postkey);
                        videoHolder.likeButton.setOnClickListener(v -> {
                            likeChecker = true;
                            likesreference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(likeChecker.equals(true)){
                                        if(snapshot.child(postkey).hasChild(currentUserId)){
                                            likesreference.child(postkey).child(currentUserId).removeValue(); //this is for that user could not like the same video again and again

                                            likeChecker = false;
                                        }else{
                                            likesreference.child(postkey).child(currentUserId).setValue(true);
                                            dislikesreference.child(postkey).child(currentUserId).removeValue();
                                            likeChecker = false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull  DatabaseError error) {
                                    Snackbar.make(bind.getRoot(),"Something went wrong", BaseTransientBottomBar.LENGTH_LONG).show();
                                }
                            });
                        });
                        videoHolder.setDislikeButtonStatus(postkey);
                        videoHolder.dislikeButton.setOnClickListener(v -> {
                            dislikeChecker = true;
                            dislikesreference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(dislikeChecker.equals(true)){
                                        if(snapshot.child(postkey).hasChild(currentUserId)){
                                            dislikesreference.child(postkey).child(currentUserId).removeValue(); //User already gives the dislike so they could not again yani user dislike wapas le rha hai

                                            dislikeChecker = false;
                                        }else{
                                            dislikesreference.child(postkey).child(currentUserId).setValue(true);//User dislike the trainer video
                                            likesreference.child(postkey).child(currentUserId).removeValue();
                                            dislikeChecker = false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Snackbar.make(bind.getRoot(),"Something went wrong",BaseTransientBottomBar.LENGTH_LONG).show();
                                }
                            });
                        });
                        videoHolder.setFollowButtonStatus(postkey);
                        videoHolder.followButton.setOnClickListener(v -> {
                            followChecker = true;
                            followsreference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(followChecker.equals(true)){
                                        reference.child(postkey).get().addOnSuccessListener(dataSnapshot -> {
                                           UploadMember model = dataSnapshot.getValue(UploadMember.class);
                                            if(snapshot.child(model.uploaderId).hasChild(currentUserId)){
                                                followsreference.child(model.uploaderId).child(currentUserId).removeValue(); //User already follows the trainer so they could not again
                                                followChecker = false;
                                            }else{
                                                followsreference.child(model.uploaderId).child(currentUserId).setValue(true); //user follows a particular trainer only at once
                                                followChecker = false;
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Snackbar.make(bind.getRoot(),"Something went wrong",BaseTransientBottomBar.LENGTH_LONG).show();
                                }
                            });
                        });
                        videoHolder.comment.setOnClickListener(v -> {

                            HomeFragmentDirections.ActionHomeFragmentToCommentFragment dir = HomeFragmentDirections.actionHomeFragmentToCommentFragment(postkey);
                            NavHostFragment.findNavController(HomeFragment.this).navigate(dir);

                        });
                    }
                };

        bind.recyclerviewVideo.setAdapter(firebaseRecyclerAdapter);

    }

    private void showDeleteDialogName(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this video?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Query query = reference.orderByChild("title").equalTo(title);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        dataSnapshot1.getRef().removeValue();
                    }
                    Toast.makeText(getActivity(), "Video Deleted", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(@NonNull  DatabaseError error) {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull  Menu menu, @NonNull  MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search_firebase);

        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                processSearch(newText);
                return false;
            }
        });


    }

    private void processSearch(String query) {
        //Code does not run due to the missing of FirebaseRecyclerOptions
        
    }



}