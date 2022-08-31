package com.nabeel.climatechange.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nabeel.climatechange.R;
import com.nabeel.climatechange.databinding.ActivityRegistrationBinding;
import com.nabeel.climatechange.model.User;
import com.nabeel.climatechange.utils.AlertDialogClass;
import com.nabeel.climatechange.utils.CommonClass;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar.getRoot());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolBar.textView.setText("Registration ");

        binding.toolBar.logout.setVisibility(View.GONE);

        initialize();

        binding.btnReg.setOnClickListener(v -> {
            if (CommonClass.isInternetOn(getApplicationContext())) {
                registerUser();
            }else {
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtLogin.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void registerUser() {
        if (checkvalidation()){
            AlertDialogClass.showProgressDialog(RegistrationActivity.this);
            mAuth.createUserWithEmailAndPassword(binding.etEmail.getText().toString().trim(),binding.etPassword.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                User user = new User(binding.etName.getText().toString().trim(),binding.etEmail.getText().toString().trim());
                                FirebaseDatabase.getInstance().getReference("user")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    mAuth.getCurrentUser().sendEmailVerification();
                                                    AlertDialogClass.dismissProgressDialog();
                                                    showPopup();

                                                }else {
                                                    Toast.makeText(getApplicationContext(), "Failed to registered! Try again!", Toast.LENGTH_SHORT).show();
                                                    AlertDialogClass.dismissProgressDialog();
                                                }
                                            }
                                        });
                            }else {
                                Toast.makeText(getApplicationContext(), "Failed to registered! Try again!", Toast.LENGTH_SHORT).show();
                                AlertDialogClass.dismissProgressDialog();
                            }
                        }
                    });
        }
    }

    private boolean checkvalidation() {
        if (binding.etName.getText().toString().trim().length()==0){
            binding.etName.setError("Full name is required");
            binding.etName.requestFocus();
            return false;
        }
        if (binding.etEmail.getText().toString().trim().length()==0){
            binding.etEmail.setError("Email is required");
            binding.etEmail.requestFocus();
            return false;
        }
        if (!binding.etEmail.getText().toString().contains("@")){
            binding.etEmail.setError("Enter valid email");
            binding.etEmail.requestFocus();
            return false;
        }
        if (binding.etPassword.getText().toString().trim().length()==0){
            binding.etPassword.setError("Password is required");
            binding.etPassword.requestFocus();
            return false;
        }
        if (binding.etPassword.getText().toString().trim().length()<=6){
            binding.etPassword.setError("Password is more than 6");
            binding.etPassword.requestFocus();
            return false;
        }
        if (binding.etConPassword.getText().toString().trim().length()==0){
            binding.etConPassword.setError("Confirm Password is required");
            binding.etConPassword.requestFocus();
            return false;
        }
        if (!binding.etPassword.getText().toString().trim().equals(binding.etConPassword.getText().toString().trim())){
            binding.etConPassword.setError("Password not match");
            binding.etPassword.setError("Password not match");
            binding.etConPassword.requestFocus();
            binding.etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void showPopup() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(
                RegistrationActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        //new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        pDialog.setTitleText("Registered successfully!")
                .setContentText("We send you a verification mail!")
                .setConfirmText("OK")
                .setCustomImage(R.drawable.tick_mark)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .show();
        pDialog.setCancelable(false);
    }

    private void initialize() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}