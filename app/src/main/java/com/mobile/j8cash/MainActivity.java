package com.mobile.j8cash;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aquery.AQuery;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobile.j8cash.models.InviteCodeResponse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    private ProgressDialog progressDialog;
    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(4);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        aq = new AQuery(this);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_shop, R.id.navigation_activity, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            if(intent.getIntExtra("Register", 0) == 1){
                showInviteCodeDialog();
            }
        }


    }


    private void showInviteCodeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.enter_referral_code_layout, null);
        builder.setView(customLayout);        // add a button
        AlertDialog dialog = builder.create();
        dialog.show();

        AQuery aa = new AQuery(customLayout);
        aa.id(R.id.btn_confirm_referral_code).click(l->{
            String code = aa.id(R.id.edit_text_invite_code).text();
            if(code.equals("")){
                aa.toast("Enter valid code");
                return;
            }
            postInviteCode(code);
            //dialog.dismiss();
        });

        aa.id(R.id.btn_cancel_referral_code).click(l->{
            dialog.dismiss();
        });
    }

    private void postInviteCode(String code){
        progressDialog.show();

        Call<InviteCodeResponse> call = RetrofitClient.getInstance().create(APIInterface.class).postInviteCode(code);
        call.enqueue(new Callback<InviteCodeResponse>() {
            @Override
            public void onResponse(Call<InviteCodeResponse> call, Response<InviteCodeResponse> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    aq.toast(response.body().message);
                }else{
                    aq.toast("An error occurred");
                }
            }

            @Override
            public void onFailure(Call<InviteCodeResponse> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    aq.toast("No internet connection");

                } else {
                    aq.toast("System failure. Try again later");
                }
            }
        });

    }


}
