package com.nabeel.climatechange.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nabeel.climatechange.R;
import com.nabeel.climatechange.databinding.ActivityMainBinding;
import com.nabeel.climatechange.fragments.CarbonFootprintCalFragment;
import com.nabeel.climatechange.fragments.EWasteFragment;
import com.nabeel.climatechange.fragments.GreenResourceFragment;
import com.nabeel.climatechange.fragments.HomeFragment;
import com.nabeel.climatechange.fragments.PlantationFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp=null;
                switch (item.getItemId()){
                    case R.id.ewaste: temp=new EWasteFragment();
                    break;
                    case R.id.green_resource: temp=new GreenResourceFragment();
                    break;
                    case R.id.carbon_footprint_cal: temp=new CarbonFootprintCalFragment();
                    break;
                    case R.id.plantation: temp=new PlantationFragment();
                    break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, temp).commit();
                return true;
            }
        });
    }
}