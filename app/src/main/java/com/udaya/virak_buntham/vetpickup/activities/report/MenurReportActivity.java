package com.udaya.virak_buntham.vetpickup.activities.report;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.permission.PermissionRespone;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenurReportActivity extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.button_report1)
    Button btnReportOne;
    @BindView(R.id.Btn_ItemReceived_By_Agency)
    Button btnItemReceivedByAgency;
    @BindView(R.id.Btn_Agency_Commission)
    Button btnAgencyCommission;
    @BindView(R.id.btn_report_menu4)
    Button btnMenu4;
    @BindView(R.id.btn_report_out_for_delivery)
    Button btnReportForDelivery;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.btn4)
    Button btn4;
    @BindView(R.id.btnCheckGoods)
    Button btnCheckGoods;
    int accessPermission = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menur_report);
        ButterKnife.bind(this);
        Toast.makeText(this, "here item", Toast.LENGTH_SHORT).show();
        mToolbarTitle.setText("របាយការណ៍ you can ");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        registerOnClick(this);
        checkerPermission(DeviceID.getDeviceId(this), RequestParams.getTokenRequestParams(this), RequestParams.getSignatureRequestParams(this), UserSession.getUserSession(this));

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
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void registerOnClick(View.OnClickListener clickListener) {
        btnReportOne.setOnClickListener(clickListener);
        btnItemReceivedByAgency.setOnClickListener(clickListener);
        btnAgencyCommission.setOnClickListener(clickListener);
        btnMenu4.setOnClickListener(clickListener);
        btnReportForDelivery.setOnClickListener(clickListener);
        btn1.setOnClickListener(clickListener);
        btn2.setOnClickListener(clickListener);
        btn3.setOnClickListener(clickListener);
        btn4.setOnClickListener(clickListener);
        btnCheckGoods.setOnClickListener(clickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_report1:
                goToReportOne(" របាយការណ៍ផ្ទេរទៅឡាន(ភ្នាក់ងារ)", "1");
                break;
            case R.id.Btn_ItemReceived_By_Agency:
                goToReportOne("របាយការណ៍ទទួលអីវ៉ាន់ (ភ្នាក់ងារ)", "2");
                break;
            case R.id.Btn_Agency_Commission:
                goToReportOne("របាយការណ៍កំរៃជើងសារ (ភ្នាក់ងារ)", "3");
                break;
            case R.id.btn_report_menu4:
                goToReportOne("របាយការណ៍ផ្ញើអីវ៉ាន់ (ភ្នាក់ងារ)", "4");
                break;
            case R.id.btn_report_out_for_delivery:
                goToReportOne("របាយការណ៍ដឹកដល់ផ្ទះ", "5");
                break;
            case R.id.btn1:
                goToReportOne("របាយការណ៍ផ្ញើអីវ៉ាន់ (Franchies)", "6");
                break;
            case R.id.btn2:
                goToReportOne("របាយការណ៍ផ្ទេរទៅឡាន (Franchies)", "7");
                break;
            case R.id.btn3:
                goToReportOne("របាយការណ៍ទទួលអីវ៉ាន់ (Franchies)", "8");
                break;
            case R.id.btn4:
                goToReportOne("របាយការណ៍កំរៃជើងសារ (Franchies)", "9");
                break;
            case R.id.btnCheckGoods:
                goToReportOne("របាយការណ៍ត្រួតពិនិត្រអីវ៉ាន់ចូល", "10");
                break;

        }
    }

    private void goToReportOne(String name, String id) {
        Intent intent = new Intent(this, ReportViewActivity.class);
        intent.putExtra("TitleName", name);
        intent.putExtra("Device", DeviceID.getDeviceId(this));
        intent.putExtra("session", UserSession.getUserSession(this));
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void checkerPermission(String device, String token, String signature, String session) {
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<PermissionRespone> call = apiService.getPermission(device, token, signature, session);
        call.enqueue(new Callback<PermissionRespone>() {
            @Override
            public void onResponse(@NonNull Call<PermissionRespone> call, @NonNull Response<PermissionRespone> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            //Replace token
                            RequestParams.persistRequestParams(MenurReportActivity.this
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            int permission = response.body().getUserPermissions().size();
                            Log.d("sizePermission==>", "" + permission);
                            for (int i = 0; i < permission; i++) {
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 7) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        btnReportOne.setVisibility(View.VISIBLE);
                                    } else {
                                        btnReportOne.setVisibility(View.GONE);
                                    }
                                }
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 8) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        btnItemReceivedByAgency.setVisibility(View.VISIBLE);
                                    } else {
                                        btnItemReceivedByAgency.setVisibility(View.GONE);
                                    }
                                }
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 9) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        btnAgencyCommission.setVisibility(View.VISIBLE);
                                    } else {
                                        btnAgencyCommission.setVisibility(View.GONE);
                                    }
                                }
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 12) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        btnReportForDelivery.setVisibility(View.VISIBLE);
                                    } else {
                                        btnReportForDelivery.setVisibility(View.GONE);
                                    }
                                }
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 15) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        btn1.setVisibility(View.VISIBLE);
                                    } else {
                                        btn1.setVisibility(View.GONE);
                                    }
                                }
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 16) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        btn2.setVisibility(View.VISIBLE);
                                    } else {
                                        btn2.setVisibility(View.GONE);
                                    }
                                }
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 17) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        btn3.setVisibility(View.VISIBLE);
                                    } else {
                                        btn3.setVisibility(View.GONE);
                                    }
                                }
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 18) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        btn4.setVisibility(View.VISIBLE);
                                    } else {
                                        btn4.setVisibility(View.GONE);
                                    }
                                }
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 19) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        btnCheckGoods.setVisibility(View.VISIBLE);
                                    } else {
                                        btnCheckGoods.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PermissionRespone> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }
}
