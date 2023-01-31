package com.mobile.j8cash;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.aquery.AQuery;
import com.mobile.j8cash.models.InviteCodeResponse;
import com.mobile.j8cash.models.User;
import com.mobile.j8cash.utils.Utils;
import com.pixplicity.easyprefs.library.Prefs;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareActivity extends BaseActivity {
    private AQuery aq;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Invite friends");
        getSupportActionBar().setElevation(0);

        aq = new AQuery(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        int points = Prefs.getInt(AccountManager.POINTS, 0);
        if(points == 0){
            aq.id(R.id.points_text).text("You have no points!");
        }else if(points == 1){
            aq.id(R.id.points_text).text("You have " + points + " points");
        }else{
            aq.id(R.id.points_text).text("You have " + points + " points");
        }

        String referralCode = Prefs.getString(AccountManager.INVITE_CODE, "") ;//generateString(new Random(), "ABCDFGHJKLMNPQRSTVWXYZabcdfghjklmnpqrstvwxyz123456789", 10 );
        aq.id(R.id.btn_share_code).text(referralCode);
        aq.id(R.id.btn_share_code).click(k->{
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "J8Cash");
            String shareMessage= "\nDownload J8Cash and send money to friends and family in Uganda and DRC Congo\n\n";
            String referralMsg = "Invite code: " + referralCode;
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n" + referralMsg;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share J8Cash to"));
        });

        aq.id(R.id.btn_enter_code).click(l->{
            showInviteCodeDialog();
        });

    }

    public static String generateString(Random rng, String characters, int length)
    {
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void showInviteCodeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ShareActivity.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.enter_referral_code_layout, null);
        builder.setView(customLayout);        // add a button
        AlertDialog dialog = builder.create();
        dialog.show();

        AQuery aa = new AQuery(customLayout);
        aa.id(R.id.btn_confirm_referral_code).click(l->{
            String code = aa.id(R.id.edit_text_invite_code).text();
            if(code.equals("")){
                aq.toast("Enter valid code");
                return;
            }
            postInviteCode(code);
            dialog.dismiss();
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