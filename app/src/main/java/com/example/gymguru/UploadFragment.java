package com.example.gymguru;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gymguru.databinding.FragmentUploadBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class UploadFragment extends Fragment {

    private static final int PICK_VIDEO_REQUEST = 1;
    private FragmentUploadBinding bind;
    private Uri videoUri;
    MediaController mediaController;
    private StorageReference storageReference;
    private DatabaseReference databaseReference,users,viewer;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    private long duration;
    Boolean checker = false;
    private Animation uploadAnim;
    private ArrayList<RegistrationModel> trainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mediaController = new MediaController(getActivity());

        storageReference = FirebaseStorage.getInstance().getReference("videos");
        databaseReference = FirebaseDatabase.getInstance().getReference("videos");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        viewer = database.getReference("Users");
        bind = FragmentUploadBinding.bind(view);

        //code for animation
        uploadAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down);
        //set animation on element
        bind.uploadAnim.setAnimation(uploadAnim);
        trainer = new ArrayList<>();
        bind.videoView.setMediaController(mediaController);
        mediaController.setAnchorView(bind.videoView);
        bind.videoView.start();

        bind.chooseBtn.setOnClickListener(view1 -> {

                viewer.child(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(dataSnapshot -> {
                   RegistrationModel registrationModel = dataSnapshot.getValue(RegistrationModel.class);
                   if(registrationModel.userType.equals("Gym Trainer")){
                       ChooseVideo();
                   }
                });

               /*for(DataSnapshot child : dataSnapshot.getChildren()){
                   RegistrationModel registrationModel = child.getValue(RegistrationModel.class);
                   if(registrationModel != null && registrationModel.userType.equals("Gym Trainer")){

                       trainer.add(registrationModel);
                       Log.d("Info",registrationModel.getUserType());
                   }else{
                       Snackbar.make(bind.getRoot(),"Viewer can't upload the video",BaseTransientBottomBar.LENGTH_LONG).show();
                   }
               }*/

        });
        bind.uploadBtn.setOnClickListener(v -> {
            Uploadvideo();
        });
        
    }

    private void ChooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() != null);

        videoUri = data.getData();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(getActivity(),videoUri);
         duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        retriever.release();
        bind.videoView.setVideoURI(videoUri);

    }
    private String getfileExt(Uri videoUri){
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(videoUri));
        // Now my extension's file is completed
    }
    private void Uploadvideo(){

        bind.progUpload.setVisibility(View.VISIBLE);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh:mm");
        String strDate = dateFormat.format(currentTime);

        if(videoUri != null){
            StorageReference reference = storageReference.child(System.currentTimeMillis()+
                    "."+getfileExt(videoUri));

            reference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    bind.progUpload.setVisibility(View.GONE);
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                        Snackbar.make(bind.getRoot(), "Upload successful",BaseTransientBottomBar.LENGTH_SHORT).show();
                        UploadMember uploadMember = new UploadMember(bind.videoName.getText().toString().trim(),uri.toString(),uid,strDate,duration);
                        String upload = databaseReference.push().getKey();
                        databaseReference.child(upload).setValue(uploadMember);
                    });



                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(bind.getRoot(), "error"+ e.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                            bind.progUpload.setVisibility(View.GONE);
                        }
                    });
        } else{
            Snackbar.make(bind.getRoot(), "No file selected",BaseTransientBottomBar.LENGTH_SHORT).show();
            bind.progUpload.setVisibility(View.GONE);
        }
    }

}