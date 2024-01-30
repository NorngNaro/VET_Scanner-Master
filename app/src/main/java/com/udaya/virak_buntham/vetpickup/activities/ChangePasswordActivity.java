package com.udaya.virak_buntham.vetpickup.activities;


import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.changepassword.ChangePasswordResponse;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.InputMethod;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.edt_current_password)
    EditText edtCurrentPassword;
    @BindView(R.id.edt_new_password)
    EditText edtNewPassword;
    @BindView(R.id.edt_re_enter_new_password)
    EditText edtReEnterNewPassword;
    @BindView(R.id.change_password_button_continue)
    Button buttonContinue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        mToolbarTitle.setText(R.string.change_password);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        registerClickListener(this);

    }

    private void registerClickListener(View.OnClickListener clickListener) {
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
        if (v.getId() == R.id.change_password_button_continue) {
            changePassword(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this)
                    , edtCurrentPassword.getText().toString()
                    , edtNewPassword.getText().toString()
                    , edtReEnterNewPassword.getText().toString());
        }
    }

    private void gotoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void hideInputMethod(View view) {
        InputMethod.hideSoftKeyboard(this);
    }

    private void changePassword(String device, String token, String signature, String session, String oldPassword, String newPassword, String reNewPassword) {

        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ChangePasswordResponse> call = apiService.changePassword(device, token, signature, session, oldPassword, newPassword, reNewPassword);
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChangePasswordResponse> call, @NonNull Response<ChangePasswordResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.i("changePassword", response.body().getInfo());
                    if (response.body() != null) {
                        if (response.body().getStatus().equals(Constants.STATUS_SUCCESS)) {
                            RequestParams.persistRequestParams(ChangePasswordActivity.this
                                    , response.body().getToken()
                                    , response.body().getSignature()
                            );

                            AlertDialogUtil.alertMessageInput(ChangePasswordActivity.this, "Change Password have been changed");
                            gotoHome();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChangePasswordResponse> call, @NonNull Throwable t) {

            }
        });


    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {
        AlertDialogUtil.alertMessageInternetConnection(this);
    }
}
