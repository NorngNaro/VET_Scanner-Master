package com.udaya.virak_buntham.vetpickup.activities.callToCustomer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;

import java.util.List;
import java.util.Objects;

public class ScanCallToCustomerActivity extends AppCompatActivity implements OnInternetConnectionListener {

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
            Log.d("code=>", "" + lastText);
            if (scan == 1) {
                startActivity(new Intent(ScanCallToCustomerActivity.this, CallToCustomerScanToLocationActivity.class)
                        .putExtra("brandId", "" + brandId)
                        .putExtra("code", lastText)
                );
                scan = 2;
            }


//            scanCustomerCall(DeviceID.getDeviceId(ScanCallToCustomerActivity.this)
//                    , RequestParams.getTokenRequestParams(ScanCallToCustomerActivity.this)
//                    , RequestParams.getSignatureRequestParams(ScanCallToCustomerActivity.this)
//                    , UserSession.getUserSession(ScanCallToCustomerActivity.this), Integer.parseInt(brandId), lastText);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_call_to_customer);
        progressDialog = new ProgressDialog(this);
        barcodeView = findViewById(R.id.barcode_scanner);
        if (scanAgain == 1) {
            Log.d("brandId==>", "" + brandId);
        } else {
            brandId = getIntent().getStringExtra("brandId");
        }
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
                if (scan == 1) {
                    startActivity(new Intent(ScanCallToCustomerActivity.this, CallToCustomerScanToLocationActivity.class)
                            .putExtra("brandId", "" + brandId)
                            .putExtra("code", edtScanValue.getText().toString().trim())
                    );
                    scan = 2;
                    edtScanValue.setText("");
                }
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private TextView.OnEditorActionListener editorListenerScan = new TextView.OnEditorActionListener() {
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
                        if (scan == 1) {
                            Toast.makeText(ScanCallToCustomerActivity.this, "go", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ScanCallToCustomerActivity.this, CallToCustomerScanToLocationActivity.class)
                                    .putExtra("brandId", "" + brandId)
                                    .putExtra("code", edtScanValue.getText().toString().trim())
                            );
                            scan = 2;
                        }
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


}