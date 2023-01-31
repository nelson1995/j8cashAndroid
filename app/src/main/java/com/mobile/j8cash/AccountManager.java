package com.mobile.j8cash;

import android.content.Context;
import android.content.Intent;

import com.mobile.j8cash.models.HomeTransaction;
import com.mobile.j8cash.models.User;
import com.pixplicity.easyprefs.library.Prefs;

public class AccountManager {

    public static final String  TOKEN = "token";
    public static final String LOGGED_IN = "logged_in";
    public static final String  EMAIL = "email";
    public static final String NAME = "name";
    public static final String COUNTRY = "country";
    public static final String COUNTRY_CODE = "country_code";
    public static final String CURRENCY = "currency";
    public static final String PHONECODE = "phone_code";
    public static final String POINTS = "points";
    public static final String INVITE_CODE = "invite_code";
    public static final String WALLET = "wallet";
    public static final String PROFILE_URL = "profile_url";

    public static void logout(Context context) {
        Prefs.clear();
        User.deleteAll(User.class);
        HomeTransaction.deleteAll(HomeTransaction.class);
        context.startActivity(new Intent(context, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
