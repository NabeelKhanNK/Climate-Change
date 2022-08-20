package com.nabeel.climatechange.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nabeel.climatechange.R;
import com.nabeel.climatechange.databinding.FragmentGreenResourceBinding;
import com.nabeel.climatechange.databinding.FragmentPlantationBinding;


public class PlantationFragment extends Fragment {

    FragmentPlantationBinding binding;

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
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolBar.textView.setText("My Plantations");

        return view;
    }
}