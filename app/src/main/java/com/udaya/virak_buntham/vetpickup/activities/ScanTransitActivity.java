package com.udaya.virak_buntham.vetpickup.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.adapters.ScanListAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.ScanItemList.ScanListItem;
import com.udaya.virak_buntham.vetpickup.models.moveitemtovan.MoveItemToVanData;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.scanner.ScanMovetoVanActivity;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;
import com.udaya.virak_buntham.vetpickup.utils.AppConfig;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanTransitActivity extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.imgScanQr)
    ImageView imgScanQr;
    @BindView(R.id.relateQr)
    RelativeLayout layoutDisplayItem;
    @BindView(R.id.btnCancle)
    Button ButtonCancel;
    @BindView(R.id.relateNoData)
    RelativeLayout layoutNoData;
    @BindView(R.id.buttonComplete)
    Button ButtonFinish;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.edt_item_code)
    EditText editScanValue;
    int cameraCheck = 0;
    private CodeScanner mCodeScanner;

    private ScanListAdapter mAdapter;
    private ArrayList<com.udaya.virak_buntham.vetpickup.models.ScanItemList.ScanListItem> ScanListItem;

    //cardValue
    private ProgressDialog progressDialog;
    String date, branch, destinationTo, departure, van, sysCode, transferId;
    CodeScannerView scannerView;
    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";


    MediaPlayer sound;

    public static String code = "";
    public static int scanCondition;
    int test = 1;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_transit);
        ButterKnife.bind(this);
        code = "";
        progressDialog = new ProgressDialog(this);
        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        checkerPermissionCamera();
        mToolbarTitle.setText(getResources().getText(R.string.move_item_to_van_bus));
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        registerOnClick(this);

        getListScan();
        swipeRefreshLayout.setOnRefreshListener(this);
        getBundle();
        editScanValue.setOnEditorActionListener(editorListener);
        if (scanCondition == 0) {
            imgScanQr.performClick();
        } else {
            editScanValue.requestFocus();
            editScanValue.setFocusable(true);
            closeKeyboard();
            imgScanQr.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AlertDialogUtil.dialogAlertBack(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerOnClick(View.OnClickListener clickListener) {
        imgScanQr.setOnClickListener(clickListener);
        ButtonCancel.setOnClickListener(clickListener);
        ButtonFinish.setOnClickListener(clickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgScanQr:
//                openCamera();
                code = "";
                Intent intent = new Intent(this, ScanMovetoVanActivity.class);
                intent.putExtra("numberQrCode", 3);
                startActivity(intent);
                break;
            case R.id.btnCancle:
                toggleButton(layoutDisplayItem);
                mCodeScanner.stopPreview();
                break;
            case R.id.buttonComplete:
                onBackPressed();
//                addCartItem("QWZ13Z", "077252748", "");
                break;
        }
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }

    //qr animation
    private void toggleButton(RelativeLayout relativeLayout) {
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(300);
        transition.addTarget(R.id.relateQr);
        TransitionManager.beginDelayedTransition(layoutDisplayItem, transition);
        relativeLayout.setVisibility(View.GONE);
    }

    //qr animation
    private void toggleTop(RelativeLayout relativeLayout) {
        Transition transition = new Slide(Gravity.TOP);
        transition.setDuration(300);
        transition.addTarget(R.id.relateQr);
        TransitionManager.beginDelayedTransition(layoutDisplayItem, transition);
        relativeLayout.setVisibility(View.VISIBLE);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void checkerPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);
        } else {
            cameraCheck = 1;
            Log.e("permission", "checkerPermissionCamera:True ");
        }
    }

    private void openCamera() {
        scanQr();
        if (cameraCheck == 1) {
            mCodeScanner.startPreview();
            toggleTop(layoutDisplayItem);
            closeKeyboard();
        } else {
            checkerPermissionCamera();
        }
    }

    private void scanQr() {
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            Log.d("qrResult==>", result.getText());
            toggleButton(layoutDisplayItem);
            requestMoveToVan(DeviceID.getDeviceId(getBaseContext())
                    , RequestParams.getTokenRequestParams(getBaseContext())
                    , RequestParams.getSignatureRequestParams(getBaseContext())
                    , UserSession.getUserSession(getBaseContext()),
                    sysCode,
                    result.getText(),
                    branch,
                    van,
                    destinationTo,
                    departure,
                    Integer.parseInt(transferId));
        }));

        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @SuppressLint("SetTextI18n")
    private void addCartItem(String itemCode, String receiveTel, String departureToName) {
        toggleButton(layoutDisplayItem);
//        ScanListItem.add(new ScanListItem(itemCode, receiveTel, departureToName));
//        layoutNoData.setVisibility(View.GONE);
//        mAdapter.notifyItemInserted(ScanListItem.size());

        ScanListItem.add(0, new ScanListItem(itemCode, receiveTel, departureToName,""));
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
        mAdapter = new ScanListAdapter(ScanListItem);
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
    public void onRefresh() {
        new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 500);
    }


    @Override
    public void onBackPressed() {
        AlertDialogUtil.dialogAlertBack(this);
    }


    private void getBundle() {
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        date = b.getString("date");
        branch = b.getString("brandId");
        destinationTo = b.getString("destinationToId");
        departure = b.getString("departure");
        van = b.getString("vanId");
        sysCode = b.getString("sysCode");
        transferId = b.getString("tranferId");
    }


    public void requestMoveToVan(String device, String token, String signature, String session, String sysCode, String code, String branch, String VanId, String destinationToId, String departure, int type) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<MoveItemToVanData> call = apiService.moveItemToVan(device, token, signature, session, sysCode, code, branch, VanId, destinationToId, departure, type,""+ AppConfig.getGetLatitude(),""+AppConfig.getGetLongitude());
        call.enqueue(new Callback<MoveItemToVanData>() {
            @Override
            public void onResponse(@NonNull Call<MoveItemToVanData> call, @NonNull Response<MoveItemToVanData> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            String code = response.body().getCode();
                            String tel = response.body().getTelephone();
                            String destination = response.body().getDestination();
                            addCartItem(code, tel, destination);
                            sound = MediaPlayer.create(ScanTransitActivity.this, R.raw.right_voice);
                        } else if (response.body().getStatus().equals("2")) {
//                            AlertDialogUtil.alertMessageScan(ScanMoveItemToVanActivity.this, mess);
                            sound = MediaPlayer.create(ScanTransitActivity.this, R.raw.scan_ready);
                        } else {
//                            AlertDialogUtil.alertMessageScan(ScanMoveItemToVanActivity.this, mess);
                            sound = MediaPlayer.create(ScanTransitActivity.this, R.raw.wrong_voice);
                        }
                    }
                    sound.start();
                    editScanValue.setFocusable(true);
                    editScanValue.requestFocus();

                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<MoveItemToVanData> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

    // get random Code
    private static String getRandomString() {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 15; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    //on click in keyboard
//on click in keyboard
    private final TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                if (keyEvent == null || keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                    return false;
                if (editScanValue.getText().toString().isEmpty()) {
                    closeKeyboard();
                } else {
                    closeKeyboard();
//                    addCartItem("code", "" + test, "phnom penh");
//                    test++;
                    requestMoveToVan(DeviceID.getDeviceId(getBaseContext())
                            , RequestParams.getTokenRequestParams(getBaseContext())
                            , RequestParams.getSignatureRequestParams(getBaseContext())
                            , UserSession.getUserSession(getBaseContext()),
                            sysCode,
                            editScanValue.getText().toString().trim(),
                            branch,
                            van,
                            destinationTo,
                            departure, Integer.parseInt(transferId));
                    editScanValue.setText("");
                    editScanValue.setFocusable(true);
                    editScanValue.requestFocus();
                    closeKeyboard();
                    TextView tvTotal = findViewById(R.id.tvtotalScan);
                    tvTotal.setText("" + ScanListItem.size());
                }
                return true;
            }
            return false;
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                requestMoveToVan(DeviceID.getDeviceId(getBaseContext())
                        , RequestParams.getTokenRequestParams(getBaseContext())
                        , RequestParams.getSignatureRequestParams(getBaseContext())
                        , UserSession.getUserSession(getBaseContext()),
                        sysCode,
                        editScanValue.getText().toString().trim(),
                        branch,
                        van,
                        destinationTo,
                        departure, Integer.parseInt(transferId));
                editScanValue.setText("");
                editScanValue.clearFocus();
                TextView tvTotal = findViewById(R.id.tvtotalScan);
                tvTotal.setText("" + ScanListItem.size());
                closeKeyboard();
//                Toast.makeText(this, "code work", Toast.LENGTH_SHORT).show();
//                addCartItem("code", "" + test, "phnom penh");
//                test++;
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!code.equals("")) {
            requestMoveToVan(DeviceID.getDeviceId(getBaseContext())
                    , RequestParams.getTokenRequestParams(getBaseContext())
                    , RequestParams.getSignatureRequestParams(getBaseContext())
                    , UserSession.getUserSession(getBaseContext()),
                    sysCode,
                    code,
                    branch,
                    van,
                    destinationTo,
                    departure, Integer.parseInt(transferId));
            Intent intent = new Intent(this, ScanMovetoVanActivity.class);
            intent.putExtra("numberQrCode", 1);
            startActivity(intent);
        }
    }
}