package com.udaya.virak_buntham.vetpickup.activities.outForDevliery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.SelectionActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.SMSRespone;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.ScanQrRespone;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiClientSMS;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutForDeliveryDetailActivity extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnInternetConnectionListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;

    public static String date, code, senderTel, receiverTel, type, receiverAdd, status, cod, paid, addLet, addLong, seriCode;
    public static int deliveryDetailId;

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvSenderTel)
    TextView tvSenderTel;
    @BindView(R.id.tvReceiverTel)
    TextView tvReceiverTel;
    @BindView(R.id.tvSenderAdd)
    TextView tvSenderAdd;
    @BindView(R.id.tvTotalFee)
    TextView tvTotalFee;
    @BindView(R.id.tvCod)
    TextView tvCod;
    @BindView(R.id.btn_receive)
    Button btnReceive;
    @BindView(R.id.btnNotSuccess)
    Button btnNotSuccess;
    @BindView(R.id.img_SMS)
    ImageView imgSMS;
    @BindView(R.id.img_Call)
    ImageView imgCALL;
    private ProgressDialog progressDialog;
    ViewGroup viewGroup;
    Button btnReason;
    int reasonId = 0;
    static String codeScan;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_goods_detail);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        mToolbarTitle.setText(R.string.out_for_delivery);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        setData();

        OutForDeliveryActivity.outForDeliveryScan = "";

