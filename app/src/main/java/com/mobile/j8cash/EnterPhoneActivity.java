package com.mobile.j8cash;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.aquery.AQuery;
import com.hbb20.CountryCodePicker;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mobile.j8cash.models.Country;
import com.mobile.j8cash.models.User;
import com.mobile.j8cash.models.VerificationCode;
import com.mobile.j8cash.utils.Utils;

import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPhoneActivity extends BaseActivity {
    private AQuery aq;
    private String country;
    private String phone_code;
    private CountryCodePicker countryPicker;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Create Account");

        aq = new AQuery(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Code...");
        progressDialog.setCancelable(false);
        countryPicker = findViewById(R.id.ccp_country);
        country = countryPicker.getDefaultCountryName();
        phone_code = countryPicker.getDefaultCountryCode();

        countryPicker.setOnCountryChangeListener(() -> {
            phone_code = countryPicker.getSelectedCountryCode();
            country = countryPicker.getDefaultCountryName();
            aq.id(R.id.phone_code_text).text("+"+phone_code);
        });

//        MaterialSpinner spinner = findViewById(R.id.spinner);
//        spinner.setItems(Country.getCountries().get(0).name, Country.getCountries().get(1).name);
//        spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
//
//            if(position == 0){
//                aq.id(R.id.phone_code_text).text(Utils.addPlusBeforeNumber(Country.getCountries().get(0).phoneCode));
//                country_id = 1;
//                phone_code = Country.getCountries().get(0).phoneCode;
//            }
//            if(position == 1){
//                aq.id(R.id.phone_code_text).text(Utils.addPlusBeforeNumber(Country.getCountries().get(1).phoneCode));
//                country_id = 2;
//                phone_code = Country.getCountries().get(1).phoneCode;
//            }
//        });

        findViewById(R.id.btn_save_phone).setOnClickListener(k->{
            sendVerificationCode();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void sendVerificationCode(){

        String phone = aq.id(R.id.input_phone).text();

        if(phone.length() != 9){
            aq.toast("Enter number e.g 78XXXXXXX");
            return;

        }

        progressDialog.show();

        Call<VerificationCode> call = RetrofitClient.getInstance().create(APIInterface.class).sendCode("+"+phone_code+phone);
        call.enqueue(new Callback<VerificationCode>() {
            @Override
            public void onResponse(Call<VerificationCode> call, Response<VerificationCode> response) {
                if(response.isSuccessful()){
                    progressDialog.hide();
                    if(response.body().status == 1){
                        // Go to Next Activity
                        Utils.log("Phone "+response.body().phone);

                        Intent i = new Intent(EnterPhoneActivity.this, VerifyPhoneActivity.class);
                        i.putExtra("Phone", phone_code+phone);
                        i.putExtra("Country", country);
                        startActivity(i);
                    }else{
                        aq.toast("An error occurred, try again later");

                    }

                }else{
                    progressDialog.hide();
                    aq.toast("An error occurred, try again later");

                }
            }

            @Override
            public void onFailure(Call<VerificationCode> call, Throwable t) {
                progressDialog.hide();
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("An error occurred, try again later");
                }
            }
        });

    }
}
