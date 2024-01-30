package com.udaya.virak_buntham.vetpickup.activities;

import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.ScanQrRespone;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiveScanActivity extends AppCompatActivity implements OnInternetConnectionListener {

    private CodeScanner mCodeScanner;

    private String CodeFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_scan);
        customScan();
    }

    public void backPressCancle(View view) {
        onBackPressed();
    }

    private void customScan() {
        CodeScannerView scannerView = findViewById(R.id.scannerReceive);

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            Toast.makeText(ReceiveScanActivity.this, "Scanning", Toast.LENGTH_SHORT).show();
            CodeFile = result.getText();
            requestQrCode(
                    DeviceID.getDeviceId(getBaseContext())
                    , RequestParams.getTokenRequestParams(getBaseContext())
                    , RequestParams.getSignatureRequestParams(getBaseContext())
                    , UserSession.getUserSession(getBaseContext())
                    , CodeFile);
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    public void CancleQr(View view) {
        onBackPressed();
    }

    private void requestQrCode(String device, String token, String signature, String session, String code) {
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
                        intent.putExtra("CodeScan", CodeFile);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Response is not Successful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void showDialog() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.invalidcode));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setConfirmClickListener(sDialog -> {
            mCodeScanner.startPreview();
            alertDialog.dismissWithAnimation();
        })
                .show();
        alertDialog.setCanceledOnTouchOutside(true);
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }
}