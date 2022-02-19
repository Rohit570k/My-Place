package com.example.nearbyfinder.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nearbyfinder.MapsActivity;
import com.example.nearbyfinder.R;
import com.example.nearbyfinder.databinding.ActivityAccountsBinding;
import com.example.nearbyfinder.databinding.BottomSheetLayoutBinding;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AccountsActivity extends AppCompatActivity {

    private static final String TAG = "ACCOUNTS ACIVITY";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ActivityAccountsBinding binding;
    private BottomSheetLayoutBinding bottomSheetLayoutBinding;
    private BottomSheetBehavior<RelativeLayout> bottomSheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("My Account");
       actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");

        bottomSheetLayoutBinding = binding.bottomSheet;
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayoutBinding.getRoot());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        binding.txtEmail.setText(firebaseAuth.getCurrentUser().getEmail());
        binding.txtUsername.setText(firebaseAuth.getCurrentUser().getDisplayName());
        Log.i(TAG, "onCreate:Checking before chnaging  "+firebaseAuth.getCurrentUser().getDisplayName());

        databaseReference.child(firebaseAuth.getUid()).child("phonenumber").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String phoneNos = snapshot.getValue(String.class);
                        binding.txtMobileNumber.setText(phoneNos);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

        binding.cardLogout.setOnClickListener(view -> {
                   logoutDialog();
                });

        bottomSheetLayoutBinding.usernameUpdateButton.setOnClickListener(view -> {
            String username =bottomSheetLayoutBinding.edtUsername.getText().toString().trim();
            if (!username.isEmpty()) {

                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build();
                firebaseAuth.getCurrentUser().updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                            Map<String, Object> map = new HashMap<>();
                            map.put("username", username);
                            databaseReference.child(firebaseAuth.getUid()).updateChildren(map);

                            Log.i(TAG, "onComplete: Checking at chanhing"+firebaseAuth.getCurrentUser().getDisplayName());
                            binding.txtUsername.setText(username);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                            Toast.makeText(AccountsActivity.this, "Username is updated", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("TAG", "onComplete: " + task.getException());
                            Toast.makeText(AccountsActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(AccountsActivity.this, "Username is required", Toast.LENGTH_SHORT).show();
            }
        });


/** IF it was fragments and layout doesnt contain fragments
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accounts, container, false);
    }  **/
/** when we are using fragemnts layout in activty
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
 **/



    }
    private void logoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_logout_layout, null, false);
        builder.setView(view);
        builder.setTitle("Logout");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // displaying a toast message on user logged out inside on click.
                    Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_SHORT).show();
                    //on below line we are signing out our user.
                    firebaseAuth.signOut();
                    //on below line we are opening our login activity.
                    Intent i = new Intent(AccountsActivity.this, LoginActivity.class);
                    startActivity(i);
                   finish();
            }

        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
            super.onBackPressed();
    }
}