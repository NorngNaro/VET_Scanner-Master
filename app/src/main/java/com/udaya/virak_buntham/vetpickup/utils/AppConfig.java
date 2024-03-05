package com.udaya.virak_buntham.vetpickup.utils;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.activities.movetovan.ReceiveItemByTelActivity;

public class AppConfig {

    //Production
    private static final String BASE_URL_PRODUCTION = "https://vetpickup.utlog.net/v4/";
    private static final String BASE_URL_REPORT_PUBLIC = "https://oc.utlog.net/mobiles/";
    private static final String BASE_URL_SEND_SMS = "https://oc.utlog.net/mobiles/";

    //QA
//    private static final String BASE_URL_PRODUCTION = "https://qacl.udaya-tech.com/VETPickup/v4/";
//    private static final String BASE_URL_REPORT_PUBLIC = "https://qacl.udaya-tech.com/0412_VETOc_Web/mobiles/";
//    private static final String BASE_URL_SEND_SMS = "https://qacl.udaya-tech.com/0412_VETOc_Web/mobiles/";

    //Locale
/*
    private static final String BASE_URL_PRODUCTION = "http://192.168.2.151/VETPickup/v4/";
    private static final String BASE_URL_REPORT_PUBLIC = "http://192.168.2.151/0412_VETOc_Web/mobiles/";
    private static final String BASE_URL_SEND_SMS = "https://192.168.2.151/0412_VETOc_Web/mobiles/";
*/

    private static final double appVersion = 1.48;
    private static double getLatitude = 0.0;
    private static double getLongitude = 0.0;

    public static String getBaseUrl() {
        return BASE_URL_PRODUCTION;
    }

    public static String getUrlReport() {
        return BASE_URL_REPORT_PUBLIC;
    }

    public static String getUrlSms() {
        return BASE_URL_SEND_SMS;
    }

    public static double getAppVersion() {
        return appVersion;
    }

    public static double getGetLatitude() {
        return getLatitude;
    }

    public static double getGetLongitude() {
        return getLongitude;
    }

    public static void setGetLatitude(double getLatitude) {
        AppConfig.getLatitude = getLatitude;
    }

    public static void setGetLongitude(double getLongitude) {
        AppConfig.getLongitude = getLongitude;
    }

    private GpsTrackerLocation gpsTracker;

    public void getLocationData(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gpsTracker = new GpsTrackerLocation(context);
            if (gpsTracker.canGetLocation()) {
                double letData = gpsTracker.getLatitude();
                double longData = gpsTracker.getLongitude();
                AppConfig.setGetLatitude(letData);
                AppConfig.setGetLongitude(longData);
                Toast.makeText(context, "==>" + AppConfig.getGetLatitude() + "==" + AppConfig.getGetLongitude(), Toast.LENGTH_SHORT).show();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }


}
