package com.mobile.j8cash;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.aquery.AQuery;
import com.google.gson.JsonObject;
import com.mobile.j8cash.models.Withdraw;
import com.mobile.j8cash.models.WithdrawFee;
import com.mobile.j8cash.models.WithdrawResponse;
import com.mobile.j8cash.utils.Constants;
import com.mobile.j8cash.utils.Utils;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawActivity extends BaseActivity {
    AQuery aq;
    String phoneCode;
    private ProgressDialog progressDialog;
    private int amount;
    private String phoneFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Withdraw");

        aq = new AQuery(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        setViews();



        aq.id(R.id.btn_withdraw).click(j->{
            getWithdrawFee();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }

    private void setViews(){
        phoneCode = Prefs.getString(AccountManager.PHONECODE, "");
        aq.id(R.id.currency_text).text(Prefs.getString(AccountManager.CURRENCY,""));
        aq.id(R.id.phone_code_text).text("+"+phoneCode);

        int wallet = (int) Prefs.getFloat(AccountManager.WALLET,0);
        aq.id(R.id.wallet_text).text("Wallet: "+ Utils.getPrice(wallet) );
    }

    private void withdraw()
    {

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setMessage("Are you sure you want to withdraw cash from your wallet to Mobile Money?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        completeWithdraw(phoneFull, amount);
//
//                    }})
//                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.setTitle("Confirm");
//        alert.show();

    }

    private void getWithdrawFee(){
        String am = aq.id(R.id.deposit_amount).text();
        String phone = aq.id(R.id.deposit_phone).text();

        if (phone.length() < 9){
            aq.toast("Phone number must be 9 digits");
            return;
        }

        if (!am.equals("")){
            amount = Integer.parseInt(am);
//            if (amount < 500){
//                aq.toast("Amount must be at least UGX 500");
//                return;
//            }
        }else{
            aq.toast("Amount field can not be empty");
            return;
        }

        phoneFull = phoneCode+phone;

        progressDialog.show();
        Call<WithdrawFee> call = RetrofitClient.getRaveAPI().create(APIInterface.class).getFee(String.valueOf(amount), Prefs.getString(AccountManager.CURRENCY, "UGX"), "mobilemoney");
        call.enqueue(new Callback<WithdrawFee>() {
            @Override
            public void onResponse(Call<WithdrawFee> call, Response<WithdrawFee> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    Utils.log("onSuccess: Fee "+ parseFee(response.body()));
                    Intent intent = new Intent(WithdrawActivity.this, ConfirmWithdrawActivity.class);
                    intent.putExtra("Fee", parseFee(response.body()));
                    intent.putExtra("Phone", phoneFull);
                    intent.putExtra("Amount", amount);
                    startActivity(intent);
                }else{
                    progressDialog.dismiss();
                    Utils.logE("onError: " + response.code());
                    aq.toast(String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<WithdrawFee> call, Throwable t) {
                progressDialog.dismiss();
                Utils.logE("onFailure: " + t.getLocalizedMessage());

                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("System failure, try again later");
                }
            }
        });
    }

    private String parseFee(WithdrawFee body){
        WithdrawFee.Datum data1 = body.data.get(0);
        if(data1.fee != 0){
            return Utils.getPrice((int) data1.fee);
        }else{
            WithdrawFee.Datum data2 = body.data.get(1);
            float fee = data2.fee * amount;
            Utils.log("Fee -> "+ fee);
            return Utils.getPrice((int) fee);
        }
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
                    Intent i = new Intent(WithdrawActivity.this, WithdrawsListActivity.class);
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
