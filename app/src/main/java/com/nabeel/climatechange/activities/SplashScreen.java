package com.nabeel.climatechange.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nabeel.climatechange.R;
import com.nabeel.climatechange.utils.SharedPrefHelper;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
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