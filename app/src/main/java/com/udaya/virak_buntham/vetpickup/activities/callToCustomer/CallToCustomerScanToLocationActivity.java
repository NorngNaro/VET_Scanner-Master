package com.udaya.virak_buntham.vetpickup.activities.callToCustomer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.SelectionActivity;
import com.udaya.virak_buntham.vetpickup.adapters.ItemNotReceiveAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.getItemNotReceive.ItemData;
import com.udaya.virak_buntham.vetpickup.models.getItemNotReceive.ItemNotReceiveResponse;
import com.udaya.virak_buntham.vetpickup.models.saveCustomerCall.SaveCustomerCallResponse;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallToCustomerScanToLocationActivity extends AppCompatActivity implements OnInternetConnectionListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.recyclerScanItem)
    RecyclerView recyclerView;
    @BindView(R.id.btnSelectLocation)
    Button btnSelectLocation;

    @BindView(R.id.tvSenderTelCall)
    TextView tvSenderTel;

    @BindView(R.id.tvReceiverTelCall)
    TextView tvReceiverTel;

    @BindView(R.id.tvItemQtyCall)
    TextView tvItemQty;

    @BindView(R.id.tvTypeCall)
    TextView tvType;

    String brandId, code, num;
    int goodTransferId, locationId;
    private ProgressDialog progressDialog;

    public static ArrayList<String> arrayNumber = new ArrayList<>();
    StringBuilder str = new StringBuilder("");
    int invalidCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_to_customer_scan_to_location);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        mToolbarTitle.setText(R.string.calltocustomer);
        brandId = getIntent().getStringExtra("brandId");
        code = getIntent().getStringExtra("code");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        findViewById(R.id.btnScan).setOnClickListener(view -> {
            for (String eachString : arrayNumber) {
                str.append(eachString).append(",");
            }
            String numberItem = str.toString();
            if (numberItem.length() > 0) {
                numberItem = numberItem.substring(0, str.length() - 1);
            }
            Log.d("numberitem==>", "" + numberItem);
            if (locationId == 0) {
                Toast.makeText(this, "សូមជ្រើសរើសទីតាំងធ្នើ", Toast.LENGTH_SHORT).show();
            } else {
                saveItemNotReceive(DeviceID.getDeviceId(CallToCustomerScanToLocationActivity.this)
                        , RequestParams.getTokenRequestParams(CallToCustomerScanToLocationActivity.this)
                        , RequestParams.getSignatureRequestParams(CallToCustomerScanToLocationActivity.this)
                        , UserSession.getUserSession(CallToCustomerScanToLocationActivity.this),
                        Integer.parseInt(brandId),
                        locationId,
                        goodTransferId, num);
            }

        });
        findViewById(R.id.btnSelectLocation).setOnClickListener(v -> gotoRequestLocation());
        scanCustomerCall(DeviceID.getDeviceId(CallToCustomerScanToLocationActivity.this)
                , RequestParams.getTokenRequestParams(CallToCustomerScanToLocationActivity.this)
                , RequestParams.getSignatureRequestParams(CallToCustomerScanToLocationActivity.this)
                , UserSession.getUserSession(CallToCustomerScanToLocationActivity.this),
                Integer.parseInt(brandId), code);

        Log.d("codeScanValue===>",""+code);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (invalidCode == 1) {
            invalidCode = 0;
            super.onBackPressed();
        } else {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

    }

    private void gotoRequestLocation() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_LOCATION_CODE);
        intent.putExtra("branchIdLocate", brandId);
        startActivityForResult(intent, Constants.REQUEST_LOCATION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOCATION_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionLocation = data.getParcelableExtra(Constants.REQUEST_LOCATION_KEY);
            assert selectionLocation != null;
            btnSelectLocation.setText(selectionLocation.getName());
            locationId = Integer.parseInt(selectionLocation.getId());
            CallToCustomerDetailActivity.locationId = Integer.parseInt(selectionLocation.getId());
        }
    }

    public void getItemNotReceive(String device, String token, String signature, String session, int goodTransferId, int branchId, String num) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ItemNotReceiveResponse> call = apiService.getItemNotReceive(device, token, signature, session, goodTransferId, branchId, num);
        call.enqueue(new Callback<ItemNotReceiveResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ItemNotReceiveResponse> call, @NonNull Response<ItemNotReceiveResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            tvReceiverTel.setText("" + response.body().getReceiver_telephone());
                            tvSenderTel.setText("" + response.body().getSender_telephone());
                            tvItemQty.setText("" + response.body().getItem_qty());
                            tvType.setText("" + response.body().getCode());
                            setupAreaAdapter(response.body().getData());
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<ItemNotReceiveResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
                getItemNotReceive(DeviceID.getDeviceId(CallToCustomerScanToLocationActivity.this)
                        , RequestParams.getTokenRequestParams(CallToCustomerScanToLocationActivity.this)
                        , RequestParams.getSignatureRequestParams(CallToCustomerScanToLocationActivity.this)
                        , UserSession.getUserSession(CallToCustomerScanToLocationActivity.this),
                        goodTransferId,
                        Integer.parseInt(brandId), num);
            }
        });
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }

    private void setupAreaAdapter(List<ItemData> itemData) {
        ItemNotReceiveAdapter itemNotReceiveAdapter = new ItemNotReceiveAdapter(itemData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itemNotReceiveAdapter);
    }

    public void saveItemNotReceive(String device, String token, String signature, String session, int branchId, int locationId, int goodTransferIdData, String num) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<SaveCustomerCallResponse> call = apiService.saveitemnotreceive(device, token, signature, session, locationId, branchId, goodTransferIdData, num);
        call.enqueue(new Callback<SaveCustomerCallResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<SaveCustomerCallResponse> call, @NonNull Response<SaveCustomerCallResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            finish();
                            CallToCustomerDetailActivity.goodsTransferId = "" + goodTransferId;
                            startActivity(new Intent(getBaseContext(), CallToCustomerDetailActivity.class));
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<SaveCustomerCallResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

    public void scanCustomerCall(String device, String token, String signature, String session, int branchId, String code) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<SaveCustomerCallResponse> call = apiService.scancustomercall(device, token, signature, session, code, branchId);
        call.enqueue(new Callback<SaveCustomerCallResponse>() {
            @Override
            public void onResponse(@NonNull Call<SaveCustomerCallResponse> call, @NonNull Response<SaveCustomerCallResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            if (!response.body().isScan_status()) {
                                goodTransferId = response.body().getGoods_transfer_id();
                                num = response.body().getNum();
                                getItemNotReceive(DeviceID.getDeviceId(CallToCustomerScanToLocationActivity.this)
                                        , RequestParams.getTokenRequestParams(CallToCustomerScanToLocationActivity.this)
                                        , RequestParams.getSignatureRequestParams(CallToCustomerScanToLocationActivity.this)
                                        , UserSession.getUserSession(CallToCustomerScanToLocationActivity.this),
                                        goodTransferId,
                                        Integer.parseInt(brandId),
                                        num);
                            } else {
                                CallToCustomerDetailActivity.goodsTransferId = "" + response.body().getGoods_transfer_id();
                                startActivity(new Intent(getBaseContext(), CallToCustomerDetailActivity.class));
                            }
                        } else {
                            invalidCode = 1;
                            finish();
                            startActivity(new Intent(CallToCustomerScanToLocationActivity.this, CallToCustomer.class));
                            Toast.makeText(CallToCustomerScanToLocationActivity.this, "លេខកូដមិនត្រឹមត្រូវ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<SaveCustomerCallResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
                scanCustomerCall(DeviceID.getDeviceId(CallToCustomerScanToLocationActivity.this)
                        , RequestParams.getTokenRequestParams(CallToCustomerScanToLocationActivity.this)
                        , RequestParams.getSignatureRequestParams(CallToCustomerScanToLocationActivity.this)
                        , UserSession.getUserSession(CallToCustomerScanToLocationActivity.this),
                        Integer.parseInt(brandId), code);
            }
        });
    }

}