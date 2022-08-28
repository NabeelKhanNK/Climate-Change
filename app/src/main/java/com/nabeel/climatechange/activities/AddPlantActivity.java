package com.nabeel.climatechange.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.nabeel.climatechange.R;
import com.nabeel.climatechange.databinding.ActivityAddPlantBinding;

import java.io.ByteArrayOutputStream;

public class AddPlantActivity extends AppCompatActivity {

    ActivityAddPlantBinding binding;
    String[] plant = {"Select Plant","Alder","Banana","Jamun","Neem Tree","Other"};
    String[] city = {"Select City","Ahmedabad","Banglore","Chennai","Delhi","Indore","Jaipur","Pune"};
    String plant_name,city_name;
    String cameraPermission[];
    private static final int CAMERA_REQUEST = 100;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String photo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPlantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar.getRoot());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolBar.textView.setText("New Plantation");

        ArrayAdapter<String> adapter_plant=new ArrayAdapter<String>(this, R.layout.spinner_list,plant);
        adapter_plant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnPlant.setAdapter(adapter_plant);

        binding.spnPlant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                plant_name="";
                if (!binding.spnPlant.getSelectedItem().equals("Select Plant")){
                    if (binding.spnPlant.getSelectedItem().equals("Other")){
                        binding.txtPlantName.setVisibility(View.VISIBLE);
                        binding.etxtPlantName.setVisibility(View.VISIBLE);
                    }else {
                        binding.txtPlantName.setVisibility(View.GONE);
                        binding.etxtPlantName.setVisibility(View.GONE);
                    }
                    plant_name=binding.spnPlant.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter_city=new ArrayAdapter<String>(this, R.layout.spinner_list,city);
        adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnCity.setAdapter(adapter_city);

        binding.spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_name="";
                if (!binding.spnCity.getSelectedItem().equals("Select City")){
                    city_name=binding.spnCity.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.camera.setOnClickListener(v -> {
            if (!checkCameraPermission()) {
                requestCameraPermission();
            } else {
                openCamera();
            }
        });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }catch (ActivityNotFoundException e){
            Log.d("error",""+e);}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                photo = Base64.encodeToString(bytes,Base64.DEFAULT);
                Log.d("img", ""+photo);
                binding.camera.setImageBitmap(bitmap);
            }catch (Exception e){}
        }
    }

    // checking camera permissions
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        cameraPermission = new String[]{Manifest.permission.CAMERA};
//        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // Requesting camera permission
    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(cameraPermission, CAMERA_REQUEST);
        }
    }
}