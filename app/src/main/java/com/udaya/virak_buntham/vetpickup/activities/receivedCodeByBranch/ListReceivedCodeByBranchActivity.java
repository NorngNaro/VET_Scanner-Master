package com.udaya.virak_buntham.vetpickup.activities.receivedCodeByBranch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.callToCustomer.CallToCustomer;
import com.udaya.virak_buntham.vetpickup.activities.callToCustomer.CallToCustomerDetailActivity;
import com.udaya.virak_buntham.vetpickup.activities.callToCustomer.CallToCustomerScanToLocationActivity;
import com.udaya.virak_buntham.vetpickup.adapters.ChangeAdapter;
import com.udaya.virak_buntham.vetpickup.adapters.CustomerReceiveFormAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemCheckListener;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.CustomerReceiveFormResponse;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.DataItem;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.ScanQrRespone;
import com.udaya.virak_buntham.vetpickup.models.saveCustomerCall.SaveCustomerCallResponse;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AppConfig;
import com.udaya.virak_buntham.vetpickup.utils.GpsTrackerLocation;
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

public class ListReceivedCodeByBranchActivity extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.edtNote)
    EditText edtNote;
    @BindView(R.id.tvTotal_cod)
    TextView tvTotalcod;
    @BindView(R.id.llCod)
    LinearLayout llCod;
    @BindView(R.id.btnSave)
    Button btnSave;


    private ProgressDialog progressDialog;
    public  static  int id ;
    private  String itemReceive;
    public static ArrayList<String> arrayNum = new ArrayList<>();
    String uom = "";

    private GpsTrackerLocation gpsTracker;
    public void getLocationData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gpsTracker = new GpsTrackerLocation(ListReceivedCodeByBranchActivity.this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (gpsTracker.canGetLocation()) {
                double letData = gpsTracker.getLatitude();
                double longData = gpsTracker.getLongitude();
                AppConfig.setGetLatitude(letData);
                AppConfig.setGetLongitude(longData);
                // Toast.makeText(this, "==>" + AppConfig.getGetLatitude() + "==" + AppConfig.getGetLongitude(), Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_list_received_code_by_branch);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        mToolbarTitle.setText("អតិថិជនទទួលអីវ៉ាន់(សាខា)");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        customerreceiveform(DeviceID.getDeviceId(ListReceivedCodeByBranchActivity.this)
                , RequestParams.getTokenRequestParams(ListReceivedCodeByBranchActivity.this)
                , RequestParams.getSignatureRequestParams(ListReceivedCodeByBranchActivity.this)
                , UserSession.getUserSession(ListReceivedCodeByBranchActivity.this),
                id);
        btnSave.setOnClickListener(v -> {
            if (arrayNum.isEmpty()){
                Toast.makeText(this, "សូមជ្រើសរើសធាតុ", Toast.LENGTH_SHORT).show();
            }else {
                getCustomerReceive(DeviceID.getDeviceId(ListReceivedCodeByBranchActivity.this)
                    , RequestParams.getTokenRequestParams(ListReceivedCodeByBranchActivity.this)
                    , RequestParams.getSignatureRequestParams(ListReceivedCodeByBranchActivity.this)
                    , UserSession.getUserSession(ListReceivedCodeByBranchActivity.this),
                    ""+ id,edtNote.getText().toString().trim(),convertObjectArrayToString(arrayNum,","));
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

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }
    private static String convertObjectArrayToString(ArrayList<String> arr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : arr)
            sb.append(obj.toString()).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }
    public void customerreceiveform(String device, String token, String signature, String session, int id) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<CustomerReceiveFormResponse> call = apiService.customerreceiveform(device, token, signature, session, id);
        call.enqueue(new Callback<CustomerReceiveFormResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<CustomerReceiveFormResponse> call, @NonNull Response<CustomerReceiveFormResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            setupAreaAdapter(response.body().getData());
                            uom = response.body().getUom();
                            if(response.body().getData().get(0).getCod().equals("0")){
                                llCod.setVisibility(View.GONE);
                            } else {
                                llCod.setVisibility(View.VISIBLE);
                                tvTotalcod.setText(""+response.body().getData().get(0).getCod());
                            }
                            if (response.body().getTotal()==0){
                                btnSave.setVisibility(View.GONE);
                                tvTotal.setText("មិនមាន");
                            }else {
                                btnSave.setVisibility(View.VISIBLE);
                                tvTotal.setText(""+response.body().getTotal()+uom);
                            }
                        }
                    }
                }
                progressDialog.hide();
            }
            @Override
            public void onFailure(@NonNull Call<CustomerReceiveFormResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void setupAreaAdapter(List<DataItem> itemData) {
        arrayNum.clear();
        RecyclerView rc = findViewById(R.id.recyclerData);
        CustomerReceiveFormAdapter customerReceiveFormAdapter = new CustomerReceiveFormAdapter(itemData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rc.setLayoutManager(layoutManager);
        rc.setAdapter(customerReceiveFormAdapter);
        customerReceiveFormAdapter.setOnItemClickListener((position, checkBox) -> {
            if (checkBox) {
                if (arrayNum.size() == 0) {
                    arrayNum.add(0, "" +itemData.get(position).getId());
                } else {
                    arrayNum.add("" + itemData.get(position).getId());
                }
            } else {
                try {
                    if (arrayNum.size() == 1) {
                        arrayNum.remove(0);
                    } else {
                        for (int i =0 ; i <arrayNum.size();i++){
                            if (arrayNum.get(i).equals("" + itemData.get(position).getId())){
                                arrayNum.remove(i);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("error===>", e.toString());
                }
            }

            Log.d("arrayData==>",""+arrayNum.size());
            if(arrayNum.size() == 0){
                btnSave.setVisibility(View.GONE);
            }else{
                btnSave.setVisibility(View.VISIBLE);
            }
            tvTotal.setText(""+arrayNum.size()+" "+uom);
        });
    }
    private void getCustomerReceive(String device, String token, String signature, String session, String id,String note ,String itemReceive) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ScanQrRespone> call = apiService.saveCustomerReceiveForm(device, token, signature, session, id ,note ,itemReceive,""+AppConfig.getGetLatitude(),""+AppConfig.getGetLongitude());

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
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ListReceivedCodeByBranchActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.message));
        sweetAlertDialog.setContentText("ទិន្នន័យត្រូវបានរក្សាទុក");
        sweetAlertDialog.setConfirmText("បិទ");
        sweetAlertDialog.setConfirmClickListener(sDialog -> {
            sDialog.dismissWithAnimation();
            finish();
            startActivity(new Intent(this,SearchActivity.class));
        });
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
    }
    private void  alertError() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ListReceivedCodeByBranchActivity.this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.message));
        sweetAlertDialog.setContentText("ទិន្នន័យមិនត្រូវបានរក្សាទុកទេ");
        sweetAlertDialog.setConfirmText("បិទ");
        sweetAlertDialog.setConfirmClickListener(sDialog -> {
            sDialog.dismissWithAnimation();
            finish();
            startActivity(new Intent(this,SearchReceivedCodeByBranchActivity.class));
        });
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
    }
}