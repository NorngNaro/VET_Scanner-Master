package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.ListSearch.SearchData;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.ScanQrRespone;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.scanner.ScanMovetoVanActivity;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchReceiveCodeActivity extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.buttonScanQr)
    Button btnScanQr;
    @BindView(R.id.edt_search_code)
    EditText editCode;
    @BindView(R.id.btn_search)
    Button btnSearchCode;
    private ProgressDialog progressDialog;
    public static String codeReceive = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_reciever_code);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        codeReceive = "";
        mToolbarTitle.setText("អតិថិជនទទួលអីវ៉ាន់(ដឹកជញ្ជូន)");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        setOnClick();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, HomeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }

    private void setOnClick() {
        btnScanQr.setOnClickListener(view ->
            startActivity(new Intent(SearchReceiveCodeActivity.this, ScanMovetoVanActivity.class))
        );
        btnSearchCode.setOnClickListener(view -> {
            if (editCode.getText().toString().isEmpty()) {
                invalidCodeMessage();
            } else {
                requestSearchList(
                        DeviceID.getDeviceId(getBaseContext())
                        , RequestParams.getTokenRequestParams(getBaseContext())
                        , RequestParams.getSignatureRequestParams(getBaseContext())
                        , UserSession.getUserSession(getBaseContext())
                        , editCode.getText().toString().trim()
                );
            }

        });
    }

    private void requestQrCode(String device, String token, String signature, String session, final String code) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ScanQrRespone> call = apiService.getScanQrCode(device, token, signature, session, code);
        call.enqueue(new Callback<ScanQrRespone>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Response<ScanQrRespone> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("0")) {
                        showDialog();
                    } else {
                        RequestParams.persistRequestParams(getBaseContext()
                                , response.body().getToken()
                                , response.body().getSignature());
                        Intent intent = new Intent(getBaseContext(), ReceivingActivity.class);
                        intent.putExtra("code", response.body().getCode());
                        intent.putExtra("sender", response.body().getSender());
                        intent.putExtra("receiver", response.body().getReceiver());
                        intent.putExtra("destination", response.body().getDestination_to());
                        intent.putExtra("balance", response.body().getTransferFee());
                        intent.putExtra("cod", response.body().getCod());
                        intent.putExtra("cod_currency", response.body().getCod_currency());
                        intent.putExtra("CodeScan", code);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Response is not Successful", Toast.LENGTH_LONG).show();
                }
                progressDialog.hide();
                codeReceive = "";
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

    private void showDialog() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.wrong_number));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setConfirmClickListener(sDialog -> alertDialog.dismissWithAnimation())
                .show();
        alertDialog.setCanceledOnTouchOutside(true);
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void invalidCodeMessage() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.plzinputcode));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setConfirmClickListener(sDialog -> alertDialog.dismissWithAnimation())
                .show();
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void requestSearchList(String device, String token, String signature, String session, String code) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<SearchData> call = apiService.searchList(device, token, signature, session, code);
        call.enqueue(new Callback<SearchData>() {
            @Override
            public void onResponse(@NonNull Call<SearchData> call, @NonNull Response<SearchData> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("0")) {
                            showDialog();
                        } else {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            Intent intent = new Intent(SearchReceiveCodeActivity.this, SearchListActivity.class);
                            intent.putExtra("SenderNumber", editCode.getText().toString().trim());
                            startActivity(intent);
                        }
                    }

                }

                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<SearchData> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!codeReceive.equals("")) {
            requestQrCode(
                    DeviceID.getDeviceId(getBaseContext())
                    , RequestParams.getTokenRequestParams(getBaseContext())
                    , RequestParams.getSignatureRequestParams(getBaseContext())
                    , UserSession.getUserSession(getBaseContext())
                    , codeReceive);
        }
    }
}