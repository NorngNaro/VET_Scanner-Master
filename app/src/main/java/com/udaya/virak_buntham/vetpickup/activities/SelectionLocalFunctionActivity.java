package com.udaya.virak_buntham.vetpickup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectionLocalFunctionActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_locale);
        ButterKnife.bind(this);
        mToolbarTitle.setText("បញ្ចូលបញ្ញើថ្មី(តំបន់)");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,HomeActivity.class));

    }

}