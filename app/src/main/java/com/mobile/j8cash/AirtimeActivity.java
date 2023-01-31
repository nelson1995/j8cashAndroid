package com.mobile.j8cash;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import com.aquery.AQuery;
import com.hbb20.CountryCodePicker;
import com.mobile.j8cash.models.AirtimeResponse;
import com.mobile.j8cash.utils.Utils;
import com.pixplicity.easyprefs.library.Prefs;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirtimeActivity extends BaseActivity {
    private AQuery aq;
    float wallet;
    CountryCodePicker ccp;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtime);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Buy Airtime");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buying airtime...");
        progressDialog.setCancelable(false);
        ccp = findViewById(R.id.ccp);
        ccp.setOnCountryChangeListener(() -> {
            String country_code = ccp.getSelectedCountryNameCode();
            if(country_code.equals("UG")){
                aq.id(R.id.currency_code).text("UGX");
            }
            if(country_code.equals("KE")){
                aq.id(R.id.currency_code).text("KES");
            }
            if(country_code.equals("TZ")){
                aq.id(R.id.currency_code).text("TZS");
            }
            if(country_code.equals("NG")){
                aq.id(R.id.currency_code).text("NGN");
            }
        });
        aq = new AQuery(this);

        wallet = Prefs.getFloat(AccountManager.WALLET,0);

        aq.id(R.id.wallet_text).text("Wallet: "+ Utils.getPrice((int)wallet) );

        aq.id(R.id.btn_buy_airtime).click(l->{
            buy_airtime();
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void buy_airtime(){
        String currencycode = aq.id(R.id.currency_code).text();
        String phone = aq.id(R.id.phone).text();
        String phoneCode = ccp.getSelectedCountryCode();
        String am = aq.id(R.id.amount).text();

        if(phone.length() < 9){
            aq.toast("Phone number must be 9 digits");
            return;
        }
        if(am.isEmpty()){
            aq.toast("Amount field can not be empty");
            return;
        }
        float amount = Float.parseFloat(am);

        if(wallet < amount){
            //aq.toast("You have insufficient balance");
            showWarning();
            return;
        }

        Utils.log("Amount -> " + currencycode+ " " + amount + " || Phone -> "+ "+"+phoneCode+phone);
        progressDialog.show();
        Call<AirtimeResponse> call = RetrofitClient.getInstance().create(APIInterface.class).buy_airtime("+"+phoneCode+phone, amount, currencycode);
        call.enqueue(new Callback<AirtimeResponse>() {
            @Override
            public void onResponse(Call<AirtimeResponse> call, Response<AirtimeResponse> response) {
                if(response.isSuccessful()){
                    progressDialog.hide();
                    if(response.body().code == 1){
                        aq.toast("Transaction complete");
                        Prefs.putFloat(AccountManager.WALLET, response.body().data.wallet_balance);
                        startActivity(new Intent(AirtimeActivity.this, MainActivity.class));
                        finish();
                    }else{
                        aq.toast(response.body().status);
                    }
                }else{
                    progressDialog.hide();
                    aq.toast("An error occurred, try again later");
                }
            }

            @Override
            public void onFailure(Call<AirtimeResponse> call, Throwable t) {
                progressDialog.hide();
                if(t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("An error occurred, try again later");
                }
            }
        });
    }

    private void showWarning(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Your wallet has insufficient balance. Please deposit money to your wallet to continue.")
                .setCancelable(false)
                .setPositiveButton("DEPOSIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(AirtimeActivity.this, DepositActivity.class));
                        finish();

                    }})
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Insufficient Balance");
        alert.show();
    }
}
