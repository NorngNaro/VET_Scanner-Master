package com.udaya.virak_buntham.vetpickup.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.adapters.ScanReceiveAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.ScanItemList.ScanListItem;
import com.udaya.virak_buntham.vetpickup.models.moveitemtovan.MoveItemToVanData;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.scanner.ScanMovetoVanActivity;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;
import com.udaya.virak_buntham.vetpickup.utils.AppConfig;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.GpsTrackerLocation;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanReceiveTransitActivity extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.imgScanQr)
    ImageView imgScanQr;
    @BindView(R.id.relateQr)
    RelativeLayout layoutDisplaItem;
    @BindView(R.id.btnCancle)
    Button ButtonCancle;
    @BindView(R.id.relateNoData)
    RelativeLayout layoutNoData;
    @BindView(R.id.buttonComplete)
    Button ButtonFinish;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.txtSelectLocation)
    TextView tvSelectLocation;

    int cameraCheck = 0;
    private CodeScanner mCodeScanner;

    private ScanReceiveAdapter mAdapter;
    private ArrayList<com.udaya.virak_buntham.vetpickup.models.ScanItemList.ScanListItem> ScanListItem;

    //cardvalue
    String reciverTel = "";
    String destinationToName = "";
    String branchId = "";
    String sysCode = "";
    private ProgressDialog progressDialog;
    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    //Get selection
    private SelectionData selectionLocation;
    String locationId = "";

    @BindView(R.id.edt_item_code)
    EditText editScanValue;
    int scanValueData = 1;
    CodeScannerView scannerView;

    MediaPlayer sound;
    public static String code = "";
    public static int scanCondition;
    RecyclerView mRecyclerView;
    ImageView imgSearch;
    LinearLayout llEdit;
    EditText edtItemCodeScan;
    static boolean fromLed = false;
    private GpsTrackerLocation gpsTracker;


    public void getLocationData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gpsTracker = new GpsTrackerLocation(ScanReceiveTransitActivity.this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (gpsTracker.canGetLocation()) {
                double letData = gpsTracker.getLatitude();
                double longData = gpsTracker.getLongitude();
                AppConfig.setGetLatitude(letData);
                AppConfig.setGetLongitude(longData);
               // Toast.makeText(getBaseContext(), "==>" + AppConfig.getGetLatitude() + "==" + AppConfig.getGetLongitude(), Toast.LENGTH_SHORT).show();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receive);
        checkerPermissonCamera();
        ButterKnife.bind(this);
        code = "";
        progressDialog = new ProgressDialog(this);
        mToolbarTitle.setText("ផ្ទេរតជើង");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        scannerView = findViewById(R.id.scanner_view);
        imgSearch = findViewById(R.id.imgSearch);
        edtItemCodeScan = findViewById(R.id.edt_item_code_scan);
        mCodeScanner = new CodeScanner(this, scannerView);
        registerOnClick(this);

        getListScan();
        swipeRefreshLayout.setOnRefreshListener(this);
        getBundle();
        editScanValue.setOnEditorActionListener(editorListener);
        edtItemCodeScan.setOnEditorActionListener(editorListenerScan);
        llEdit = findViewById(R.id.llEdit);
        if (scanCondition == 0) {
//            imgScanQr.performClick();
            llEdit.setVisibility(View.VISIBLE);
        } else {
            llEdit.setVisibility(View.GONE);
            editScanValue.requestFocus();
            editScanValue.setFocusable(true);
            closeKeyboard();
            imgScanQr.setVisibility(View.GONE);
        }
        imgSearch.setOnClickListener(v -> {
            closeKeyboard();
            getScanTransitItem(DeviceID.getDeviceId(getBaseContext())
                    , RequestParams.getTokenRequestParams(getBaseContext())
                    , RequestParams.getSignatureRequestParams(getBaseContext())
                    , UserSession.getUserSession(getBaseContext()),
                    sysCode,
                    edtItemCodeScan.getText().toString().trim(),
                    branchId
            );
            editScanValue.setText("");
            TextView tvTotal = findViewById(R.id.tvtotalScan);
            tvTotal.setText("" + ScanListItem.size());
        });
//        editScanValue.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    if (editScanValue.getText().toString().isEmpty()) {
//                        closeKeyboard();
//                    } else {
//                        closeKeyboard();
//                        getScanReceivedItem(DeviceID.getDeviceId(getBaseContext())
//                                , RequestParams.getTokenRequestParams(getBaseContext())
//                                , RequestParams.getSignatureRequestParams(getBaseContext())
//                                , UserSession.getUserSession(getBaseContext()),
//                                getRandomString(),
//                                editScanValue.getText().toString().trim(),
//                                branchId,
//                                locationId
//                        );
//                        editScanValue.setText("");
//                        editScanValue.clearFocus();
//                        TextView tvTotal = findViewById(R.id.tvtotalScan);
//                        tvTotal.setText("" + ScanListItem.size());
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    private void registerOnClick(View.OnClickListener clickListener) {
        imgScanQr.setOnClickListener(clickListener);
        ButtonCancle.setOnClickListener(clickListener);
        ButtonFinish.setOnClickListener(clickListener);
        tvSelectLocation.setOnClickListener(clickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                code = "";
                AlertDialogUtil.dialogAlertTransitBack(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgScanQr:
                code = "";
                openCamera();
                break;
            case R.id.btnCancle:
                mCodeScanner.stopPreview();
                toggleButtom(layoutDisplaItem);
                break;
            case R.id.buttonComplete:
                onBackPressed();
                break;
            case R.id.txtSelectLocation:
                code = "";
                gotoRequestLocation();
                break;
        }
    }

    @Override
    public void onInternetAvailable() {}

    @Override
    public void onInternetUnavailable() {}

    //qr animation
    private void toggleButtom(RelativeLayout relativeLayout) {
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(300);
        transition.addTarget(R.id.relateQr);
        TransitionManager.beginDelayedTransition(layoutDisplaItem, transition);
        relativeLayout.setVisibility(View.GONE);
    }

    //qr animation
    private void toggleTop(RelativeLayout relativeLayout) {
        Transition transition = new Slide(Gravity.TOP);
        transition.setDuration(300);
        transition.addTarget(R.id.relateQr);
        TransitionManager.beginDelayedTransition(layoutDisplaItem, transition);
        relativeLayout.setVisibility(View.VISIBLE);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void checkerPermissonCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
        } else if (cameraCheck == 0) {
            cameraCheck = 1;
            Log.e("permission", "checkerPermissonCamera:True ");
        } else {
//            Toast.makeText(this, "allow Permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCamera() {
//        if (tvSelectLocation.getText().toString().trim().equals(getResources().getString(R.string.please_select_location))) {
//            AlertDialogUtil.alertMessageScan(ScanReceiveActivity.this, getResources().getString(R.string.please_select_location));
//        } else {
        Intent intent = new Intent(this, ScanMovetoVanActivity.class);
        intent.putExtra("numberQrCode", 3);
        startActivity(intent);
//            scanQr();
//            if (cameraCheck == 1) {
//                mCodeScanner.startPreview();
//                toggleTop(layoutDisplaItem);
//                closeKeyboard();
//            } else {
//                checkerPermissonCamera();
//            }
//        }

    }

    private void scanQr() {
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("qrResult==>", result.getText());
                getScanTransitItem(DeviceID.getDeviceId(getBaseContext())
                        , RequestParams.getTokenRequestParams(getBaseContext())
                        , RequestParams.getSignatureRequestParams(getBaseContext())
                        , UserSession.getUserSession(getBaseContext()),
                        sysCode,
                        result.getText(),
                        branchId
                );
                toggleButtom(layoutDisplaItem);

            }
        }));

        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    private void addcartItem(String itemcode, String reciverTel, String departureToName, String locationName) {
        toggleButtom(layoutDisplaItem);
//        ScanListItem.add(new ScanListItem(itemcode, reciverTel, departureToName));
//        layoutNoData.setVisibility(View.GONE);
//        mAdapter.notifyItemInserted(ScanListItem.size());
        ScanListItem.add(0, new ScanListItem(itemcode, reciverTel, departureToName,locationName));
        layoutNoData.setVisibility(View.GONE);
        mAdapter.notifyItemInserted(0);
        mRecyclerView.smoothScrollToPosition(0);
        mAdapter.notifyDataSetChanged();

        TextView tvTotal = findViewById(R.id.tvtotalScan);
        tvTotal.setText("" + ScanListItem.size());
    }

    //list recycler Scan
    private void getListScan() {
        ScanListItem = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recyclerScan);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ScanReceiveAdapter(ScanListItem);
        mAdapter.setOnItemClickListener(getOnItemClickListener());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter.getItemCount() == 0) {
            layoutNoData.setVisibility(View.VISIBLE);
        }
    }

    private OnItemClickListener getOnItemClickListener() {
        return position -> {
        };
    }

    @Override
    public void onBackPressed() {
        AlertDialogUtil.dialogAlertTransitBack(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    // do your work here
                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
                    Toast.makeText(this, "Go to Settings and Grant the permission to use this feature.", Toast.LENGTH_SHORT).show();
                    // User selected the Never Ask Again Option
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getScanTransitItem(String device, String token, String signature, String session, String sysCode, String code, String branch) {
        Log.e("", "getScanReceivedItem: "+ sysCode );
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<MoveItemToVanData> call = apiService.getTransitItem(device, token, signature, session, sysCode, code, branch, ""+AppConfig.getGetLatitude(),""+AppConfig.getGetLongitude());
        call.enqueue(new Callback<MoveItemToVanData>() {
            @Override
            public void onResponse(@NonNull Call<MoveItemToVanData> call, @NonNull Response<MoveItemToVanData> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
//                        if (tvSelectLocation.getText().toString().trim().equals(getResources().getString(R.string.please_select_location))) {
//                            AlertDialogUtil.alertMessageScan(ScanReceiveActivity.this, getResources().getString(R.string.please_select_location));
//                        } else {
                        assert response.body() != null;
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            String code = response.body().getCode();
                            String tel = response.body().getTelephone();
                            String getDestCode = response.body().getDestCode();
                            String getDestName = response.body().getLocationName();
                            Log.e("", "onResponse: Get Desname"+ getDestName );
                            addcartItem(code, tel, getDestCode, getDestName);

                            if(!response.body().getLed().equals("")){
                                CheckDeviceActivity.location = response.body().getLed();
                                fromLed = false;
                                startActivity(new Intent(ScanReceiveTransitActivity.this, CheckDeviceActivity.class));
                            }

                            if (response.body().getSound().equals("delivery")) {
                                sound = MediaPlayer.create(ScanReceiveTransitActivity.this, R.raw.delivery);
                            } else if (response.body().getSound().equals("vip")) {
                                sound = MediaPlayer.create(ScanReceiveTransitActivity.this, R.raw.vip);
                            } else if (response.body().getSound().equals("express")) {
                                sound = MediaPlayer.create(ScanReceiveTransitActivity.this, R.raw.express);
                            }else {
                                sound = MediaPlayer.create(ScanReceiveTransitActivity.this, R.raw.right_voice);
                            }
                            if (scanCondition == 0) {
                                edtItemCodeScan.setText("");
                            }
                        } else if ((response.body().getStatus().equals("2"))) {
                            String mess = response.body().getInfo();
//                            AlertDialogUtil.alertMessageScan(ScanReceiveActivity.this, mess);
                            sound = MediaPlayer.create(ScanReceiveTransitActivity.this, R.raw.scan_ready);
                        } else {
                            Log.e("", "onResponse:  Not true" );
                            String mess = response.body().getInfo();
//                            AlertDialogUtil.alertMessageScan(ScanReceiveActivity.this, mess);
                            sound = MediaPlayer.create(ScanReceiveTransitActivity.this, R.raw.wrong_voice);
                        }
//                        }
                       sound.start();
                    }
                }
                progressDialog.hide();
                scanValueData = 2;
            }

            @Override
            public void onFailure(@NonNull Call<MoveItemToVanData> call, @NonNull Throwable t) {
                Log.i("requestInfo", t.getMessage());
                progressDialog.hide();
                scanValueData = 2;
            }
        });
    }

    private void getBundle() {
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        branchId = b.getString("brandId");
        sysCode = b.getString("sysCode");
        locationId = b.getString("locationId");
        Log.d("branch==>", "" + branchId);
    }

    // get ramdom Code
    private static String getRandomString() {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 15; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void gotoRequestLocation() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_LOCATION_CODE);
        intent.putExtra("branchIdLocate", branchId);
        startActivityForResult(intent, Constants.REQUEST_LOCATION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getLocationData();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOCATION_CODE && resultCode == RESULT_OK && data != null) {
            selectionLocation = data.getParcelableExtra(Constants.REQUEST_LOCATION_KEY);
            tvSelectLocation.setText(selectionLocation.getName());
            locationId = selectionLocation.getId();
        }
    }

    //on click in keyboard
    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_GO:

                    if (keyEvent == null || keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                        return false;
                    if (editScanValue.getText().toString().isEmpty()) {
                        closeKeyboard();
                    } else {
                        closeKeyboard();
                        getScanTransitItem(DeviceID.getDeviceId(getBaseContext())
                                , RequestParams.getTokenRequestParams(getBaseContext())
                                , RequestParams.getSignatureRequestParams(getBaseContext())
                                , UserSession.getUserSession(getBaseContext()),
                                sysCode,
                                editScanValue.getText().toString().trim(),
                                branchId
                        );
                        editScanValue.setText("");
//                        editScanValue.clearFocus();
                        TextView tvTotal = findViewById(R.id.tvtotalScan);
                        tvTotal.setText("" + ScanListItem.size());
                    }
                    return true;
            }
            return false;
        }
    };

    private TextView.OnEditorActionListener editorListenerScan = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_GO:

                    if (keyEvent == null || keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                        return false;
                    if (editScanValue.getText().toString().isEmpty()) {
                        closeKeyboard();
                    } else {
                        closeKeyboard();
                        getScanTransitItem(DeviceID.getDeviceId(getBaseContext())
                                , RequestParams.getTokenRequestParams(getBaseContext())
                                , RequestParams.getSignatureRequestParams(getBaseContext())
                                , UserSession.getUserSession(getBaseContext()),
                                sysCode,
                                edtItemCodeScan.getText().toString().trim(),
                                branchId
                        );
                        editScanValue.setText("");
//                        editScanValue.clearFocus();
                        TextView tvTotal = findViewById(R.id.tvtotalScan);
                        tvTotal.setText("" + ScanListItem.size());
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (scanCondition == 0) {
                    getScanTransitItem(DeviceID.getDeviceId(getBaseContext())
                            , RequestParams.getTokenRequestParams(getBaseContext())
                            , RequestParams.getSignatureRequestParams(getBaseContext())
                            , UserSession.getUserSession(getBaseContext()),
                            sysCode,
                            edtItemCodeScan.getText().toString().trim(),
                            branchId
                    );
                } else {
                    getScanTransitItem(DeviceID.getDeviceId(getBaseContext())
                            , RequestParams.getTokenRequestParams(getBaseContext())
                            , RequestParams.getSignatureRequestParams(getBaseContext())
                            , UserSession.getUserSession(getBaseContext()),
                            sysCode,
                            editScanValue.getText().toString().trim(),
                            branchId
                    );
                }

                closeKeyboard();
                editScanValue.setText("");
//                editScanValue.clearFocus();
                TextView tvTotal = findViewById(R.id.tvtotalScan);
                tvTotal.setText("" + ScanListItem.size());
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.hide();
        if(!fromLed){
            getLocationData();
            if (code.equals("")) {
                if (scanCondition==1){
                    imgScanQr.setVisibility(View.GONE);
                }else {
                    imgScanQr.setVisibility(View.VISIBLE);
                }
            } else {
                getScanTransitItem(DeviceID.getDeviceId(getBaseContext())
                        , RequestParams.getTokenRequestParams(getBaseContext())
                        , RequestParams.getSignatureRequestParams(getBaseContext())
                        , UserSession.getUserSession(getBaseContext()),
                        sysCode,
                        code,
                        branchId
                );
                if (ScanMovetoVanActivity.autoScan == 1) {
                    Intent intent = new Intent(this, ScanMovetoVanActivity.class);
                    intent.putExtra("numberQrCode", 2);
                    startActivity(intent);
                } else {
                    imgScanQr.setVisibility(View.VISIBLE);
                }
            }
        } else {
            progressDialog.dismiss();
            fromLed = false;
        }
    }

}