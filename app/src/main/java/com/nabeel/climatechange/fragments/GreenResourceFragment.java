package com.nabeel.climatechange.fragments;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nabeel.climatechange.R;
import com.nabeel.climatechange.adapter.GreenResourceAdapter;
import com.nabeel.climatechange.databinding.FragmentCarbonFootprintCalBinding;
import com.nabeel.climatechange.databinding.FragmentGreenResourceBinding;


public class GreenResourceFragment extends Fragment {

    FragmentGreenResourceBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGreenResourceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolBar.getRoot());
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolBar.textView.setText("Green Resources");

        int[] logo = {R.drawable.e_waste, R.drawable.cfc};
        String[] name = {"Klima-Live carbon neutral","Disaster Alert"};
        String[] description = {"Klima is the no.1 app to take instant climate action! Calculate your carbon footprint and neutralize 100% of your CO2e emissions in just three minutes. How? By funding science-based climate projects that capture or prevent the same emissions elsewhere. Next, learn how to shrink your own footprint sustainably and watch your positive impact grow.",
        "Disaster Alert is a free mobile app for public use that provides the global community with critical hazard alerts and information needed to stay safe anywhere in the world. Built on PDC’s DisasterAWARE®️ platform, Disaster Alert™️ offers near real-time updates about 18 different types of natural hazards as they are unfolding around the globe.",
        };

        GreenResourceAdapter adapter = new GreenResourceAdapter(logo,name,description);
        binding.rvResources.setHasFixedSize(true);
        binding.rvResources.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvResources.setAdapter(adapter);

        return view;
    }
}