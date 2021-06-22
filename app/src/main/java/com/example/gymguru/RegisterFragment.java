package com.example.gymguru;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gymguru.databinding.FragmentRegisterBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterFragment extends Fragment {

    private FirebaseAuth auth;
    private FragmentRegisterBinding bind;
    private Animation registerLogoAnim;
    private Animation scrollAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();


        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentRegisterBinding.bind(view);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //code for animation
        registerLogoAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        scrollAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        //set animation on element
        bind.registerImgAnim.setAnimation(registerLogoAnim);
        bind.scrollAnim.setAnimation(scrollAnim);

        bind.userSwch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                Toast.makeText(getActivity(), "Gym Trainer", Toast.LENGTH_SHORT).show();
                bind.trainerPro.setVisibility(View.VISIBLE);
                bind.trainerChannel.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(getActivity(), "Viewer", Toast.LENGTH_SHORT).show();
                bind.trainerPro.setVisibility(View.GONE);
                bind.trainerChannel.setVisibility(View.GONE);
            }
        });

        bind.btnReg.setOnClickListener(v -> {

            String username = bind.editName.getText().toString();
            String email = bind.editUemail.getText().toString();
            String password = bind.editUpass.getText().toString();
            String cPass = bind.Cpass.getText().toString();
            String age = bind.editAge.getText().toString();
            String experience = bind.editExperience.getText().toString();
            String channelName = bind.channelName.getText().toString();
            String genderType = "";
            String userType = "Viewer";
            bind.progReg.setVisibility(View.VISIBLE);
            if (bind.radioMale.isChecked()) {
                genderType = "Male";
            }
            if (bind.radioFemale.isChecked()) {
                genderType = "Female";
            }
            if (bind.userSwch.isChecked()) {
                userType = "Gym Trainer";

            }

            if(userType.equals("Gym Trainer")) {
                registeruser(username, email, password, cPass, age, experience, channelName, genderType, userType);
            }
            else{
                registeruser(username, email, password, cPass, age, genderType, userType);
            }
        });
    }
    private void registeruser(String username, String email, String password, String cPass, String age, String genderType, String userType) {
        if (!username.trim().isEmpty()) {
            if (!age.trim().isEmpty() && age.length() <= 2) {
                if (email.length() >= 10) {
                        if (password.length() >= 8) {
                            if (cPass.equals(password)) {
                                String finalUserType = userType;
                                String finalGenderType = genderType;
                                auth.createUserWithEmailAndPassword(email, password)
                                        .addOnSuccessListener(authResult -> {
                                            String uid = authResult.getUser().getUid();
                                            RegistrationModel registrationModel = new RegistrationModel(uid, username, age, email, "", "", finalGenderType, finalUserType, "");
                                            updateUserProfile(registrationModel);
                                            })
                                        .addOnFailureListener(e -> {
                                            Snackbar.make(bind.getRoot(), e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                                            updateUserProfile(null);
                                            bind.progReg.setVisibility(View.GONE);
                                        });
                                } else {
                                    bind.Cpass.setError("Password do not match!");
                                    bind.Cpass.requestFocus();
                                    updateUserProfile(null);
                                    bind.progReg.setVisibility(View.GONE);
                                }
                            } else {
                                bind.editUpass.setError("Invalid Password!");
                                bind.editUpass.requestFocus();
                                updateUserProfile(null);
                                bind.progReg.setVisibility(View.GONE);
                            }
                } else {
                    bind.editUemail.setError("Invalid Email!");
                    bind.editUemail.requestFocus();
                    updateUserProfile(null);
                    bind.progReg.setVisibility(View.GONE);
                }
            } else {
                bind.editAge.setError("Age must be provided!");
                bind.editAge.requestFocus();
                updateUserProfile(null);
                bind.progReg.setVisibility(View.GONE);
            }
        } else {
            bind.editName.setError("Name must be provided!");
            bind.editName.requestFocus();
            updateUserProfile(null);
            bind.progReg.setVisibility(View.GONE);
        }
    }

    private void registeruser(String username, String email, String password, String cPass, String age, String experience, String channelName, String genderType, String userType) {
        if (!username.trim().isEmpty()) {
            if (!age.trim().isEmpty() && age.length() <= 2) {
                if (email.length() >= 10) {
                    if (!experience.trim().isEmpty()) {
                        if (!channelName.isEmpty()) {
                            if (password.length() >= 8) {
                                if (cPass.equals(password)) {
                                    String finalUserType = userType;
                                    String finalGenderType = genderType;
                                    auth.createUserWithEmailAndPassword(email, password)
                                            .addOnSuccessListener(authResult -> {
                                                String uid = authResult.getUser().getUid();
                                                RegistrationModel registrationModel = new RegistrationModel(uid, username, age, email, experience, channelName, finalGenderType, finalUserType, "");
                                                updateUserProfile(registrationModel);
                                            })
                                            .addOnFailureListener(e -> {
                                                Snackbar.make(bind.getRoot(), e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                                                updateUserProfile(null);
                                                bind.progReg.setVisibility(View.GONE);
                                            });
                                } else {
                                    bind.Cpass.setError("Password do not match!");
                                    bind.Cpass.requestFocus();
                                    updateUserProfile(null);
                                    bind.progReg.setVisibility(View.GONE);
                                }
                            } else {
                                bind.editUpass.setError("Invalid Password!");
                                bind.editUpass.requestFocus();
                                updateUserProfile(null);
                                bind.progReg.setVisibility(View.GONE);
                            }

                        } else {
                            bind.channelName.setError("Channel Name must be provided!");
                            bind.channelName.requestFocus();
                            bind.progReg.setVisibility(View.GONE);
                        }
                    } else {
                        bind.editExperience.setError("Profession must be provided!");
                        bind.editExperience.requestFocus();
                        updateUserProfile(null);
                        bind.progReg.setVisibility(View.GONE);
                    }
                } else {
                    bind.editUemail.setError("Invalid Email!");
                    bind.editUemail.requestFocus();
                    updateUserProfile(null);
                    bind.progReg.setVisibility(View.GONE);
                }
            } else {
                bind.editAge.setError("Age must be provided!");
                bind.editAge.requestFocus();
                updateUserProfile(null);
                bind.progReg.setVisibility(View.GONE);
            }
        } else {
            bind.editName.setError("Name must be provided!");
            bind.editName.requestFocus();
            updateUserProfile(null);
            bind.progReg.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        new AlertDialog.Builder(getActivity()).setTitle("Error").setMessage(message).setPositiveButton("OK",
                (dialog, which) -> {
                }).show();
    }

    private void updateUserProfile(RegistrationModel registrationModel) {
        if (registrationModel != null) {
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser()
                    .getUid()).setValue(registrationModel).addOnSuccessListener(aVoid -> {

                showSuccess("User registered successfully done");
                bind.progReg.setVisibility(View.GONE);
            }).addOnFailureListener(e -> {
                showError(e.getMessage());
                bind.progReg.setVisibility(View.GONE);
            });
        }else{
            Snackbar.make(bind.getRoot(),"Details Invalid!",BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }


    private void showSuccess(String message) {
        new AlertDialog.Builder(getActivity()).setTitle("Success").setMessage(message).setPositiveButton("Continue",
                ((dialog, which) -> {
                    NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_dashboardFragment);
                })).show();
    }
}