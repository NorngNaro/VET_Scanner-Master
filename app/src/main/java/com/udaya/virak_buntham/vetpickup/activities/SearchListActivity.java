package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.adapters.SearchListAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.ListSearch.SearchData;
import com.udaya.virak_buntham.vetpickup.models.ListSearch.SearchListData;
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

public class SearchListActivity extends AppCompatActivity implements OnItemClickListener, OnInternetConnectionListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.recyclerSearchlist)
    RecyclerView recyclerSearchList;

    private List<SearchListData> searchDataList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        ButterKnife.bind(this);
        mToolbarTitle.setText(getResources().getString(R.string.customer_receive_list));
        progressDialog = new ProgressDialog(this);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        try {
            Bundle extras = getIntent().getExtras();
            String senderValue = extras.getString("SenderNumber");
            requestSearchList(DeviceID.getDeviceId(this)
                    , RequestParams.getTokenRequestParams(this)
                    , RequestParams.getSignatureRequestParams(this)
                    , UserSession.getUserSession(this), senderValue
            );
        } catch (Exception ignored) {

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupAreaAdapter(List<SearchListData> selectionList) {
        SearchListAdapter searchListAdapter = new SearchListAdapter(selectionList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerSearchList.setLayoutManager(layoutManager);
        recyclerSearchList.setAdapter(searchListAdapter);
    }


    private void requestSearchList(String device, String token, String signature, String session, String code) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<SearchData> call = apiService.searchList(device, token, signature, session, code);
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