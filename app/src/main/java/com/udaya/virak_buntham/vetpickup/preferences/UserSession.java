package com.udaya.virak_buntham.vetpickup.preferences;

import android.content.Context;

import com.udaya.virak_buntham.vetpickup.activities.ConfigBaseUrl;

import static android.content.Context.MODE_PRIVATE;

public class UserSession {
    private static final String PREF_USER_SESSION = "pref_user_session";
    private static final String USER_SESSION = "user_session";
    private static final String BASE_URL_SESSION = "base_url";

    private static final String BASE_URL_TEST = "http://rmoapp.udaya-tech.com:8082/VET_PickUp/v1/";
    //    private static final String BASE_URL_PRODUCTION = "https://vetpickup.utlog.net/v1/";
//    private static final String BASE_URL_PRODUCTION = "http://192.168.102.29/VETPickup/v1/";
    private static final String BASE_URL_PRODUCTION = "https://qacl.udaya-tech.com/VETPickup/";

    private static final String BASE_URL = BASE_URL_PRODUCTION;

    public UserSession() {
    }

    public static void persistSession(Context context, String session) {
        context.getSharedPreferences(PREF_USER_SESSION, MODE_PRIVATE)
                .edit()
                .putString(USER_SESSION, session)
                .apply();
    }

    public static boolean checkUserSession(Context context) {
        return context.getSharedPreferences(PREF_USER_SESSION, MODE_PRIVATE).getString(USER_SESSION, null) != null;
    }

    public static void clearSession(Context context) {
        context.getSharedPreferences(PREF_USER_SESSION, MODE_PRIVATE).edit().clear().apply();
    }

    public static String getUserSession(Context context) {
        return context.getSharedPreferences(PREF_USER_SESSION, MODE_PRIVATE).getString(USER_SESSION, null);
    }

    public static void setBaseUrl(Context context, String baseurl) {
        context.getSharedPreferences(PREF_USER_SESSION, MODE_PRIVATE).edit().putString(BASE_URL_SESSION, baseurl).apply();
    }

    public static String getBaseUrl(Context context) {
        return context.getSharedPreferences(PREF_USER_SESSION, MODE_PRIVATE).getString(BASE_URL_SESSION, BASE_URL);
    }
}
