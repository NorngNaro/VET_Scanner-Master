package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.adapters.SearchListByBranchAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.ListSearch.SearchData;
import com.udaya.virak_buntham.vetpickup.models.ListSearch.SearchListData;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.ScanQrRespone;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AppConfig;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchListByBranchActivity extends AppCompatActivity implements OnItemClickListener, OnInternetConnectionListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    private ProgressDialog progressDialog;
    @BindView(R.id.recyclerSearchlistByBranch)
    RecyclerView recyclerSearchListByBranch;
    private List<SearchListData> searchDataList;

    String codeReceived = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list_by_branch);
        ButterKnife.bind(this);
        mToolbarTitle.setText(getResources().getString(R.string.customer_receive_list));
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        progressDialog = new ProgressDialog(this);
        try {
            Bundle extras = getIntent().getExtras();
            assert extras != null;
            String senderValue = extras.getString("receivedNumber");
            requestSearchList(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this), senderValue
            );
        } catch (Exception e) {
            Log.d("error==>", "" + e);
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
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }

    @Override
    public void itemClick(int position) {

    }

    private void setupAreaAdapter(List<SearchListData> selectionList) {
        SearchListByBranchAdapter searchListAdapter = new SearchListByBranchAdapter(selectionList);
        searchListAdapter.setItemClickListener(getOnItemClickListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerSearchListByBranch.setLayoutManager(layoutManager);
        recyclerSearchListByBranch.setAdapter(searchListAdapter);
    }

    private OnItemClickListener getOnItemClickListener() {
        return position -> {
            codeReceived = searchDataList.get(position).getSeries_code();
            final SweetAlertDialog alertDialog = new SweetAlertDialog(getBaseContext(), SweetAlertDialog.ERROR_TYPE);
            alertDialog.setTitleText(getResources().getString(R.string.message));
            alertDialog.setContentText("Do you Want to Receive");
            alertDialog.setConfirmText(getResources().getString(R.string.ok));
            alertDialog.setCancelClickListener(sweetAlertDialog -> alertDialog.dismissWithAnimation());
            alertDialog.setConfirmClickListener(sDialog -> getCustomerReceive(DeviceID.getDeviceId(getBaseContext())
                    , RequestParams.getTokenRequestParams(getBaseContext())
                    , RequestParams.getSignatureRequestParams(getBaseContext())
                    , UserSession.getUserSession(getBaseContext()), codeReceived)).show();
            alertDialog.setCanceledOnTouchOutside(false);
            Button btn = alertDialog.findViewById(R.id.confirm_button);
            btn.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));

        };
    }

    private void requestSearchList(String device, String token, String signature, String session, String code) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<SearchData> call = apiService.searchListByBranch(device, token, signature, session, code);
        call.enqueue(new Callback<SearchData>() {
            @Override
            public void onResponse(@NonNull Call<SearchData> call, @NonNull Response<SearchData> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        RequestParams.persistRequestParams(getBaseContext()
                                , response.body().getToken()
                                , response.body().getSignature());
                    }

                    if (!response.body().getStatus().equals("0")) {
                        searchDataList = response.body().getData();
                        setupAreaAdapter(searchDataList);
                    }

                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<SearchData> call, @NonNull Throwable t) {
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
//                    if (response.body().getStatus() != null) {
                    assert response.body() != null;
                    showDialog(response.body().getStatus());
//                    }
                    if (response.body().getStatus().equals("0")) {
                        showDialog();
                    } else {
                        RequestParams.persistRequestParams(getBaseContext()
                                , response.body().getToken()
                                , response.body().getSignature());
                    }
                } else {
                    Toast.makeText(SearchListByBranchActivity.this, "incorrect_code", Toast.LENGTH_SHORT).show();
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

    private void showDialog() {
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

    private void showDialog(String status) {

        SweetAlertDialog alertDialog = new SweetAlertDialog(this);
        alertDialog.setTitleText(getResources().getString(R.string.message));

        if (status.equals("0")) {
            alertDialog.setContentText(getResources().getString(R.string.data_could_not_save));
            alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        } else {
            alertDialog.setContentText(getResources().getString(R.string.data_has_been_save));
            alertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        }

        alertDialog.setConfirmText(getResources().getString(R.string.close));
        alertDialog.setConfirmClickListener(sDialog -> startActivity(new Intent(getBaseContext(), HomeActivity.class))).show();
        alertDialog.setCanceledOnTouchOutside(true);
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

    }


}