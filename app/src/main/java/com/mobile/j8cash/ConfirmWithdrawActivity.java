package com.mobile.j8cash;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.aquery.AQuery;
import com.google.gson.JsonObject;
import com.mobile.j8cash.models.Withdraw;
import com.mobile.j8cash.models.WithdrawResponse;
import com.mobile.j8cash.utils.Constants;
import com.mobile.j8cash.utils.Utils;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmWithdrawActivity extends BaseActivity {
    AQuery aq;
    String phoneCode;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_withdraw);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Approve Withdraw");

        aq = new AQuery(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        Intent intent = getIntent();
        if(intent == null){
            finish();
            return;
        }

        String fee = intent.getStringExtra("Fee");
        int amount = intent.getIntExtra("Amount", 0);
        String phone = intent.getStringExtra("Phone");

        TextView textView = findViewById(R.id.text_view);
        String text = "<font color=#031B4E>You are deducting</font> <font color=#00c47e>" +Utils.getPrice(amount)+  "</font> from your wallet to your mobile money account-<strong>"+ phone + "</strong>. A fee of <font color=#00c47e>" + fee + "</font>  will be charged." ;
        textView.setText(Html.fromHtml(text));

        aq.id(R.id.btn_confirm_withdraw).click(l->{
            withdraw(phone, amount);
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }

    private void withdraw(String phone, int amount)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to continue?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        completeWithdraw(phone, amount);
                    }})
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Confirm");
        alert.show();

    }

    private void completeWithdraw(String phone, int amount){
        progressDialog.show();
        String txref = "J8CASH-"+UUID.randomUUID().toString();
        JsonObject data = new JsonObject();
        data.addProperty("account_bank", "MPS");
        data.addProperty("account_number", phone);
        data.addProperty("amount", amount);
        data.addProperty("narration", "J8Cash Withdraw by "+Prefs.getString(AccountManager.NAME, ""));
        data.addProperty("currency", Prefs.getString(AccountManager.CURRENCY,""));
        data.addProperty("reference", txref);
        data.addProperty("callback_url", Constants.WITHDRAW_CALLBACK_URL);
        data.addProperty("debit_currency", Prefs.getString(AccountManager.CURRENCY,""));
        data.addProperty("beneficiary_name", Prefs.getString(AccountManager.NAME, ""));

        Call<WithdrawResponse> call = RetrofitClient.getRaveAPI().create(APIInterface.class).withdraw(data);
        call.enqueue(new Callback<WithdrawResponse>() {
            @Override
            public void onResponse(Call<WithdrawResponse> call, Response<WithdrawResponse> response) {
                if(response.isSuccessful()){
                    Utils.log("Withdraw => "+ Utils.getJSONString(response.body()));
                    if(response.body().status.equals("success")){
                        postWithdraw(amount, phone, txref);
                    }
                    if(response.body().status.equals("error")){
                        progressDialog.dismiss();
                        aq.toast(response.body().message);
                    }
                }else{
                    progressDialog.dismiss();

                    aq.toast("System failure, try again later");
                }

            }

            @Override
            public void onFailure(Call<WithdrawResponse> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("System failure, try again later");
                }
            }
        });
    }

    private void postWithdraw(int amount, String phone, String txref){

        Call<Withdraw> call = RetrofitClient.getInstance().create(APIInterface.class).postWithdraw(amount, txref, phone);
        call.enqueue(new Callback<Withdraw>() {
            @Override
            public void onResponse(Call<Withdraw> call, Response<Withdraw> response) {
                if(response.isSuccessful()){
                    aq.toast("Your transaction is being processed");
                    progressDialog.dismiss();
                    Intent i = new Intent(ConfirmWithdrawActivity.this, WithdrawsListActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    try {
                        Utils.log("onError: "+ response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    aq.toast("System failure, try again later");
                }
            }

            @Override
            public void onFailure(Call<Withdraw> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("System failure, try again later");
                }
            }
        });
    }


}
