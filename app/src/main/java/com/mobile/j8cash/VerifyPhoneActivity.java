package com.mobile.j8cash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.aquery.AQuery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobile.j8cash.models.VerificationCode;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPhoneActivity extends BaseActivity {

    private OtpView otpView;
    private String phone;
    private ProgressDialog progressDialog;
    private AQuery aq;
    private String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Verify Phone Number");

        aq = new AQuery(this);
        phone = getIntent().getStringExtra("Phone");
        country = getIntent().getStringExtra("Country");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verifying number...");
        progressDialog.setCancelable(false);
        otpView = findViewById(R.id.otp_view);
        otpView.setOtpCompletionListener(code->{
            verify(code);
        });

//        findViewById(R.id.btn_resend_code).setOnClickListener(k->{
//            //startActivity(new Intent(this, SignupActivity.class));
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void verify(String code) {
        progressDialog.show();
        Call<VerificationCode> call = RetrofitClient.getInstance().create(APIInterface.class).confirmCode("+" + phone, code);
        call.enqueue(new Callback<VerificationCode>() {
            @Override
            public void onResponse(Call<VerificationCode> call, Response<VerificationCode> response) {
                if (response.isSuccessful()) {
                    progressDialog.hide();
                    if (response.body().status == 1) {
                        // Go to Next Activity

                        Intent i = new Intent(VerifyPhoneActivity.this, SignupActivity.class);
                        i.putExtra("Phone", phone);
                        i.putExtra("Country", country);
                        startActivity(i);
                        finish();
                    } else {
//                        Intent i = new Intent(VerifyPhoneActivity.this, SignupActivity.class);
//                        i.putExtra("Phone", phone);
//                        startActivity(i);
                        finish();
                        aq.toast("Your verification code is incorrect");

                    }

                } else {
                    progressDialog.hide();
                    aq.toast("An error occurred, please try again");

                }
            }

            @Override
            public void onFailure(Call<VerificationCode> call, Throwable t) {
                progressDialog.hide();
                if (t instanceof IOException) {
                    aq.toast("No internet connection");

                } else {
                    aq.toast("System failure. Try again later");
                }
            }
        });

    }
}
