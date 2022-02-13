package com.example.nearbyfinder.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.nearbyfinder.MapsActivity;
import com.example.nearbyfinder.R;
import com.example.nearbyfinder.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnSignUp.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });

        binding.txtForgetPassword.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
        });
        binding.btnLogin.setOnClickListener(view -> {
            if (areFieldReady()) {
                login();
            }
        });
    }

    private void login() {
        binding.idPBLoading.setVisibility(View.VISIBLE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        binding.idPBLoading.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                        startActivity(intent);
                        finish();

                    } else {

                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> email) {
                                if (email.isSuccessful()) {
                                    binding.idPBLoading.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Please verify email", Toast.LENGTH_SHORT).show();
                                } else {
                                    binding.idPBLoading.setVisibility(View.GONE);
                                    Log.d("TAG", "onComplete: EmailVerification" + email.getException());
                                    Toast.makeText(LoginActivity.this, "Error : " + email.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                } else {
                    binding.idPBLoading.setVisibility(View.GONE);
                    Log.d("TAG", "onComplete:UserLogin" + task.getException());
                    Toast.makeText(LoginActivity.this, "Enter valid user credentials..", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean areFieldReady() {

        email = binding.edtEmail.getText().toString().trim();
        password = binding.edtPassword.getText().toString().trim();

        boolean flag = false;
        View requestView = null;

        if (email.isEmpty()) {
            binding.edtEmail.setError("Field is required");
            flag = true;
            requestView = binding.edtEmail;
        } else if (password.isEmpty()) {
            binding.edtPassword.setError("Field is required");
            flag = true;
            requestView = binding.edtPassword;
        } else if (password.length() < 8) {
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
}