package com.mobile.j8cash;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.aquery.AQuery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobile.j8cash.models.UpdateProfile;
import com.mobile.j8cash.models.User;
import com.mobile.j8cash.utils.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends BaseActivity {

    private ProgressDialog progressDialog;
    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        aq = new AQuery(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving changes...");

        User user = User.listAll(User.class).get(0);


        aq.id(R.id.input_name).text(user.name);
        aq.id(R.id.input_username).text(user.username);
        aq.id(R.id.input_email).text(user.email);

        aq.id(R.id.btn_update_profile).click(l->{
            updateProfile();
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    private void updateProfile(){

        String name = aq.id(R.id.input_name).text();
        String email = aq.id(R.id.input_email).text();
        String username = aq.id(R.id.input_username).text();
        if (name.isEmpty()){
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

        progressDialog.show();

        Call<UpdateProfile> call = RetrofitClient.getInstance().create(APIInterface.class).update_profile(email,name,username);
        call.enqueue(new Callback<UpdateProfile>() {
            @Override
            public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
                progressDialog.hide();
                if(response.isSuccessful()){
                    aq.toast(response.body().message);
                    if(response.body().status == 1){
                        response.body().data.save();
                        finish();
                    }
                }else {
                    aq.toast("An error occurred, try again later");
                }
            }

            @Override
            public void onFailure(Call<UpdateProfile> call, Throwable t) {
                progressDialog.hide();
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("An error occurred, try again later");
                }
            }
        });
    }
}
