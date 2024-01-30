package com.udaya.virak_buntham.vetpickup.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.changeDestination.ChangeBranchActivity;
import com.udaya.virak_buntham.vetpickup.activities.changeDestination.ChangeDestinationActivity;
import com.udaya.virak_buntham.vetpickup.activities.changeDestination.ReturnActivity;
import com.udaya.virak_buntham.vetpickup.activities.changeDestination.ReturnToCampusActivity;
import com.udaya.virak_buntham.vetpickup.activities.outForDevliery.OutForDeliveryActivity;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;
import com.udaya.virak_buntham.vetpickup.utils.Constants;

import java.util.List;

public class ScanChangeDestinationActivity extends AppCompatActivity {

    private DecoratedBarcodeView barcodeView = null;
    private BeepManager beepManager = null;

    public  static  String condition ;

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            beepManager.playBeepSoundAndVibrate();
            String lastText = result.getText();
            barcodeView.pause();
            switch (condition) {
                case "return":
                    ReturnActivity.code = lastText;
                    break;
                case "changeBranch":
                    ChangeBranchActivity.code = lastText;
                    break;
                case "changeDestination":
                    ChangeDestinationActivity.code = lastText;
                    break;
                case "returnHQ":
                    ReturnToCampusActivity.code = lastText;
                    break;
                case "outForDelivery":
                    OutForDeliveryActivity.outForDeliveryScan = lastText;
                    break;

            }
            onBackPressed();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    private void returnIntent(String key, String result) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(key, result);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_change_destination);
        barcodeView = findViewById(R.id.barcode_scanner);
        barcodeView.setStatusText("");
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);
        beepManager = new BeepManager(this);
        barcodeView.resume();

        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
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