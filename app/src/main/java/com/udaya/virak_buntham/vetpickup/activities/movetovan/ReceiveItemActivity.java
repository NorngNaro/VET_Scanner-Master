package com.udaya.virak_buntham.vetpickup.activities.movetovan;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.ScanReceiveActivity;
import com.udaya.virak_buntham.vetpickup.activities.SelectionActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.permission.PermissionRespone;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;
import com.udaya.virak_buntham.vetpickup.utils.AppConfig;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.GPSTracker;
import com.udaya.virak_buntham.vetpickup.utils.GpsTrackerLocation;
import com.udaya.virak_buntham.vetpickup.utils.GrobleFuntion;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiveItemActivity extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.button_start_Scan)
    Button btnStartScan;
    @BindView(R.id.btn_select_date)
    Button ButtonDate;
    @BindView(R.id.button_select_branch)
    Button ButtonSelectBranch;
    @BindView(R.id.btnSelectLocation)
    Button btnSelectLocation;
    @BindView(R.id.btnMobileScanByTel)
    Button btnMobileScanByTel;


    String date;

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    public static String code = "";


    public static String branch = "";
    public static String branchId = "";
    public static String locationId = "";

    private GpsTrackerLocation gpsTracker;
    public void getLocationData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gpsTracker = new GpsTrackerLocation(ReceiveItemActivity.this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (gpsTracker.canGetLocation()) {
                double letData = gpsTracker.getLatitude();
                double longData = gpsTracker.getLongitude();
                AppConfig.setGetLatitude(letData);
                AppConfig.setGetLongitude(longData);
             //   Toast.makeText(getBaseContext(), "==>" + AppConfig.getGetLatitude() + "==" + AppConfig.getGetLongitude(), Toast.LENGTH_SHORT).show();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocationData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_item);
        ButterKnife.bind(this);
        mToolbarTitle.setText("ទទួលអីវ៉ាន់ពីឡាន");
        registerOnClick(this);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        GrobleFuntion.setDate(ButtonDate);
//        if (branch.isEmpty() && branchId.isEmpty()) {
//            ButtonSelectBranch.setText(getResources().getString(R.string.please_select));
//        } else {
//            ButtonSelectBranch.setText(branch);
//        }
        checkerPermission(DeviceID.getDeviceId(this), RequestParams.getTokenRequestParams(this), RequestParams.getSignatureRequestParams(this), UserSession.getUserSession(this));

        findViewById(R.id.btnMobileScan).setOnClickListener(v -> {
            ScanReceiveActivity.scanCondition = 1;
            gotoScanReceiveItem();
        });
        getLetLong();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, HomeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerOnClick(View.OnClickListener clickListener) {
        btnStartScan.setOnClickListener(clickListener);
        ButtonSelectBranch.setOnClickListener(clickListener);
        btnSelectLocation.setOnClickListener(clickListener);
        btnMobileScanByTel.setOnClickListener(clickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start_Scan:
                ScanReceiveActivity.scanCondition = 0;
                gotoScanReceiveItem();
                break;
            case R.id.button_select_branch:
                gotoRequestBrand();
                break;
            case R.id.btnSelectLocation:
                gotoRequestLocation();
                break;
            case R.id.btnMobileScanByTel:
                if (ButtonSelectBranch.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
                    AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.plzSelectBranch));
                } else if (btnSelectLocation.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
                    AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.please_select_location));
                } else {
                    Intent intent = new Intent(this, ReceiveItemByTelActivity.class);
                    intent.putExtra("type", "1"); // 1 is received item
                    intent.putExtra("brandId", "" + branchId);
                    intent.putExtra("locationId", "" + locationId);
                    startActivity(intent);
                }
                break;

        }
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }

    private void gotoScanReceiveItem() {
        checkValue();
//        startActivity(new Intent(this, ScanReceiveActivity.class));
    }

    private void gotoRequestBrand() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_BRANCH_CODE);
        startActivityForResult(intent, Constants.REQUEST_BRANCH_CODE);
    }

    private void gotoRequestLocation() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_LOCATION_CODE);
        intent.putExtra("branchIdLocate", branchId);
        startActivityForResult(intent, Constants.REQUEST_LOCATION_CODE);
    }

    private void checkValue() {
        if (ButtonSelectBranch.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.plzSelectBranch));
        } else if (btnSelectLocation.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.please_select_location));
        } else {
            Intent intent = new Intent(this, ScanReceiveActivity.class);
            intent.putExtra("date", date);
            intent.putExtra("brandId", "" + branchId);
            intent.putExtra("locationId", "" + locationId);
            intent.putExtra("sysCode", getRandomString());
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,HomeActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_BRANCH_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionBranch = data.getParcelableExtra(Constants.REQUEST_BRANCH_KEY);
            assert selectionBranch != null;
            ButtonSelectBranch.setText(selectionBranch.getName());
            branchId = selectionBranch.getId();
            btnSelectLocation.setEnabled(true);
        } else if (requestCode == Constants.REQUEST_LOCATION_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionLocation = data.getParcelableExtra(Constants.REQUEST_LOCATION_KEY);
            btnSelectLocation.setText(selectionLocation.getName());
            locationId = selectionLocation.getId();
        }
    }

    // get random Code
    private static String getRandomString() {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 15; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void getLetLong() {
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            String stringLongitude = String.valueOf(gpsTracker.longitude);
            Log.d("letAndLong==>", "" + stringLatitude + "=" + stringLongitude);
        }
    }

    private void checkerPermission(String device, String token, String signature, String session) {
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<PermissionRespone> call = apiService.getPermission(device, token, signature, session);
        call.enqueue(new Callback<PermissionRespone>() {
            @Override
            public void onResponse(@NonNull Call<PermissionRespone> call, @NonNull Response<PermissionRespone> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            //Replace token
                            RequestParams.persistRequestParams(ReceiveItemActivity.this
                                    , response.body().getToken()
                                    , response.body().getSignature());

                            branchId = response.body().getBranchId();
                            branch = response.body().getBranchName();
                            if (branch.isEmpty() && branchId.isEmpty()) {
                                ButtonSelectBranch.setEnabled(true);
                                ButtonSelectBranch.setText(getResources().getString(R.string.please_select));
                            } else {
                                ButtonSelectBranch.setText(branch);
                                ButtonSelectBranch.setEnabled(false);
                                btnSelectLocation.setEnabled(true);
                            }
                        }

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<PermissionRespone> call, @NonNull Throwable t) {
            }
        });
    }
}