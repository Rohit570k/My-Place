package com.example.nearbyfinder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nearbyfinder.MapsActivity;
import com.example.nearbyfinder.WebServices.UserModel;
import com.example.nearbyfinder.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "CHECKING MOBILE NOS";
    private ActivityLoginBinding binding;
    private String email, password;
    private UserModel user;
    private DatabaseReference ref;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = new UserModel();
        firebaseAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("User_Credential");

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

//Single event listener
        Log.i(TAG, "login: firebase.aUTH"+firebaseAuth.getUid());
        Log.d(TAG, "CHECKING VALIDAITING ITS: "+android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()+",,!!"+android.util.Patterns.PHONE.matcher(email).matches());
          if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
              Log.d(TAG, "notPHONENosChangedtoEMAIL" + email+", "+password);
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
          }else if(android.util.Patterns.PHONE.matcher(email).matches()){
              ref.child(email).addListenerForSingleValueEvent(
                      new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              Log.d(TAG, "PHONENosBeforetoEMAIL" + email);
                              email=  snapshot.getValue(String.class);
                              Log.d(TAG, "PHONENosChangedtoEMAIL" + email+", "+password);
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
                                          Toast.makeText(LoginActivity.this, "Auth: "+task.getException(), Toast.LENGTH_SHORT).show();
                                      }

                                  }
                              });
                          }
                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {
                              Log.d("TAG", "onComplete:UserLogin" + error);
                              Toast.makeText(LoginActivity.this, "Enter valid user credentials", Toast.LENGTH_SHORT).show();
                          }
                      });
          }
//          if(email.equals(firebaseAuth.getCurrentUser().getEmail())){
//              Log.d(TAG, "CHECKING MALFORMED EMAIL"+"true"+email+", "+firebaseAuth.getCurrentUser().getEmail());
//          }else{
//              Log.d(TAG, "CHECKING MALFORMED EMAIL"+"false"+email+", "+firebaseAuth.getCurrentUser().getEmail());
//
//          }
//        ref.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // Get user value
//                        user = dataSnapshot.getValue(UserModel.class);
//                        Log.i(TAG, "onDataChange: " + user.getEmail() + user.getPhonenumber());
//
//                        //user.email now has your email value
//                    }

                    /**
                     * This method will be triggered in the event that this listener either failed at the server, or
                     * is removed as a result of the security and Firebase Database rules. For more information on
                     * securing your data, see: <a
                     * href="https://firebase.google.com/docs/database/security/quickstart" target="_blank"> Security
                     * Quickstart</a>
                     *
                     * @param error A description of the error that occurred
                     */
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//        Log.i(TAG, "onDataChange: " + user.getEmail() + user.getPhonenumber());
//        if (email.equals(user.getEmail()) || email.equals(user.getPhonenumber())) {

            //        C;
            //  Log.d(TAG, "login: " + firebaseAuth.getCurrentUser().getPhoneNumber());
//        Log.i(TAG, "login: PasswordnMail"+email+password);
//            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
//                            binding.idPBLoading.setVisibility(View.GONE);
//                            Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
//                            startActivity(intent);
//                            finish();
//
//                        } else {
//
//                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> email) {
//                                    if (email.isSuccessful()) {
//                                        binding.idPBLoading.setVisibility(View.GONE);
//                                        Toast.makeText(LoginActivity.this, "Please verify email", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        binding.idPBLoading.setVisibility(View.GONE);
//                                        Log.d("TAG", "onComplete: EmailVerification" + email.getException());
//                                        Toast.makeText(LoginActivity.this, "Error : " + email.getException(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }
//
//                    } else {
//                        binding.idPBLoading.setVisibility(View.GONE);
//                        Log.d("TAG", "onComplete:UserLogin" + task.getException());
//                        Toast.makeText(LoginActivity.this, "Enter valid user credentials..", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            });


//        } else {
//            binding.idPBLoading.setVisibility(View.GONE);
//            Log.d("TAG", "onATphone check:UserLogin");
//            Toast.makeText(LoginActivity.this, "Enter valid user credentials", Toast.LENGTH_SHORT).show();
//
//        }
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


    @Override
    protected void onStart() {
        super.onStart();
        //in on start method checking if the user is already sign in.
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null&&user.isEmailVerified()) {
            //if the user is not null then we are opening a main activity on below line.
            Intent i = new Intent(LoginActivity.this, MapsActivity.class);
            startActivity(i);
            this.finish();
        }else {
//            Toast.makeText(LoginActivity.this, "Please verify email", Toast.LENGTH_SHORT).show();
        }

    }
}