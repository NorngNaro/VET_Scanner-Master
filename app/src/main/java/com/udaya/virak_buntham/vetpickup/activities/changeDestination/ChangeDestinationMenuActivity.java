package com.udaya.virak_buntham.vetpickup.activities.changeDestination;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeDestinationMenuActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_destination_menu);
        ButterKnife.bind(this);
        mToolbarTitle.setText("ប្តូរ (Change)");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        findViewById(R.id.btn_return).setOnClickListener(v -> {
            startActivity(new Intent(this,ReturnActivity.class));
        });
        findViewById(R.id.btn_change_branch).setOnClickListener(v -> {
            startActivity(new Intent(this, ChangeBranchActivity.class));
        });
        findViewById(R.id.btn_changeDestination).setOnClickListener(v -> {
            startActivity(new Intent(this, ChangeDestinationActivity.class));
        });
        findViewById(R.id.btn_return_HQ).setOnClickListener(v -> {
            startActivity(new Intent(this,ReturnToCampusActivity.class));
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, HomeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}