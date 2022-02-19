package com.example.nearbyfinder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.nearbyfinder.R;
import com.example.nearbyfinder.WebServices.UserModel;
import com.example.nearbyfinder.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG ="PHONE NUMBER CHECKING" ;
    private ActivitySignUpBinding binding;
    private String email, username, password,repassword,phonenumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //storageReference = FirebaseStorage.getInstance().getReference();
        Glide.with(this).load(R.raw.markerpng).into(binding.imgPick);




        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.txtLogin.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.btnSignUp.setOnClickListener(view -> {
            if (areFieldReady()) {
//                if (imageUri != null) {
                signUp();
//                } else {
//                    Toast.makeText(this, "Image is required", Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }

    private boolean areFieldReady() {
        username = binding.edtUsername.getText().toString().trim();
        email = binding.edtEmail.getText().toString().trim();
        password = binding.edtPassword.getText().toString().trim();
        repassword=binding.edtConfirmPassword.getText().toString().trim();
        phonenumber=binding.edtPhonenumber.getText().toString().trim();
        boolean flag = false;
        View requestView = null;

        if (username.isEmpty()) {
            binding.edtUsername.setError("Field is required");
            flag = true;
            requestView = binding.edtUsername;
        } else if (email.isEmpty()) {
            binding.edtEmail.setError("Field is required");
            flag = true;
            requestView = binding.edtEmail;
        } else if (password.isEmpty()) {
            binding.edtPassword.setError("Field is required");
            flag = true;
            requestView = binding.edtPassword;
        } else if (repassword.isEmpty()) {
            binding.edtConfirmPassword.setError("Field is required");
            flag = true;
            requestView = binding.edtPassword;
        } else if (phonenumber.isEmpty()) {
            binding.edtPhonenumber.setError("Field is required");
            flag = true;
            requestView = binding.edtPhonenumber;
        } else if(!password.equals(repassword)){
            binding.edtConfirmPassword.setError("Password not same,Re-enter");
            flag = true;
            requestView = binding.edtConfirmPassword;
        }else if (password.length() < 8) {
            binding.edtPassword.setError("Minimum 8 characters");
            flag = true;
            requestView = binding.edtPassword;
        }

        if (flag) {
            requestView.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void signUp() {
        binding.idPBLoading.setVisibility(View.VISIBLE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference drefpattern=FirebaseDatabase.getInstance().getReference("User_Credential");
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> singUp) {

                if (singUp.isSuccessful()) {
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();

                    firebaseAuth.getCurrentUser().updateProfile(profileChangeRequest).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        UserModel userModel = new UserModel(email,
                                                username,phonenumber, true);
                                        databaseReference.child(firebaseAuth.getUid())
                                                .setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                firebaseAuth.getCurrentUser().sendEmailVerification()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                drefpattern.child(phonenumber).setValue(email);
                                                                binding.idPBLoading.setVisibility(View.GONE);
                                                                // send the user to verify the phone
//                                                                Intent phone = new Intent(SignUpActivity.this,PhoneVerfifcationActivity.class);
//                                                                phone.putExtra("phone","+91"+phonenumber);
                                                                Toast.makeText(SignUpActivity.this, "Verify your email,Mail has been sent", Toast.LENGTH_LONG).show();
//                                                                startActivity(phone);
//
                                                                onBackPressed();
                                                            }
                                                        });

                                            }

                                        });

                                    } else {
                                        binding.idPBLoading.setVisibility(View.GONE);
                                        Log.d("TAG", "onComplete: Update Profile" + task.getException());
                                        Toast.makeText(SignUpActivity.this, "Update Profile" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                } else {
                    binding.idPBLoading.setVisibility(View.GONE);
                    Log.d("TAG", "onComplete: Create user" + singUp.getException());
                    Toast.makeText(SignUpActivity.this, "" + singUp.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
