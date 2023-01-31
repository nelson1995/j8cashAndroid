package com.mobile.j8cash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.amulyakhare.textdrawable.TextDrawable;
import com.aquery.AQuery;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.mobile.j8cash.models.User;
import com.mobile.j8cash.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ViewQRActivity extends BaseActivity {
    private AQuery aq;
    User user;
    ImageView imageViewQrCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("View QR");
        getSupportActionBar().setElevation(0);

        aq = new AQuery(this);
        imageViewQrCode = findViewById(R.id.qr_code);

        user = User.listAll(User.class).get(0);
        aq.id(R.id.fullnames).text(user.name);
        aq.id(R.id.username).text("@"+user.username);

        generateQRCode();
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

    private void generateQRCode(){
        String user_code = user.name;

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(user_code, BarcodeFormat.QR_CODE, 400, 400);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch(Exception e) {

        }
    }

}