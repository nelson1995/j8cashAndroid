package com.mobile.j8cash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.aquery.AQuery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hbb20.CountryCodePicker;
import com.mobile.j8cash.models.User;
import com.mobile.j8cash.utils.Utils;
import com.pixplicity.easyprefs.library.Prefs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity {
    private AQuery aq;
    private String phone;
    private String country;
    private ProgressDialog progressDialog;
    private String phone_code;
    private CountryCodePicker countryPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Sign Up");

        aq = new AQuery(this);
//        Intent intent = getIntent();
//        phone = intent.getStringExtra("Phone");
//        country = intent.getStringExtra("Country");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Account...");
        progressDialog.setCancelable(false);
        countryPicker = findViewById(R.id.ccp_country);
        country = countryPicker.getDefaultCountryName();
        countryPicker.setOnCountryChangeListener(() -> {
            phone_code = countryPicker.getSelectedCountryCode();
            country = countryPicker.getDefaultCountryName();
            aq.id(R.id.phone_code_text).text("+"+phone_code);
        });

       findViewById(R.id.btn_signup_continue).setOnClickListener(k->{
           //startActivity(new Intent(this, EnterPinActivity.class));
           getFormData();
       });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }

    private void getFormData(){
        String fullnames = aq.id(R.id.input_name).text();
        String username = aq.id(R.id.input_username).text();
        String email = aq.id(R.id.input_email).text();
        String confirm_email = aq.id(R.id.input_confirm_email).text();
        String password = aq.id(R.id.input_password).text();
        String cpassword = aq.id(R.id.input_password_confirmation).text();
        String phone = aq.id(R.id.input_phone).text();
        String phoneFull = phone_code+phone;

        if(phone.length() != 9){
            aq.toast("Enter number e.g 78XXXXXXX");
            return;
        }

        if (fullnames.isEmpty()){
            aq.toast("Enter your full names");
            return;
        }
        if (username.isEmpty()){
            aq.toast("Enter your username");

            return;
        }
        if (!Utils.isValidEmail(email)){
            aq.toast("Enter a valid email");
            return;
        }

        if(!email.equals(confirm_email)){
            aq.toast("Emails do not match");
            return;
        }

        if (password.length() < 8){
            aq.toast("Password must be at least 8 characters");
            return;
        }

        if(!password.equals(cpassword)){
            aq.toast("Passwords do not match");
            return;
        }

        progressDialog.show();

        Call<User> call = RetrofitClient.getInstance().create(APIInterface.class).register(fullnames,username, email, phoneFull, country, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    progressDialog.hide();
                    aq.toast("Finished Creating Account");
                    processLogin(response.body());

                }else{
                    progressDialog.hide();
                    try {
                        Utils.log("OnError Register -> "+ response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(response.code() == 422){
                        aq.toast("Email or username already taken");
                    }else{
                        aq.toast("An error occurred, try again later");
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.hide();
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("An error occurred, try again later");
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

        Intent i = new Intent(SignupActivity.this, EnterPinActivity.class);
        i.putExtra("UserId", user.getId().intValue());
        startActivity(i);
    }

}
