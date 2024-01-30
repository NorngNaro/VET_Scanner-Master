package com.udaya.virak_buntham.vetpickup.activities.outForDevliery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.ScanChangeDestinationActivity;
import com.udaya.virak_buntham.vetpickup.adapters.OutFroDeliveryAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.ScanQrRespone;
import com.udaya.virak_buntham.vetpickup.models.getdelivery.GetDeliveryDataItem;
import com.udaya.virak_buntham.vetpickup.models.getdelivery.GetDeliveryRespone;
import com.udaya.virak_buntham.vetpickup.models.requesttoken.RequestTokenResponse;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutForDeliveryActivity extends AppCompatActivity implements OnItemClickListener, OnInternetConnectionListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.btnClose)
    Button btnClose;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.recyclerOutForDelivery)
    RecyclerView recyclerOutForDelivery;

    private ProgressDialog progressDialog;
    private List<GetDeliveryDataItem> getDeliveryDataItems;
    @BindView(R.id.btnListDelivery)
    Button btnListDelivery;
    @BindView(R.id.btnMap)
    Button btnMap;
    @BindView(R.id.llList)
    LinearLayout llList;
    @BindView(R.id.llMaps)
    LinearLayout llMaps;

    @BindView(R.id.edt_code_scan)
    EditText search;
    @BindView(R.id.imgScanQr)
    ImageView imgScanQr;

    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;
    private SupportMapFragment mapFragment = null;

    private final ArrayList<String> letData = new ArrayList<>();

    private final ArrayList<String> longData = new ArrayList<>();

    private final ArrayList<String> nameData = new ArrayList<>();

    private final ArrayList<String> status = new ArrayList<>();

    public int sizeMap;

    public static BottomSheetDialog bt;

    private TextView tvDate, tvCode, tvReceiverTel, tvReceiverAdd, tvTotalFee, tvCod, tvClose;
    private Button btnReceivedGood;

    public int mapClick = 0;

    private OutFroDeliveryAdapter outFroDeliveryAdapter;
    public static String outForDeliveryScan = "";
    public static int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_for_delivery);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDisplay);
        assert mapFragment != null;
        mapFragment.getMapAsync(OutForDeliveryActivity.this);
        ButterKnife.bind(this);
        bt = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        progressDialog = new ProgressDialog(this);
        mToolbarTitle.setText(R.string.out_for_delivery);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        getOutForDeliveryList(DeviceID.getDeviceId(this)
                , RequestParams.getTokenRequestParams(this)
                , RequestParams.getSignatureRequestParams(this)
                , UserSession.getUserSession(this)
        );
        new Handler().postDelayed(() -> {
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapDisplay);
            assert mapFragment != null;
            mapFragment.getMapAsync(OutForDeliveryActivity.this);
        }, 500);
        btnListDelivery.setOnClickListener(v -> {
            llList.setVisibility(View.VISIBLE);
            llMaps.setVisibility(View.GONE);

            btnListDelivery.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            btnListDelivery.setTextColor(getResources().getColor(R.color.colorWhite));

            btnMap.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            btnMap.setTextColor(getResources().getColor(R.color.colorAccent));
        });

        btnMap.setOnClickListener(v -> {
            llMaps.setVisibility(View.VISIBLE);
            llList.setVisibility(View.GONE);

            btnListDelivery.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            btnListDelivery.setTextColor(getResources().getColor(R.color.colorAccent));

            btnMap.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            btnMap.setTextColor(getResources().getColor(R.color.colorWhite));
        });

        btnClose.setVisibility(View.VISIBLE);
        btnClose.setOnClickListener(v -> showDialogCloseDelivery());



        imgScanQr.setOnClickListener(view -> {

            ScanChangeDestinationActivity.condition = "outForDelivery";
            startActivity(new Intent(this, ScanChangeDestinationActivity.class));

        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
                    getOutForDeliveryList(DeviceID.getDeviceId(OutForDeliveryActivity.this)
                            , RequestParams.getTokenRequestParams(OutForDeliveryActivity.this)
                            , RequestParams.getSignatureRequestParams(OutForDeliveryActivity.this)
                            , UserSession.getUserSession(OutForDeliveryActivity.this)
                    );
                }else {
                    type = 1; // type = 1 is search by telephone
                    outFroDeliveryAdapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!outForDeliveryScan.equals("")){
            type = 2; // type = 2 is scan QR
            String str = outForDeliveryScan;
            String[] strArray = null;
            strArray = str.split(",");
            if(strArray.length != 4){
                Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
            }else {
                getDeliveryDetailByCode(strArray[0]);
            }
        }
    }


    private void getDeliveryDetailByCode(String code){
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<GetDeliveryRespone> call = apiService.getdeliveryByCode(
                DeviceID.getDeviceId(OutForDeliveryActivity.this),
                RequestParams.getTokenRequestParams(OutForDeliveryActivity.this),
                RequestParams.getSignatureRequestParams(OutForDeliveryActivity.this),
                UserSession.getUserSession(OutForDeliveryActivity.this),
                code);
        call.enqueue(new Callback<GetDeliveryRespone>() {
            @Override
            public void onResponse(@NonNull Call<GetDeliveryRespone> call, @NonNull Response<GetDeliveryRespone> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {

                                RequestParams.persistRequestParams(OutForDeliveryActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());

                                tvNoData.setVisibility(View.VISIBLE);
                                btnClose.setVisibility(View.GONE);
                            } else {

                                RequestParams.persistRequestParams(OutForDeliveryActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());

                                getDeliveryDataItems = response.body().getData();
                                sizeMap = response.body().getData().size();
                                Log.d("SizeDate1==>", "" + sizeMap);
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    if (response.body().getData().get(i).getReceiverLats() != null && response.body().getData().get(i).getReceiverLongs() != null) {
                                        Log.d("letLong==>", "\n" + response.body().getData().get(i).getReceiverLats() + "---" + response.body().getData().get(i).getReceiverLongs());
                                        letData.add(response.body().getData().get(i).getReceiverLats());
                                        longData.add(response.body().getData().get(i).getReceiverLongs());
                                        nameData.add(response.body().getData().get(i).getSeriesCode());
                                        status.add(response.body().getData().get(i).getStatus());
                                    }
                                }

                                OutForDeliveryDetailActivity.date = getDeliveryDataItems.get(0).getDate();
                                OutForDeliveryDetailActivity.code = getDeliveryDataItems.get(0).getCode();
                                OutForDeliveryDetailActivity.seriCode = getDeliveryDataItems.get(0).getSeriesCode();
                                OutForDeliveryDetailActivity.senderTel = getDeliveryDataItems.get(0).getSenderTel();
                                OutForDeliveryDetailActivity.receiverTel = getDeliveryDataItems.get(0).getReceiverTel();
                                OutForDeliveryDetailActivity.receiverAdd = getDeliveryDataItems.get(0).getReceiverAddr();
                                OutForDeliveryDetailActivity.status = getDeliveryDataItems.get(0).getStatus();
                                OutForDeliveryDetailActivity.addLet = getDeliveryDataItems.get(0).getReceiverLats();
                                OutForDeliveryDetailActivity.addLong = getDeliveryDataItems.get(0).getReceiverLongs();
                                OutForDeliveryDetailActivity.deliveryDetailId = getDeliveryDataItems.get(0).getDelivery_detail_id();
                                if (getDeliveryDataItems.get(0).getTransfer_fee().equals("0")) {
                                    OutForDeliveryDetailActivity.paid = getResources().getString(R.string.paid);
                                } else {
                                    OutForDeliveryDetailActivity.paid = getDeliveryDataItems.get(0).getTransfer_fee() + "៛";
                                }
                                if (getDeliveryDataItems.get(0).getCod().equals("")) {
                                    OutForDeliveryDetailActivity.cod = getResources().getString(R.string.none);
                                } else {
                                    OutForDeliveryDetailActivity.cod = getDeliveryDataItems.get(0).getCod();
                                }
                                if (getDeliveryDataItems.get(0).getStatus().equals("3")) {
                                    OutForDeliveryDetailActivity.status = getResources().getString(R.string.closed);
                                } else {
                                    OutForDeliveryDetailActivity.status = getResources().getString(R.string.on_delivery);
                                }
                                startActivity(new Intent(OutForDeliveryActivity.this, OutForDeliveryDetailActivity.class));
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(OutForDeliveryActivity.this, response.body().getInfo());
                        }
                    }
                }
                progressDialog.hide();
            }
            @Override
            public void onFailure(@NonNull Call<GetDeliveryRespone> call, @NonNull Throwable t) {
                Log.i("requestInfo", ""+t.getMessage());
                progressDialog.hide();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
        Call<GetDeliveryRespone> call = apiService.getdelivery(device, token, signature, session);
        call.enqueue(new Callback<GetDeliveryRespone>() {
            @Override
            public void onResponse(@NonNull Call<GetDeliveryRespone> call, @NonNull Response<GetDeliveryRespone> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(OutForDeliveryActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                tvNoData.setVisibility(View.VISIBLE);
                                btnClose.setVisibility(View.GONE);
                            } else {
//                                selectionList = Objects.requireNonNull(response.body()).getData();
                                //Replace request params

                                RequestParams.persistRequestParams(OutForDeliveryActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());

                                getDeliveryDataItems = response.body().getData();
                                sizeMap = response.body().getData().size();
                                Log.d("SizeDate1==>", "" + sizeMap);
                                setupAreaAdapter(getDeliveryDataItems);
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    if (response.body().getData().get(i).getReceiverLats() != null && response.body().getData().get(i).getReceiverLongs() != null) {
                                        Log.d("letLong==>", "\n" + response.body().getData().get(i).getReceiverLats() + "---" + response.body().getData().get(i).getReceiverLongs());
                                        letData.add(response.body().getData().get(i).getReceiverLats());
                                        longData.add(response.body().getData().get(i).getReceiverLongs());
                                        nameData.add(response.body().getData().get(i).getSeriesCode());
                                        status.add(response.body().getData().get(i).getStatus());
                                    }
                                }
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(OutForDeliveryActivity.this, response.body().getInfo());
                        }
                    }
                }
                progressDialog.hide();
            }
            @Override
            public void onFailure(@NonNull Call<GetDeliveryRespone> call, @NonNull Throwable t) {
                Log.i("requestInfo", ""+t.getMessage());
                progressDialog.hide();
            }
        });
    }

    private void setupAreaAdapter(List<GetDeliveryDataItem> getDeliveryDataItems) {
        outFroDeliveryAdapter = new OutFroDeliveryAdapter(getDeliveryDataItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerOutForDelivery.setLayoutManager(layoutManager);
        recyclerOutForDelivery.setAdapter(outFroDeliveryAdapter);
        outFroDeliveryAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onInternetAvailable() { }

    @Override
    public void onInternetUnavailable() { }

    @Override
    public void itemClick(int position) {
        OutForDeliveryDetailActivity.date = getDeliveryDataItems.get(position).getDate();
        OutForDeliveryDetailActivity.code = getDeliveryDataItems.get(position).getCode();
        OutForDeliveryDetailActivity.seriCode = getDeliveryDataItems.get(position).getSeriesCode();
        OutForDeliveryDetailActivity.senderTel = getDeliveryDataItems.get(position).getSenderTel();
        OutForDeliveryDetailActivity.receiverTel = getDeliveryDataItems.get(position).getReceiverTel();
        OutForDeliveryDetailActivity.receiverAdd = getDeliveryDataItems.get(position).getReceiverAddr();
        OutForDeliveryDetailActivity.status = getDeliveryDataItems.get(position).getStatus();
        OutForDeliveryDetailActivity.addLet = getDeliveryDataItems.get(position).getReceiverLats();
        OutForDeliveryDetailActivity.addLong = getDeliveryDataItems.get(position).getReceiverLongs();
        OutForDeliveryDetailActivity.deliveryDetailId = getDeliveryDataItems.get(position).getDelivery_detail_id();
        if (getDeliveryDataItems.get(position).getTransfer_fee().equals("0")) {
            OutForDeliveryDetailActivity.paid = getResources().getString(R.string.paid);
        } else {
            OutForDeliveryDetailActivity.paid = getDeliveryDataItems.get(position).getTransfer_fee() + "៛";
        }
        if (getDeliveryDataItems.get(position).getCod().equals("")) {
            OutForDeliveryDetailActivity.cod = getResources().getString(R.string.none);
        } else {
            OutForDeliveryDetailActivity.cod = getDeliveryDataItems.get(position).getCod();
        }
        if (getDeliveryDataItems.get(position).getStatus().equals("3")) {
            OutForDeliveryDetailActivity.status = getResources().getString(R.string.closed);
        } else {
            OutForDeliveryDetailActivity.status = getResources().getString(R.string.on_delivery);
        }
        startActivity(new Intent(this, OutForDeliveryDetailActivity.class));
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
        if (marker.equals(mCurrLocationMarker)) {
            //handle click here
            Toast.makeText(this, "marker click", Toast.LENGTH_SHORT).show();
        }
        return true;
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

                if (status.get(i).equals("3")) {
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
                    bottomSheetSelected(OutForDeliveryActivity.this, marker.getTitle());
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
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.bottmsheet_map, null);
        tvDate = view.findViewById(R.id.tvDate);
        tvCode = view.findViewById(R.id.tvCode);
        tvReceiverTel = view.findViewById(R.id.tvReceiverTel);
        tvReceiverAdd = view.findViewById(R.id.tvSenderAdd);
        tvTotalFee = view.findViewById(R.id.tvTotalFee);
        tvCod = view.findViewById(R.id.tvCod);
        tvClose = view.findViewById(R.id.tvClose);
        btnReceivedGood = view.findViewById(R.id.btnReceivedGood);
        bt.setContentView(view);
        findDeliveryByCode(DeviceID.getDeviceId(OutForDeliveryActivity.this)
                , RequestParams.getTokenRequestParams(OutForDeliveryActivity.this)
                , RequestParams.getSignatureRequestParams(OutForDeliveryActivity.this)
                , UserSession.getUserSession(OutForDeliveryActivity.this),
                marker
        );

    }

    private void findDeliveryByCode(String device, String token, String signature, String
            session, String marker) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        } else {
            progressDialog.show();
        }

        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<GetDeliveryRespone> call = apiService.getdeliverybycode(device, token, signature, session, marker);
        call.enqueue(new Callback<GetDeliveryRespone>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<GetDeliveryRespone> call, @NonNull Response<GetDeliveryRespone> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(OutForDeliveryActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                            } else {
//                                selectionList = Objects.requireNonNull(response.body()).getData();

                                //Replace request params
                                RequestParams.persistRequestParams(OutForDeliveryActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                                tvDate.setText(response.body().getData().get(0).getDate());
                                tvCode.setText(response.body().getData().get(0).getCode());
                                tvReceiverTel.setText(response.body().getData().get(0).getReceiverTel());
                                tvReceiverAdd.setText(response.body().getData().get(0).getReceiverAddr());
                                //LatLng latLng = new LatLng(Double.parseDouble(response.body().getData().get(0).getReceiverLats()), Double.parseDouble(response.body().getData().get(0).getReceiverLongs()));
                                if (response.body().getData().get(0).getTransfer_fee().equals("0")) {
                                    tvTotalFee.setText(getResources().getString(R.string.paid));
                                } else {
                                    tvTotalFee.setText(getDeliveryDataItems.get(0).getTransfer_fee() + "៛");
                                }
                                if (response.body().getData().get(0).getCod().equals("")) {
                                    tvCod.setText(getResources().getString(R.string.none));
                                } else {
                                    tvCod.setText(getDeliveryDataItems.get(0).getCod());
                                }
                                if (response.body().getData().get(0).getStatus().equals("3")) {
                                    tvClose.setVisibility(View.VISIBLE);
                                    btnReceivedGood.setVisibility(View.GONE);
                                    btnReceivedGood.setOnClickListener(v -> bt.hide());
//                                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(latLng));
                                } else {
                                    btnReceivedGood.setText(getResources().getString(R.string.Receive));
                                    btnReceivedGood.setOnClickListener(v -> showDialog(marker));
                                }

                                bt.show();

                            }
                        } else {
                            AlertDialogUtil.alertMessageError(OutForDeliveryActivity.this, response.body().getInfo());
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<GetDeliveryRespone> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

    private void getCustomerReceive(String device, String token, String signature, String session, String code) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ScanQrRespone> call = apiService.getCustomerReceive(device, token, signature, session, code,""+ AppConfig.getGetLongitude(),""+AppConfig.getGetLatitude());
        call.enqueue(new Callback<ScanQrRespone>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Response<ScanQrRespone> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("1")) {
                        showDialogCheck();
                        RequestParams.persistRequestParams(OutForDeliveryActivity.this
                                , Objects.requireNonNull(response.body()).getToken()
                                , Objects.requireNonNull(response.body()).getSignature());
                    } else {
                        showDialogFailCheck();
                    }
                } else {
                    Toast.makeText(OutForDeliveryActivity.this, "incorrect_code", Toast.LENGTH_SHORT).show();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<ScanQrRespone> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }

    private void showDialogCheck() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.data_has_been_save));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setConfirmClickListener(sDialog -> {
            mapClick = 1;
            finish();
            startActivity(getIntent());

            alertDialog.dismissWithAnimation();
            bt.hide();
        }).show();
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

    private void showDialog(String marker) {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.do_you_want_receive));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setCancelText(getResources().getString(R.string.cancel));
        alertDialog.setConfirmClickListener(sDialog -> {
            getCustomerReceive(DeviceID.getDeviceId(getBaseContext())
                    , RequestParams.getTokenRequestParams(getBaseContext())
                    , RequestParams.getSignatureRequestParams(getBaseContext())
                    , UserSession.getUserSession(getBaseContext()), marker);
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

    private void showDialogCloseDelivery() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText("តើអ្នកពិតជាចង់បិទការដឹក?");
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setCancelText(getResources().getString(R.string.cancel));
        alertDialog.setConfirmClickListener(sDialog -> {
            closeDelivery(DeviceID.getDeviceId(getBaseContext())
                    , RequestParams.getTokenRequestParams(getBaseContext())
                    , RequestParams.getSignatureRequestParams(getBaseContext())
                    , UserSession.getUserSession(getBaseContext()));
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

    private void closeDelivery(String device, String token, String signature, String
            session) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        } else {
            progressDialog.show();
        }

        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<RequestTokenResponse> call = apiService.closedelivery(device, token, signature, session);
        call.enqueue(new Callback<RequestTokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<RequestTokenResponse> call, @NonNull Response<RequestTokenResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {

                            //Replace request params
                            RequestParams.persistRequestParams(OutForDeliveryActivity.this
                                    , Objects.requireNonNull(response.body()).getToken()
                                    , Objects.requireNonNull(response.body()).getSignature());
                            if (response.body().getStatus().equals("1")) {
                                finish();
                                startActivity(getIntent());
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(OutForDeliveryActivity.this, response.body().getInfo());
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<RequestTokenResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }


}
