package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.ScanQrRespone;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AppConfig;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceivingActivity extends AppCompatActivity implements OnInternetConnectionListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    SignaturePad mSignaturePad;

    String codeValue = "";
    String codeScan = "";
    Bundle extras = null;
    TextView tvTotalFee;
    TextView tvCod;
    TextView tvTransferCode;
    TextView tvFee;

    private ProgressDialog progressDialog;
    String intentReceiving = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        mToolbarTitle.setText(getResources().getString(R.string.receive));
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        getSignature();
        getIntentData();
        ReceiveData();

        extras = getIntent().getExtras();
        String code = extras.getString("SerirerCode");
        String status = extras.getString("SerirerStatus");
        if (status != null) {
            requestQrCode(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this),
                    code
            );
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }

    private void getSignature() {
        mSignaturePad = findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
            }

            @Override
            public void onClear() {
            }
        });
    }

    private void getCustomerReceive(String device, String token, String signature, String session, String code) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ScanQrRespone> call = apiService.getCustomerReceive(device, token, signature, session, code,""+ AppConfig.getGetLongitude(),""+AppConfig.getGetLatitude());
        call.enqueue(new Callback<ScanQrRespone>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Response<ScanQrRespone> response) {
                if (response.isSuccessful()) {
//                    if (response.body().getStatus() != null) {
                    assert response.body() != null;
                    showDialog(response.body().getStatus());
//                    }
                    if (response.body().getStatus().equals("0")) {
                        showDialog();
                    } else {
                        RequestParams.persistRequestParams(getBaseContext()
                                , response.body().getToken()
                                , response.body().getSignature());
                        if (intentReceiving.equals("1")) {
                            Intent intent = new Intent(ReceivingActivity.this, PrintReceiptCustomerReceiveActivity.class);
                            intent.putExtra("CODValue", tvCod.getText().toString().trim());
                            intent.putExtra("FEEValue", tvFee.getText().toString().trim());
                            intent.putExtra("CODEValue", tvTransferCode.getText().toString().trim());
                            intent.putExtra("NameValue", response.body().getUsername());
                            startActivity(intent);
                        }
                    }
                } else {
                    Toast.makeText(ReceivingActivity.this, "incorrect_code", Toast.LENGTH_SHORT).show();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @SuppressLint("SetTextI18n")
    private void getIntentData() {
        extras = getIntent().getExtras();
        try {
            codeValue = extras.getString("code");
            String receiverValue = extras.getString("receiver");
            String balanceValue = extras.getString("balance");
            String codValue = extras.getString("cod");
            String codeCurrencyValue = extras.getString("cod_currency");
            codeScan = extras.getString("CodeScan");
            tvTransferCode = findViewById(R.id.tvTransferCode);
            TextView tvReceiverNumber = findViewById(R.id.tvReceiverTelephone);
            tvTotalFee = findViewById(R.id.tvTotalFee);
            tvCod = findViewById(R.id.tvCod);
            TextView tvCurrency = findViewById(R.id.tvCurrency);

            tvTransferCode.setText(codeValue);
            tvReceiverNumber.setText(receiverValue);
            if (balanceValue.equals("0")) {
                tvTotalFee.setText(getResources().getString(R.string.paid));
            } else {
                tvTotalFee.setText(balanceValue + "៛");
            }
            if (codValue.equals("")) {
                tvCod.setText(getResources().getString(R.string.none));
            } else {
                tvCod.setText(codValue + codeCurrencyValue);
            }

            tvCurrency.setText(codeCurrencyValue);

            Log.d("CodeScan==>", "" + codeScan);
            Log.d("CodeCOD==>", "" + codValue);
        } catch (Exception e) {
//            Toast.makeText(this, "can not get intent Data", Toast.LENGTH_SHORT).show();
        }


    }

    private void showDialog(String status) {

        SweetAlertDialog alertDialog = new SweetAlertDialog(this);
        alertDialog.setTitleText(getResources().getString(R.string.message));

        if (status.equals("0")) {
            alertDialog.setContentText(getResources().getString(R.string.data_could_not_save));
            alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        } else {
            alertDialog.setContentText(getResources().getString(R.string.data_has_been_save));
            alertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        }

        alertDialog.setConfirmText(getResources().getString(R.string.close));
        alertDialog.setConfirmClickListener(sDialog -> startActivity(new Intent(getBaseContext(), HomeActivity.class))).show();
        alertDialog.setCanceledOnTouchOutside(true);
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

    }

    public void ReceiveData() {
        final Button btnReceive = findViewById(R.id.btnReceiv);
        btnReceive.setOnClickListener(view -> {
            btnReceive.setText(getResources().getString(R.string.loadings));
            btnReceive.setClickable(false);
            tvFee = findViewById(R.id.tvTotalFee);
            TextView tvCod = findViewById(R.id.tvCod);
//                getCustomerReceive(DeviceID.getDeviceId(getBaseContext())
//                        , RequestParams.getTokenRequestParams(getBaseContext())
//                        , RequestParams.getSignatureRequestParams(getBaseContext())
//                        , UserSession.getUserSession(getBaseContext()), codeScan);
            if (!tvFee.getText().toString().trim().equals(getResources().getString(R.string.paid)) || !tvCod.getText().toString().trim().equals(getResources().getString(R.string.none))) {
                intentReceiving = "1";
            }
            getCustomerReceive(DeviceID.getDeviceId(getBaseContext())
                    , RequestParams.getTokenRequestParams(getBaseContext())
                    , RequestParams.getSignatureRequestParams(getBaseContext())
                    , UserSession.getUserSession(getBaseContext()), codeScan);
        });


    }

    public void clearSignature(View view) {
        mSignaturePad.clear();
    }

    private void showDialog() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.data_could_not_save));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setConfirmClickListener(sDialog -> alertDialog.dismissWithAnimation())
                .show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setConfirmClickListener(sDialog -> startActivity(new Intent(getBaseContext(), HomeActivity.class))).show();
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void requestQrCode(String device, String token, String signature, String session, String code) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ScanQrRespone> call = apiService.getScanQrCode(device, token, signature, session, code);
        call.enqueue(new Callback<ScanQrRespone>() {
            @SuppressLint("SetTextI18n")
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
                        try {
                            codeValue = response.body().getCode();
                            String receiverValue = response.body().getReceiver();
                            String balanceValue = response.body().getTransferFee();
                            String codValue = response.body().getCod();
                            String codeCurrencyValue = response.body().getCod_currency();
                            codeScan = response.body().getCode();
                            tvTransferCode = findViewById(R.id.tvTransferCode);
                            TextView tvReceiverNumber = findViewById(R.id.tvReceiverTelephone);
                            tvTotalFee = findViewById(R.id.tvTotalFee);
                            tvCod = findViewById(R.id.tvCod);
                            TextView tvCurrency = findViewById(R.id.tvCurrency);

                            tvTransferCode.setText(codeValue);
                            tvReceiverNumber.setText(receiverValue);
                            if (balanceValue.equals("0")) {
                                tvTotalFee.setText(getResources().getString(R.string.paid));
                            } else {
                                tvTotalFee.setText(balanceValue + "៛");
                            }
                            if (codValue.equals("")) {
                                tvCod.setText(getResources().getString(R.string.none));
                            } else {
                                tvCod.setText(codValue + codeCurrencyValue);
                            }
                            tvCurrency.setText(codeCurrencyValue);

                            Log.d("CodeScan==>", "" + codeScan);
                            Log.d("CodeCOD==>", "" + codValue);
                        } catch (Exception e) {
//            Toast.makeText(this, "can not get intent Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Response is not Successful", Toast.LENGTH_LONG).show();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }
}