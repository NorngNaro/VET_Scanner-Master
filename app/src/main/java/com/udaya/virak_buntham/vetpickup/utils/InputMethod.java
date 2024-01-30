package com.udaya.virak_buntham.vetpickup.utils;

import android.app.Activity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;

public class InputMethod {
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            try {
                inputMethodManager.hideSoftInputFromWindow(
                        Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
            }catch (Exception e){
                Log.d("erroInput==>",e.toString());
            }
        }
    }
}
