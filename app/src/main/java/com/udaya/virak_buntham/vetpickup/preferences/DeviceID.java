package com.udaya.virak_buntham.vetpickup.preferences;

import android.content.Context;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

public class DeviceID {
    private static final String PREF_DEVICE_ID = "pref_device_id";
    private static final String DEVICE_ID = "device_id";

    public static void persistDeviceID(Context context) {
        context.getSharedPreferences(PREF_DEVICE_ID, Context.MODE_PRIVATE)
                .edit()
                .putString(DEVICE_ID, deviceId(context))
                .apply();
    }

    private static String deviceId(Context context) {
        String deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceID == null ? "Can not get device ID" : deviceID;
    }

    public static String getDeviceId(Context context) {
        return context.getSharedPreferences(PREF_DEVICE_ID, Context.MODE_PRIVATE)
                .getString(DEVICE_ID, null);
    }
}
