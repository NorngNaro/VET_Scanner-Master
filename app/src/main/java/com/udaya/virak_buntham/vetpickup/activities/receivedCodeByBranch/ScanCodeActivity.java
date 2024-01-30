package com.udaya.virak_buntham.vetpickup.activities.receivedCodeByBranch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.ScanReceiveActivity;
import com.udaya.virak_buntham.vetpickup.activities.callToCustomer.CallToCustomerScanToLocationActivity;
import com.udaya.virak_buntham.vetpickup.activities.callToCustomer.ItemDetailActivity;
import com.udaya.virak_buntham.vetpickup.activities.callToCustomer.ScanCallToCustomerActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.saveCustomerCall.CallHistoryResponse;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanCodeActivity extends AppCompatActivity  implements OnInternetConnectionListener{

    public static String brandId;
    public static int scanAgain = 0;
    private DecoratedBarcodeView barcodeView = null;
    private BeepManager beepManager = null;
    private String lastText = null;
    int scan = 1;



    public static int checkScan;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            beepManager.playBeepSoundAndVibrate();
            lastText = result.getText();
            if (scan == 1) {
                Log.d("code=>", "" + lastText);
                Log.d("brandId=>", "" + brandId);
                scanCustomerReceive(DeviceID.getDeviceId(ScanCodeActivity.this)
                        , RequestParams.getTokenRequestParams(ScanCodeActivity.this)
                        , RequestParams.getSignatureRequestParams(ScanCodeActivity.this)
                        , UserSession.getUserSession(ScanCodeActivity.this),
                      brandId,lastText);
                scan = 2;
            }
            barcodeView.pause();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    private ProgressDialog progressDialog;


    //process mobile scan
    EditText edtScanValue;
    RelativeLayout relativeLayoutScan, relativeLayoutMobileScan;

    MediaPlayer sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        progressDialog = new ProgressDialog(this);
        barcodeView = findViewById(R.id.barcode_scanner);
//        if (scanAgain == 1) {
//            Log.d("brandId==>", "" + brandId);
//        } else {
//            brandId = getIntent().getStringExtra("brandId");
//        }
        findViewById(R.id.btnBack).setOnClickListener(view -> onBackPressed());
        edtScanValue = findViewById(R.id.edtScanValue);
        relativeLayoutScan = findViewById(R.id.relatetiveScan);
        relativeLayoutMobileScan = findViewById(R.id.relativeMobileScanner);
        if (checkScan == 1) {
            edtScanValue.requestFocus();
            edtScanValue.setFocusable(true);
            closeKeyboard();
            edtScanValue.setOnEditorActionListener(editorListenerScan);
            relativeLayoutMobileScan.setVisibility(View.VISIBLE);
            relativeLayoutScan.setVisibility(View.GONE);
        } else {
            barcodeView.setStatusText("");
            barcodeView.initializeFromIntent(getIntent());
            barcodeView.decodeContinuous(callback);
            beepManager = new BeepManager(this);
            barcodeView.resume();
            relativeLayoutMobileScan.setVisibility(View.GONE);
            relativeLayoutScan.setVisibility(View.VISIBLE);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                edtScanValue.clearFocus();
                closeKeyboard();
//                if (scan == 1) {
//                    startActivity(new Intent(getBaseContext(), CallToCustomerScanToLocationActivity.class)
//                            .putExtra("brandId", "" + brandId)
//                            .putExtra("code", edtScanValue.getText().toString().trim())
//                    );
//                    Toast.makeText(this, "scan mobile receive", Toast.LENGTH_SHORT).show();
                scanCustomerReceiveMobile(DeviceID.getDeviceId(ScanCodeActivity.this)
                            , RequestParams.getTokenRequestParams(ScanCodeActivity.this)
                            , RequestParams.getSignatureRequestParams(ScanCodeActivity.this)
                            , UserSession.getUserSession(ScanCodeActivity.this),
                            brandId,edtScanValue.getText().toString().trim());
                    edtScanValue.setText("");
//                }
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private final TextView.OnEditorActionListener editorListenerScan = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_GO:
                    if (keyEvent == null || keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                        return false;
                    if (edtScanValue.getText().toString().isEmpty()) {
                        closeKeyboard();
                    } else {
                        closeKeyboard();
//                        if (scan == 1) {
                            startActivity(new Intent(getBaseContext(), CallToCustomerScanToLocationActivity.class)
                                    .putExtra("brandId", "" + brandId)
                                    .putExtra("code", edtScanValue.getText().toString().trim())
                            );
//                            scan = 2;
//                        }
                        edtScanValue.setText("");
                    }
                    return true;
            }
            return false;
        }
    };

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (checkScan == 0) {
            barcodeView.resume();
        }else{
            edtScanValue.requestFocus();
            edtScanValue.setFocusable(true);
            closeKeyboard();
            edtScanValue.setOnEditorActionListener(editorListenerScan);
            relativeLayoutMobileScan.setVisibility(View.VISIBLE);
            relativeLayoutScan.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (checkScan == 0) {
            barcodeView.pause();
        }

    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }
    private void scanCustomerReceive(String device, String token, String signature, String session,String brandId,String code) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        barcodeView.pause();
        Log.d("action work==>","ture");
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<CallHistoryResponse> call = apiService.scanCustomerReceive(device, token, signature, session, brandId,code);
        call.enqueue(new Callback<CallHistoryResponse>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<CallHistoryResponse> call, @androidx.annotation.NonNull Response<CallHistoryResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            try {
                                ListReceivedCodeByBranchActivity.id = response.body().getId();
                                startActivity(new Intent(getBaseContext(),ListReceivedCodeByBranchActivity.class));
                                scan = 1;
                            }catch (Exception e){
                                Log.d("errordata==>",""+e.toString());
                            }
                        }else{
                            sound = MediaPlayer.create(getBaseContext(), R.raw.wrong_voice);
                            try {
                                sound.start();
                                onBackPressed();
                            } catch (Exception ignored) {}

                        }
                    }
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<CallHistoryResponse> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", ""+t.getMessage());
                Toast.makeText(ScanCodeActivity.this, "លេខកូដមិនត្រឹមត្រូវ", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }
    private void scanCustomerReceiveMobile(String device, String token, String signature, String session,String brandId,String code) {
        Log.d("action work==>","ture");
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<CallHistoryResponse> call = apiService.scanCustomerReceive(device, token, signature, session, brandId,code);
        call.enqueue(new Callback<CallHistoryResponse>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<CallHistoryResponse> call, @androidx.annotation.NonNull Response<CallHistoryResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            try {
                                ListReceivedCodeByBranchActivity.id = response.body().getId();
                                startActivity(new Intent(getBaseContext(),ListReceivedCodeByBranchActivity.class));
                                scan = 2;
                            }catch (Exception e){
                                Log.d("errordata==>",""+e.toString());
                            }

                        }else{
                            sound = MediaPlayer.create(getBaseContext(), R.raw.wrong_voice);
                            try {
                                sound.start();
                                onBackPressed();
                            } catch (Exception ignored) {}

                        }
                    }
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<CallHistoryResponse> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", ""+t.getMessage());
                Toast.makeText(ScanCodeActivity.this, "លេខកូដមិនត្រឹមត្រូវ", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }
}

