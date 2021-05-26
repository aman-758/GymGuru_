package com.example.gymguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gymguru.databinding.FragmentCommentBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;


public class CommentFragment extends Fragment {
    private FragmentCommentBinding bind;
    DatabaseReference userref, commentref, userreference;
    String postkey;
    ArrayList<CommentModel> commentModels;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentCommentBinding.bind(view);
        postkey = CommentFragmentArgs.fromBundle(getArguments()).getPostkey();
        userref = FirebaseDatabase.getInstance().getReference().child("Users");
        commentref = FirebaseDatabase.getInstance().getReference().child("videos").child(postkey).child("comments"); //Here i created comment children
        database = FirebaseDatabase.getInstance();
        userreference = database.getReference("Users");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        bind.commentRecview.setHasFixedSize(true);
        bind.commentRecview.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentModels = new ArrayList<>();
        CommentAdapter commentAdapter = new CommentAdapter(getActivity(), commentModels);
        bind.commentRecview.setAdapter(commentAdapter);
        commentref.get().addOnSuccessListener(dataSnapshot -> {
            commentModels.clear();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                CommentModel commentModel = child.getValue(CommentModel.class);
                if (commentModel != null) {
                    commentModels.add(commentModel);
                }
                bind.commentRecview.getAdapter().notifyDataSetChanged();
            }
        }).addOnFailureListener(e -> {
            Snackbar.make(bind.getRoot(), e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
        });

        bind.commentSubmit.setOnClickListener(v -> {
            userref.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("username").getValue().toString();
                        String userimage = snapshot.child("imageUrl").getValue().toString();
                        processcomment(username, userimage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle the error
                }
            });
        });

    }

    private void processcomment(String username, String userimage) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String commentpost = bind.commentText.getText().toString();

        if (!commentpost.trim().isEmpty() && !commentpost.equals("hmm") && !commentpost.equals("kk")) {

            final String userId = user.getUid();
            String randompostkey = userId + "" + new Random().nextInt(1000); //This is for generating the key of the comment

            Calendar datevalue = Calendar.getInstance();
            //to get date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
            String cdate = dateFormat.format(datevalue.getTime());
            //to get time
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String ctime = timeFormat.format(datevalue.getTime());

            //Now creating Hashmap
            HashMap cmnt = new HashMap();
            cmnt.put("uid", userId);
            cmnt.put("username", username);
            cmnt.put("imageUrl", userimage);
            cmnt.put("usermsg", commentpost);
            cmnt.put("date", cdate);
            cmnt.put("time", ctime);

            commentref.child(randompostkey).updateChildren(cmnt).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Comment Added", Toast.LENGTH_SHORT).show();
                } else {
                    //
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else{
            bind.commentText.setError("Comment cannot be empty or please don't type hmm or kk!");
            bind.commentText.requestFocus();
            return;
        }


    }
}