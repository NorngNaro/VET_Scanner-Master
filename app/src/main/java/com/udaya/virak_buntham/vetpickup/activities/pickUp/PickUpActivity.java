package com.udaya.virak_buntham.vetpickup.activities.pickUp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.goodsLocal.GoodsTransferLocalActivity;
import com.udaya.virak_buntham.vetpickup.activities.goodsTransfer.GoodsTransferActivity;
import com.udaya.virak_buntham.vetpickup.adapters.PickUpAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.PickUp.PickUpDataItem;
import com.udaya.virak_buntham.vetpickup.models.PickUp.PickUpRespone;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;
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

public class PickUpActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener, OnInternetConnectionListener, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    @BindView(R.id.recyclerPickUp)
    RecyclerView recyclerViewPickUp;
    private ProgressDialog progressDialog;
    private List<PickUpDataItem> pickUpDataItems;

    @BindView(R.id.btnListDelivery)
    Button btnListDelivery;
    @BindView(R.id.btnMap)
    Button btnMap;
    @BindView(R.id.tvPickUp_No_Data)
    TextView tvNoData;

    @BindView(R.id.llList)
    RelativeLayout llList;
    @BindView(R.id.llMaps)
    LinearLayout llMaps;


    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;
    private SupportMapFragment mapFragment = null;

    private final ArrayList<String> letData = new ArrayList<>();

    private final ArrayList<String> longData = new ArrayList<>();

    private final ArrayList<String> nameData = new ArrayList<>();

    private final ArrayList<String> status = new ArrayList<>();

    public int sizeMap;
    public static BottomSheetDialog bt;
    private TextView tvDate, tvCode, tvCustomer, tvSenderTel;
    private LinearLayout llBottomSheet;
    private Button btnSheetProvince, btnSheetLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        bt = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        mToolbarTitle.setText(R.string.house_pick_up);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        getOutForDeliveryList(DeviceID.getDeviceId(this)
                , RequestParams.getTokenRequestParams(this)
                , RequestParams.getSignatureRequestParams(this)
                , UserSession.getUserSession(this)
        );

        btnListDelivery.setOnClickListener(v -> {
            llList.setVisibility(View.VISIBLE);
            llMaps.setVisibility(View.GONE);

            btnListDelivery.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            btnListDelivery.setTextColor(getResources().getColor(R.color.colorWhite));

            btnMap.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            btnMap.setTextColor(getResources().getColor(R.color.colorAccent));

        });

        btnMap.setOnClickListener(v -> {
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapDisplay);
            assert mapFragment != null;
            mapFragment.getMapAsync(this);

            llMaps.setVisibility(View.VISIBLE);
            llList.setVisibility(View.GONE);

            btnListDelivery.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            btnListDelivery.setTextColor(getResources().getColor(R.color.colorAccent));

            btnMap.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            btnMap.setTextColor(getResources().getColor(R.color.colorWhite));
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
    public void itemClick(int position) {
        PickUpDetailActivity.date = pickUpDataItems.get(position).getDate();
        PickUpDetailActivity.code = pickUpDataItems.get(position).getCode();
        PickUpDetailActivity.senderTel = pickUpDataItems.get(position).getSenderTel();
        PickUpDetailActivity.receiverTel = pickUpDataItems.get(position).getReceiverTel();
        PickUpDetailActivity.senderAddr = pickUpDataItems.get(position).getSenderAddr();
        PickUpDetailActivity.status = pickUpDataItems.get(position).getStatus();
        PickUpDetailActivity.addLet = pickUpDataItems.get(position).getSenderLats();
        PickUpDetailActivity.addLong = pickUpDataItems.get(position).getSenderLongs();
        PickUpDetailActivity.customer = pickUpDataItems.get(position).getCusId();
        PickUpDetailActivity.reference = pickUpDataItems.get(position).getReference();
        PickUpDetailActivity.customerTel = pickUpDataItems.get(position).getCus_tel();
        PickUpDetailActivity.customerId = pickUpDataItems.get(position).getCusId();
        PickUpDetailActivity.customerName = pickUpDataItems.get(position).getCus_name();
        PickUpDetailActivity.note = pickUpDataItems.get(position).getNote();
        startActivity(new Intent(this, PickUpDetailActivity.class));
    }

    private void setupAreaAdapter(List<PickUpDataItem> pickUpDataItems) {
        PickUpAdapter outFroDeliveryAdapter = new PickUpAdapter(pickUpDataItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewPickUp.setLayoutManager(layoutManager);
        recyclerViewPickUp.setAdapter(outFroDeliveryAdapter);
        outFroDeliveryAdapter.setOnItemClickListener(this);
    }

    private void getOutForDeliveryList(String device, String token, String signature, String
            session) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        } else {
            progressDialog.show();
        }

        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<PickUpRespone> call = apiService.getPickUp(device, token, signature, session);
        call.enqueue(new Callback<PickUpRespone>() {
            @Override
            public void onResponse(@NonNull Call<PickUpRespone> call, @NonNull Response<PickUpRespone> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(PickUpActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                tvNoData.setVisibility(View.VISIBLE);
                            } else {
//                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(PickUpActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                pickUpDataItems = response.body().getData();
                                sizeMap = response.body().getData().size();
                                setupAreaAdapter(pickUpDataItems);
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    if (response.body().getData().get(i).getSenderLats() != null && response.body().getData().get(i).getSenderLongs() != null) {
                                        Log.d("letLong==>", "\n" + response.body().getData().get(i).getSenderLats() + "---" + response.body().getData().get(i).getSenderLongs());
                                        letData.add(response.body().getData().get(i).getSenderLats());
                                        longData.add(response.body().getData().get(i).getSenderLongs());
                                        nameData.add(response.body().getData().get(i).getReference());
                                        status.add(response.body().getData().get(i).getStatus());
                                    }
                                }
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(PickUpActivity.this, response.body().getInfo());
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<PickUpRespone> call, @NonNull Throwable t) {
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
            Log.d("SizeDate2==>", "" + sizeMap);
            Log.d("letData==>", "" + letData);
            Log.d("longData==>", "" + longData);
            Log.d("nameData==>", "" + nameData);
            for (int i = 0; i < sizeMap; i++) {
                LatLng latLng = new LatLng(Double.parseDouble(letData.get(i)), Double.parseDouble(longData.get(i)));

                Log.d("letLongDataItem", "" + latLng);
//                if (mCurrLocationMarker != null) {
//                    mCurrLocationMarker.setPosition(latLng);
//                } else {

//                mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                        .title(nameData.get(i)));

                if (status.get(i).equals("2")) {
                    mCurrLocationMarker = googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .title(nameData.get(i)));
                } else {
                    mCurrLocationMarker = googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .title(nameData.get(i)));
                }

//                }

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                googleMap.setOnMarkerClickListener(marker -> {
                    bottomSheetSelected(PickUpActivity.this, marker.getTitle());
//                        marker.setTitle(tvReceiverAdd.getText().toString().trim());
                    return true;
                });
            }
            //Place current location marker

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

    private void bottomSheetSelected(Context context, String marker) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.bottmsheet_pick_up, null);
        tvDate = view.findViewById(R.id.tvDate);
        tvCode = view.findViewById(R.id.tvCode);
        tvSenderTel = view.findViewById(R.id.tvSenderTel);
        tvCustomer = view.findViewById(R.id.tvCustomer);
        llBottomSheet = view.findViewById(R.id.ll_BottomShett_Dialog);
        btnSheetProvince = view.findViewById(R.id.btnSheetProvince);
        btnSheetLocale = view.findViewById(R.id.btnSheetLocale);
        bt.setContentView(view);
        findPickupByCode(DeviceID.getDeviceId(PickUpActivity.this)
                , RequestParams.getTokenRequestParams(PickUpActivity.this)
                , RequestParams.getSignatureRequestParams(PickUpActivity.this)
                , UserSession.getUserSession(PickUpActivity.this),
                marker
        );
    }

    private void findPickupByCode(String device, String token, String signature, String
            session, String marker) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        } else {
            progressDialog.show();
        }

        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<PickUpRespone> call = apiService.getpickupbycode(device, token, signature, session, marker);
        call.enqueue(new Callback<PickUpRespone>() {
            @Override
            public void onResponse(@NonNull Call<PickUpRespone> call, @NonNull Response<PickUpRespone> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(PickUpActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                            } else {
//                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(PickUpActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                tvDate.setText(response.body().getData().get(0).getDate());
                                tvCode.setText(response.body().getData().get(0).getCode());
                                tvSenderTel.setText(response.body().getData().get(0).getSenderTel());
                                if (response.body().getData().get(0).getCus_name().equals("")) {
                                    tvCustomer.setText(getResources().getString(R.string.genaral));
                                } else {
                                    tvCustomer.setText(pickUpDataItems.get(0).getCus_name());
                                }
                                if (response.body().getData().get(0).getStatus().equals("2")) {
                                    llBottomSheet.setVisibility(View.GONE);
                                } else {
                                    llBottomSheet.setVisibility(View.VISIBLE);
                                }
                                btnSheetProvince.setOnClickListener(v -> {
                                    GoodsTransferActivity.referenceLocal = response.body().getData().get(0).getReference();
                                    GoodsTransferActivity.customerLocalId = response.body().getData().get(0).getCusId();
                                    GoodsTransferActivity.customerLocalName = response.body().getData().get(0).getCus_name();
                                    GoodsTransferActivity.customerLocalTel = response.body().getData().get(0).getCus_tel();
                                    startActivity(new Intent(getBaseContext(), GoodsTransferActivity.class));
                                });

                                btnSheetLocale.setOnClickListener(v -> {
                                    GoodsTransferLocalActivity.referenceLocal = response.body().getData().get(0).getReference();
                                    GoodsTransferLocalActivity.customerLocalId = response.body().getData().get(0).getCusId();
                                    GoodsTransferLocalActivity.customerLocalName = response.body().getData().get(0).getCus_name();
                                    GoodsTransferLocalActivity.customerLocalTel = response.body().getData().get(0).getCus_tel();
                                    startActivity(new Intent(getBaseContext(), GoodsTransferLocalActivity.class));
                                });
                                bt.show();
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(PickUpActivity.this, response.body().getInfo());
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<PickUpRespone> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }
}
