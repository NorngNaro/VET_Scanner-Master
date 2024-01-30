package com.udaya.virak_buntham.vetpickup.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.udaya.virak_buntham.vetpickup.R;

public class RegisterActionBar {
    public static void registerSupportToolbar(AppCompatActivity applicationContext, Toolbar mToolbar){
        applicationContext.setSupportActionBar(mToolbar);
        if (applicationContext.getSupportActionBar()!=null){
            applicationContext.getSupportActionBar().setTitle("");
            applicationContext.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
            applicationContext.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
