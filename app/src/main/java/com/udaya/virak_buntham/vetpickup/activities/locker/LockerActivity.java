package com.udaya.virak_buntham.vetpickup.activities.locker;

import static com.udaya.virak_buntham.vetpickup.bluetoothprinter.CityPrintActivity.QRCodeWidth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.outForDevliery.OutForDeliveryActivity;
import com.udaya.virak_buntham.vetpickup.adapters.LockerAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.locker.LockerData;
import com.udaya.virak_buntham.vetpickup.models.locker.LockerLoginResponse;
import com.udaya.virak_buntham.vetpickup.models.locker.LockerResponse;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.Constants;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LockerActivity extends AppCompatActivity implements OnItemClickListener, OnInternetConnectionListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    @BindView(R.id.btn_show_qr_code)
    ImageView btnShowQRCode;

    @BindView(R.id.btn_back)
    ImageView btnBack;

    @BindView(R.id.btnLoadItem)
    Button btnLoadItem;

    @BindView(R.id.btnGetItem)
    Button btnGetItem;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    @BindView(R.id.edt_code_scan)
    EditText search;

    @BindView(R.id.lockerRecycler)
    RecyclerView lockerRecycler;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private LockerAdapter lockerAdapter;

    private ProgressDialog progressDialog;

    boolean isPickUp = true;

    private ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker);
        ButterKnife.bind(this);

        mToolbarTitle.setText("Locker");

        btnBack.setOnClickListener(v -> finish());

        progressDialog = new ProgressDialog(this);

        onTabBarClick();

        toolBarQRCode();

        onSearch();

        onRefresh();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isPickUp){
            getLockerPickUp();
        }else {
            getLockerDropOff();
        }
    }

    private void onSearch(){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
                    if(isPickUp){
                        getLockerPickUp();
                    }else {
                        getLockerDropOff();
                    }
                }else {
                    lockerAdapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void onRefresh(){
        refreshLayout.setOnRefreshListener(() -> {
            if(isPickUp){
                getLockerPickUp();
               refreshLayout.setRefreshing(false);
            }else {
                getLockerDropOff();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void getLockerPickUp(){
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, LockerActivity.this).create(ApiInterface.class);
        Call<LockerResponse> call = apiService.getLockerPickUp(
                DeviceID.getDeviceId(LockerActivity.this),
                RequestParams.getTokenRequestParams(LockerActivity.this),
                RequestParams.getSignatureRequestParams(LockerActivity.this),
                UserSession.getUserSession(LockerActivity.this) );
        call.enqueue(new Callback<LockerResponse>() {
            @Override
            public void onResponse(@NonNull Call<LockerResponse> call, @NonNull Response<LockerResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {

                            RequestParams.persistRequestParams(LockerActivity.this
                                    , Objects.requireNonNull(response.body()).getToken()
                                    , Objects.requireNonNull(response.body()).getSignature());

                            setupAreaAdapter(response.body().getData());

                            if (response.body().getData().size() != 0) {
                                tvNoData.setVisibility(View.GONE);
                            } else {
                                tvNoData.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.e("", "onResponse: 1" );
                            Toast.makeText(LockerActivity.this, "សូមព្យាយាមម្តងទៀត", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                progressDialog.hide();
            }
            @Override
            public void onFailure(@NonNull Call<LockerResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", ""+t.getMessage());
                progressDialog.hide();
            }
        });
    }

    private void getLockerDropOff(){
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, LockerActivity.this).create(ApiInterface.class);
        Call<LockerResponse> call = apiService.getLockerDropOff(
                DeviceID.getDeviceId(LockerActivity.this),
                RequestParams.getTokenRequestParams(LockerActivity.this),
                RequestParams.getSignatureRequestParams(LockerActivity.this),
                UserSession.getUserSession(LockerActivity.this) );
        call.enqueue(new Callback<LockerResponse>() {
            @Override
            public void onResponse(@NonNull Call<LockerResponse> call, @NonNull Response<LockerResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {

                            RequestParams.persistRequestParams(LockerActivity.this
                                    , Objects.requireNonNull(response.body()).getToken()
                                    , Objects.requireNonNull(response.body()).getSignature());
                            setupAreaAdapter(response.body().getData());

                            if (response.body().getData().size() != 0) {
                                tvNoData.setVisibility(View.GONE);
                            } else {
                                tvNoData.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.e("", "onResponse: 3" );
                            Toast.makeText(LockerActivity.this, "សូមព្យាយាមម្តងទៀត", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                 progressDialog.hide();
            }
            @Override
            public void onFailure(@NonNull Call<LockerResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", ""+t.getMessage());
                 progressDialog.hide();
            }
        });
    }

    private void getQrCodeLogin(){
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, LockerActivity.this).create(ApiInterface.class);
        Call<LockerLoginResponse> call = apiService.getLoginQR(
                DeviceID.getDeviceId(LockerActivity.this),
                RequestParams.getTokenRequestParams(LockerActivity.this),
                RequestParams.getSignatureRequestParams(LockerActivity.this),
                UserSession.getUserSession(LockerActivity.this) );
        call.enqueue(new Callback<LockerLoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LockerLoginResponse> call, @NonNull Response<LockerLoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {

                            RequestParams.persistRequestParams(LockerActivity.this
                                    , Objects.requireNonNull(response.body()).getToken()
                                    , Objects.requireNonNull(response.body()).getSignature());

                            View dialogView = LayoutInflater.from(LockerActivity.this).inflate(R.layout.custom_dialog_qr_code, viewGroup, false);

                            ImageView btn_positive = dialogView.findViewById(R.id.close);
                            ImageView QRCode = dialogView.findViewById(R.id.qr_code_login);


                            try {
                                QRCode.setImageBitmap(TextToImageEncode(response.body().getData()));
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }

                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LockerActivity.this);
                            builder.setView(dialogView);
                            android.app.AlertDialog alertDialog = builder.create();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            alertDialog.setCancelable(false);
                            alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                            alertDialog.show();
                            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                            btn_positive.setOnClickListener(v1 -> alertDialog.cancel());

                        } else {
                            Log.e("", "onResponse: 2" );
                            Toast.makeText(LockerActivity.this, "សូមព្យាយាមម្តងទៀត", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                progressDialog.hide();
            }
            @Override
            public void onFailure(@NonNull Call<LockerLoginResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", ""+t.getMessage());
                 progressDialog.hide();
            }
        });
    }

    void toolBarQRCode(){

        btnShowQRCode.setOnClickListener(v -> {

            getQrCodeLogin();

        });
    }

    void onTabBarClick(){
        btnGetItem.setOnClickListener(v -> {
            btnLoadItem.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            btnLoadItem.setTextColor(getResources().getColor(R.color.colorAccent));
            btnGetItem.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            btnGetItem.setTextColor(getResources().getColor(R.color.colorWhite));

            isPickUp = true;
            getLockerPickUp();
        });

        btnLoadItem.setOnClickListener(v -> {
            btnLoadItem.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            btnLoadItem.setTextColor(getResources().getColor(R.color.colorWhite));
            btnGetItem.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            btnGetItem.setTextColor(getResources().getColor(R.color.colorAccent));

            isPickUp = false;
            getLockerDropOff();
        });
    }


    private void setupAreaAdapter(List<LockerData> getDeliveryDataItems) {

        lockerAdapter = new LockerAdapter(getDeliveryDataItems, isPickUp, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        lockerRecycler.setLayoutManager(layoutManager);
        lockerRecycler.setAdapter(lockerAdapter);
        lockerAdapter.setOnItemClickListener(this);
    }

    @Override
    public void itemClick(int position) {


       // startActivity(new Intent(this, LockerPrintOldActivity.class));

    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRCodeWidth, QRCodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.colorBlack) : getResources().getColor(R.color.colorWhite);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 800, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

}