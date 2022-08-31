package com.nabeel.climatechange.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabeel.climatechange.R;
import com.nabeel.climatechange.activities.AddPlantActivity;
import com.nabeel.climatechange.activities.LoginActivity;
import com.nabeel.climatechange.adapter.NewsAdapter;
import com.nabeel.climatechange.adapter.PlantAdapter;
import com.nabeel.climatechange.databinding.FragmentGreenResourceBinding;
import com.nabeel.climatechange.databinding.FragmentPlantationBinding;
import com.nabeel.climatechange.model.Plant;
import com.nabeel.climatechange.utils.CommonClass;
import com.nabeel.climatechange.utils.SharedPrefHelper;

import java.util.ArrayList;


public class PlantationFragment extends Fragment {

    FragmentPlantationBinding binding;
    ArrayList<Plant> plantArrayList;
    DatabaseReference databaseReference;
    PlantAdapter adapter;
    SharedPrefHelper sharedPrefHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPlantationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolBar.getRoot());
        binding.toolBar.textView.setText("My Plantations");
        sharedPrefHelper = new SharedPrefHelper(getContext());

        binding.addPlant.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddPlantActivity.class));
        });

        binding.toolBar.logout.setOnClickListener(v -> {
            logoutDialog();
        });

        String node = FirebaseAuth.getInstance().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("plant").child(node);
        plantArrayList = new ArrayList<>();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new PlantAdapter(getContext(), plantArrayList);
        binding.rvPlant.setLayoutManager(mLayoutManager);
        binding.rvPlant.setAdapter(adapter);

        if (CommonClass.isInternetOn(getContext())) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    plantArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Plant plant = dataSnapshot.getValue(Plant.class);
                        plantArrayList.add(plant);
                    }
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }

        return view;
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