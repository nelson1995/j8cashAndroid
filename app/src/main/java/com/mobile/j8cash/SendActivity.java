package com.mobile.j8cash;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.aquery.AQuery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobile.j8cash.models.SendResponse;
import com.mobile.j8cash.utils.Utils;
import com.pixplicity.easyprefs.library.Prefs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendActivity extends BaseActivity {
    private AQuery aq;
    private int clientId;
    private String clientName;
    private ProgressDialog pDialog;
    private int wallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Send");
        getSupportActionBar().setElevation(0);

        aq = new AQuery(this);
        wallet = (int) Prefs.getFloat(AccountManager.WALLET,0);

        Intent intent = getIntent();
        clientId = intent.getIntExtra("ClientId",0);
        clientName = getIntent().getStringExtra("ClientName");

        TextView toView = findViewById(R.id.to_text_view);
        toView.setText("You are sending money to "+ clientName);
        aq.id(R.id.currency_text).text(Prefs.getString(AccountManager.CURRENCY,""));
        aq.id(R.id.wallet_text).text("Wallet: "+ Utils.getPrice(wallet) );
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Sending...");

        findViewById(R.id.btn_send_now).setOnClickListener(n->{
            initialize();
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }

    private void initialize(){
        String reason = aq.id(R.id.send_reason).text();
        String am = aq.id(R.id.send_amount).text();

        int amount;
        if (!am.equals("")){
            amount = Integer.parseInt(am);
            if (amount < 500){
                aq.toast("Amount must be at least UGX 500");
                return;
            }
        }else{
            aq.toast("Amount field can not be empty");
            return;
        }

        if(reason.isEmpty()){
            aq.toast("Enter your reason");
            return;
        }

        if(wallet < amount){
            showWarning();
            //aq.toast("You have insufficient balance");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to send money from your wallet?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        postMoney(amount, reason);

                    }})
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Confirm");
        alert.show();

    }

    private void postMoney(int amount, String reason){

        pDialog.show();

        Call<SendResponse> call = RetrofitClient.getInstance().create(APIInterface.class).send(clientId, amount, reason);
        call.enqueue(new Callback<SendResponse>() {
            @Override
            public void onResponse(Call<SendResponse> call, Response<SendResponse> response) {
                if(response.isSuccessful()){
                    Prefs.putFloat(AccountManager.WALLET, response.body().wallet_balance);
                    startActivity(new Intent(SendActivity.this, MainActivity.class));
                    finish();
                    Toast.makeText(SendActivity.this,"You have sent money to " + clientName, Toast.LENGTH_LONG ).show();

                }else{
                    aq.toast("An error occurred");
                }
            }

            @Override
            public void onFailure(Call<SendResponse> call, Throwable t) {
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("Failed to send money. Try again later");
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
                        startActivity(new Intent(SendActivity.this, DepositActivity.class));
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
