package com.udaya.virak_buntham.vetpickup.activities.pickUp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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
import com.udaya.virak_buntham.vetpickup.activities.goodsLocal.GoodsTransferLocalActivity;
import com.udaya.virak_buntham.vetpickup.activities.goodsTransfer.GoodsTransferActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.ScanQrRespone;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AppConfig;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickUpDetailActivity extends AppCompatActivity implements
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

    public static String date, code, customer, receiverTel,senderTel, senderAddr, status, cod, addLet, addLong, seriCode, reference, customerTel, customerName, customerId, note;

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
    @BindView(R.id.btn_province_Add)
    Button btnProvinceAdd;
    @BindView(R.id.tvCustomer)
    TextView tvCustomer;
    @BindView(R.id.btn_local_Add)
    Button btnAddLocal;

    @BindView(R.id.tvNote)
    TextView tvNote;

    private ProgressDialog progressDialog;

    @BindView(R.id.llBtn)
    LinearLayout llBtn;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up__detail);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        mToolbarTitle.setText(R.string.house_pick_up);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        setData();
        tvSenderTel.setOnClickListener(v -> {
            Intent sIntent = new Intent(Intent.ACTION_CALL, Uri
                    .parse("tel:" + tvSenderTel.getText().toString()));
            sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(sIntent);
        });
        btnAddLocal.setOnClickListener(v -> {
            GoodsTransferLocalActivity.referenceLocal = reference;
            GoodsTransferLocalActivity.customerLocalId = customerId;
            GoodsTransferLocalActivity.customerLocalName = customerName;
            GoodsTransferLocalActivity.customerLocalTel = customerTel;
            GoodsTransferLocalActivity.receiverLocalTel = receiverTel;
            startActivity(new Intent(getBaseContext(), GoodsTransferLocalActivity.class));
        });
        btnProvinceAdd.setOnClickListener(v -> {
            GoodsTransferActivity.referenceLocal = reference;
            GoodsTransferActivity.customerLocalId = customerId;
            GoodsTransferActivity.customerLocalName = customerName;
            GoodsTransferActivity.customerLocalTel = customerTel;
            GoodsTransferActivity.receiverLocalTel = receiverTel;
            startActivity(new Intent(getBaseContext(), GoodsTransferActivity.class));
        });
    }

    private void setData() {
        tvDate.setText(date);
        tvCode.setText(code);
        tvSenderTel.setText(senderTel);
        tvSenderAdd.setText(senderAddr);
        tvCustomer.setText(customer);
        tvReceiverTel.setText(receiverTel);
        tvNote.setText(note);
        if (customer.isEmpty()) {
            tvCustomer.setText(getResources().getString(R.string.genaral));
        } else {
            tvCustomer.setText(customerName);
        }
        if (status.equals("2")) {
            llBtn.setVisibility(View.GONE);
        } else {
            llBtn.setVisibility(View.VISIBLE);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
            // Place current location marker
            LatLng latLng = new LatLng(Double.parseDouble(addLet), Double.parseDouble(addLong));

            Log.d("LetLong", "" + latLng);
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.setPosition(latLng);
            } else {
                mCurrLocationMarker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(senderAddr));
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
                    , UserSession.getUserSession(getBaseContext()), seriCode);
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

    private void getCustomerReceive(String device, String token, String signature, String session, String code) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ScanQrRespone> call = apiService.getCustomerReceive(device, token, signature, session, code,""+ AppConfig.getGetLongitude(),""+AppConfig.getGetLatitude());
        call.enqueue(new Callback<ScanQrRespone>() {
            @Override
            public void onResponse(@NonNull Call<ScanQrRespone> call, @NonNull Response<ScanQrRespone> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("1")) {
                        showDialogCheck();
                        RequestParams.persistRequestParams(PickUpDetailActivity.this
                                , Objects.requireNonNull(response.body()).getToken()
                                , Objects.requireNonNull(response.body()).getSignature());
                    } else {
                        showDialogFailCheck();
                    }
                } else {
                    Toast.makeText(PickUpDetailActivity.this, "incorrect_code", Toast.LENGTH_SHORT).show();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<ScanQrRespone> call, @NonNull Throwable t) {
                Log.i("requestReport", Objects.requireNonNull(t.getMessage()));
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
}
