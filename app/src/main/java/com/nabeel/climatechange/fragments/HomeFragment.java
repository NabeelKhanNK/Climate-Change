package com.nabeel.climatechange.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.google.firebase.auth.FirebaseAuth;
import com.nabeel.climatechange.R;
import com.nabeel.climatechange.RestAPIs.ClientAPI;
import com.nabeel.climatechange.RestAPIs.ClimateChange_API;
import com.nabeel.climatechange.activities.LoginActivity;
import com.nabeel.climatechange.databinding.FragmentHomeBinding;
import com.nabeel.climatechange.databinding.FragmentNewsBinding;
import com.nabeel.climatechange.model.AQIModelClass;
import com.nabeel.climatechange.model.AirQualityIndex;
import com.nabeel.climatechange.model.News;
import com.nabeel.climatechange.model.NewsModelClass;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.denzcoskun.imageslider.models.SlideModel;
import com.nabeel.climatechange.utils.AlertDialogClass;
import com.nabeel.climatechange.utils.CommonClass;
import com.nabeel.climatechange.utils.GPSLocation;
import com.nabeel.climatechange.utils.GpsUtils;
import com.nabeel.climatechange.utils.SharedPrefHelper;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    ArrayList<NewsModelClass> modelClassArrayList;
    ArrayList<AQIModelClass> aqiModelClassArrayList;
    ArrayList<SlideModel> imageList;
    public static final int GPS_REQUEST = 3033;
    boolean isGPS=false;
    String lat="0.0",lon="0.0";
    SharedPrefHelper sharedPrefHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolBar.getRoot());
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolBar.textView.setText("Home");
        sharedPrefHelper = new SharedPrefHelper(getContext());

        binding.toolBar.logout.setOnClickListener(v -> {
            logoutDialog();
        });

        imageList = new ArrayList<>();
        aqiModelClassArrayList = new ArrayList<>();

        getNews();

        binding.btnAqi.setOnClickListener(v -> {

            checkGPSEnable();
            if (isGPS==true){
                GPSLocation mGPS = new GPSLocation(getContext());
                lat = String.valueOf(mGPS.getLatitude());
                lon = String.valueOf(mGPS.getLongitude());
                if (CommonClass.isInternetOn(getContext())){
                    getAQI();
                }else {
                    Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }


    private void getNews(){
        modelClassArrayList = new ArrayList<>();
        ClientAPI.getClient().create(ClimateChange_API.class).getNews("climateChange", "en", ClientAPI.KEY).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                try {
                    if (response.isSuccessful()){
                        modelClassArrayList.addAll(response.body().getArticles());
                        if (modelClassArrayList.size()>10) {
                            for (int i = 10; i > 0; i--) {
                                String img = modelClassArrayList.get(i).getUrlToImage();
                                String title = modelClassArrayList.get(i).getTitle();
                                imageList.add(new SlideModel(img,title, ScaleTypes.CENTER_CROP));
                            }
                        }else {
                            for (int i = 0; i < modelClassArrayList.size(); i++) {
                                String img = modelClassArrayList.get(i).getUrlToImage();
                                String title = modelClassArrayList.get(i).getTitle();
                                imageList.add(new SlideModel(img, title, ScaleTypes.CENTER_CROP));
                            }
                        }
                        binding.imageSlider.setImageList(imageList);
                    }
                }catch (Exception e){
                    Log.d("hhjh", ""+e);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d("fail", ""+t);
            }
        });
    }

    private void getAQI(){
        AlertDialogClass.showProgressDialog(getContext());
        ClientAPI.getAQI().create(ClimateChange_API.class).getAqi(lat, lon, "1").enqueue(new Callback<AirQualityIndex>() {
            @Override
            public void onResponse(Call<AirQualityIndex> call, Response<AirQualityIndex> response) {
                try {
                    if (response.isSuccessful()){
                        AlertDialogClass.dismissProgressDialog();
                        aqiModelClassArrayList.addAll(response.body().getData());
                        if (aqiModelClassArrayList.size()>0) {
                            showPopup(Float.parseFloat(String.valueOf(aqiModelClassArrayList.get(0).getAqi())));
                        }
                    }else {
                        AlertDialogClass.dismissProgressDialog();
                    }
                }catch (Exception e){
                    Log.d("hhjh", ""+e);
                    AlertDialogClass.dismissProgressDialog();
                    Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AirQualityIndex> call, Throwable t) {
                Log.d("fail", ""+t);
                AlertDialogClass.dismissProgressDialog();
                Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPopup(float result) {
        String message = "";
        int img = R.drawable.green_circle;
        if (result<=50f){
            message = "Air quality is satisfactory, and air pollution poses little or no risk.";
        }
        if (result>51f && result<=100f){
            img = R.drawable.yellow_circle;
            message = "Air quality is acceptable. However, there may be a risk for some people, particularly those who are unusually sensitive to air pollution.";
        }
        if (result>100f && result<=150f){
            img = R.drawable.circle_org;
            message = "Members of sensitive groups may experience health effects. The general public is less likely to be affected.";
        }
        if (result>151f && result<=200f){
            img = R.drawable.red_circle;
            message = "Some members of the general public may experience health effects; members of sensitive groups may experience more serious health effects.";
        }
        if (result>201f && result<=300f){
            img = R.drawable.circle_purple;
            message = "Health alert: The risk of health effects is increased for everyone.";
        }
        if (result>301f){
            img = R.drawable.circle_maroon;
            message = "Health warning of emergency conditions: everyone is more likely to be affected.";
        }
        final SweetAlertDialog pDialog = new SweetAlertDialog(
                getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        //new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        pDialog.setTitleText("AQI : "+result)
                .setContentText(message)
                .setCustomImage(img)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .show();
        pDialog.setCancelable(false);
    }

    public void checkGPSEnable(){
        new GpsUtils(getContext()).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == GPS_REQUEST) {
                isGPS=false;
            }
        }
    }

    private void logoutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.want_logout))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        sharedPrefHelper.setString("isLogin","");
                        Intent i = new Intent(getContext(),
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