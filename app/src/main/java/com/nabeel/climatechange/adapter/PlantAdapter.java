package com.nabeel.climatechange.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nabeel.climatechange.R;
import com.nabeel.climatechange.databinding.CustomNewsBinding;
import com.nabeel.climatechange.databinding.CustomPlantBinding;
import com.nabeel.climatechange.model.Plant;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder>{

    CustomPlantBinding binding;
    Context context;
    ArrayList<Plant> plantArrayList;

    public PlantAdapter(Context context, ArrayList<Plant> plantArrayList) {
        this.context=context;
        this.plantArrayList=plantArrayList;
    }

    @NonNull
    @Override
    public PlantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = CustomPlantBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull PlantAdapter.ViewHolder holder, int position) {
        holder.binding.plantName.setText(plantArrayList.get(position).getPlant_name());
        holder.binding.cityName.setText(plantArrayList.get(position).getState_name());
        holder.binding.plantationDate.setText(plantArrayList.get(position).getDate());
        if (!plantArrayList.get(position).getImage().equals("")) {
            byte[] decodedString = Base64.decode(plantArrayList.get(position).getImage(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.binding.image.setImageBitmap(bitmap);
        }else {
            holder.binding.image.setImageResource(R.drawable.plant);
        }
    }

    @Override
    public int getItemCount() {
        return plantArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomPlantBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomPlantBinding.bind(itemView);
        }
    }
}
