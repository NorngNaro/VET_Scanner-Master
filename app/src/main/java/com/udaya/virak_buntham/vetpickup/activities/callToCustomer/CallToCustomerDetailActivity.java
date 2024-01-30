package com.udaya.virak_buntham.vetpickup.activities.callToCustomer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.SelectionActivity;
import com.udaya.virak_buntham.vetpickup.adapters.CallToCustomerAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.getCustomer.CustomerDataItem;
import com.udaya.virak_buntham.vetpickup.models.getCustomer.GetCustomerResponse;
import com.udaya.virak_buntham.vetpickup.models.saveCustomerCall.SaveCustomerCallResponse;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallToCustomerDetailActivity extends AppCompatActivity implements OnInternetConnectionListener, OnItemClickListener {
    ViewGroup viewGroup;
    RadioButton radioAlreadyCall, radioNotPickUp, radioCanNotCall, radioDeliveryToHome;
    LinearLayout llAlreadyCall, llNotPickUp, llCanNotCall, llDeliveryToHome, llDelivery, llReason;
    Button buttonSelectDelivery;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.recyclerCall)
    RecyclerView recyclerCall;
    EditText edtDeliveryFee;

    @BindView(R.id.imgScanQrCode)
    ImageView imgScanQrCode;

    LinearLayout llAddress;
    private ProgressDialog progressDialog;
    public static String date, receiverTel;
    public static int branchId, locationId, mobileOpt, type, statusCall;
    android.app.AlertDialog alertDialog;
    List<CustomerDataItem> customerDataItems;
    int deliveryDestinationId;
    int statusDialog = 0;

    public static String goodsTransferId;

    public static int back;
    int qrBack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_call_to_customer_detail);
        ButterKnife.bind(this);
        mToolbarTitle.setText(R.string.calltocustomer);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        try {
//            goodsTransferId = getIntent().getStringExtra("goodsTransferId");
            Log.d("goodsTransferId==>", "" + goodsTransferId);
            if (goodsTransferId == null) {
                goodsTransferId = "0";
            }
        } catch (Exception e) {
            Log.d("error==>", "" + e.toString());
        }
        if (goodsTransferId.equals("0")) {
            imgScanQrCode.setVisibility(View.GONE);
            requestCallToCustomer(DeviceID.getDeviceId(CallToCustomerDetailActivity.this)
                    , RequestParams.getTokenRequestParams(CallToCustomerDetailActivity.this)
                    , RequestParams.getSignatureRequestParams(CallToCustomerDetailActivity.this)
                    , UserSession.getUserSession(CallToCustomerDetailActivity.this),
                    branchId, locationId, date, mobileOpt, receiverTel, type, statusCall, Integer.parseInt(goodsTransferId));
        } else {
            requestCallToCustomer(DeviceID.getDeviceId(CallToCustomerDetailActivity.this)
                    , RequestParams.getTokenRequestParams(CallToCustomerDetailActivity.this)
                    , RequestParams.getSignatureRequestParams(CallToCustomerDetailActivity.this)
                    , UserSession.getUserSession(CallToCustomerDetailActivity.this),
                    branchId, 0, date, 0, "", 0, 0, Integer.parseInt(goodsTransferId));
        }
        imgScanQrCode.setOnClickListener(v -> {
            ScanCallToCustomerActivity.scanAgain = 1;
            ScanCallToCustomerActivity.brandId = "" + branchId;
            finish();
            startActivity(new Intent(this, ScanCallToCustomerActivity.class));
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
    public void onBackPressed() {
        CallToCustomer.branchId = "";
        mobileOpt = 0;
        locationId = 0;
        type = 0;
        statusCall = 0;
        RequestParams.persistBranch(CallToCustomerDetailActivity.this
                , ""
                , "");
        if (back == 1) {
            back = 0;
            finish();
            startActivity(new Intent(this, CallToCustomer.class));
        } else {
            finish();
            startActivity(new Intent(this, CallToCustomer.class));
        }
        if (qrBack == 1) {
            super.onBackPressed();
        }

    }

    public void checkDataAlertDialog(Context context, ViewGroup viewGroup,
                                     String senderTel, int id, int goodsTransferId) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_call_to_customer, viewGroup, false);
        Button btnCancel = dialogView.findViewById(R.id.btnCancle);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        EditText edtReason = dialogView.findViewById(R.id.edtReason);
        llAddress = dialogView.findViewById(R.id.llAddress);
        EditText edtAddress = dialogView.findViewById(R.id.edtAddress);
        edtDeliveryFee = dialogView.findViewById(R.id.edtDeliveryFee);
        buttonSelectDelivery = dialogView.findViewById(R.id.buttonSelectDelivery);
        TextView tvSenderTel = dialogView.findViewById(R.id.tvSenderTel);
        tvSenderTel.setText(senderTel);
        buttonSelectDelivery.setOnClickListener(view -> gotoRequestAreaToDeliver());

        radioAlreadyCall = dialogView.findViewById(R.id.radioAlreadyCall);
        radioNotPickUp = dialogView.findViewById(R.id.radioNotPickUp);
        radioCanNotCall = dialogView.findViewById(R.id.radioCanNotCall);
        radioDeliveryToHome = dialogView.findViewById(R.id.radioDeliveryToHome);

        llAlreadyCall = dialogView.findViewById(R.id.llAlreadyCall);
        llNotPickUp = dialogView.findViewById(R.id.llNotPickUp);
        llCanNotCall = dialogView.findViewById(R.id.llCanNotCall);
        llDeliveryToHome = dialogView.findViewById(R.id.llDeliveryToHome);
        llDelivery = dialogView.findViewById(R.id.llDelivery);
        llReason = dialogView.findViewById(R.id.llReason);

        llAlreadyCall.setOnClickListener(view -> {
            radioAlreadyCall.setChecked(true);
            radioNotPickUp.setChecked(false);
            radioCanNotCall.setChecked(false);
            radioDeliveryToHome.setChecked(false);

            buttonSelectDelivery.setText(getResources().getString(R.string.please_select));
            edtDeliveryFee.setText("");
            edtAddress.setText("");
            deliveryDestinationId = 0;

            llDelivery.setVisibility(View.GONE);
            llAddress.setVisibility(View.GONE);
            llReason.setVisibility(View.GONE);
            statusDialog = 1;
        });

        llNotPickUp.setOnClickListener(view -> {
            radioAlreadyCall.setChecked(false);
            radioNotPickUp.setChecked(true);
            radioCanNotCall.setChecked(false);
            radioDeliveryToHome.setChecked(false);

            buttonSelectDelivery.setText(getResources().getString(R.string.please_select));
            edtDeliveryFee.setText("");
            edtAddress.setText("");
            deliveryDestinationId = 0;

            llDelivery.setVisibility(View.GONE);
            llAddress.setVisibility(View.GONE);
            llReason.setVisibility(View.VISIBLE);

            statusDialog = 2;
        });
        llCanNotCall.setOnClickListener(view -> {
            radioNotPickUp.setChecked(false);
            radioAlreadyCall.setChecked(false);
            radioCanNotCall.setChecked(true);
            radioDeliveryToHome.setChecked(false);

            buttonSelectDelivery.setText(getResources().getString(R.string.please_select));
            edtDeliveryFee.setText("");
            edtAddress.setText("");
            deliveryDestinationId = 0;

            llDelivery.setVisibility(View.GONE);
            llReason.setVisibility(View.VISIBLE);
            llAddress.setVisibility(View.GONE);

            statusDialog = 3;
        });
        llDeliveryToHome.setOnClickListener(view -> {
            radioNotPickUp.setChecked(false);
            radioAlreadyCall.setChecked(false);
            radioCanNotCall.setChecked(false);
            radioDeliveryToHome.setChecked(true);

            llDelivery.setVisibility(View.VISIBLE);
            llReason.setVisibility(View.GONE);
            statusDialog = 4;
        });

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        btnCancel.setOnClickListener(v ->
                alertDialog.cancel()
        );
        btnSave.setOnClickListener(v -> {
            if (statusDialog == 2 || statusDialog == 3) {
                if (edtReason.getText().toString().isEmpty()) {
                    Toast.makeText(context, "សូមបញ្ចូលចំណាំ", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.cancel();
                    saveCustomerCall(
                            DeviceID.getDeviceId(CallToCustomerDetailActivity.this)
                            , RequestParams.getTokenRequestParams(CallToCustomerDetailActivity.this)
                            , RequestParams.getSignatureRequestParams(CallToCustomerDetailActivity.this)
                            , UserSession.getUserSession(CallToCustomerDetailActivity.this),
                            id,
                            goodsTransferId,
                            edtReason.getText().toString().trim(),
                            statusDialog,
                            deliveryDestinationId,
                            edtDeliveryFee.getText().toString().trim(),
                            edtAddress.getText().toString().trim()
                    );
                }
            } else if (statusDialog == 4) {
                if (buttonSelectDelivery.getText().toString().trim().equals("Other")) {
                    if (edtAddress.getText().toString().isEmpty()) {
                        Toast.makeText(context, "សូមបញ្ចូលអាសយដ្ឋាន", Toast.LENGTH_SHORT).show();
                    } else if (edtDeliveryFee.getText().toString().trim().isEmpty()) {
                        Toast.makeText(context, "សូមបញ្ចូលថ្លៃដឹក", Toast.LENGTH_SHORT).show();
                    } else {
                        alertDialog.cancel();
                        saveCustomerCall(
                                DeviceID.getDeviceId(CallToCustomerDetailActivity.this)
                                , RequestParams.getTokenRequestParams(CallToCustomerDetailActivity.this)
                                , RequestParams.getSignatureRequestParams(CallToCustomerDetailActivity.this)
                                , UserSession.getUserSession(CallToCustomerDetailActivity.this),
                                id,
                                goodsTransferId,
                                edtReason.getText().toString().trim(),
                                statusDialog,
                                deliveryDestinationId,
                                edtDeliveryFee.getText().toString().trim(),
                                edtAddress.getText().toString().trim()
                        );
                    }
                } else if (edtDeliveryFee.getText().toString().trim().isEmpty()) {
                    Toast.makeText(context, "សូមបញ្ចូលថ្លៃដឹក", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.cancel();
                    saveCustomerCall(
                            DeviceID.getDeviceId(CallToCustomerDetailActivity.this)
                            , RequestParams.getTokenRequestParams(CallToCustomerDetailActivity.this)
                            , RequestParams.getSignatureRequestParams(CallToCustomerDetailActivity.this)
                            , UserSession.getUserSession(CallToCustomerDetailActivity.this),
                            id,
                            goodsTransferId,
                            edtReason.getText().toString().trim(),
                            statusDialog,
                            deliveryDestinationId,
                            edtDeliveryFee.getText().toString().trim(),
                            edtAddress.getText().toString().trim()
                    );
                }
            } else if (statusDialog == 1) {
                alertDialog.cancel();
                saveCustomerCall(
                        DeviceID.getDeviceId(CallToCustomerDetailActivity.this)
                        , RequestParams.getTokenRequestParams(CallToCustomerDetailActivity.this)
                        , RequestParams.getSignatureRequestParams(CallToCustomerDetailActivity.this)
                        , UserSession.getUserSession(CallToCustomerDetailActivity.this),
                        id,
                        goodsTransferId,
                        edtReason.getText().toString().trim(),
                        statusDialog,
                        deliveryDestinationId,
                        edtDeliveryFee.getText().toString().trim(),
                        edtAddress.getText().toString().trim()
                );
            } else {
                Toast.makeText(context, "សូមជ្រើសរើស", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoRequestAreaToDeliver() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_AREA_DELIVER_CODE);
        startActivityForResult(intent, Constants.REQUEST_AREA_DELIVER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_AREA_DELIVER_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionDeliverArea = data.getParcelableExtra(Constants.REQUEST_AREA_DELIVER_KEY);
            assert selectionDeliverArea != null;
            buttonSelectDelivery.setText(selectionDeliverArea.getName());
            deliveryDestinationId = Integer.parseInt(selectionDeliverArea.getId());
            if (buttonSelectDelivery.getText().toString().trim().equals("Other")) {
                llAddress.setVisibility(View.VISIBLE);
            } else {
                llAddress.setVisibility(View.GONE);
            }
            edtDeliveryFee.setEnabled(true);
        }
    }

    public void requestCallToCustomer(String device, String token, String signature, String session, int branchId, int locationId, String date, int mobileOpt, String receiverTel, int type, int status, int goodsTransferId) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<GetCustomerResponse> call = apiService.getCustomerCall(device, token, signature, session, branchId, locationId, date, mobileOpt, receiverTel, type, status, goodsTransferId);
        call.enqueue(new Callback<GetCustomerResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetCustomerResponse> call, @NonNull Response<GetCustomerResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                        }
                        if (!response.body().getData().isEmpty()) {
                            tvNoData.setVisibility(View.GONE);
                            customerDataItems = response.body().getData();
                            setupAreaAdapter(response.body().getData());
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<GetCustomerResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
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

    private void setupAreaAdapter(List<CustomerDataItem> customerDataItems) {
        CallToCustomerAdapter callToCustomerAdapter = new CallToCustomerAdapter(customerDataItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerCall.setLayoutManager(layoutManager);
        recyclerCall.setAdapter(callToCustomerAdapter);
        callToCustomerAdapter.setOnItemClickListener(this);
    }

    @Override
    public void itemClick(int position) {
        checkDataAlertDialog(this, viewGroup,
                customerDataItems.get(position).getReceiverTelephone(),
                customerDataItems.get(position).getId(),
                customerDataItems.get(position).getGoodsTransferId()
        );
    }

    public void saveCustomerCall(String device, String token, String signature, String session, int id, int goodsTransferId, String reason, int status, int deliveryDestinationId, String deliverFee, String deliveryAddress) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<SaveCustomerCallResponse> call = apiService.saveCustomerCall(device, token, signature, session, id, goodsTransferId, reason, status, deliveryDestinationId, deliverFee, deliveryAddress);
        call.enqueue(new Callback<SaveCustomerCallResponse>() {
            @Override
            public void onResponse(@NonNull Call<SaveCustomerCallResponse> call, @NonNull Response<SaveCustomerCallResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        RequestParams.persistRequestParams(getBaseContext()
                                , response.body().getToken()
                                , response.body().getSignature());
                        startActivity(getIntent());
//                        requestCallToCustomer(DeviceID.getDeviceId(CallToCustomerDetailActivity.this)
//                                , RequestParams.getTokenRequestParams(CallToCustomerDetailActivity.this)
//                                , RequestParams.getSignatureRequestParams(CallToCustomerDetailActivity.this)
//                                , UserSession.getUserSession(CallToCustomerDetailActivity.this), branchId, locationId, date, mobileOpt, receiverTel, type, statusCall, goodsTransferId);
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

}