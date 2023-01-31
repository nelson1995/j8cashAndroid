package com.mobile.j8cash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.accounts.Account;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.aquery.AQuery;
import com.mobile.j8cash.models.UpdateProfile;
import com.mobile.j8cash.models.User;
import com.mobile.j8cash.utils.Utils;
import com.pixplicity.easyprefs.library.Prefs;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {
    private AQuery aq;
    private User user;
    private Uri photoUri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setElevation(0);

        aq = new AQuery(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        aq.id(R.id.link_edit_photo).click(k->{
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        });
        aq.id(R.id.btn_edit_profile).click(k->{
            startActivity(new Intent(this, UpdateProfileActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = User.listAll(User.class).get(0);
        displayProfile();
        String profile_pic = Prefs.getString(AccountManager.PROFILE_URL, "");
        if(profile_pic.equals("") || profile_pic == null){
            return;
        }
        aq.id(R.id.profile_image).image(profile_pic);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                photoUri = result.getUri();
                updateProfilePicture();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void displayProfile(){
        aq.id(R.id.user_wallet).text(Utils.getPrice((int) user.wallet));
        aq.id(R.id.fullnames).text(user.name);
        aq.id(R.id.username).text(user.username);
        aq.id(R.id.email).text(user.email);
        aq.id(R.id.phone).text("+"+user.phone);
    }

    private void updateProfilePicture(){
        progressDialog.show();
        RequestBody requestFile = null;
        File file = null;
        MultipartBody.Part filePart = null;
        if(photoUri != null){
            file = new File(photoUri.getPath());
            Utils.log("File "+ file.exists() + "---path -> "+ file.getPath());
            requestFile = RequestBody.create(null, file);
            filePart = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        }

        Call<UpdateProfile> call = RetrofitClient.getInstance().create(APIInterface.class).update_profile_pic(user.email,user.name,user.username, filePart);
        call.enqueue(new Callback<UpdateProfile>() {
            @Override
            public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
                progressDialog.hide();
                if(response.isSuccessful()){
                    aq.toast(response.body().message);
                    response.body().data.profile_pic_url = response.body().profile_pic_url;
                    response.body().data.save();
                    Prefs.putString(AccountManager.PROFILE_URL, response.body().profile_pic_url);
                    aq.id(R.id.profile_image).image(response.body().profile_pic_url);
                }else {
                    aq.toast("An error occurred, try again later");
                    try {
                        Utils.logE("onError: "+ response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateProfile> call, Throwable t) {
                progressDialog.hide();
                Utils.logE("onFailure: "+ t.getLocalizedMessage());
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("An error occurred, try again later");
                }
            }
        });


    }
}