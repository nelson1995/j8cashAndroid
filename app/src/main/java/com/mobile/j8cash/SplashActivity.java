package com.mobile.j8cash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import com.pixplicity.easyprefs.library.Prefs;

public class SplashActivity extends BaseActivity {
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        startSplashTimeout(3000);
    }

    private void startSplashTimeout(int timeout){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Prefs.getBoolean(AccountManager.LOGGED_IN, false)) {
                    i = new Intent(SplashActivity.this, MainActivity.class);
                }else{
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }
                SplashActivity.this.finish();
                SplashActivity.this.startActivity(i);
            }
        }, timeout);
    }
}
