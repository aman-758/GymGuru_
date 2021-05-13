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
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class EditProfileFragment extends Fragment {


    private FirebaseAuth fAuth;
    private FirebaseStorage Storage;
    StorageReference storageReference;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private FragmentEditProfileBinding bind;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        user = FirebaseAuth.getInstance().getCurrentUser();
        fAuth = FirebaseAuth.getInstance();
        Storage = FirebaseStorage.getInstance();
        storageReference = Storage.getReference();


        // I am doing this because every user have their own profile image.
        StorageReference profileRef = storageReference.child("Users/" + fAuth.getCurrentUser().getUid() + "/Profiles");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(bind.changeProfileImg);
        });
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();

                //bind.changeProfileImg.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);


            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        final StorageReference fileRef = storageReference.child("Users/" + fAuth.getCurrentUser().getUid() + "/Profiles");

        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(bind.changeProfileImg);
                Snackbar.make(bind.getRoot(), "Image uploaded successfully", BaseTransientBottomBar.LENGTH_LONG).show();
                bind.progressBar.setVisibility(View.GONE);

            });
        }).addOnFailureListener(e -> {
            Snackbar.make(bind.getRoot(), "Upload Failed", BaseTransientBottomBar.LENGTH_LONG).show();
            bind.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentEditProfileBinding.bind(view);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference(); // this storage reference is to upload the image on firebase
        userId = user.getUid();


        // Now get user's data from the database.

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
                    if(userType.equals("Gym Trainer")){
                        bind.fireSwitch.setChecked(true);
                        bind.fireSwitch.setText(userType);
                    }
                    else{
                        bind.fireSwitch.setChecked(false);
                        bind.fireSwitch.setText(userType);
                    }

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
            Boolean userType = bind.fireSwitch.getShowText();
            String age = bind.fireAge.getText().toString();

            updateData(name, experience, age, userType);
            bind.progressBar.setVisibility(View.VISIBLE);
        });

        // This is the add profile segment
        bind.addImg.setOnClickListener(v -> {
            // Here i am open to gallery
            Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGallery, 1000);
            bind.progressBar.setVisibility(View.VISIBLE);
        });

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }
    //This is the function of update user's data
    private void updateData(String name, String experience, String age, Boolean userType) {
        // Now creating hashmap to update the data
        HashMap User = new HashMap();
        User.put("username", name);
        User.put("experience", experience);
        User.put("age", age);
        User.put("UserType",userType);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        // Now update the child of the Users
        reference.child(userId).updateChildren(User).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                bind.fireName.setText(name);
                bind.fireExperience.setText(experience);
                bind.fireAge.setText(age);
                Snackbar.make(bind.getRoot(), "Data successfully updated", BaseTransientBottomBar.LENGTH_LONG).show();
                bind.progressBar.setVisibility(View.GONE);
            } else {
                Snackbar.make(bind.getRoot(), "Failed to update", BaseTransientBottomBar.LENGTH_LONG).show();
                bind.progressBar.setVisibility(View.GONE);
            }

        });

    }

}
