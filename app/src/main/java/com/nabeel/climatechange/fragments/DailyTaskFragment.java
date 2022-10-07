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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabeel.climatechange.R;
import com.nabeel.climatechange.activities.LoginActivity;
import com.nabeel.climatechange.adapter.PlantAdapter;
import com.nabeel.climatechange.adapter.TaskAdapter;
import com.nabeel.climatechange.databinding.FragmentDailyTaskBinding;
import com.nabeel.climatechange.databinding.FragmentPlantationBinding;
import com.nabeel.climatechange.interfaces.ItemClickListener;
import com.nabeel.climatechange.model.Plant;
import com.nabeel.climatechange.model.TaskPojo;
import com.nabeel.climatechange.utils.AlertDialogClass;
import com.nabeel.climatechange.utils.CommonClass;
import com.nabeel.climatechange.utils.SharedPrefHelper;

import java.util.ArrayList;


public class DailyTaskFragment extends Fragment {

    FragmentDailyTaskBinding binding;
    SharedPrefHelper sharedPrefHelper;
    ArrayList<TaskPojo> taskArrayList;
    DatabaseReference databaseReference;
    TaskAdapter adapter;
    ItemClickListener clickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDailyTaskBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolBar.getRoot());
        binding.toolBar.textView.setText("Daily Task");
        sharedPrefHelper = new SharedPrefHelper(getContext());

        clickListener = new ItemClickListener() {
            @Override
            public void onClick(int pos,Long id, String title, String desc) {
                TaskFragment taskFragment = new TaskFragment();
                Bundle args = new Bundle();
                args.putString("task_title",title);
                args.putString("task_desc",desc);
                args.putString("task_id", String.valueOf(id));
                taskFragment.setArguments(args);
                taskFragment.show(getFragmentManager(), taskFragment.getTag());
            }
        };

        binding.toolBar.logout.setOnClickListener(v -> {
            logoutDialog();
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("daily_task");  //CommonClass.getCurrentDay()
        taskArrayList = new ArrayList<>();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new TaskAdapter(getContext(), taskArrayList, clickListener);
        binding.rvTask.setLayoutManager(mLayoutManager);
        binding.rvTask.setAdapter(adapter);

        getDailyTasks();

        return view;
    }

    private void getDailyTasks(){
        if (CommonClass.isInternetOn(getContext())) {
            AlertDialogClass.showProgressDialog(getContext());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    taskArrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            TaskPojo taskPojo = dataSnapshot.getValue(TaskPojo.class);
                            taskArrayList.add(taskPojo);
                        }
                    AlertDialogClass.dismissProgressDialog();
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
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