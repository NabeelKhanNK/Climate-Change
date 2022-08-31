package com.nabeel.climatechange.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.nabeel.climatechange.R;
import com.nabeel.climatechange.databinding.ActivityNewsBinding;
import com.nabeel.climatechange.utils.SharedPrefHelper;

public class NewsActivity extends AppCompatActivity {

    ActivityNewsBinding binding;
    String url = "";
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar.getRoot());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolBar.textView.setText("News ");
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            url = bundle.getString("url","");
            binding.webView.setWebViewClient(new WebViewClient());
            binding.webView.loadUrl(url);
            WebSettings webSettings = binding.webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }

        binding.toolBar.logout.setOnClickListener(v -> {
            logoutDialog();
        });


    }



    @Override
    public void onBackPressed() {
        if (binding.webView.canGoBack()){
            binding.webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutDialog() {
        new AlertDialog.Builder(NewsActivity.this)
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.want_logout))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        sharedPrefHelper.setString("isLogin","");
                        Intent i = new Intent(getApplicationContext(),
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