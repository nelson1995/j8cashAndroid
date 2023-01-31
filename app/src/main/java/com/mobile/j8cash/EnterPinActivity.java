package com.mobile.j8cash;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.aquery.AQuery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobile.j8cash.models.PINResponse;
import com.mobile.j8cash.models.User;
import com.mukesh.OtpView;
import com.pixplicity.easyprefs.library.Prefs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPinActivity extends BaseActivity {

    private OtpView otpView;
    boolean isSet = false;
    String pin1;
    String pin2;
    private AQuery aq;
    private int user_id;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Create PIN");

        user_id = getIntent().getIntExtra("UserId", 0);

        aq = new AQuery(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Creating PIN...");
        otpView = findViewById(R.id.otp_view);
        otpView.setOtpCompletionListener(l->{
            Log.d("onOtpCompleted=>", l);

            if (!isSet){
                pin1 = l;
                // Clear pin and toast
                isSet = true;
                aq.toast("Enter the PIN again");
                otpView.setText("");

            } else {
                // Verify if the two pins match
                pin2 = l;

                if(pin1.equals(pin2)){
                    createPIN(pin1);
                }else{
                    aq.toast("PIN does not match. Try Again");
                    isSet = false;
                    otpView.setText("");
                }
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }

    private void createPIN(String pin){

        if(user_id == 0){
            aq.toast("Start Registration");
            startActivity(new Intent(this, EnterPhoneActivity.class));
            finish();
            return;
        }

        progressDialog.show();

        Call<PINResponse> call = RetrofitClient.getInstance().create(APIInterface.class).create_pin(user_id, pin);
        call.enqueue(new Callback<PINResponse>() {
            @Override
            public void onResponse(Call<PINResponse> call, Response<PINResponse> response) {
                progressDialog.hide();
                if(response.isSuccessful()){
                    PINResponse resp = response.body();
                    if(resp.status == 1){
                        aq.toast(resp.message);
                        Intent intent = new Intent(EnterPinActivity.this, MainActivity.class);
                        intent.putExtra("Register", 1);
                        startActivity(intent);
                        finish();
                    }else{
                        aq.toast(resp.message);
                    }

                }else{
                    aq.toast("An error occurred");
                }
            }

            @Override
            public void onFailure(Call<PINResponse> call, Throwable t) {
                progressDialog.hide();
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("Failed to create account. Try again later");
                }
            }
        });
    }



}
