package com.udaya.virak_buntham.vetpickup.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.udaya.virak_buntham.vetpickup.custom.FontType;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.printerutils.AidlUtil;
import com.udaya.virak_buntham.vetpickup.utils.Constants;

import java.util.Locale;

public class BaseApp extends Application {
    private boolean isAidl;

    public boolean isAidl() {
        return isAidl;
    }

    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setDefaultFont();

        isAidl = true;
        AidlUtil.getInstance().connectPrinterService(this);
        DeviceID.persistDeviceID(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (base.getSharedPreferences(Constants.LANGUAGE_PREF_NAME, MODE_PRIVATE).getString(Constants.LANGUAGE_PREF_KEY, null) == null) {
            setLocale("en");
        } else {
            setLocale(base.getSharedPreferences(Constants.LANGUAGE_PREF_NAME, MODE_PRIVATE).getString(Constants.LANGUAGE_PREF_KEY, null));
        }
    }

    public void setLocale(String lang) {
        persistLanguage(lang);
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    private void persistLanguage(String language) {
        getSharedPreferences(Constants.LANGUAGE_PREF_NAME, MODE_PRIVATE)
                .edit()
                .putString(Constants.LANGUAGE_PREF_KEY, language)
                .apply();
    }

    private void setDefaultFont() {
        FontOverride.setDefaultFont(this, "DEFAULT", FontType.getFontRegular());
        FontOverride.setDefaultFont(this, "MONOSPACE", FontType.getFontRegular());
        FontOverride.setDefaultFont(this, "SERIF", FontType.getFontRegular());
        FontOverride.setDefaultFont(this, "SANS_SERIF", FontType.getFontRegular());
    }
}
