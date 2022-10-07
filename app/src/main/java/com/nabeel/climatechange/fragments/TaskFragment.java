package com.nabeel.climatechange.fragments;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nabeel.climatechange.R;
import com.nabeel.climatechange.activities.AddPlantActivity;
import com.nabeel.climatechange.databinding.FragmentDailyTaskBinding;
import com.nabeel.climatechange.databinding.FragmentTaskBinding;
import com.nabeel.climatechange.model.Plant;
import com.nabeel.climatechange.model.TaskPojo;
import com.nabeel.climatechange.model.UserTaskDataPojo;
import com.nabeel.climatechange.utils.CommonClass;


public class TaskFragment extends BottomSheetDialogFragment {

    FragmentTaskBinding binding;
    BottomSheetDialog dialog;
    BottomSheetBehavior<View> bottomSheetBehavior;
    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolBar.getRoot());
        binding.toolBar.textView.setText("Task");
        binding.toolBar.logout.setVisibility(View.GONE);
//        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.toolBar.getRoot().setNavigationIcon(R.drawable.ic_baseline_close_24);
        binding.toolBar.getRoot().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior = BottomSheetBehavior.from((View) rootView.getParent());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        if (getArguments() != null){
            binding.taskTitle.setText(getArguments().getString("task_title"));
            binding.taskDetails.setText(getArguments().getString("task_desc"));
        }

        binding.btnSubmit.setOnClickListener(v -> {
            if (binding.etTask.getText().toString().trim().length()!=0) {
                sendTaskData();
            }else {
                binding.etTask.setError("This field is required");
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        CoordinatorLayout layout = dialog.findViewById(R.id.bottom_sheet_dialog);
        assert layout != null;
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);

    }

    private void sendTaskData(){
        UserTaskDataPojo userTask = new UserTaskDataPojo(binding.etTask.getText().toString().trim(), CommonClass.getCurrentDate(), "Task Completed");
        FirebaseDatabase.getInstance().getReference("user_task_data")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getArguments().getString("task_id"))
                .setValue(userTask).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Task Completed Successfully!", Toast.LENGTH_SHORT).show();
                            bottomSheetBehavior = BottomSheetBehavior.from((View) rootView.getParent());
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }else {
                            Toast.makeText(getContext(), "Please try again!`", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}