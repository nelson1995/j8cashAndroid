package com.mobile.j8cash;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import com.orm.SugarApp;
import com.pixplicity.easyprefs.library.Prefs;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class J8Application extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath(getResources().getString(R.string.font_path_default))
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
}
