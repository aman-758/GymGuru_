package com.example.gymguru;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.gymguru.databinding.FragmentUploadBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class UploadFragment extends Fragment {

    private static final int PICK_VIDEO_REQUEST = 1;
    private FragmentUploadBinding bind;
    private Uri videoUri;
    MediaController mediaController;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;


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
        bind = FragmentUploadBinding.bind(view);
        bind.videoView.setMediaController(mediaController);
        mediaController.setAnchorView(bind.videoView);
        bind.videoView.start();

        bind.chooseBtn.setOnClickListener(view1 -> {
            ChooseVideo();
        });
        bind.uploadBtn.setOnClickListener(v -> {
            Uploadvideo();
        });
        
    }

    private void ChooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_VIDEO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() != null);

        videoUri = data.getData();
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
        if(videoUri != null){
            StorageReference reference = storageReference.child(System.currentTimeMillis()+
                    "."+getfileExt(videoUri));

            reference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    bind.progUpload.setVisibility(View.GONE);
                    Snackbar.make(bind.getRoot(), "Upload successful",BaseTransientBottomBar.LENGTH_SHORT).show();
                    UploadMember uploadMember = new UploadMember(bind.videoName.getText().toString().trim(),
                            taskSnapshot.getUploadSessionUri().toString());
                    String upload = databaseReference.push().getKey();
                    databaseReference.child(upload).setValue(uploadMember);
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