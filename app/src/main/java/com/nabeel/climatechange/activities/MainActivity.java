package com.nabeel.climatechange.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nabeel.climatechange.R;
import com.nabeel.climatechange.databinding.ActivityMainBinding;
import com.nabeel.climatechange.fragments.CarbonFootprintCalFragment;
import com.nabeel.climatechange.fragments.EWasteFragment;
import com.nabeel.climatechange.fragments.GreenResourceFragment;
import com.nabeel.climatechange.fragments.HomeFragment;
import com.nabeel.climatechange.fragments.NewsFragment;
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
                    case R.id.home: temp=new HomeFragment();
                    break;
                    case R.id.green_resource: temp=new NewsFragment();
                    break;
                    case R.id.carbon_footprint_cal: temp=new CarbonFootprintCalFragment();
                    break;
                    case R.id.plantation: temp=new PlantationFragment();
                    break;
                    case R.id.game:
                        Toast.makeText(MainActivity.this, "This section not available", Toast.LENGTH_SHORT).show();
                        break;
                }

                if (temp!=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, temp).commit();
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (binding.bottomNavigation.getSelectedItemId()==R.id.home){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setIcon(R.drawable.alerts);
            builder.setTitle("Alert!");
            builder.setMessage(R.string.are_you_sure_to_want_to_exit_application);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "yes", then he is allowed to exit from application
                    finishAffinity();
                }
            });


            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    dialog.cancel();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else {
            binding.bottomNavigation.setSelectedItemId(R.id.home);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
        }
    }
}