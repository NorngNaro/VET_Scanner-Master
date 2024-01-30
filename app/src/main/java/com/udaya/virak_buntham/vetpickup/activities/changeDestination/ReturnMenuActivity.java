package com.udaya.virak_buntham.vetpickup.activities.changeDestination;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReturnMenuActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_menu);
        ButterKnife.bind(this);
        mToolbarTitle.setText("ត្រឡប់ (Return)");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void returnOrigin(View view) {
        startActivity(new Intent(this,ReturnActivity.class));
    }

    public void returnHQ(View view) {
        startActivity(new Intent(this,ReturnToCampusActivity.class));
    }
}