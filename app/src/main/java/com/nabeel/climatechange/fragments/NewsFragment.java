package com.nabeel.climatechange.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.nabeel.climatechange.R;
import com.nabeel.climatechange.RestAPIs.ClientAPI;
import com.nabeel.climatechange.RestAPIs.ClimateChange_API;
import com.nabeel.climatechange.activities.LoginActivity;
import com.nabeel.climatechange.adapter.NewsAdapter;
import com.nabeel.climatechange.databinding.FragmentGreenResourceBinding;
import com.nabeel.climatechange.databinding.FragmentNewsBinding;
import com.nabeel.climatechange.model.News;
import com.nabeel.climatechange.model.NewsModelClass;
import com.nabeel.climatechange.utils.CommonClass;
import com.nabeel.climatechange.utils.SharedPrefHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment {

    FragmentNewsBinding binding;
    ProgressDialog dialog;
    ArrayList<NewsModelClass> modelClassArrayList;
    NewsAdapter adapter;
    SharedPrefHelper sharedPrefHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolBar.getRoot());
        binding.toolBar.textView.setText("News");
        sharedPrefHelper= new SharedPrefHelper(getContext());

        binding.toolBar.logout.setOnClickListener(v -> {
            logoutDialog();
        });

        modelClassArrayList = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new NewsAdapter(getContext(), modelClassArrayList);
        binding.rvNews.setLayoutManager(mLayoutManager);
        binding.rvNews.setAdapter(adapter);
        if (CommonClass.isInternetOn(getContext())) {
            getNews();
        }else {
            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void getNews(){
        dialog = ProgressDialog.show(getContext(), "", "Please wait...", true);

        ClientAPI.getClient().create(ClimateChange_API.class).getNews("climateChange", "en", ClientAPI.KEY).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                try {
                    if (response.isSuccessful()){
                        modelClassArrayList.addAll(response.body().getArticles());
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }catch (Exception e){
                    Log.d("hhjh", ""+e);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d("fail", ""+t);
                dialog.dismiss();
            }
        });
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