package com.nabeel.climatechange.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nabeel.climatechange.R;
import com.nabeel.climatechange.databinding.ActivityAddPlantBinding;
import com.nabeel.climatechange.model.Plant;
import com.nabeel.climatechange.utils.CommonClass;
import com.nabeel.climatechange.utils.SharedPrefHelper;

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
    StorageReference storageReference;
    DatabaseReference databaseReference;
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPlantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar.getRoot());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolBar.textView.setText("New Plantation");

        initialize();

        binding.toolBar.logout.setOnClickListener(v -> {
            logoutDialog();
        });

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

        binding.btnSubmit.setOnClickListener(v -> {
            if (CommonClass.isInternetOn(getApplicationContext())) {
                sendDataToServer();
            }else {
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendDataToServer() {
        Plant plant = new Plant(plant_name, city_name, photo, CommonClass.getCurrentDate());
        FirebaseDatabase.getInstance().getReference("plant")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(CommonClass.getUniqueId())
                .setValue(plant).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Data save successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(AddPlantActivity.this, "Please try again!`", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initialize() {
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutDialog() {
        new AlertDialog.Builder(AddPlantActivity.this)
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.want_logout))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        sharedPrefHelper.setString("isLogin","");
                        Intent i = new Intent(getApplicationContext(),
                                LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        sharedPrefHelper.setString("uid", "");
                        sharedPrefHelper.setInt("isLogin",0);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.alerts)
                .show();
    }
}