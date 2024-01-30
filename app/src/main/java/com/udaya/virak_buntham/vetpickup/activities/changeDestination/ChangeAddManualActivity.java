package com.udaya.virak_buntham.vetpickup.activities.changeDestination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.goodsTransfer.GoodsTransferActivity;
import com.udaya.virak_buntham.vetpickup.adapters.ChangeAdapter;
import com.udaya.virak_buntham.vetpickup.adapters.CheckAddManualAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemClickListener;
import com.udaya.virak_buntham.vetpickup.models.change.ChangeAddResponse;
import com.udaya.virak_buntham.vetpickup.models.change.ChangeResponse;
import com.udaya.virak_buntham.vetpickup.models.change.DataItem;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeAddManualActivity extends AppCompatActivity implements OnInternetConnectionListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    private ProgressDialog progressDialog;
    @BindView(R.id.recyclerData)
    RecyclerView recyclerData;
    @BindView(R.id.tvNoData)
    TextView tvNoData;

    public static String condition;
    private int back = 0;

    public static String brandId, telephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_add_manaul);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        mToolbarTitle.setText("Change Add Manual");

        Log.e("", "onCreate: ");

        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        switch (condition) {
            case "return":
            case "returnHQ":
                changeAddManual(DeviceID.getDeviceId(getBaseContext()),
                        RequestParams.getTokenRequestParams(getBaseContext()),
                        RequestParams.getSignatureRequestParams(getBaseContext()),
                        UserSession.getUserSession(getBaseContext()),
                        telephone,
                        brandId,
                        "2");
                break;
            case "changeBranch":
                changeAddManual(DeviceID.getDeviceId(getBaseContext()),
                        RequestParams.getTokenRequestParams(getBaseContext()),
                        RequestParams.getSignatureRequestParams(getBaseContext()),
                        UserSession.getUserSession(getBaseContext()),
                        telephone,
                        brandId,
                        "3");
                break;
            case "changeDestination":
                changeAddManual(DeviceID.getDeviceId(getBaseContext()),
                        RequestParams.getTokenRequestParams(getBaseContext()),
                        RequestParams.getSignatureRequestParams(getBaseContext()),
                        UserSession.getUserSession(getBaseContext()),
                        telephone,
                        brandId,
                        "1");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (back == 0) {
            switch (condition) {
                case "return":
                    ReturnActivity.code = "";
                    break;
                case "returnHQ":
                    ReturnToCampusActivity.code = "";
                    break;
                case "changeBranch":
                    ChangeBranchActivity.code = "";
                    break;
                case "changeDestination":
                    ChangeDestinationActivity.code = "";
                    break;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeAddManual(String device, String token, String signature, String session, String telephone, String brandId, String type) {
        progressDialog.setMessage("កំពុងដំណើរការ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ChangeAddResponse> call = apiService.changeAddManaul(device, token, signature, session, telephone, brandId, type);
        call.enqueue(new Callback<ChangeAddResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChangeAddResponse> call, @NonNull Response<ChangeAddResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            //Replace token
                            RequestParams.persistRequestParams(ChangeAddManualActivity.this
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            if (response.body().getData().isEmpty()) {
                                tvNoData.setVisibility(View.VISIBLE);
                                recyclerData.setVisibility(View.GONE);
                            } else {
                                setupAreaAdapter(response.body().getData());
                                tvNoData.setVisibility(View.GONE);
                                recyclerData.setVisibility(View.VISIBLE);
                            }
                        } else {
                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerData.setVisibility(View.GONE);
                        }
                    }

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ChangeAddResponse> call, @NonNull Throwable t) {

                Log.e("", "onFailure: "+ t );

                progressDialog.dismiss();
                Log.d("errorDataItem==>",""+t.toString());

                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ChangeAddManualActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitleText("ព័ត៌មាន");
                    sweetAlertDialog.setContentText("សូមព្យាយាមម្ដងទៀត");

                    sweetAlertDialog.setCancelButton(getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            switch (condition) {
                                //   case "return":
                                case "returnHQ":
                                    changeAddManual(DeviceID.getDeviceId(getBaseContext()),
                                            RequestParams.getTokenRequestParams(getBaseContext()),
                                            RequestParams.getSignatureRequestParams(getBaseContext()),
                                            UserSession.getUserSession(getBaseContext()),
                                            telephone,
                                            brandId,
                                            "2");
                                    break;
                                case "changeBranch":
                                    changeAddManual(DeviceID.getDeviceId(getBaseContext()),
                                            RequestParams.getTokenRequestParams(getBaseContext()),
                                            RequestParams.getSignatureRequestParams(getBaseContext()),
                                            UserSession.getUserSession(getBaseContext()),
                                            telephone,
                                            brandId,
                                            "3");
                                    break;
                                case "changeDestination":
                                    changeAddManual(DeviceID.getDeviceId(getBaseContext()),
                                            RequestParams.getTokenRequestParams(getBaseContext()),
                                            RequestParams.getSignatureRequestParams(getBaseContext()),
                                            UserSession.getUserSession(getBaseContext()),
                                            telephone,
                                            brandId,
                                            "1");
                                    break;
                            }
                        }
                    }).show();

                Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
                btn.setBackgroundColor(Color.parseColor("#f48539"));
                btn.setVisibility(View.GONE);


            }
        });
    }

    private void setupAreaAdapter(List<DataItem> itemData) {
        CheckAddManualAdapter checkAddManualAdapter = new CheckAddManualAdapter(itemData);
        checkAddManualAdapter.setOnItemClickListener(position -> {
            switch (condition) {
                case "return":
                    back = 1;
                    ReturnActivity.code = itemData.get(position).getScanCode();
                    onBackPressed();
                    break;
                case "returnHQ":
                    back = 1;
                    ReturnToCampusActivity.code = itemData.get(position).getScanCode();
                    onBackPressed();
                    break;
                case "changeBranch":
                    back = 1;
                    ChangeBranchActivity.code = itemData.get(position).getScanCode();
                    onBackPressed();
                    break;
                case "changeDestination":
                    back = 1;
                    ChangeDestinationActivity.code = itemData.get(position).getScanCode();
                    onBackPressed();
                    break;
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerData.setLayoutManager(layoutManager);
        recyclerData.setAdapter(checkAddManualAdapter);
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }
}