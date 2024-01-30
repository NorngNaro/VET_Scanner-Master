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
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.verification_button_continue)
    MButton buttonContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);

        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        registerOnClick(this);
    }

    private void registerOnClick(View.OnClickListener clickListener){
        buttonContinue.setOnClickListener(clickListener);
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
        if (v.getId() == R.id.verification_button_continue) {
            buttonContinue.showAnimation(this);
            gotoSetupPassword();
        }
    }

    private void gotoSetupPassword(){
        Intent intent = new Intent(this, SetupPasswordActivity.class);
        startActivity(intent);
    }
}
