package com.udaya.virak_buntham.vetpickup.activities.callToCustomer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.adapters.CallToCustomerAdapter;
import com.udaya.virak_buntham.vetpickup.adapters.CallToCustomerHistoryAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.ScanQrRespone;
import com.udaya.virak_buntham.vetpickup.models.getCustomer.CustomerDataItem;
import com.udaya.virak_buntham.vetpickup.models.saveCustomerCall.CallHistoryResponse;
import com.udaya.virak_buntham.vetpickup.models.saveCustomerCall.DataItem;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity implements OnInternetConnectionListener, OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.recyclerItem)
    RecyclerView recyclerItem;


    public static String customerReceiveId;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);
        mToolbarTitle.setText(R.string.calltocustomer);
        progressDialog = new ProgressDialog(this);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        processListItem(DeviceID.getDeviceId(ItemDetailActivity.this)
                , RequestParams.getTokenRequestParams(ItemDetailActivity.this)
                , RequestParams.getSignatureRequestParams(ItemDetailActivity.this)
                , UserSession.getUserSession(ItemDetailActivity.this));
    }

    private void processListItem(String device, String token, String signature, String session) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<CallHistoryResponse> call = apiService.callHistory(device, token, signature, session, customerReceiveId);
        call.enqueue(new Callback<CallHistoryResponse>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<CallHistoryResponse> call, @androidx.annotation.NonNull Response<CallHistoryResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            if (response.body().getData().isEmpty()) {
                                tvNoData.setVisibility(View.VISIBLE);
                                recyclerItem.setVisibility(View.GONE);
                            } else {
                                tvNoData.setVisibility(View.GONE);
                                recyclerItem.setVisibility(View.VISIBLE);
                                setupAreaAdapter(response.body().getData());
                            }

                        }
                    }
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<CallHistoryResponse> call, @androidx.annotation.NonNull Throwable t) {
                Log.i("requestReport", Objects.requireNonNull(t.getMessage()));
                processListItem(DeviceID.getDeviceId(ItemDetailActivity.this)
                        , RequestParams.getTokenRequestParams(ItemDetailActivity.this)
                        , RequestParams.getSignatureRequestParams(ItemDetailActivity.this)
                        , UserSession.getUserSession(ItemDetailActivity.this));
                progressDialog.hide();
            }
        });
    }

    private void setupAreaAdapter(List<DataItem> customerDataItems) {
        CallToCustomerHistoryAdapter callToCustomerAdapter = new CallToCustomerHistoryAdapter(customerDataItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerItem.setLayoutManager(layoutManager);
        recyclerItem.setAdapter(callToCustomerAdapter);
        callToCustomerAdapter.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }

    @Override
    public void itemClick(int position) {

    }
}