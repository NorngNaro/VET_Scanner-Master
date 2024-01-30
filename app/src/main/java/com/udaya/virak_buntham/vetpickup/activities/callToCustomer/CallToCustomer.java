package com.udaya.virak_buntham.vetpickup.activities.callToCustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.SelectionActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.permission.PermissionRespone;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.GrobleFuntion;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallToCustomer extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    //    @SuppressLint("StaticFieldLeak")
//    @BindView(R.id.btn_select_date)
    @BindView(R.id.button_select_branch)
    Button ButtonSelectBranch;
    @BindView(R.id.btnLocationCall)
    Button btnLocationCall;
    @BindView(R.id.spinnerOperator)
    MaterialSpinner spinnerOperator;
    @BindView(R.id.spinnerStatus)
    MaterialSpinner spinnerStatus;
    @BindView(R.id.spinnerType)
    MaterialSpinner spinnerType;
    @BindView(R.id.edtReceiveTel)
    EditText edtReceiveTel;
    @BindView(R.id.buttonScanQr)
    Button buttonScanQr;


    public static String branch = "";
    public static String branchId = "";
    String mobileOperator, status, type;
    public static Button ButtonDate;
    public static int checkResume = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_to_customer);
        ButterKnife.bind(this);
        mToolbarTitle.setText(R.string.calltocustomer);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        ButtonDate = findViewById(R.id.btn_select_date_call);
        ButtonDate.setOnClickListener(this::showDatePickerFromDialog);// lambda
//        ButtonDate.setOnClickListener(v -> {   showDatePickerFromDialog(v);  });
        GrobleFuntion.setDate(ButtonDate);
        checkerPermission(DeviceID.getDeviceId(this),
                RequestParams.getTokenRequestParams(this),
                RequestParams.getSignatureRequestParams(this),
                UserSession.getUserSession(this));


        if (branch.isEmpty() && branchId.isEmpty()) {
            ButtonSelectBranch.setText(getResources().getString(R.string.please_select));
        } else {
            ButtonSelectBranch.setText(branch);
        }
        registerOnClick(this);
        getMobileOperator();
        getStatus();
        getType();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("SetTextI18n")
    private void setDateTimeField() {
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog StartTime = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            int month = monthOfYear + 1;
            String mon = "";
            if (month > 9) {
                mon = "0" + month;
            }
            Toast.makeText(this, "===>" + month, Toast.LENGTH_SHORT).show();
            ButtonDate.setText("" + year + "-" + mon + "-" + dayOfMonth);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.show();
    }

    //data from picker
    public void showDatePickerFromDialog(View v) {
        DialogFragment newFragment = new DatePickerFromFragment();
        assert getFragmentManager() != null;
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void ScanQrMobileScanner(View view) {
        ScanCallToCustomerActivity.checkScan = 1;
        if (branchId.isEmpty()) {
            Toast.makeText(this, "សូមជ្រើសរើសសាខា", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, ScanCallToCustomerActivity.class).putExtra("brandId", "" + branchId));
        }
    }

    public static class DatePickerFromFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public @NotNull Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(), this, year,
                    month, day);
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        }

        @SuppressLint("SetTextI18n")
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month += 1;
            String mon, days;
            if (month < 10) {
                mon = "0" + month;
            } else {
                mon = "" + month;
            }
            if (day < 10) {
                days = "0" + day;
            } else {
                days = "" + day;
            }
            ButtonDate.setText(year + "-" + mon + "-" + days);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerOnClick(View.OnClickListener clickListener) {
        ButtonSelectBranch.setOnClickListener(clickListener);
        btnLocationCall.setOnClickListener(clickListener);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_select_branch) {
            gotoRequestBrand();
        } else if (v.getId() == R.id.btnLocationCall) {
            gotoRequestLocation();
        }
