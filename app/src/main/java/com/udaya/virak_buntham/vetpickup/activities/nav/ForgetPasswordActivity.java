package com.udaya.virak_buntham.vetpickup.activities.nav;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.VerificationActivity;
import com.udaya.virak_buntham.vetpickup.custom.MButton;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.forget_password_button_continue)
    MButton buttonContinue;

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

        mToolbarTitle.setText("Forget Password");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        registerClick(this);
    }

    private void registerClick(View.OnClickListener clickListener) {
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
        if (v.getId() == R.id.forget_password_button_continue) {
            buttonContinue.showAnimation(this);
            gotoVerificationMobile();
        }
    }

    private void gotoVerificationMobile() {
        Intent intent = new Intent(this, VerificationActivity.class);
        startActivity(intent);
    }

    private static String getRandomString() {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 15; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}
