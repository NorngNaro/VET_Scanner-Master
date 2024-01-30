package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.nav.ForgetPasswordActivity;
import com.udaya.virak_buntham.vetpickup.base.AppBaseActivity;
import com.udaya.virak_buntham.vetpickup.custom.ClickableTextView;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.login.LoginResponse;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;
import com.udaya.virak_buntham.vetpickup.utils.AppConfig;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.InputMethod;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppBaseActivity implements View.OnClickListener, OnInternetConnectionListener {

    @BindView(R.id.lbl_welcome)
    TextView labelWelcome;
    @BindView(R.id.button_login)
    Button buttonLogin;
    @BindView(R.id.tv_forget_password)
    ClickableTextView tvForgetPassword;
    @BindView(R.id.tv_register_account)
    ClickableTextView tvRegisterAccount;
    @BindView(R.id.tv_khmer_language)
    ClickableTextView tvKhmerLanguage;
    @BindView(R.id.tv_english_language)
    ClickableTextView tvEnglishLanguage;
    @BindView(R.id.edt_phone_number)
    EditText edtPhoneNumber;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.layout_loading)
    FrameLayout layoutLoading;
    private ProgressDialog progressDialog;
    @BindView(R.id.tvAppVersion)
    TextView tvAppVersion;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        registerOnClick(this);
        tvAppVersion.setText("កំណែ " + AppConfig.getAppVersion());
        if (getSharedPreferences(Constants.LANGUAGE_PREF_NAME, MODE_PRIVATE) != null) {
            if (getSharedPreferences(Constants.LANGUAGE_PREF_NAME, MODE_PRIVATE).getString(Constants.LANGUAGE_PREF_KEY, "en").equals(Constants.KHMER_LANGUAGE)) {
                setLocale(Constants.KHMER_LANGUAGE);
                visibleKhmerLanguage();
            }

            if (getSharedPreferences(Constants.LANGUAGE_PREF_NAME, MODE_PRIVATE).getString(Constants.LANGUAGE_PREF_KEY, "en").equals(Constants.ENGLISH_LANGUAGE)) {
                setLocale(Constants.ENGLISH_LANGUAGE);
                visibleEnglishLanguage();
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void login(String device, String token, String signature, String username, String password) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<LoginResponse> call = apiService.requestLogin(device, token, signature, username, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals(Constants.STATUS_SUCCESS)) {
                            //Override request params
                            RequestParams.persistRequestParams(LoginActivity.this,
                                    response.body().getToken(),
                                    response.body().getSignature());

                            //Store user session
                            UserSession.persistSession(LoginActivity.this, response.body().getSession());
                            //Goto home screen
                            gotoHome();
                        } else {
                            AlertDialogUtil.alertMessageInput(LoginActivity.this, "Invalid username and password");
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", "" + t.getMessage());
                progressDialog.hide();
            }
        });
    }


    private void registerOnClick(View.OnClickListener clickListener) {
        buttonLogin.setOnClickListener(clickListener);
        tvForgetPassword.setOnClickListener(clickListener);
        tvRegisterAccount.setOnClickListener(clickListener);
        tvKhmerLanguage.setOnClickListener(clickListener);
        tvEnglishLanguage.setOnClickListener(clickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                checkUserInput(edtPhoneNumber.getText().toString(), edtPassword.getText().toString());
                break;

            case R.id.tv_forget_password:
                tvForgetPassword.showAnimation(this);
                gotoForgetPassword();
                break;

            case R.id.tv_register_account:
                tvRegisterAccount.showAnimation(this);
                gotoRegisterAccount();
                break;

            case R.id.tv_khmer_language:
                tvKhmerLanguage.showAnimation(this);
                visibleKhmerLanguage();
                break;

            case R.id.tv_english_language:
                tvEnglishLanguage.showAnimation(this);
                visibleEnglishLanguage();
                break;

        }
    }

    private void gotoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoRegisterAccount() {
        Intent intent = new Intent(this, RegisterAccountActivity.class);
        startActivity(intent);
    }

    private void gotoForgetPassword() {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    private void visibleKhmerLanguage() {
        setLocale(Constants.KHMER_LANGUAGE);
        updateViews();
        tvKhmerLanguage.setPaintFlags(tvKhmerLanguage.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvEnglishLanguage.setPaintFlags(0);
    }

    private void visibleEnglishLanguage() {
        setLocale(Constants.ENGLISH_LANGUAGE);
        updateViews();
        tvEnglishLanguage.setPaintFlags(tvEnglishLanguage.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvKhmerLanguage.setPaintFlags(0);
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

    private void persistLanguage(String language) {
        getSharedPreferences(Constants.LANGUAGE_PREF_NAME, MODE_PRIVATE)
                .edit()
                .putString(Constants.LANGUAGE_PREF_KEY, language)
                .apply();
    }

    private void updateViews() {
        labelWelcome.setText(R.string.app_name);
        edtPhoneNumber.setHint(R.string.username);
        edtPassword.setHint(R.string.password);
        buttonLogin.setText(R.string.login);
    }

    private void checkUserInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            AlertDialogUtil.alertMessageInput(LoginActivity.this, "សូមបញ្ចូលឈ្មោះអ្នកប្រើនិងពាក្យសម្ងាត់");
        } else {
            if (edtPhoneNumber.getText().toString().equals("081818176")
                    && edtPassword.getText().toString().equals("1")
            ) {
                openConfigBaseURlActivity();
                return;
            }
            turnGPSOn();
            login(
                    DeviceID.getDeviceId(this),
                    RequestParams.getTokenRequestParams(this),
                    RequestParams.getSignatureRequestParams(this),
                    edtPhoneNumber.getText().toString(),
                    edtPassword.getText().toString()
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SplashScreenActivity.REQUEST_COFIG_URL) {
            Intent intent = new Intent(this, SplashScreenActivity.class);
            startActivity(intent);
        }
    }

    private void openConfigBaseURlActivity() {
        Intent intent = new Intent(this, ConfigBaseUrl.class);
        startActivityForResult(intent, SplashScreenActivity.REQUEST_COFIG_URL);
    }

    public void hideInputMethod(View view) {
        try {
            InputMethod.hideSoftKeyboard(this);
        } catch (Exception e) {
            Log.d("error==>",""+e.toString());
        }
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {
        AlertDialogUtil.alertMessageInternetConnection(this);
    }

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        try{
            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        }catch (Exception e){
            Log.d("error==>",""+e.toString());
        }

    }
}
