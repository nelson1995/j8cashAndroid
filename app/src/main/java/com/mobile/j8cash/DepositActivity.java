package com.mobile.j8cash;

import android.content.Intent;
import android.os.Bundle;

import com.aquery.AQuery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobile.j8cash.models.Country;
import com.mobile.j8cash.utils.Utils;
import com.pixplicity.easyprefs.library.Prefs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class DepositActivity extends BaseActivity {
    AQuery aq;
    String phoneCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Deposit");

        aq = new AQuery(this);
        setViews();

        aq.id(R.id.btn_deposit_amount).click(j->{
            deposit();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }

    private void setViews(){
        phoneCode = Prefs.getString(AccountManager.PHONECODE, "");
        aq.id(R.id.currency_text).text(Prefs.getString(AccountManager.CURRENCY,""));
        aq.id(R.id.phone_code_text).text("+"+phoneCode);
    }

    private void deposit()
    {

        String am = aq.id(R.id.deposit_amount).text();
        String phone = aq.id(R.id.deposit_phone).text();


        if (phone.length() < 9){
            aq.toast("Phone number must be 9 digits");
            return;
        }
        int amount;
        if (!am.equals("")){
            amount = Integer.parseInt(am);
//            if (amount < 500){
//                aq.toast("Amount must be at least UGX 500");
//                return;
//            }
        }else{
            aq.toast("Amount field can not be empty");
            return;
        }
        Intent i = new Intent(this, PaymentMethodsActivity.class);
        i.putExtra("Phone", phoneCode+phone);
        i.putExtra("Amount", amount);
        startActivity(i);
    }


}
