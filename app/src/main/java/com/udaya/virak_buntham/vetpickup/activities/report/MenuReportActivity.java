package com.udaya.virak_buntham.vetpickup.activities.report;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuReportActivity extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.button_report1)
    RelativeLayout btnReportOne;
    @BindView(R.id.Btn_ItemReceived_By_Agency)
    RelativeLayout btnItemReceivedByAgency;
    @BindView(R.id.Btn_Agency_Commission)
    RelativeLayout btnAgencyCommission;
    @BindView(R.id.btn_report_menu4)
    RelativeLayout btnMenu4;
    @BindView(R.id.btn_report_out_for_delivery)
    RelativeLayout btnReportForDelivery;
    @BindView(R.id.Btn_ItemReceived_By_franchise)
    RelativeLayout btnItemReceivedByFranchise;
    @BindView(R.id.report_COD)
    RelativeLayout report_COD;


    @BindView(R.id.btn1)
    RelativeLayout btn1;
    @BindView(R.id.btn2)
    RelativeLayout btn2;
    @BindView(R.id.btn3)
    RelativeLayout btn3;
    @BindView(R.id.btn4)
    RelativeLayout btn4;
    @BindView(R.id.btnCheckGoods)
    RelativeLayout btnCheckGoods;
    @BindView(R.id.btnGoodsNotReceive)
    RelativeLayout btnGoodsNotReceive;
    @BindView(R.id.report_return)
    RelativeLayout reportReturn;
    @BindView(R.id.btnFranchiseOld)
    RelativeLayout btnFranchiseOld;

    int accessPermission = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menur_report);
        ButterKnife.bind(this);
        mToolbarTitle.setText("របាយការណ៍");
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
        btnItemReceivedByFranchise.setOnClickListener(clickListener);
        btnGoodsNotReceive.setOnClickListener(clickListener);
        reportReturn.setOnClickListener(clickListener);
        btnFranchiseOld.setOnClickListener(clickListener);
        report_COD.setOnClickListener(clickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_report1:
                goToReportOne("របាយការណ៍ផ្ទេរទៅឡាន(ភ្នាក់ងារ)", "1");
                break;
            case R.id.Btn_ItemReceived_By_Agency:
                goToReportOne("របាយការណ៍ទទួលអីវ៉ាន់ (ភ្នាក់ងារ)", "2");
                break;
            case R.id.Btn_Agency_Commission:
                goToReportOne("របាយការណ៍កំរៃជើងសារ (ភ្នាក់ងារ)", "3");
                break;
            case R.id.btn_report_menu4:
//                goToReportOne("របាយការណ៍ផ្ញើអីវ៉ាន់ (ភ្នាក់ងារ)", "4");
                break;
            case R.id.btn_report_out_for_delivery:
                goToReportOne("របាយការណ៍ដឹកដល់ផ្ទះ", "5");
                break;
            case R.id.btn1:
                goToReportOne("របាយការណ៍ផ្ញើអីវ៉ាន់ (Franchise)", "6");
                break;
            case R.id.btn2:
                goToReportOne("របាយការណ៍ផ្ទេរទៅឡាន (Franchise)", "7");
                break;
            case R.id.btn3:
                goToReportOne("របាយការណ៍ទទួលអីវ៉ាន់ (Franchise)", "8");
                break;
            case R.id.btn4:
                goToReportOne("របាយការណ៍កំរៃជើងសារ (Franchise)", "9");
                break;
            case R.id.btnCheckGoods:
                goToReportOne("របាយការណ៍ត្រួតពិនិត្យអីវ៉ាន់ចូល (Franchise", "10");
                break;
            case R.id.Btn_ItemReceived_By_franchise:
                goToReportOne("របាយការណ៍អតិថិជនទទួលអីវ៉ាន់ (Franchise)", "20");
                break;
            case R.id.btnGoodsNotReceive:
                goToReportOne("របាយការណ៍ទំនិញអតិថិជនមិនទាន់ទទួល", "21");
                break;
            case R.id.report_return:
                goToReportOne("របាយការណ៍អីវ៉ាន់ប្តូរ (Change)", "22");
                break;
            case R.id.btnFranchiseOld:
                goToReportOne("របាយការណ៍កំរៃជើងសារ (Franchise) (Old)", "23");
                break;
            case R.id.report_COD:
                goToReportOne("របាយការណ៍ទទួលអីវ៉ាន់ COD (Franchise)", "24");
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
        LinearLayout llReport = findViewById(R.id.llReport);
        llReport.setVisibility(View.GONE);
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<PermissionRespone> call = apiService.getPermission(device, token, signature, session);
        call.enqueue(new Callback<PermissionRespone>() {
            @Override
            public void onResponse(@NonNull Call<PermissionRespone> call, @NonNull Response<PermissionRespone> response) {
                llReport.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            //Replace token
                            RequestParams.persistRequestParams(MenuReportActivity.this
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
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 20) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        btnItemReceivedByFranchise.setVisibility(View.VISIBLE);
                                    } else {
                                        btnItemReceivedByFranchise.setVisibility(View.GONE);
                                    }
                                }
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 21) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        btnGoodsNotReceive.setVisibility(View.VISIBLE);
                                    } else {
                                        btnGoodsNotReceive.setVisibility(View.GONE);
                                    }
                                }
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 22) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        reportReturn.setVisibility(View.VISIBLE);
                                    } else {
                                        reportReturn.setVisibility(View.GONE);
                                    }
                                }
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 23) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        btnFranchiseOld.setVisibility(View.VISIBLE);
                                    } else {
                                        btnFranchiseOld.setVisibility(View.GONE);
                                    }
                                }
                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 24) {
                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                    if (accessPermission == 1) {
                                        report_COD.setVisibility(View.VISIBLE);
                                    } else {
                                        report_COD.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PermissionRespone> call, @NonNull Throwable t) {
                Log.i("requestInfo", "" + t.getMessage());
                checkerPermission(DeviceID.getDeviceId(getBaseContext()), RequestParams.getTokenRequestParams(getBaseContext()), RequestParams.getSignatureRequestParams(getBaseContext()), UserSession.getUserSession(getBaseContext()));

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
