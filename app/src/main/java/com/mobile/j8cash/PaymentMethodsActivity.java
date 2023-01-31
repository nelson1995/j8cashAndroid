package com.mobile.j8cash;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.aquery.AQuery;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.flutterwave.raveandroid.rave_presentation.RaveNonUIManager;
import com.flutterwave.raveandroid.rave_presentation.ugmobilemoney.UgandaMobileMoneyPaymentCallback;
import com.flutterwave.raveandroid.rave_presentation.ugmobilemoney.UgandaMobileMoneyPaymentManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mobile.j8cash.models.Deposit;
import com.mobile.j8cash.models.FlutterwaveResponse;
import com.mobile.j8cash.utils.Constants;
import com.mobile.j8cash.utils.Utils;
import com.pixplicity.easyprefs.library.Prefs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethodsActivity extends BaseActivity {

    String email, fname, narration, txRef, phone;
    int amount;
    String payment_method;
    ProgressDialog progressDialog;
    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Select payment method");

        aq = new AQuery(this);
        Intent intent = getIntent();

        email = Prefs.getString(AccountManager.EMAIL, "");
        fname = Prefs.getString(AccountManager.NAME, "");
        narration = "Deposit Funds";
        txRef = UUID.randomUUID().toString();
        phone = intent.getStringExtra("Phone");
        amount = intent.getIntExtra("Amount", 0);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Completing Transaction...");
        progressDialog.setCancelable(false);

        String country  = Prefs.getString(AccountManager.COUNTRY_CODE, "UG");
        switch (country){
            case "UG":
                aq.id(R.id.btn_ugmobilemoney).show();
                aq.id(R.id.btn_mpesa).hide();
                aq.id(R.id.btn_zambia_mobile_money).hide();
                aq.id(R.id.btn_rw_mobile_money).hide();
                break;
            case "KE":
                aq.id(R.id.btn_ugmobilemoney).hide();
                aq.id(R.id.btn_mpesa).show();
                aq.id(R.id.btn_zambia_mobile_money).hide();
                aq.id(R.id.btn_rw_mobile_money).hide();
                break;
            case "ZM":
                aq.id(R.id.btn_ugmobilemoney).hide();
                aq.id(R.id.btn_mpesa).hide();
                aq.id(R.id.btn_zambia_mobile_money).show();
                aq.id(R.id.btn_rw_mobile_money).hide();
                break;
            case "RW":
                aq.id(R.id.btn_ugmobilemoney).hide();
                aq.id(R.id.btn_mpesa).hide();
                aq.id(R.id.btn_zambia_mobile_money).hide();
                aq.id(R.id.btn_rw_mobile_money).show();
                break;
            default:
                aq.id(R.id.btn_ugmobilemoney).hide();
                aq.id(R.id.btn_mpesa).hide();
                aq.id(R.id.btn_zambia_mobile_money).show();
                aq.id(R.id.btn_rw_mobile_money).hide();
                break;
        }

        findViewById(R.id.btn_ugmobilemoney).setOnClickListener(k->{
            ugMobileMoney();
        });

        findViewById(R.id.btn_card).setOnClickListener(k->{
            cardPayment();
        });

        findViewById(R.id.btn_mpesa).setOnClickListener(k->{
            mpesa();
        });

        findViewById(R.id.btn_rw_mobile_money).setOnClickListener(k->{
            rwMobileMoney();
        });

        findViewById(R.id.btn_zambia_mobile_money).setOnClickListener(k->{
            zambiaMobileMoney();
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         *  We advise you to do a further verification of transaction's details on your server to be
         *  sure everything checks out before providing service or goods.
         */
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                postDeposit(message);
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "An error occurred. Try again later", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                //Toast.makeText(this, "Cancelled ", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void ugMobileMoney(){
        payment_method = "Uganda Mobile Money";
        new RaveUiManager(this).setAmount(amount)
                .setCountry(Constants.RAVE_COUNTRY)
                .setCurrency(Constants.RAVE_CURRENCY)
                .setEmail(email)
                .setfName(fname)
                .setlName("")
                .setNarration(narration)
                .setPublicKey(Constants.getPublicKey())
                .setEncryptionKey(Constants.getEncryptionKey())
                .setTxRef(txRef)
                .setPhoneNumber(phone)
                .acceptCardPayments(false)
                .acceptUgMobileMoneyPayments(true)
                .allowSaveCardFeature(false)
                .onStagingEnv(Constants.DEV)
                .withTheme(R.style.RaveCustomTheme)
                .shouldDisplayFee(true)
                .showStagingLabel(true)
                .initialize();

    }

    private void rwMobileMoney(){
        payment_method = "Rwanda Mobile Money";
        new RaveUiManager(this).setAmount(amount)
                .setCountry("NG")
                .setCurrency("RWF")
                .setEmail(email)
                .setfName(fname)
                .setlName("")
                .setNarration(narration)
                .setPublicKey(Constants.getPublicKey())
                .setEncryptionKey(Constants.getEncryptionKey())
                .setTxRef(txRef)
                .setPhoneNumber(phone)
                .acceptCardPayments(false)
                .acceptRwfMobileMoneyPayments(true)
                .allowSaveCardFeature(false)
                .onStagingEnv(Constants.DEV)
                .withTheme(R.style.RaveCustomTheme)
                .shouldDisplayFee(true)
                .showStagingLabel(true)
                .initialize();
    }


    private void zambiaMobileMoney(){
        payment_method = "Zambia Mobile Money";
        new RaveUiManager(this).setAmount(amount)
                .setCountry("NG")
                .setCurrency("ZMW")
                .setEmail(email)
                .setfName(fname)
                .setlName("")
                .setNarration(narration)
                .setPublicKey(Constants.getPublicKey())
                .setEncryptionKey(Constants.getEncryptionKey())
                .setTxRef(txRef)
                .setPhoneNumber(phone)
                .acceptCardPayments(false)
                .acceptZmMobileMoneyPayments(true)
                .allowSaveCardFeature(false)
                .onStagingEnv(Constants.DEV)
                .withTheme(R.style.RaveCustomTheme)
                .shouldDisplayFee(true)
                .showStagingLabel(true)
                .initialize();

    }

    private void mpesa(){
        payment_method = "Mpesa";
        new RaveUiManager(this).setAmount(amount)
                .setCountry("KE")
                .setCurrency("KES")
                .setEmail(email)
                .setfName(fname)
                .setlName("")
                .setNarration(narration)
                .setPublicKey(Constants.getPublicKey())
                .setEncryptionKey(Constants.getEncryptionKey())
                .setTxRef(txRef)
                .setPhoneNumber(phone)
                .acceptCardPayments(false)
                .acceptMpesaPayments(true)
                .allowSaveCardFeature(false)
                .onStagingEnv(Constants.DEV)
                .withTheme(R.style.RaveCustomTheme)
                .shouldDisplayFee(true)
                .showStagingLabel(true)
                .initialize();

    }

    private void cardPayment(){
        payment_method = "Card Payment";
        new RaveUiManager(this).setAmount(amount)
                .setCountry(Constants.RAVE_COUNTRY)
                .setCurrency(Constants.RAVE_CURRENCY)
                .setEmail(email)
                .setfName(fname)
                .setlName("")
                .setNarration(narration)
                .setPublicKey(Constants.getPublicKey())
                .setEncryptionKey(Constants.getEncryptionKey())
                .setTxRef(txRef)
                .setPhoneNumber(phone)
                .acceptCardPayments(true)
                .acceptUgMobileMoneyPayments(false)
                .allowSaveCardFeature(true)
                .onStagingEnv(Constants.DEV)
                .withTheme(R.style.RaveCustomTheme)
                .shouldDisplayFee(true)
                .showStagingLabel(true)
                .initialize();
    }

    private void postDeposit(String response){
        FlutterwaveResponse.Transaction transaction = new Gson().fromJson(response, FlutterwaveResponse.class).transaction;
        String transactionRef = transaction.txRef;
        String method = transaction.paymentType;
        int amountPaid = transaction.amount;
        String number = (String) transaction.customerPhone;

        Utils.log("Rave response: "+ response);
        progressDialog.show();
        Call<Deposit> call = RetrofitClient.getInstance().create(APIInterface.class).deposit(method,number,amountPaid,transactionRef);
        call.enqueue(new Callback<Deposit>() {
            @Override
            public void onResponse(Call<Deposit> call, Response<Deposit> response) {
                progressDialog.hide();
                if(response.isSuccessful()){
                    Utils.log("onSuccess Deposit -> " + Utils.getJSONString(response.body()));
                    Prefs.putFloat(AccountManager.WALLET, response.body().wallet_balance);
                    Intent i = new Intent(PaymentMethodsActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    aq.toast("Deposit was successful");
                }else{
                    aq.toast("An error occurred");
                    try {
                        Utils.log("onError Deposit -> "+ response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Deposit> call, Throwable t) {
                progressDialog.hide();
                if(t instanceof IOException){
                    aq.toast("No internet connection");

                }else{
                    aq.toast("Failed to process transaction");
                }
            }
        });

    }

}