//        tvReceiverTel.setOnClickListener(v -> {
//            Intent sIntent = new Intent(Intent.ACTION_CALL, Uri
//                    .parse("tel:" + tvReceiverTel.getText().toString()));
//            sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            startActivity(sIntent);
//        });

        btnReceive.setOnClickListener(v -> showDialog());
        btnNotSuccess.setOnClickListener(v -> alertDialogReason());

        imgCALL.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + tvReceiverTel.getText().toString()));
            if (ActivityCompat.checkSelfPermission(getBaseContext(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        });

        imgSMS.setOnClickListener(v -> showDialogSMS());


    }

    private void alertDialogReason() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_reason, viewGroup, false);
        btnReason = dialogView.findViewById(R.id.buttonSelectReason);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        btnReason.setOnClickListener(v -> setIntent());
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(dialogView);
        android.app.AlertDialog alertDialog;
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        btnSave.setOnClickListener(v -> {
            if (reasonId == 0) {
                Toast.makeText(this, "Please Select reason", Toast.LENGTH_SHORT).show();
            } else {
                alertDialog.dismiss();
                getUnDelivery(DeviceID.getDeviceId(getBaseContext())
                        , RequestParams.getTokenRequestParams(getBaseContext())
                        , RequestParams.getSignatureRequestParams(getBaseContext())
                        , UserSession.getUserSession(getBaseContext())
                        , deliveryDetailId
                        , reasonId);
            }

        });
    }

    private void setIntent() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_REASON_CODE);
        startActivityForResult(intent, Constants.REQUEST_REASON_CODE);

    }

    private void setData() {
        tvDate.setText(date);
        tvCode.setText(code);
        tvSenderTel.setText(senderTel);
        tvReceiverTel.setText(receiverTel);
        tvSenderAdd.setText(receiverAdd);
        tvTotalFee.setText(paid);
        tvCod.setText(cod);
        if (status.equals(getResources().getString(R.string.closed))) {
            btnReceive.setVisibility(View.GONE);
            btnNotSuccess.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }

        try {
            //Place current location marker
            LatLng latLng = new LatLng(Double.parseDouble(addLet), Double.parseDouble(addLong));
            Log.d("letLong", "" + latLng);
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.setPosition(latLng);
            } else {
                mCurrLocationMarker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(receiverAdd));
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 25));
        } catch (Exception ignored) {

        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void showDialog() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.do_you_want_receive));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setCancelText(getResources().getString(R.string.cancel));
        alertDialog.setConfirmClickListener(sDialog -> {
            getCustomerReceive(DeviceID.getDeviceId(getBaseContext())
                    , RequestParams.getTokenRequestParams(getBaseContext())
                    , RequestParams.getSignatureRequestParams(getBaseContext())
                    , UserSession.getUserSession(getBaseContext()), deliveryDetailId);
            alertDialog.dismissWithAnimation();
        })
                .show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelClickListener(sDialog -> alertDialog.dismissWithAnimation()).show();
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        Button btnCancel = alertDialog.findViewById(R.id.cancel_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        btnCancel.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        btn.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        btnCancel.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
    }

    private void getCustomerReceive(String device, String token, String signature, String session, int deliveryDetailId) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ScanQrRespone> call = apiService.savedeliveryreceive(device, token, signature, session, deliveryDetailId);
        call.enqueue(new Callback<ScanQrRespone>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Response<ScanQrRespone> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("1")) {
                        showDialogCheck();
                        RequestParams.persistRequestParams(OutForDeliveryDetailActivity.this
                                , Objects.requireNonNull(response.body()).getToken()
                                , Objects.requireNonNull(response.body()).getSignature());
                    } else {
                        showDialogFailCheck();
                    }
                } else {
                    Toast.makeText(OutForDeliveryDetailActivity.this, "incorrect_code", Toast.LENGTH_SHORT).show();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", "" + t.getMessage());
                progressDialog.hide();
            }
        });
    }

    private void getUnDelivery(String device, String token, String signature, String session, int deliveryDetailId, int reasonId) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ScanQrRespone> call = apiService.saveUnDelivery(device, token, signature, session, deliveryDetailId, reasonId);
        call.enqueue(new Callback<ScanQrRespone>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Response<ScanQrRespone> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("1")) {
                        showDialogCheck();
                        RequestParams.persistRequestParams(OutForDeliveryDetailActivity.this
                                , Objects.requireNonNull(response.body()).getToken()
                                , Objects.requireNonNull(response.body()).getSignature());
                    } else {
                        showDialogFailCheck();
                    }
                } else {
                    Toast.makeText(OutForDeliveryDetailActivity.this, "incorrect_code", Toast.LENGTH_SHORT).show();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", "" + t.getMessage());
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

    private void showDialogCheck() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.data_has_been_save));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setConfirmClickListener(sDialog -> alertDialog.dismissWithAnimation())
                .show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setConfirmClickListener(sDialog -> startActivity(new Intent(getBaseContext(), HomeActivity.class))).show();
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void showDialogFailCheck() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.data_could_not_save));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setConfirmClickListener(sDialog -> alertDialog.dismissWithAnimation())
                .show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setConfirmClickListener(sDialog -> startActivity(new Intent(getBaseContext(), HomeActivity.class))).show();
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void showDialogSMS() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.do_you_want_to_send_sms));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setCancelText(getResources().getString(R.string.cancel));
        alertDialog.setConfirmClickListener(sDialog -> {
            setRequestLink();
            alertDialog.dismissWithAnimation();
        }).show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelClickListener(sDialog -> alertDialog.dismissWithAnimation()).show();
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        Button btnCancel = alertDialog.findViewById(R.id.cancel_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        btnCancel.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        btn.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        btnCancel.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
    }

    private void setRequestLink() {
        ApiInterface apiService = ApiClientSMS.getSMSClient(this, this).create(ApiInterface.class);
        Call<SMSRespone> call = apiService.getPosts(seriCode);
        call.enqueue(new Callback<SMSRespone>() {
            @Override
            public void onResponse(@NonNull Call<SMSRespone> call, @NonNull Response<SMSRespone> response) {
                Log.d("onResponse==>", "" + response.toString());
                assert response.body() != null;
                if (response.body().getError() == 0) {
                    ShowCanSendSMS();
                } else {
                    ShowCanNotSendSMS();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SMSRespone> call, @NonNull Throwable t) {
                Log.d("Fail==>", "" + t.toString());
            }
        });
    }

    private void ShowCanSendSMS() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.send_sms_success));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setConfirmClickListener(sDialog -> alertDialog.dismissWithAnimation())
                .show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setConfirmClickListener(sDialog -> alertDialog.dismissWithAnimation()).show();
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void ShowCanNotSendSMS() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.send_sms_fail));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setConfirmClickListener(sDialog -> alertDialog.dismissWithAnimation()).show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setConfirmClickListener(sDialog -> alertDialog.dismissWithAnimation()).show();
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_REASON_CODE && resultCode == RESULT_OK && data != null) {
            //Get selection
            SelectionData selectionVan = data.getParcelableExtra(Constants.REQUEST_REASON_KEY);
            assert selectionVan != null;
            btnReason.setText(selectionVan.getName());
            reasonId = Integer.parseInt(selectionVan.getId());
        }
    }
}
