package com.example.gymguru;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gymguru.databinding.FragmentEditProfileBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileFragment extends Fragment {

    private static final int REQUEST_IMAGE_GET = 78;
    private FirebaseAuth fAuth;
    private FirebaseDatabase db;
    private FirebaseStorage Storage;
    StorageReference storageReference;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private FragmentEditProfileBinding bind;
    private CircleImageView Img;
    private boolean isImageUpload = false;
    private Uri fullPhotoUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        user = FirebaseAuth.getInstance().getCurrentUser();
        Storage = FirebaseStorage.getInstance();
        storageReference = Storage.getReference();
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                bind.changeProfileImg.setImageURI(imageUri);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentEditProfileBinding.bind(view);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();


        // Now get users data from the database.

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RegistrationModel registrationModel = snapshot.getValue(RegistrationModel.class);
                // registrationModel is the object of RegistrationModel where all user's data are there
                if (registrationModel != null) {
                    String username = registrationModel.username;
                    String email = registrationModel.email;
                    String age = registrationModel.age;
                    String experience = registrationModel.experience;
                    String gender = registrationModel.gender;
                    String userType = registrationModel.userType;

                    bind.fireName.setText(username);
                    bind.fireEmail.setText(email);
                    bind.fireAge.setText(age);
                    bind.fireExperience.setText(experience);
                    bind.fireGender.setText(gender);
                    bind.fireSwitch.setText(userType);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(bind.getRoot(), "Something went wrong, Data cannot be fetched", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

        bind.btnSaveChanges.setOnClickListener(v -> {
            String name = bind.fireName.getText().toString();
            String experience = bind.fireExperience.getText().toString();

            String age = bind.fireAge.getText().toString();

            updateData(name,experience,age);
            bind.progressBar.setVisibility(View.VISIBLE);
        });

        bind.changeProfileImg.setOnClickListener(v -> {
            // Here i am open to gallery
            Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGallery, 1000);
        });


    }

    private void updateData(String name, String experience, String age) {
        // Now creating hashmap to update the data
        HashMap User = new HashMap();
        User.put("username",name);
        User.put("experience",experience);

        User.put("age",age);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        // Now update the child of the Users
        reference.child(userId).updateChildren(User).addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                bind.fireName.setText(name);
                bind.fireExperience.setText(experience);

                bind.fireAge.setText(age);
                Snackbar.make(bind.getRoot(),"Data successfully updated",BaseTransientBottomBar.LENGTH_LONG).show();
                bind.progressBar.setVisibility(View.GONE);
            } else{
                Snackbar.make(bind.getRoot(),"Failed to update",BaseTransientBottomBar.LENGTH_LONG).show();
                bind.progressBar.setVisibility(View.GONE);
            }
            bind.progressBar.setVisibility(View.GONE);
        });



    }

}