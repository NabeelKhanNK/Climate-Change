package com.nabeel.climatechange.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.nabeel.climatechange.R;
import com.nabeel.climatechange.utils.SharedPrefHelper;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private static final int REQUEST = 112;
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());

        callNextActivity();
    }

    private void callNextActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPrefHelper.getInt("isLogin",0)==1){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}