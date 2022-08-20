package com.nabeel.climatechange.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nabeel.climatechange.databinding.CustomResourceLayoutBinding;

public class GreenResourceAdapter extends RecyclerView.Adapter<GreenResourceAdapter.ViewHolder>{

    CustomResourceLayoutBinding binding;
    int[] logo;
    String[] name;
    String[] description;

    public GreenResourceAdapter(int[] logo, String[] name, String[] description) {
        this.logo=logo;
        this.name=name;
        this.description=description;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = CustomResourceLayoutBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.resourceIcon.setImageResource(logo[position]);
        holder.binding.resourceName.setText(name[position]);
        holder.binding.resourceDescription.setText(description[position]);
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomResourceLayoutBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomResourceLayoutBinding.bind(itemView);
        }
    }
}
