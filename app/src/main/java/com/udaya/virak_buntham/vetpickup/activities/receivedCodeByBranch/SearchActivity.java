package com.udaya.virak_buntham.vetpickup.activities.receivedCodeByBranch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.ScanReceiveActivity;
import com.udaya.virak_buntham.vetpickup.activities.SelectionActivity;
import com.udaya.virak_buntham.vetpickup.activities.movetovan.ReceiveItemActivity;
import com.udaya.virak_buntham.vetpickup.activities.movetovan.ReceiveItemByTelActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.permission.PermissionRespone;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.InputMethod;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.button_select_branch)
    Button ButtonSelectBranch;
    @BindView(R.id.edt_search_code_by_branch)
    EditText edtTel;


    public static String branch = "";
    public static String branchId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        branchId= "";
        mToolbarTitle.setText("អតិថិជនទទួលអីវ៉ាន់(សាខា)");
        registerOnClick(this);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        checkerPermission(DeviceID.getDeviceId(this), RequestParams.getTokenRequestParams(this), RequestParams.getSignatureRequestParams(this), UserSession.getUserSession(this));

        findViewById(R.id.btnSearch).setOnClickListener(v -> {
            if(edtTel.getText().toString().isEmpty()){
                Toast.makeText(this, "សូមបញ្ចូលលេខអ្នកទទួល", Toast.LENGTH_SHORT).show();
            }else {
                InputMethod.hideSoftKeyboard(SearchActivity.this);
                SearchReceivedCodeByBranchActivity.tel = edtTel.getText().toString().trim();
                startActivity(new Intent(this, SearchReceivedCodeByBranchActivity.class));
            }
        });
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
                            RequestParams.persistRequestParams(SearchActivity.this
                                    , response.body().getToken()
                                    , response.body().getSignature());

                            branchId = response.body().getBranchId();
                            branch = response.body().getBranchName();
                            if (branch.isEmpty() && branchId.isEmpty()) {
                                ButtonSelectBranch.setEnabled(true);
                                ButtonSelectBranch.setText(getResources().getString(R.string.please_select));
                            } else {
                                ButtonSelectBranch.setText(branch);
                                ButtonSelectBranch.setEnabled(false);
                            }
                        }

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<PermissionRespone> call, @NonNull Throwable t) {
            }
        });
    }

    private void registerOnClick(View.OnClickListener clickListener) {
        ButtonSelectBranch.setOnClickListener(clickListener);
    }

    private void gotoRequestBrand() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_BRANCH_CODE);
        startActivityForResult(intent, Constants.REQUEST_BRANCH_CODE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, HomeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_select_branch) {
            gotoRequestBrand();
        }
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(this,HomeActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_BRANCH_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionBranch = data.getParcelableExtra(Constants.REQUEST_BRANCH_KEY);
            assert selectionBranch != null;
            ButtonSelectBranch.setText(selectionBranch.getName());
            branchId = selectionBranch.getId();
        }
    }
    public void CameraScan(View view) {
        if(branchId.equals("")){
            Toast.makeText(this, "សូមជ្រើសរើសសាខា", Toast.LENGTH_SHORT).show();
        }else{
            ScanCodeActivity.checkScan = 0;
            ScanCodeActivity.brandId = branchId;
            startActivity(new Intent(this,ScanCodeActivity.class));
        }

    }

    public void MobileScanner(View view) {
        if(branchId.equals("")){
            Toast.makeText(this, "សូមជ្រើសរើសសាខា", Toast.LENGTH_SHORT).show();
        }else{
            ScanCodeActivity.checkScan = 1;
            ScanCodeActivity.brandId = branchId;
            startActivity(new Intent(this,ScanCodeActivity.class));
        }
    }
}