//        else if (v.getId() == R.id.btn_select_date) {
//            setDateTimeField();
//            showDatePickerFromDialog(v);
//        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        RequestParams.persistBranch(CallToCustomer.this
                , ""
                , "");
        finish();
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void gotoRequestBrand() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_BRANCH_CODE);
        startActivityForResult(intent, Constants.REQUEST_BRANCH_CODE);
    }

    private void gotoRequestLocation() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_LOCATION_CODE);
        intent.putExtra("branchIdLocate", branchId);
        startActivityForResult(intent, Constants.REQUEST_LOCATION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_BRANCH_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionBranch = data.getParcelableExtra(Constants.REQUEST_BRANCH_KEY);
            assert selectionBranch != null;
            ButtonSelectBranch.setText(selectionBranch.getName());
            branchId = selectionBranch.getId();
            RequestParams.persistBranch(CallToCustomer.this
                    , branchId
                    , selectionBranch.getName());
            CallToCustomerDetailActivity.branchId = Integer.parseInt(branchId);
            btnLocationCall.setEnabled(true);
        } else if (requestCode == Constants.REQUEST_LOCATION_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionLocation = data.getParcelableExtra(Constants.REQUEST_LOCATION_KEY);
            assert selectionLocation != null;
            btnLocationCall.setText(selectionLocation.getName());
            CallToCustomerDetailActivity.locationId = Integer.parseInt(selectionLocation.getId());
        }
    }

    public void getMobileOperator() {
        spinnerOperator.setItems("ទាំងអស់", "SMART", "CELLCARD", "METFONE", "OTHER");
        spinnerOperator.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            mobileOperator = "" + position;
            CallToCustomerDetailActivity.mobileOpt = Integer.parseInt(mobileOperator);
        });
    }

    public void getStatus() {
        spinnerStatus.setItems("ទាំងអស់", "មិនទាន់តេ", "តេរួច", "តេមិនលើក", "តេមិនចូល", "ដឹកដល់ផ្ទះ");
        spinnerStatus.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            status = "" + position;
            CallToCustomerDetailActivity.statusCall = Integer.parseInt(status);
        });
    }

    public void getType() {
        spinnerType.setItems("ទាំងអស់", "ធម្មតា", "ទាន់ចិត្ដ", "VIP", "ដឹកដល់ផ្ទះ", "VDEUK");
        spinnerType.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            type = "" + position;
            CallToCustomerDetailActivity.type = Integer.parseInt(type);
        });
    }

    @Override
    public void onInternetAvailable() {
    }

    @Override
    public void onInternetUnavailable() {
    }

    public void goToDetail(View view) {
        CallToCustomerDetailActivity.back = 1;
        CallToCustomerDetailActivity.receiverTel = edtReceiveTel.getText().toString().trim();
        CallToCustomerDetailActivity.date = ButtonDate.getText().toString().trim();
        CallToCustomerDetailActivity.goodsTransferId = "0";
        if (branchId.isEmpty()) {
            Toast.makeText(this, "សូមជ្រើសរើសសាខា", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, CallToCustomerDetailActivity.class));
        }

    }

    public void ScanQr(View view) {
        ScanCallToCustomerActivity.checkScan = 0;
        if (branchId.isEmpty()) {
            Toast.makeText(this, "សូមជ្រើសរើសសាខា", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, ScanCallToCustomerActivity.class).putExtra("brandId", "" + branchId));
        }
    }

    private void checkerPermission(String device, String token, String signature, String session) {
        buttonScanQr.setText(getResources().getString(R.string.loading));
        buttonScanQr.setEnabled(false);
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<PermissionRespone> call = apiService.getPermission(device, token, signature, session);
        call.enqueue(new Callback<PermissionRespone>() {
            @Override
            public void onResponse(@NonNull Call<PermissionRespone> call, @NonNull Response<PermissionRespone> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            //Replace token
                            RequestParams.persistRequestParams(CallToCustomer.this
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            String brandId = response.body().getBranchId();
                            String brandName = response.body().getBranchName();
                            RequestParams.persistBranch(CallToCustomer.this
                                    , brandId
                                    , brandName);
                            if (!RequestParams.getBranchIdRequestParams(getBaseContext()).isEmpty()) {
                                branchId = "" + Integer.parseInt(RequestParams.getBranchIdRequestParams(getBaseContext()));
                                CallToCustomerDetailActivity.branchId = Integer.parseInt(branchId);
                                ButtonSelectBranch.setText(RequestParams.getBranchNameRequestParams(getBaseContext()));
                                btnLocationCall.setEnabled(true);
                            } else {
                                branchId = "";
                                ButtonSelectBranch.setHint(getResources().getString(R.string.please_select));
                            }
                        }
                    }
                }
                buttonScanQr.setText("ស្កែនតាមកាមេរ៉ាទូរស័ព្ទ");
                buttonScanQr.setEnabled(true);
            }

            @Override
            public void onFailure(@NonNull Call<PermissionRespone> call, @NonNull Throwable t) {
            }
        });
    }
}