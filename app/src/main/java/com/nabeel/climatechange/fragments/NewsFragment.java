package com.nabeel.climatechange.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nabeel.climatechange.R;
import com.nabeel.climatechange.RestAPIs.ClientAPI;
import com.nabeel.climatechange.RestAPIs.ClimateChange_API;
import com.nabeel.climatechange.adapter.NewsAdapter;
import com.nabeel.climatechange.databinding.FragmentGreenResourceBinding;
import com.nabeel.climatechange.databinding.FragmentNewsBinding;
import com.nabeel.climatechange.model.News;
import com.nabeel.climatechange.model.NewsModelClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment {

    FragmentNewsBinding binding;
    ProgressDialog dialog;
    ArrayList<NewsModelClass> modelClassArrayList;
    NewsAdapter adapter;

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
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolBar.textView.setText("News");

        modelClassArrayList = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter = new NewsAdapter(getContext(), modelClassArrayList);
        binding.rvNews.setLayoutManager(mLayoutManager);
        binding.rvNews.setAdapter(adapter);
        getNews();

        return view;
    }

    private void getNews(){
        dialog = ProgressDialog.show(getContext(), "", "Please wait...", true);

        ClientAPI.getClient().create(ClimateChange_API.class).getNews("ClimateChange", ClientAPI.KEY).enqueue(new Callback<News>() {
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
}