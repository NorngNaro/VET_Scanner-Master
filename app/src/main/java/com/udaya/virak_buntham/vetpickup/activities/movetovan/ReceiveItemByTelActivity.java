package com.udaya.virak_buntham.vetpickup.activities.movetovan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.MoveItemToVanActivity;
import com.udaya.virak_buntham.vetpickup.activities.receivedCodeByBranch.ListReceivedCodeByBranchActivity;
import com.udaya.virak_buntham.vetpickup.adapters.ChangeAdapter;
import com.udaya.virak_buntham.vetpickup.adapters.ReceiveAddManualAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.ScanQrRespone;
import com.udaya.virak_buntham.vetpickup.models.moveitemtovan.DataItem;
import com.udaya.virak_buntham.vetpickup.models.moveitemtovan.ReceiveAddManduleResponse;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AppConfig;
import com.udaya.virak_buntham.vetpickup.utils.GpsTrackerLocation;
import com.udaya.virak_buntham.vetpickup.utils.InputMethod;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class ReceiveItemByTelActivity extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.edt_item_tel)
    EditText edtTel;
    @BindView(R.id.tvtotalScan)
    TextView tvTotalScan;
    @BindView(R.id.button_save)
    Button btnSave;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.recyclerData)
    RecyclerView rc;

    String type;
    String departure;
    String transferTo;
    String vanId;
    boolean check = false;


    String locationId = "";
    String branchId = "";
    public static ArrayList<String> arrayNum = new ArrayList<>();
    private ProgressDialog progressDialog;

    private GpsTrackerLocation gpsTracker;
    public void getLocationData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gpsTracker = new GpsTrackerLocation(ReceiveItemByTelActivity.this);
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
        setContentView(R.layout.activity_receive_item_by_tel);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);

        getBundle();
        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        findViewById(R.id.imgSearch).setOnClickListener(v -> {
            InputMethod.hideSoftKeyboard(this);
            if (edtTel.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "សូមបញ្ចូលលេខទូរស័ព្ទ", Toast.LENGTH_SHORT).show();
            } else {
                if(type.equals("2")){
                    check = false;
                    getCustomerReceiveMoveToVan(DeviceID.getDeviceId(ReceiveItemByTelActivity.this)
                            , RequestParams.getTokenRequestParams(ReceiveItemByTelActivity.this)
                            , RequestParams.getSignatureRequestParams(ReceiveItemByTelActivity.this)
                            , UserSession.getUserSession(ReceiveItemByTelActivity.this));
                } else {
                    check = false;
                    getCustomerReceive(DeviceID.getDeviceId(ReceiveItemByTelActivity.this)
                            , RequestParams.getTokenRequestParams(ReceiveItemByTelActivity.this)
                            , RequestParams.getSignatureRequestParams(ReceiveItemByTelActivity.this)
                            , UserSession.getUserSession(ReceiveItemByTelActivity.this));
                    }
                }
        });

        btnSave.setOnClickListener(v -> {
            if(!edtTel.getText().toString().isEmpty()){
                if(check){
                    if (arrayNum.isEmpty()) {
                        Toast.makeText(ReceiveItemByTelActivity.this, "សូមជ្រើសរើសធាតុ", Toast.LENGTH_SHORT).show();
                    } else {
                        if(type.equals("2")){
                            processSaveMoveToVan(DeviceID.getDeviceId(ReceiveItemByTelActivity.this)
                                    , RequestParams.getTokenRequestParams(ReceiveItemByTelActivity.this)
                                    , RequestParams.getSignatureRequestParams(ReceiveItemByTelActivity.this)
                                    , UserSession.getUserSession(ReceiveItemByTelActivity.this), "" + convertObjectArrayToString(arrayNum, ","));
                        } else {
                            processSave(DeviceID.getDeviceId(ReceiveItemByTelActivity.this)
                                    , RequestParams.getTokenRequestParams(ReceiveItemByTelActivity.this)
                                    , RequestParams.getSignatureRequestParams(ReceiveItemByTelActivity.this)
                                    , UserSession.getUserSession(ReceiveItemByTelActivity.this), "" + convertObjectArrayToString(arrayNum, ","));
                        }
                    }
                } else {
                    Toast.makeText(ReceiveItemByTelActivity.this, "មិនមានទិន្នន័យ!សូមស្វែងរក", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "សូមបញ្ចូលលេខទូរស័ព្ទ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static String convertObjectArrayToString(ArrayList<String> arr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : arr)
            sb.append(obj.toString()).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }

    private void getBundle() {
        Bundle b;
        b = getIntent().getExtras();
        type = b.getString("type");
        branchId = b.getString("brandId");
        // 2 is from move item to van
        if(type.equals("2")){
            mToolbarTitle.setText("ផ្ទេរអីវ៉ាន់ឡើងឡាន");
            vanId = b.getString("vanId");
            transferTo = b.getString("tranferId");
            departure = b.getString("departure");
        } else {
            mToolbarTitle.setText("ទទួលអីវ៉ាន់ពីឡាន");
            locationId = b.getString("locationId");
        }
    }

    private void getCustomerReceive(String device, String token, String signature, String session) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ReceiveAddManduleResponse> call = apiService.receiveAddManaul(device, token, signature, session, edtTel.getText().toString().trim(),
                branchId, locationId);
        call.enqueue(new Callback<ReceiveAddManduleResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<ReceiveAddManduleResponse> call, @androidx.annotation.NonNull Response<ReceiveAddManduleResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            if(response.body().getData().isEmpty()){
                                tvNoData.setVisibility(View.VISIBLE);
                                rc.setVisibility(View.GONE);
                            }else {
                                check = true;
                                tvNoData.setVisibility(View.GONE);
                                rc.setVisibility(View.VISIBLE);
                                tvTotalScan.setText("" + response.body().getData().size());
                                setupAreaAdapter(response.body().getData());
                            }
                        }
                    }
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<ReceiveAddManduleResponse> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }


    private void getCustomerReceiveMoveToVan(String device, String token, String signature, String session) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ReceiveAddManduleResponse> call = apiService.moveToVanAddManaul(device, token, signature, session, edtTel.getText().toString().trim(), branchId);
        call.enqueue(new Callback<ReceiveAddManduleResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<ReceiveAddManduleResponse> call, @androidx.annotation.NonNull Response<ReceiveAddManduleResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            Log.e("", "onResponse: "+response.body() );
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            if(response.body().getData().isEmpty()){
                                tvNoData.setVisibility(View.VISIBLE);
                                rc.setVisibility(View.GONE);
                            }else {
                                check = true;
                                tvNoData.setVisibility(View.GONE);
                                rc.setVisibility(View.VISIBLE);
                                tvTotalScan.setText("" + response.body().getData().size());
                                setupAreaAdapter(response.body().getData());
                            }
                        }
                    }
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<ReceiveAddManduleResponse> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

    private void processSave(String device, String token, String signature, String session, String item) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ScanQrRespone> call = apiService.saveReceiveAddManaul(device, token, signature, session,
                branchId, locationId, item,""+ AppConfig.getGetLatitude(),""+AppConfig.getGetLongitude());
        call.enqueue(new Callback<ScanQrRespone>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Response<ScanQrRespone> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            alertReceiving();
                        }else {
                            alertError();
                        }
                    }
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

