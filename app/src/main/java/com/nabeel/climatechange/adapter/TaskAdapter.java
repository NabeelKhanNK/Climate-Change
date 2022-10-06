package com.nabeel.climatechange.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nabeel.climatechange.R;
import com.nabeel.climatechange.databinding.CustomPlantBinding;
import com.nabeel.climatechange.databinding.CustomTaskBinding;
import com.nabeel.climatechange.fragments.TaskFragment;
import com.nabeel.climatechange.interfaces.ItemClickListener;
import com.nabeel.climatechange.model.TaskPojo;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    ArrayList<TaskPojo> taskArrayList;
    CustomTaskBinding binding;
    ItemClickListener itemClickListener;

    public TaskAdapter(Context context, ArrayList<TaskPojo> taskArrayList, ItemClickListener itemClickListener) {
        this.context=context;
        this.taskArrayList=taskArrayList;
        this.itemClickListener=itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = CustomTaskBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.taskTitle.setText(taskArrayList.get(position).getTitle());
        String desc = taskArrayList.get(position).getDescription().replace("\\n", "\n");
        holder.binding.taskDetails.setText(desc);
        if (!taskArrayList.get(position).getImg().equals("")) {
            try {
                Picasso.get()
                        .load(taskArrayList.get(position).getImg())
                        .placeholder(R.drawable.ic_baseline_collections_24)
                        .into(holder.binding.taskImg);
            } catch (Exception e) {
                Log.d("Exception", "" + e);
            }
        }
        holder.binding.cvView.setOnClickListener(v -> {
            itemClickListener.onClick(position, taskArrayList.get(position).getTitle(), desc);
        });
    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTaskBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CustomTaskBinding.bind(itemView);
        }
    }
}
