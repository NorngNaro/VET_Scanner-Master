package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.nav.ChangeLanguageActivity;
import com.udaya.virak_buntham.vetpickup.custom.MButton;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    @BindView(R.id.button_change_password)
    LinearLayout buttonChangePassword;
    @BindView(R.id.button_change_language)
    LinearLayout buttonChangeLanguage;
    @BindView(R.id.button_logout)
    MButton buttonLogout;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mToolbarTitle.setText("Setting");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        registerOnClick(this);
    }

    private void registerOnClick(View.OnClickListener clickListener){
        buttonChangePassword.setOnClickListener(clickListener);
        buttonChangeLanguage.setOnClickListener(clickListener);
        buttonLogout.setOnClickListener(clickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_change_password:
                gotoChangePassword();
                break;

            case R.id.button_change_language:
                gotoChangeLanguage();
                break;

            case R.id.button_logout:
                buttonLogout.showAnimation(this);
                gotoLogin();
                break;

        }
    }

    private void gotoChangePassword(){
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    private void gotoChangeLanguage(){
        Intent intent = new Intent(this, ChangeLanguageActivity.class);
        startActivity(intent);
    }

    private void gotoLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
