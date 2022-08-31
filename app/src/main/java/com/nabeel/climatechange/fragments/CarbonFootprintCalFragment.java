package com.nabeel.climatechange.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nabeel.climatechange.R;
import com.nabeel.climatechange.activities.LoginActivity;
import com.nabeel.climatechange.databinding.FragmentCarbonFootprintCalBinding;
import com.nabeel.climatechange.utils.SharedPrefHelper;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class CarbonFootprintCalFragment extends Fragment {

    FragmentCarbonFootprintCalBinding binding;
    int selection = 0;
    SharedPrefHelper sharedPrefHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCarbonFootprintCalBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolBar.getRoot());
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolBar.textView.setText("Carbon Footprint Calculator");
        sharedPrefHelper = new SharedPrefHelper(getContext());

        binding.toolBar.logout.setOnClickListener(v -> {
            logoutDialog();
        });

        String[] list = getResources().getStringArray(R.array.activities);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
        binding.spinner.setAdapter(adapter);



        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                binding.editText.setText("");

                selection = position;
                if (selection > 0) {
                    Log.i("spinner", "selection made");
                    binding.button.setEnabled(true);
                }else {
                    binding.button.setEnabled(false);
                }

                switch (selection) {
                    case 0: //empty
                        binding.textView4.setText("");
                        break;
                    case 1: //driving
                        binding.textView4.setText("How many miles did you drive?");
                        break;
                    case 2: //bus
                    case 3: //subway or train
                    case 4: //flight
                        binding.textView4.setText("How many miles did you travel?");
                        break;
                    case 5: //electricity uses
                        binding.textView4.setText("How many kWh did you use?");
                        break;
                    case 6: //eating beef, pork, or lamb
                    case 7: //eating fruits or vegetables
                        binding.textView4.setText("How many ounces?");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.i("calc", "button clicked");

                int number = -1;
                if (!isEmpty(binding.editText)) {
                    number = Integer.parseInt(binding.editText.getText().toString());
                }

                if (0 <= number && number <= 4000) {
                    int result = 0;
                    String info = "grams of CO2 equivalents emitted";

                    //diff calc for each case
                    switch (selection) {
                        case 1: //driving
                            result = calculate(number, 444);
                            info = "grams of CO2 equivalents emitted, assuming 20 MPG";
                            break;
                        case 2: //bus
                            result = calculate(number, 107);
                            break;
                        case 3: //subway or train
                            result = calculate(number, 163);
                            break;
                        case 4: //flight
                            if (number < 400) {
                                result = calculate(number, 254);
                            }
                            else if (number < 1500) {
                                result = calculate(number, 204);
                            }
                            else if (number < 3000) {
                                result = calculate(number, 181);
                            }
                            else {
                                result = calculate(number, 172);
                            }
                            break;
                        case 5: //electricity uses
                            result = calculate(number, 835);
                            info = "grams of CO2 equivalents emitted, and the average person in the US uses 12 kWh per day";
                            break;
                        case 6: //eating beef, pork, or lamb
                            result = calculate(number, 445);
                            break;
                        case 7: //eating fruits or vegetables
                            result = calculate(number, 50);
                            break;
                    }
                    showPopup(result, info);
//                    binding.textView2.setText(getString(R.string.showResult, (int) result));
//                    binding.textView3.setText(info);
                }
                else {
                    Toast.makeText(getContext(), "Type a number [0, 4000]", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private int calculate(int input, int multiplier) {
        int result = 0;
        result = input * multiplier;
        return result;
    }

    private boolean isEmpty(EditText e) {
        return e.getText().toString().trim().length() == 0;
    }

    private void showPopup(int result, String info) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(
                getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        //new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        pDialog.setTitleText(getString(R.string.showResult, (int) result))
                .setContentText(info)
                .setCustomImage(R.drawable.carbon_footprint)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .show();
        pDialog.setCancelable(false);
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