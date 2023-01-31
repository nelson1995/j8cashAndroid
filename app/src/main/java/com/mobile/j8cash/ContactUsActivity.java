package com.mobile.j8cash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.aquery.AQuery;
import com.mobile.j8cash.models.User;
import com.mobile.j8cash.utils.Utils;

public class ContactUsActivity extends BaseActivity {
    private AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Contact Us");
        getSupportActionBar().setElevation(0);

        aq = new AQuery(this);

        aq.id(R.id.link_email_us).click(p->{
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"j8cash20@gmail.com"});
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Contact Us");
            startActivity(Intent.createChooser(intent, "Contact J8Cash"));
        });

        aq.id(R.id.link_call_us).click(p->{
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:256782590869"));
            startActivity(callIntent);
        });

        aq.id(R.id.link_whatsapp).click(p->{
                Uri uri = Uri.parse("https://wa.me/message/DTXKNIWIC5O4A1"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
        });

        aq.id(R.id.link_telegram).click(p->{
            Uri uri = Uri.parse("https://t.me/J8Cash"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        aq.id(R.id.link_facebook).click(p->{
            String username = "J8Cash20";
            String id ="101979794919801";
            Intent intent = null;
            try {
                this.getPackageManager()
                        .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
                intent =  new Intent(Intent.ACTION_VIEW,
                        Uri.parse("fb://profile/"+id)); //Trys to make intent with FB's URI
            } catch (Exception e) {
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/"+username)); //catches and opens a url to the desired page
            }
            startActivity(intent);
        });
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

}