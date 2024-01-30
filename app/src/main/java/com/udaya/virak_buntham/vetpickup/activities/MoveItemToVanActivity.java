package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.movetovan.ReceiveItemByTelActivity;
import com.udaya.virak_buntham.vetpickup.fragment.ExampleItem;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.maltiSelectDestination.DestinationMaltiActivity;
import com.udaya.virak_buntham.vetpickup.models.permission.PermissionRespone;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;
import com.udaya.virak_buntham.vetpickup.utils.AppConfig;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.GpsTrackerLocation;
import com.udaya.virak_buntham.vetpickup.utils.GrobleFuntion;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoveItemToVanActivity extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.button_start_Scan)
    TextView ButtonStartScan;
    @BindView(R.id.btn_select_date)
    Button ButtonDate;
    @BindView(R.id.spinnerHour)
    MaterialSpinner spHour;
    @BindView(R.id.spinnerMinute)
    MaterialSpinner spMinute;
    @BindView(R.id.spinnerTo)
    MaterialSpinner spinnerTo;
    @BindView(R.id.button_select_branch)
    Button ButtonSelectBranch;
    @BindView(R.id.button_select_van)
    Button ButtonSelectVan;
    @BindView(R.id.button_select_destination_to)
    Button ButtonSelectDestinationTo;
    @BindView(R.id.checkDestinationTo)
    CheckBox checkDestinationToAll;
    @BindView(R.id.btnMobileScanByTel)
    Button btnMobileScanByTel;


    String date, hour, min, destinationToId, vanId, transferTo;

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    public static ArrayList<String> listIdDestination = new ArrayList<>();
    public static ArrayList<String> listNameDestination = new ArrayList<>();

    public static int destinationChoose = 0;

    public static String branch = "";
    public static String branchId = "";

    private GpsTrackerLocation gpsTracker;
    public void getLocationData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gpsTracker = new GpsTrackerLocation(MoveItemToVanActivity.this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (gpsTracker.canGetLocation()) {
                double letData = gpsTracker.getLatitude();
                double longData = gpsTracker.getLongitude();
                AppConfig.setGetLatitude(letData);
                AppConfig.setGetLongitude(longData);
              //  Toast.makeText(getBaseContext(), "==>" + AppConfig.getGetLatitude() + "==" + AppConfig.getGetLongitude(), Toast.LENGTH_SHORT).show();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_item_to_van);
        ProgressDialog progressDialog = new ProgressDialog(this);
        ButterKnife.bind(this);
        mToolbarTitle.setText("ផ្ទេរអីវ៉ាន់ឡើងឡាន");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        //        if (branch.isEmpty() && branchId.isEmpty()) {
//            ButtonSelectBranch.setEnabled(true);
//            ButtonSelectBranch.setText(getResources().getString(R.string.please_select));
//            ButtonSelectDestinationTo.setEnabled(false);
//        } else {
//            ButtonSelectBranch.setText(branch);
//            ButtonSelectBranch.setEnabled(false);
//            ButtonSelectDestinationTo.setEnabled(true);
//        }
        checkerPermission(DeviceID.getDeviceId(this), RequestParams.getTokenRequestParams(this), RequestParams.getSignatureRequestParams(this), UserSession.getUserSession(this));

        registerOnClick(this);
        GrobleFuntion.setDate(ButtonDate);
        getHour();
        getMin();
        getSpinnerTo();
        date = ButtonDate.getText().toString().trim();
        checkDestinationToAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                listIdDestination.clear();
                listIdDestination.add(0, "0");
                ButtonSelectDestinationTo.setEnabled(false);
                ButtonSelectDestinationTo.setHint(getResources().getString(R.string.all));
            } else {
                ButtonSelectDestinationTo.setEnabled(true);
                ButtonSelectDestinationTo.setHint(getResources().getString(R.string.please_select));
            }
        });
        findViewById(R.id.btnMobileScan).setOnClickListener(v -> {
            ScanMoveItemToVanActivity.scanCondition = 1;
            gotoScanMoveToVan();
        });


        // set current time
        spHour.setText("" + GrobleFuntion.getCurrentHour());
        spMinute.setText("" + GrobleFuntion.getCurrentMins());
        hour = spHour.getText().toString().trim();
        min = spMinute.getText().toString().trim();

    }

    private void registerOnClick(View.OnClickListener clickListener) {
        ButtonStartScan.setOnClickListener(clickListener);
        ButtonSelectVan.setOnClickListener(clickListener);
        ButtonSelectBranch.setOnClickListener(clickListener);
        ButtonSelectDestinationTo.setOnClickListener(clickListener);
        btnMobileScanByTel.setOnClickListener(clickListener);
    }

    public ArrayList<String> getArrayList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        Log.d("dataValueReturn==>", "" + json);
        return gson.fromJson(json, type);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocationData();
