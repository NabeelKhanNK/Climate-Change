package com.nabeel.climatechange.adapter;

import static android.graphics.Color.YELLOW;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabeel.climatechange.R;
import com.nabeel.climatechange.activities.LoginActivity;
import com.nabeel.climatechange.activities.RegistrationActivity;
import com.nabeel.climatechange.databinding.CustomPlantBinding;
import com.nabeel.climatechange.databinding.CustomTaskBinding;
import com.nabeel.climatechange.fragments.TaskFragment;
import com.nabeel.climatechange.interfaces.ItemClickListener;
import com.nabeel.climatechange.model.TaskPojo;
import com.nabeel.climatechange.utils.AlertDialogClass;
import com.nabeel.climatechange.utils.CommonClass;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    ArrayList<TaskPojo> taskArrayList;
    CustomTaskBinding binding;
    ItemClickListener itemClickListener;
    DatabaseReference databaseReference;

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

    @SuppressLint("ResourceAsColor")
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

        if (taskArrayList.get(position).getCreated_at().equals("") && taskArrayList.get(position).getExp_date().equals("")){
            holder.binding.taskStatus.setText("Upcoming Task");
            holder.binding.taskStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("user_task_data").child(FirebaseAuth.getInstance().getUid()).child(String.valueOf(taskArrayList.get(position).getId()));
        getTaskStatus(taskArrayList.get(position).getId(), holder.binding.taskStatus);


            holder.binding.cvView.setOnClickListener(v -> {
                String task_status = holder.binding.taskStatus.getText().toString();
                if (task_status.equals("Upcoming Task")){
                    showPopupForUpcomingTask();
                }else if (holder.binding.taskStatus.getText().toString().equals("Completed")) {
                    showPopupForCompletedTask();
                }else {
                    itemClickListener.onClick(position, taskArrayList.get(position).getId(), taskArrayList.get(position).getTitle(), desc);
                }
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

    private void getTaskStatus(Long id, TextView taskStatus) {
        if (CommonClass.isInternetOn(context)) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String status="";
                        for (int i = 0; i < snapshot.getKey().length(); i++) {
                            status = snapshot.child("status").getValue().toString();
                        }
                        if (status.equals("Task Completed")){
                            taskStatus.setText("Completed");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void showPopupForCompletedTask() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(
                context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        //new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        pDialog.setTitleText("Completed")
                .setContentText("This task already completed by you.")
                .setConfirmText("OK")
                .setCustomImage(R.drawable.tick_mark)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .show();
        pDialog.setCancelable(false);
    }

    private void showPopupForUpcomingTask() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(
                context, SweetAlertDialog.WARNING_TYPE);
        //new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        pDialog.setTitleText("Upcoming Task")
                .setContentText("This task is not active. Please wait for it or checkout other tasks :)")
                .setConfirmText("OK")
//                .setCustomImage(R.drawable.tick_mark)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .show();
        pDialog.setCancelable(false);
    }
}
