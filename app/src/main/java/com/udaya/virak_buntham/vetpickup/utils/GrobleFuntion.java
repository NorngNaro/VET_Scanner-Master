package com.udaya.virak_buntham.vetpickup.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class GrobleFuntion {
    public static void setDate(Button btnDate) {
        Date d = new Date();
        CharSequence s = DateFormat.format("yyyy-MM-dd", d.getTime());
        btnDate.setText(s);
    }

    public static void setSpinnerHour(MaterialSpinner spinner, final Context context) {
        spinner.setItems("Please Select", "00", "01", "02", "03", "04", "05", "06", "07", "07", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Toast.makeText(context, item, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static void setSpinnerMinute(MaterialSpinner spinner, final Context context) {
        spinner.setItems("Please Select", "00","05","10","15","20","25","30","35","40","45","50","55","60");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
//                Toast.makeText(context, item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public  static  String getCurrentHour(){
        Calendar c = Calendar .getInstance();
        System.out.println("Current time => "+c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("hh");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
    public  static  String getCurrentMins(){
        Calendar c = Calendar .getInstance();
        System.out.println("Current time => "+c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("mm");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
