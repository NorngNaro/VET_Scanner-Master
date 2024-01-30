package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.custom.MButton;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterAccountActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    @BindView(R.id.button_select_province)
    MButton buttonSelectProvince;
    @BindView(R.id.button_select_district)
    MButton buttonSelectDistrict;
    @BindView(R.id.button_select_commune)
    MButton buttonSelectCommune;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        ButterKnife.bind(this);

        mToolbarTitle.setText("Register");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        registerOnClick(this);
    }

    private void registerOnClick(View.OnClickListener clickListener){
        buttonSelectProvince.setOnClickListener(clickListener);
        buttonSelectDistrict.setOnClickListener(clickListener);
        buttonSelectCommune.setOnClickListener(clickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_PROVINCE && resultCode == RESULT_OK && data != null)
            buttonSelectProvince.setText(data.getExtras().getString(Constants.RETURN_PROVINCE_KEY));

        if (requestCode == Constants.REQUEST_DISTRICT && resultCode == RESULT_OK && data != null)
            buttonSelectDistrict.setText(data.getExtras().getString(Constants.RETURN_DISTRICT_KEY));

        if (requestCode == Constants.REQUEST_COMMUNE && resultCode == RESULT_OK && data != null)
            buttonSelectCommune.setText(data.getExtras().getString(Constants.RETURN_COMMUNE_KEY));

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
            case R.id.button_select_province:
                buttonSelectProvince.showAnimation(this);
                gotoRequestProvince();
                break;

            case R.id.button_select_district:
                buttonSelectDistrict.showAnimation(this);
                gotoRequestDistrict();
                break;

            case R.id.button_select_commune:
                buttonSelectCommune.showAnimation(this);
                gotoRequestCommune();
                break;
        }
    }

    private void gotoRequestProvince(){
        Intent intent = new Intent(this, SelectAreaActivity.class);
        intent.putExtra(Constants.REQUEST_AREA_KEY, Constants.REQUEST_PROVINCE);
        startActivityForResult(intent, Constants.REQUEST_PROVINCE);
    }

    private void gotoRequestDistrict(){
        Intent intent = new Intent(this, SelectAreaActivity.class);
        intent.putExtra(Constants.REQUEST_AREA_KEY, Constants.REQUEST_DISTRICT);
        startActivityForResult(intent, Constants.REQUEST_DISTRICT);
    }

    private void gotoRequestCommune(){
        Intent intent = new Intent(this, SelectAreaActivity.class);
        intent.putExtra(Constants.REQUEST_AREA_KEY, Constants.REQUEST_COMMUNE);
        startActivityForResult(intent, Constants.REQUEST_COMMUNE);
    }

}
