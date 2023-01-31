package com.mobile.j8cash.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.mobile.j8cash.AccountManager;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static Pattern pattern;
    private static Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static void log(String msg){
        if(msg == null || !Constants.DEV){
            return;
        }

        int maxLogSize = 1000;

        for(int i = 0; i <= msg.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > msg.length() ? msg.length() : end;
            Log.d(Constants.LOG_TAG, msg.substring(start, end));
        }
    }

    public static void logE(String msg){
        if(msg == null || !Constants.DEV){
            return;
        }

        int maxLogSize = 1000;

        for(int i = 0; i <= msg.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > msg.length() ? msg.length() : end;
            Log.e(Constants.LOG_TAG, msg.substring(start, end));
        }
    }

    public static boolean validatePhone(String phone){
        return phone.matches("(078|077|075|070|079)([0-9]{7})$");
    }

    public static String addPlusBeforeNumber(String str){
        return "+" + str;
    }

    public static String getFormattedPrice(float price){
        return String.format("%2f", price);
//        return String.format("%,d", price.());



    }
    public static String getFormattedPrice(String price){
        return String.format("%,d", Integer.parseInt(price));
    }

    public static String getPrice(int price){
        return Prefs.getString(AccountManager.CURRENCY, "") + " " + String.format("%,d", price);
    }


    public static String getJSONString(Object object){
        Gson g = new Gson();
        String json = g.toJson(object);

        return json;
    }

    public static void setSoftKeyboard(Activity activity, View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setSoftKeyboard(activity, innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView()
                .getWindowToken(), 0);
    }

    public static boolean isValidEmail(String email){
        if(TextUtils.isEmpty(email)){
            return false;
        }

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String convertToTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }
}