/*    @Field("branch_id") String branchId,
    @Field("van_id") String van,
    @Field("item") String item,
    @Field("departure") String departure,
    @Field("type") String type,
    @Field("lats") String lats,
    @Field("longs") String longs*/

    private void processSaveMoveToVan(String device, String token, String signature, String session, String item) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.e("", "processSaveMoveToVan: " + vanId);
        Log.e("", "processSaveMoveToVan: " + departure);
        Log.e("", "processSaveMoveToVan: " + transferTo);

        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ScanQrRespone> call = apiService.saveReceiveAddManaulMoveToVan(device, token, signature, session, branchId, vanId.toString(), item, departure.toString(), transferTo.toString(),""+ AppConfig.getGetLatitude(),""+AppConfig.getGetLongitude());
        call.enqueue(new Callback<ScanQrRespone>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Response<ScanQrRespone> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            alertReceiving();
                        }else {
                            alertError();
                        }
                    }
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }


    private void  alertReceiving() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ReceiveItemByTelActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.message));
        sweetAlertDialog.setContentText("ទិន្នន័យត្រូវបានរក្សាទុក");
        sweetAlertDialog.setConfirmText("បិទ");
        sweetAlertDialog.setConfirmClickListener(sDialog -> {
            sDialog.dismissWithAnimation();
            finish();
            if(type.equals("2")){
                startActivity(new Intent(this, MoveItemToVanActivity.class));
            } else {
                startActivity(new Intent(this,ReceiveItemActivity.class));
            }
        });
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
    }

    private void  alertError() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ReceiveItemByTelActivity.this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.message));
        sweetAlertDialog.setContentText("ទិន្នន័យមិនត្រូវបានរក្សាទុកទេ");
        sweetAlertDialog.setConfirmText("បិទ");
        sweetAlertDialog.setConfirmClickListener(sDialog -> {
            sDialog.dismissWithAnimation();
            finish();
            if(type.equals("2")){
                startActivity(new Intent(this, MoveItemToVanActivity.class));
            } else {
                startActivity(new Intent(this,ReceiveItemActivity.class));
            }
        });
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
    }

    @SuppressLint("SetTextI18n")
    private void setupAreaAdapter(List<DataItem> itemData) {
        arrayNum.clear();
        ReceiveAddManualAdapter receiveAddManualAdapter = new ReceiveAddManualAdapter(itemData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rc.setLayoutManager(layoutManager);
        rc.setAdapter(receiveAddManualAdapter);
        receiveAddManualAdapter.setOnItemClickListener((position, checkBox) -> {
            if (checkBox) {
                if (arrayNum.size() == 0) {
                    arrayNum.add(0, "" + itemData.get(position).getScanCode());
                } else {
                    arrayNum.add("" + itemData.get(position).getScanCode());
                }
            } else {
                try {
                    if (arrayNum.size() == 1) {
                        arrayNum.remove(0);
                    } else {
                        for (int i = 0; i < arrayNum.size(); i++) {
                            if (arrayNum.get(i).equals("" + itemData.get(position).getScanCode())) {
                                arrayNum.remove(i);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("error===>", e.toString());
                }
            }
            Log.d("arrayNumber==>", "" + arrayNum);
            tvTotalScan.setText("" + arrayNum.size());
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }
}