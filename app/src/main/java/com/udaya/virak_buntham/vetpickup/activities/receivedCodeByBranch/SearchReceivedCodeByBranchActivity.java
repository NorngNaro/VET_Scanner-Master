package com.udaya.virak_buntham.vetpickup.activities.receivedCodeByBranch;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
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
import com.udaya.virak_buntham.vetpickup.utils.InputMethod;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchReceivedCodeByBranchActivity extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.btn_search_by_branch)
    Button btnSearchByBranch;
    private ProgressDialog progressDialog;
    @BindView(R.id.edt_search_code_by_branch)
    EditText editCodeByBranch;
    @BindView(R.id.recyclerSearchByBranch)
    RecyclerView recyclerByBranchNoData;
    @BindView(R.id.relateByBranchNoData)
    RelativeLayout relateByBranchNoData;

    private List<SearchListData> searchDataList;
    String codeReceived = "";

    public static String tel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_received_code_by_branch);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        mToolbarTitle.setText("អតិថិជនទទួលអីវ៉ាន់(សាខា)");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        searchList();

        requestSearchList(
                DeviceID.getDeviceId(getBaseContext())
                , RequestParams.getTokenRequestParams(getBaseContext())
                , RequestParams.getSignatureRequestParams(getBaseContext())
                , UserSession.getUserSession(getBaseContext())
                , tel);
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
        super.onBackPressed();

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

    private void searchList() {
        btnSearchByBranch.setOnClickListener(v -> {
            if (editCodeByBranch.getText().toString().isEmpty()) {
                invalidCodeMessage();
            } else {
                InputMethod.hideSoftKeyboard(SearchReceivedCodeByBranchActivity.this);
                requestSearchList(
                        DeviceID.getDeviceId(getBaseContext())
                        , RequestParams.getTokenRequestParams(getBaseContext())
                        , RequestParams.getSignatureRequestParams(getBaseContext())
                        , UserSession.getUserSession(getBaseContext())
                        , editCodeByBranch.getText().toString().trim()
                );
            }
        });
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
                        if (response.body().getStatus().equals("0")) {
                            showDialog();
                        } else {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
//                            Intent intent = new Intent(SearchReceivedCodeByBranchActivity.this, SearchListByBranchActivity.class);
//                            intent.putExtra("receivedNumber", editCodeByBranch.getText().toString().trim());
//                            startActivity(intent);
                            searchDataList = response.body().getData();
                            if (response.body().getData().isEmpty()) {
                                relateByBranchNoData.setVisibility(View.VISIBLE);
                            } else {
                                editCodeByBranch.setText("");
                                relateByBranchNoData.setVisibility(View.GONE);
                                setupAreaAdapter(searchDataList);
                            }

                        }
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

    private void showDialog() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.wrong_number));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setConfirmClickListener(sDialog -> {
            alertDialog.dismissWithAnimation();
            onBackPressed();
//            startActivity(new Intent(getBaseContext(), HomeActivity.class));
        }).show();
        alertDialog.setCanceledOnTouchOutside(true);
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void invalidCodeMessage() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.plzinputcode));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setConfirmClickListener(sDialog -> alertDialog.dismissWithAnimation())
                .show();
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void setupAreaAdapter(List<SearchListData> selectionList) {
        SearchListByBranchAdapter searchListAdapter = new SearchListByBranchAdapter(selectionList);
        searchListAdapter.setItemClickListener(getOnItemClickListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerByBranchNoData.setLayoutManager(layoutManager);
        recyclerByBranchNoData.setAdapter(searchListAdapter);
    }

    private OnItemClickListener getOnItemClickListener() {
        return position -> {
//            codeReceived = searchDataList.get(position).getSeries_code();
//             alertReceiving(codeReceived);
            ListReceivedCodeByBranchActivity.id = searchDataList.get(position).getId();
            startActivity(new Intent(this, ListReceivedCodeByBranchActivity.class));

        };
    }

    private void getCustomerReceive(String device, String token, String signature, String session, String code) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ScanQrRespone> call = apiService.getCustomerReceive(device, token, signature, session, code, "" + AppConfig.getGetLongitude(), "" + AppConfig.getGetLatitude());
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
                    Toast.makeText(SearchReceivedCodeByBranchActivity.this, "incorrect_code", Toast.LENGTH_SHORT).show();
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


    private void showDialog(String status) {

        final SweetAlertDialog alertDialog = new SweetAlertDialog(this);
        alertDialog.setTitleText(getResources().getString(R.string.message));

        if (status.equals("0")) {
            alertDialog.setContentText(getResources().getString(R.string.data_could_not_save));
            alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        } else {
            alertDialog.setContentText(getResources().getString(R.string.data_has_been_save));
            alertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        }

        alertDialog.setConfirmText(getResources().getString(R.string.close));
        alertDialog.setConfirmClickListener(sDialog -> {
            alertDialog.dismissWithAnimation();
            startActivity(new Intent(getBaseContext(), HomeActivity.class));
        }).show();

        alertDialog.setCanceledOnTouchOutside(true);
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    //    private void  alertReceiving(final String code) {
////        final SweetAlertDialog alertDialog = new SweetAlertDialog(SearchReceivedCodeByBranchActivity.this, SweetAlertDialog.WARNING_TYPE);
////        alertDialog.setTitleText(getResources().getString(R.string.message));
////        alertDialog.setContentText(getResources().getString(R.string.doyouwanttoreveiced));
////        alertDialog.setConfirmText(getResources().getString(R.string.ok));
//////        alertDialog.showCancelButton(true);
////        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
////            @Override
////            public void onClick(SweetAlertDialog sweetAlertDialog) {
////                alertDialog.dismissWithAnimation();
////            }
////        });
////        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
////            @Override
////            public void onClick(SweetAlertDialog sDialog) {
////                getCustomerReceive(DeviceID.getDeviceId(getBaseContext())
////                        , RequestParams.getTokenRequestParams(getBaseContext())
////                        , RequestParams.getSignatureRequestParams(getBaseContext())
////                        , UserSession.getUserSession(getBaseContext()), code);
////            }
////        }).show();
////        alertDialog.setCanceledOnTouchOutside(false);
////        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
////        btn.setBackgroundColor(ContextCompat.getColor(SearchReceivedCodeByBranchActivity.this, R.color.colorPrimary));
////    }
    private void alertReceiving(final String code) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SearchReceivedCodeByBranchActivity.this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.message));
        sweetAlertDialog.setContentText(getResources().getString(R.string.doyouwanttoreveiced));
        sweetAlertDialog.setConfirmText(getResources().getString(R.string.ok));
        sweetAlertDialog.setConfirmClickListener(sDialog -> {
            sDialog.dismissWithAnimation();
            getCustomerReceive(DeviceID.getDeviceId(getBaseContext())
                    , RequestParams.getTokenRequestParams(getBaseContext())
                    , RequestParams.getSignatureRequestParams(getBaseContext())
                    , UserSession.getUserSession(getBaseContext()), code);
        });
        sweetAlertDialog.setCancelButton(getResources().getString(R.string.cancel), SweetAlertDialog::dismissWithAnimation).show();
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        Button btn = sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(SearchReceivedCodeByBranchActivity.this, R.color.colorPrimary));
    }

    public void CameraScan(View view) {
        ScanCodeActivity.checkScan = 0;
        startActivity(new Intent(this, ScanCodeActivity.class));
    }

    public void MobileScanner(View view) {
        ScanCodeActivity.checkScan = 1;
        startActivity(new Intent(this, ScanCodeActivity.class));
    }
}