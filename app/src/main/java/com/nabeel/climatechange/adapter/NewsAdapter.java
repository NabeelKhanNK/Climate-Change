package com.nabeel.climatechange.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nabeel.climatechange.databinding.CustomNewsBinding;
import com.nabeel.climatechange.databinding.CustomResourceLayoutBinding;
import com.nabeel.climatechange.model.NewsModelClass;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    CustomNewsBinding binding;
    Context context;
    ArrayList<NewsModelClass> modelClassArrayList;

    public NewsAdapter(Context context, ArrayList<NewsModelClass> modelClassArrayList) {
        this.context=context;
        this.modelClassArrayList=modelClassArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = CustomNewsBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.mainheading.setText(modelClassArrayList.get(position).getAuthor());
        holder.binding.description.setText(modelClassArrayList.get(position).getDescription());
        holder.binding.author.setText(modelClassArrayList.get(position).getAuthor());
        holder.binding.time.setText("Publish At: "+modelClassArrayList.get(position).getPublishedAt());
    }

    @Override
    public int getItemCount() {
        return modelClassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomNewsBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomNewsBinding.bind(itemView);
        }
    }
}
