package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.checklogin.CheckLoginResponse;
import com.udaya.virak_buntham.vetpickup.models.requesttoken.RequestTokenResponse;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.Constants;

import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity implements OnInternetConnectionListener {

    @BindView(R.id.loading)
    ProgressBar loading;

    public static int REQUEST_COFIG_URL = 2222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        visibleKhmerLanguage();
        requestToken(DeviceID.getDeviceId(this));
    }

    private void requestToken(final String device) {
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<RequestTokenResponse> call = apiService.getToken(device);
        call.enqueue(new Callback<RequestTokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<RequestTokenResponse> call, @NonNull Response<RequestTokenResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        RequestParams.persistRequestParams(SplashScreenActivity.this, response.body().getToken(), response.body().getSignature());

                        if (UserSession.checkUserSession(SplashScreenActivity.this))
                            checkLogin(device
                                    , RequestParams.getTokenRequestParams(SplashScreenActivity.this)
                                    , RequestParams.getSignatureRequestParams(SplashScreenActivity.this)
                                    , UserSession.getUserSession(SplashScreenActivity.this)
                            );
                        else {
                            gotoLogin();
                        }
                    } else {
                        Log.i("responseToken", "is null");
                    }
                } else {
                    gotoLogin();
                    Log.i("responseToken", "Request unsuccessful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<RequestTokenResponse> call, @NonNull Throwable t) {
                Log.i("requestToken", Objects.requireNonNull(t.getMessage()));
                //loading.setVisibility(View.GONE);
            }
        });
    }

    private void checkLogin(String device, String token, String signature, String session) {
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<CheckLoginResponse> call = apiService.checkLogin(device, token, signature, session);
        call.enqueue(new Callback<CheckLoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<CheckLoginResponse> call, @NonNull Response<CheckLoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals(Constants.STATUS_SUCCESS)) {
                            RequestParams.persistRequestParams(SplashScreenActivity.this, response.body().getToken(), response.body().getSignature());
                            gotoHome();
                        } else {
                            UserSession.clearSession(SplashScreenActivity.this);
                            gotoLogin();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckLoginResponse> call, @NonNull Throwable t) {
                Log.i("requestCheckLogin", Objects.requireNonNull(t.getMessage()));
                checkLogin(device
                        , RequestParams.getTokenRequestParams(SplashScreenActivity.this)
                        , RequestParams.getSignatureRequestParams(SplashScreenActivity.this)
                        , UserSession.getUserSession(SplashScreenActivity.this)
                );
            }
        });
    }


    private void gotoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void alertMessageInternetConnection(final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage("No internet connection");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry",
                (dialog, which) -> {
                    SplashScreenActivity.super.recreate();
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {
        loading.setVisibility(View.GONE);
        alertMessageInternetConnection(this);
    }

    private void visibleKhmerLanguage() {
        setLocale(Constants.KHMER_LANGUAGE);
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
}
