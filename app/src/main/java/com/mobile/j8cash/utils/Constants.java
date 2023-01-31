package com.mobile.j8cash.utils;

public class Constants {
    public static final boolean DEV = true;
    public static final String BASE_URL = "http://178.62.111.70/api/";
    public static final String LOG_TAG = "J8CASH";
    public static final String RAVE_WITHDRAW_URL = "https://api.flutterwave.com/v3/";
    public static final String WITHDRAW_CALLBACK_URL = BASE_URL + "callback_url";

    public static final String RAVE_COUNTRY = "UG";
    public static final String RAVE_CURRENCY = "UGX";

    public static final String RAVE_PUBLIC_KEY_LIVE = "FLWPUBK-a8f747da2ee218c05f51ac462c6dc0b8-X";
    public static final String RAVE_ENCRYPTION_KEY_LIVE = "4ba8c88fbc7d6a6de9500588";

    public static final String RAVE_PUBLIC_KEY_DEV = "FLWPUBK-67d7d7245c858ba527a3c0d65add4c93-X";
    public static final String RAVE_ENCRYPTION_KEY_DEV = "eb7f91b86b8bf288c1bfddbe";

//    public static final String RAVE_SEC_KEY = "FLWSECK-2448e772b7b073feef97cd4d3a8da093-X";  // ben

    public static final String RAVE_SEC_KEY = "FLWSECK-4ba8c88fbc7da307ec30738cae6ddafc-X";

    public static String getPublicKey(){
        return (DEV == true) ? RAVE_PUBLIC_KEY_DEV : RAVE_PUBLIC_KEY_LIVE;
    }

    public static String getEncryptionKey(){
        return (DEV == true) ? RAVE_ENCRYPTION_KEY_DEV : RAVE_ENCRYPTION_KEY_LIVE;
    }


}
