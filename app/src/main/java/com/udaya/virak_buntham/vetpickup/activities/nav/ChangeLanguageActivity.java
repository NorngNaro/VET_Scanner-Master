package com.udaya.virak_buntham.vetpickup.activities.nav;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeLanguageActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.change_language_button_khmer)
    Button buttonKhmer;
    @BindView(R.id.change_language_button_english)
    Button buttonEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        ButterKnife.bind(this);

        mToolbarTitle.setText(R.string.change_language);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        registerOnClick(this);

        if (getSharedPreferences(Constants.LANGUAGE_PREF_NAME, MODE_PRIVATE) != null){
            if (getSharedPreferences(Constants.LANGUAGE_PREF_NAME, MODE_PRIVATE).getString(Constants.LANGUAGE_PREF_KEY, "kh").equals(Constants.KHMER_LANGUAGE))
                visibleButtonKhmer();
            if (getSharedPreferences(Constants.LANGUAGE_PREF_NAME, MODE_PRIVATE).getString(Constants.LANGUAGE_PREF_KEY, "en").equals(Constants.ENGLISH_LANGUAGE))
                visibleButtonEnglish();
        }
    }

    private void registerOnClick(View.OnClickListener clickListener) {
        buttonKhmer.setOnClickListener(clickListener);
        buttonEnglish.setOnClickListener(clickListener);

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
        switch (v.getId()) {
            case R.id.change_language_button_khmer:
                visibleButtonKhmer();
                setLocale(Constants.KHMER_LANGUAGE);
                gotoHome();
                break;

            case R.id.change_language_button_english:
                visibleButtonEnglish();
                setLocale(Constants.ENGLISH_LANGUAGE);
                gotoHome();
                break;
        }
    }

    private void visibleButtonKhmer() {
        buttonKhmer.setBackground(getDrawable(R.color.colorAccent));
        buttonEnglish.setBackground(getDrawable(R.color.colorAsbestos));
    }

    private void visibleButtonEnglish() {
        buttonKhmer.setBackgroundColor(getResources().getColor(R.color.colorAsbestos));
        buttonEnglish.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    private void gotoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void setLocale(String lang) {
        persistLanguage(lang);

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    private void persistLanguage(String language){
        getSharedPreferences(Constants.LANGUAGE_PREF_NAME, MODE_PRIVATE)
                .edit()
                .putString(Constants.LANGUAGE_PREF_KEY, language)
                .apply();
    }


}