//        getArrayList("ArrayDestination");
//        Log.d("nameArrayDestination==>", "" + listIdDestination);

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
        switch (v.getId()) {
            case R.id.button_start_Scan:
                ScanMoveItemToVanActivity.scanCondition = 0;
                gotoScanMoveToVan();
                break;
            case R.id.button_select_van:
                gotoRequestVan();
                break;
            case R.id.button_select_branch:
                gotoRequestBrand();
                break;
            case R.id.button_select_destination_to:
                gotoRequestDestinationTo();
                break;
            case R.id.btnMobileScanByTel:
                checkValueSearchTelephone();
                break;
        }
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }

    private void gotoScanMoveToVan() {
        checkValue();
//        startActivity(new Intent(this, ScanMoveItemToVanActivity.class));

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,HomeActivity.class));
    }

    private void checkValue() {
        if (ButtonSelectBranch.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.plzSelectBranch));
        } else if (ButtonSelectVan.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.plzSelectVan));
        } else if (spHour.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.please_select_hour));
        } else if (spMinute.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.please_select_min));
        } else if (ButtonSelectDestinationTo.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.please_select_destination));
        } else if ((spinnerTo.getText().toString().trim().equals(getResources().getString(R.string.please_select)))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.plz_seleted_tranfer));
        } else {
            StringBuffer sb = new StringBuffer();
            for (String s : listIdDestination) {
                sb.append(s);
                sb.append(",");
            }
            //date format

            String str = sb.toString();
            String destination = "";
            if(str.length() != 0){
                destination = str.substring(0, str.length() - 1);
            }
            String departure = hour + ":" + min + ":00";
            Intent intent = new Intent(this, ScanMoveItemToVanActivity.class);
            intent.putExtra("date", date);
            intent.putExtra("brandId", "" + branchId);
//            intent.putExtra("destinationToId", "" + destinationToId);
            intent.putExtra("destinationToId", "" + destination);
            intent.putExtra("departure", "" + departure);
            intent.putExtra("vanId", "" + vanId);
            intent.putExtra("sysCode", "" + getRandomString());
            intent.putExtra("tranferId", "" + transferTo);
            startActivity(intent);


        }

