package com.udaya.virak_buntham.vetpickup.scanner;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.MoveItemToVanActivity;
import com.udaya.virak_buntham.vetpickup.activities.ScanReceiveTransitActivity;
import com.udaya.virak_buntham.vetpickup.activities.movetovan.ReceiveItemActivity;
import com.udaya.virak_buntham.vetpickup.activities.ScanMoveItemToVanActivity;
import com.udaya.virak_buntham.vetpickup.activities.ScanReceiveActivity;
import com.udaya.virak_buntham.vetpickup.activities.ScanTransitActivity;
import com.udaya.virak_buntham.vetpickup.activities.SearchReceiveCodeActivity;
import com.udaya.virak_buntham.vetpickup.activities.TransitActivity;

import java.util.List;

public class ScanMovetoVanActivity extends AppCompatActivity {

    private DecoratedBarcodeView barcodeView = null;
    private BeepManager beepManager = null;
    private String lastText = null;
    private CheckBox checkBoxAutoScan;
    public static int autoScan;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            beepManager.playBeepSoundAndVibrate();
            lastText = result.getText();
            ScanMoveItemToVanActivity.code = lastText;
            ScanReceiveActivity.code = lastText;
            ScanReceiveTransitActivity.code = lastText;
            SearchReceiveCodeActivity.codeReceive = lastText;
            ScanTransitActivity.code = lastText;
            onBackPressed();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_moveto_van);
        barcodeView = findViewById(R.id.barcode_scanner);
        checkBoxAutoScan = findViewById(R.id.checkBoxAutoScan);
        barcodeView.setStatusText("");
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);
        beepManager = new BeepManager(this);
        barcodeView.resume();

        Button back = findViewById(R.id.btnBack);
        back.setOnClickListener(v -> {
            Bundle b = getIntent().getExtras();
            try {
                int number = b.getInt("numberQrCode");
                if (number == 1) {
                    MoveItemToVanActivity.branch = "";
                    MoveItemToVanActivity.branchId = "";
                    autoScan = 0;
                    ScanMoveItemToVanActivity.code = "";
                    onBackPressed();
//                    startActivity(new Intent(getBaseContext(), MoveItemToVanActivity.class));
                } else if (number == 2) {
                    ReceiveItemActivity.branch = "";
                    ReceiveItemActivity.branchId = "";
                    autoScan = 0;
                    ScanReceiveActivity.code = "";
                    onBackPressed();
//                    startActivity(new Intent(getBaseContext(), ReceiveItemActivity.class));
                } else if (number == 3) {
                    TransitActivity.branch = "";
                    TransitActivity.branchId = "";
                    startActivity(new Intent(getBaseContext(), TransitActivity.class));
                }
            } catch (Exception e) {
                onBackPressed();
            }
        });

        if (autoScan == 1) {
            checkBoxAutoScan.setChecked(true);
        } else {
            autoScan = 0;
        }
        checkBoxAutoScan.setOnClickListener(v -> {
            if (checkBoxAutoScan.isChecked()) {
                autoScan = 1;
            } else {
                autoScan = 0;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }


}
