package com.udaya.virak_buntham.vetpickup.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.models.membership.Membership;
import com.udaya.virak_buntham.vetpickup.utils.Constants;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MembershipScannerActivity extends AppCompatActivity implements SurfaceHolder.Callback, Detector.Processor<Barcode>, View.OnClickListener {
    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    @BindView(R.id.button_cancel_scan)
    Button buttonCancelScan;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private String[] membershipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_scanner);
        ButterKnife.bind(this);

        buttonCancelScan.setOnClickListener(this);
        initBarcodeDetector();
    }

    private void initBarcodeDetector() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(this);
        barcodeDetector.setProcessor(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraSource != null) {
            cameraSource.release();
        }
        if (barcodeDetector != null) {
            barcodeDetector.release();
        }

    }

    // Methods implement from SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (ActivityCompat.checkSelfPermission(MembershipScannerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(surfaceView.getHolder());
            } else {
                ActivityCompat.requestPermissions(MembershipScannerActivity.this, new
                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        cameraSource.stop();
    }

    // Methods implement from Detector.Processor<Barcode>
    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        final SparseArray<Barcode> barcode = detections.getDetectedItems();
        if (barcode.size() != 0) {
            buttonCancelScan.post(() -> {
                if (!barcode.valueAt(0).displayValue.isEmpty()) {
                    membershipData = getListFromString(barcode.valueAt(0).displayValue);
                    Log.i("scanValueCode", barcode.valueAt(0).displayValue);
                    Log.i("scanValue", membershipData[0]);
                    backToGoodsTransfer(new Membership(membershipData[0], membershipData[membershipData.length - 1]));
                    barcodeDetector.release();
                }
            });
        }
    }

    private String[] getListFromString(String str) {
        return str.split(",");
    }

    private void backToGoodsTransfer(Membership result) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.MEMBERSHIP_KEY, result);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_cancel_scan) {
            onBackPressed();
        }
    }
}