//        String departure = hour + ":" + min + ":00";
//        Intent intent = new Intent(this, ScanMoveItemToVanActivity.class);
//        intent.putExtra("date", date);
//        intent.putExtra("brandId", "" + 1);
//        intent.putExtra("destinationToId", "" + 1);
//        intent.putExtra("departure", "" + departure);
//        intent.putExtra("vanId", "" + 1);
//        startActivity(intent);

    }

    private void checkValueSearchTelephone() {
        if (ButtonSelectBranch.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.plzSelectBranch));
        } else if (ButtonSelectVan.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.plzSelectVan));
        } else if (spHour.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.please_select_hour));
        } else if (spMinute.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.please_select_min));
        } else if (ButtonSelectDestinationTo.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.please_select_destination));
        } else if ((spinnerTo.getText().toString().trim().equals(getResources().getString(R.string.please_select)))) {
            AlertDialogUtil.dialogCheckValue(this, getResources().getString(R.string.information), getResources().getString(R.string.plz_seleted_tranfer));
        } else {
            StringBuffer sb = new StringBuffer();
            for (String s : listIdDestination) {
                sb.append(s);
                sb.append(",");
            }

            //date format

            // String str = sb.toString();

            String departure = hour + ":" + min + ":00";
            Intent intent = new Intent(this, ReceiveItemByTelActivity.class);
            intent.putExtra("type", "2"); // 2 is move item to van
            intent.putExtra("brandId", "" + branchId);
            intent.putExtra("departure", "" + departure);
            intent.putExtra("vanId", "" + vanId);
            intent.putExtra("tranferId", "" + transferTo); // is "type" in request

            Log.e("", "checkValueSearchTelephone: "+ vanId);
            Log.e("", "checkValueSearchTelephone: "+ departure);
            Log.e("", "checkValueSearchTelephone: "+ transferTo);

            startActivity(intent);


        }
    }

    private static String getRandomString() {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 15; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void gotoRequestVan() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_VAN_CODE);
        startActivityForResult(intent, Constants.REQUEST_VAN_CODE);
    }

    private void gotoRequestBrand() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_BRANCH_CODE);
        startActivityForResult(intent, Constants.REQUEST_BRANCH_CODE);
    }

    private void gotoRequestDestinationTo() {
//        Intent intent = new Intent(this, SelectionActivity.class);

        ButtonSelectDestinationTo.setEnabled(true);
        Intent intent = new Intent(this, DestinationMaltiActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_DESTINATION_BY_BRANCH_CODE);
        intent.putExtra("destination_to_by_branch_id", branchId);
        startActivityForResult(intent, Constants.REQUEST_DESTINATION_BY_BRANCH_CODE);
        listIdDestination.clear();
        listNameDestination.clear();
//        ButtonSelectDestinationTo.setText("");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_VAN_CODE && resultCode == RESULT_OK && data != null) {
            //Get selection
            SelectionData selectionVan = data.getParcelableExtra(Constants.REQUEST_VAN_KEY);
            assert selectionVan != null;
            ButtonSelectVan.setText(selectionVan.getName());
            vanId = selectionVan.getId();
        }
        if (requestCode == Constants.REQUEST_BRANCH_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionBranch = data.getParcelableExtra(Constants.REQUEST_BRANCH_KEY);
            assert selectionBranch != null;
            ButtonSelectBranch.setText(selectionBranch.getName());
            branchId = selectionBranch.getId();
            ButtonSelectDestinationTo.setEnabled(true);
//            if (!branchId.equals("")) {
//                branchId = selectionBranch.getId();
//                ButtonSelectDestinationTo.setEnabled(true);
//            }
        }

        if (requestCode == Constants.REQUEST_DESTINATION_BY_BRANCH_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionDestinationTo = data.getParcelableExtra(Constants.REQUEST_DESTINATION_TO_BY_BRANCH_KEY);
            assert selectionDestinationTo != null;
            ButtonSelectDestinationTo.setText(selectionDestinationTo.getName());
            destinationToId = selectionDestinationTo.getId();
        }
        if (destinationChoose == 1) {
            for (int i = 0; i < listNameDestination.size(); i++) {
                ButtonSelectDestinationTo.append(listNameDestination.get(i) + " ,");
            }
        }
    }

    private void getHour() {
        spHour.setItems("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        spHour.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> hour = item);
    }

    public void getMin() {
        spMinute.setItems("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60");
        spMinute.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> min = item);
    }

    public void getSpinnerTo() {
        spinnerTo.setItems(getResources().getString(R.string.please_select), "ទិសដៅចុងទី", "ឃ្លាំង OCIC", "ឃ្លាំងជ្រោយចង្វា", "ឃ្លាំង60ម៉ែត្រ");
        spinnerTo.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            if (item.trim().equals("ទិសដៅចុងទី")) {
                transferTo = "" + 1;
            } else if (item.trim().equals("ឃ្លាំង OCIC")) {
                transferTo = "" + 2;
            } else if (item.trim().equals("ឃ្លាំងជ្រោយចង្វា")) {
                transferTo = "" + 4;
            } else if (item.trim().equals("ឃ្លាំង60ម៉ែត្រ")) {
                transferTo = "" + 5;
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
                            RequestParams.persistRequestParams(MoveItemToVanActivity.this
                                    , response.body().getToken()
                                    , response.body().getSignature());

                            branchId = response.body().getBranchId();
                            branch = response.body().getBranchName();
                            if (branch.isEmpty() && branchId.isEmpty()) {
                                ButtonSelectBranch.setEnabled(true);
                                ButtonSelectBranch.setText(getResources().getString(R.string.please_select));
                                ButtonSelectDestinationTo.setEnabled(false);
                            } else {
                                ButtonSelectBranch.setText(branch);
                                ButtonSelectBranch.setEnabled(false);
                                ButtonSelectDestinationTo.setEnabled(true);
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

}