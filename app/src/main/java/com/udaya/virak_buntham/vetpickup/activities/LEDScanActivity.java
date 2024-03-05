package com.udaya.virak_buntham.vetpickup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.adapters.LEDAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.ledScan.LEDScanItem;
import com.udaya.virak_buntham.vetpickup.models.moveitemtovan.MoveItemToVanData;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;
import com.udaya.virak_buntham.vetpickup.utils.AppConfig;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LEDScanActivity extends AppCompatActivity implements OnInternetConnectionListener {

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.edt_item_code_scan)
    EditText codeInput;
    @BindView(R.id.imgScanQr)
    ImageView qrCode;
    @BindView(R.id.scanLedRecycler)
    RecyclerView ledScanRecycler;
    @BindView(R.id.relateNoData)
    LinearLayoutCompat empty;
    @BindView(R.id.button_select_branch)
    Button buttonBranch;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvTel)
    TextView tvTel;
    @BindView(R.id.tvNameTo)
    TextView tvNameTo;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.llWrong)
    LinearLayoutCompat llWrong;
    @BindView(R.id.tvWrong)
    TextView tvWrong;

    private LEDAdapter ledAdapter;
    List<MoveItemToVanData> data = new ArrayList<>();
    String branchId = "";
    int scanStatus = 1;
    MediaPlayer sound;
    private ProgressDialog progressDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledscan);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);

        mToolbarTitle.setText("LED Scan");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        qrCode.setOnClickListener(v -> {
            CheckDeviceActivity.location = "1";
            startActivity(new Intent(LEDScanActivity.this, CheckDeviceActivity.class));
         /*   if(scanStatus == 1){
                if(branchId.isEmpty()){
                    AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.plzSelectBranch));
                } else {
                    getScanTransitItem(DeviceID.getDeviceId(getBaseContext())
                            , RequestParams.getTokenRequestParams(getBaseContext())
                            , RequestParams.getSignatureRequestParams(getBaseContext())
                            , UserSession.getUserSession(getBaseContext()),
                            getRandomString(),
                            codeInput.getText().toString(),
                            branchId
                    );
                }
            }*/
        });


        // * make focus and close keyboard
        clearFocus();

        // * Check item
        if (data.size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }

        // * Clear Focus and close keyboard
        getWindow().getDecorView().setOnTouchListener((v, event) -> {
            clearFocus();
            return false;
        });

        buttonBranch.setOnClickListener(v -> gotoRequestBrand());

    }


    @SuppressLint("SetTextI18n")
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (scanStatus == 1) {

                if(branchId.isEmpty()){
                    AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.plzSelectBranch));
                } else {
                    getScanTransitItem(DeviceID.getDeviceId(getBaseContext())
                            , RequestParams.getTokenRequestParams(getBaseContext())
                            , RequestParams.getSignatureRequestParams(getBaseContext())
                            , UserSession.getUserSession(getBaseContext()),
                            getRandomString(),
                            codeInput.getText().toString(),
                            branchId
                    );
                }
                scanStatus = 2;
                codeInput.setText("");

                new Handler().postDelayed(this::clearFocus, 50);

            }
        }
        return super.dispatchKeyEvent(event);
    }

    void setData(MoveItemToVanData data) {
        tvCode.setText(data.getCode());
        tvTel.setText(data.getTelephone());
        tvNameTo.setText(data.getDestination());
        tvLocation.setText(data.getLocationName());
    }

    void clearFocus() {
        codeInput.requestFocus();
        codeInput.setFocusable(true);
        closeKeyboard();
    }

    private void closeKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AlertDialogUtil.dialogAlertLEDScanBack(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialogUtil.dialogAlertLEDScanBack(this);
    }

    private void setupAreaAdapter(List<MoveItemToVanData> data) {
        ledAdapter = new LEDAdapter(data, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        ledScanRecycler.setLayoutManager(layoutManager);
        ledScanRecycler.setAdapter(ledAdapter);

        if (data.size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }
    }

    private void getScanTransitItem(String device, String token, String signature, String session, String sysCode, String code, String branch) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<MoveItemToVanData> call = apiService.getTransitItem(device, token, signature, session, sysCode, code, branch, String.valueOf(AppConfig.getGetLatitude()), String.valueOf(AppConfig.getGetLongitude()));
        call.enqueue(new Callback<MoveItemToVanData>() {
            @Override
            public void onResponse(@NonNull Call<MoveItemToVanData> call, @NonNull Response<MoveItemToVanData> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());

                            scanStatus = 1;

                            llWrong.setVisibility(View.GONE);

                            data.add(response.body());
                            setData(response.body());

                            setupAreaAdapter(data);

                            if (!response.body().getLed().equals("")) {
                                CheckDeviceActivity.location = response.body().getLed();
                                progressDialog.dismiss();
                                startActivity(new Intent(LEDScanActivity.this, CheckDeviceActivity.class));
                            }

                            if (response.body().getSound().equals("delivery")) {
                                sound = MediaPlayer.create(LEDScanActivity.this, R.raw.delivery);
                            } else if (response.body().getSound().equals("vip")) {
                                sound = MediaPlayer.create(LEDScanActivity.this, R.raw.vip);
                            } else if (response.body().getSound().equals("express")) {
                                sound = MediaPlayer.create(LEDScanActivity.this, R.raw.express);
                            }else {
                                sound = MediaPlayer.create(LEDScanActivity.this, R.raw.right_voice);
                            }

                        } else if ((response.body().getStatus().equals("2"))) {
                            sound = MediaPlayer.create(LEDScanActivity.this, R.raw.scan_ready);
                            llWrong.setVisibility(View.VISIBLE);
                            tvWrong.setText("ស្កេនរួចហើយ");
                        } else {
                            sound = MediaPlayer.create(LEDScanActivity.this, R.raw.wrong_voice);
                            llWrong.setVisibility(View.VISIBLE);
                            tvWrong.setText("មិនត្រឹមត្រូវ");
                        }
                        sound.start();
                    }
                    progressDialog.hide();
                    scanStatus = 1;
                }else {
                    progressDialog.hide();
                    scanStatus = 1;
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoveItemToVanData> call, @NonNull Throwable t) {
                Log.i("requestInfo", t.getMessage());
                scanStatus = 1;
                progressDialog.hide();
            }
        });
    }


    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_BRANCH_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionBranch = data.getParcelableExtra(Constants.REQUEST_BRANCH_KEY);
            assert selectionBranch != null;
            buttonBranch.setText(selectionBranch.getName());
            branchId = selectionBranch.getId();
        }
    }

    private void gotoRequestBrand() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_BRANCH_CODE);
        startActivityForResult(intent, Constants.REQUEST_BRANCH_CODE);
    }

    private static String getRandomString() {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 15; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

}