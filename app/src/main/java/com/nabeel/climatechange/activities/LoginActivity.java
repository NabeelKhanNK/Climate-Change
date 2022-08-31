package com.nabeel.climatechange.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nabeel.climatechange.databinding.ActivityLoginBinding;
import com.nabeel.climatechange.utils.AlertDialogClass;
import com.nabeel.climatechange.utils.CommonClass;
import com.nabeel.climatechange.utils.SharedPrefHelper;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    String emailPattern = "[a-zA-Z0-9]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth;
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();



        binding.txtRegister.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
        });

        binding.txtWithoutLogin.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etUserName.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            if (checkValidation(email,password)){
                if (CommonClass.isInternetOn(getApplicationContext())) {
                    login(email, password);
                }else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void login(String email, String password) {
        AlertDialogClass.showProgressDialog(LoginActivity.this);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sharedPrefHelper.setString("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    sharedPrefHelper.setInt("isLogin",1);
                    AlertDialogClass.dismissProgressDialog();
                    callNextActivity();
                }else {
                    AlertDialogClass.dismissProgressDialog();
                    Toast.makeText(LoginActivity.this, "Invalid user!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void callNextActivity(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private boolean checkValidation(String email, String password) {
        if (email.length()==0){
            binding.etUserName.setError("Email is required");
            binding.etUserName.requestFocus();
            return false;
        }
        if (!email.matches(emailPattern)){
            binding.etUserName.setError("Enter valid email");
            binding.etUserName.requestFocus();
            return false;
        }
        if (binding.etPassword.getText().toString().trim().length()==0){
            binding.etPassword.setError("Password is required");
            binding.etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void initialize() {
        mAuth = FirebaseAuth.getInstance();
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
    }
}