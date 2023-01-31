package com.mobile.j8cash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.aquery.AQuery;
import com.mobile.j8cash.models.Country;
import com.mobile.j8cash.utils.Utils;

import com.mobile.j8cash.models.User;
import com.pixplicity.easyprefs.library.Prefs;

import android.view.View;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private AQuery aq;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");

        aq = new AQuery(this);
        progressDialog  = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.logging_in));

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        findViewById(R.id.link_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

    }

    private void login(){

        String email = aq.id(R.id.email).text();
        String password = aq.id(R.id.password).text();

        if(email.isEmpty() || !Utils.isValidEmail(email)){
            aq.toast(getString(R.string.enter_a_valid_email));
            return;
        }

        if(password.length() < 8){
            aq.toast(getString(R.string.password_must_have_eight_chars));
            return;
        }

        Utils.log("Email -> "+ email + " Password -> "+password);

        progressDialog.show();
        Call<User> call = RetrofitClient.getInstance().create(APIInterface.class).login(email,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Utils.log("onSuccess: "+ Utils.getJSONString(response.body()));
                if(response.isSuccessful()){
                    processLogin(response.body());

                }else{
                    progressDialog.hide();
                    if(response.code() == 401){
                        aq.toast("Your email or password is incorrect");
                    }else{
                        aq.toast("An error occurred. Try again later");
                    }
                    try {
                        Utils.logE("onError Login: "+ response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.hide();
                Utils.logE("onFailure Login: "+ t.getLocalizedMessage());
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("Failed to login");
                }
            }
        });


    }

    private void processLogin(User user){
        Prefs.putString(AccountManager.TOKEN, user.token);
        Prefs.putBoolean(AccountManager.LOGGED_IN, true);
        Prefs.putString(AccountManager.NAME, user.name);
        Prefs.putString(AccountManager.EMAIL, user.email);
        Prefs.putString(AccountManager.COUNTRY, user.country);
        Prefs.putString(AccountManager.COUNTRY_CODE, user.country_code);
        Prefs.putFloat(AccountManager.WALLET, user.wallet);
        Prefs.putString(AccountManager.CURRENCY, user.currency);
        Prefs.putString(AccountManager.PHONECODE, user.phone_code);
        Prefs.putInt(AccountManager.POINTS, user.points);
        Prefs.putString(AccountManager.INVITE_CODE, user.invite_code);
        User.deleteAll(User.class);
        user.save();

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
