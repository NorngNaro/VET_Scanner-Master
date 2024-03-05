package com.udaya.virak_buntham.vetpickup.activities.changeDestination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.ScanChangeDestinationActivity;
import com.udaya.virak_buntham.vetpickup.activities.SelectionActivity;
import com.udaya.virak_buntham.vetpickup.adapters.ChangeAdapter;
import com.udaya.virak_buntham.vetpickup.adapters.ItemNotReceiveAdapter;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.listeners.OnItemCheckListener;
import com.udaya.virak_buntham.vetpickup.models.change.ChangeAddResponse;
import com.udaya.virak_buntham.vetpickup.models.change.ChangeResponse;
import com.udaya.virak_buntham.vetpickup.models.change.SaveResponse;
import com.udaya.virak_buntham.vetpickup.models.getItemNotReceive.ItemData;
import com.udaya.virak_buntham.vetpickup.models.permission.PermissionRespone;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.SelectionData;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.InputMethod;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeDestinationActivity extends AppCompatActivity implements View.OnClickListener, OnInternetConnectionListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.button_select_branch)
    Button ButtonSelectBranch;
    @BindView(R.id.button_select_destination_to)
    Button ButtonSelectDestinationTo;
    @BindView(R.id.imgScanQr)
    ImageView imgScanQr;
    @BindView(R.id.edt_code_scan)
    EditText edtCodeScan;
    @BindView(R.id.bottom_save)
    Button btnSave;
    @BindView(R.id.llData)
    LinearLayout llData;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.tvSenderTel)
    TextView tvSenderTel;
    @BindView(R.id.tvReceiverTel)
    TextView tvReceiverTel;
    @SuppressLint("StaticFieldLeak")
    public static TextView tvQty;
    @BindView(R.id.edt_tel)
    EditText edtTel;
    @BindView(R.id.recyclerCode)
    RecyclerView recyclerCode;


    public static String branch = "";
    public static String branchId = "";
    public static String code = "";

    public static String destinationChangeId = "";

    private ProgressDialog progressDialog;

    private int destinationToId;
    private String search;

    public static boolean checkCode = false;

    //process save
    public static String goodsTransferId, customerReceiverId;

    public static ArrayList<String> arrayNum = new ArrayList<>();
    public static StringBuilder numberArray = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_destination);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        mToolbarTitle.setText("ប្តូរទិសដៅ (Change Destination)");
        tvQty = findViewById(R.id.tvQty);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        checkerPermission(DeviceID.getDeviceId(this), RequestParams.getTokenRequestParams(this), RequestParams.getSignatureRequestParams(this), UserSession.getUserSession(this));
        registerOnClick(this);
        findViewById(R.id.imgSearchTel).setOnClickListener(v -> {
            if (branchId.isEmpty()) {
                Toast.makeText(getBaseContext(), "សូមជ្រើសរើសសាខា", Toast.LENGTH_SHORT).show();
            } else {
                if (edtTel.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getBaseContext(), "សូមបញ្ចូលលេខទូរស័ព្ទ", Toast.LENGTH_SHORT).show();
                } else {
                    ChangeAddManualActivity.condition = "changeDestination";
                    ChangeAddManualActivity.brandId = branchId;
                    ChangeAddManualActivity.telephone = edtTel.getText().toString().trim();
                    startActivity(new Intent(getBaseContext(), ChangeAddManualActivity.class));
                }
            }
        });
        findViewById(R.id.imgSearchCode).setOnClickListener(v -> {
            {
                if (branchId.isEmpty()) {
                    Toast.makeText(getBaseContext(), "សូមជ្រើសរើសសាខា", Toast.LENGTH_SHORT).show();
                } else {
                    if (edtCodeScan.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getBaseContext(), "សូមបញ្ចូលលេខកូដអីវ៉ាន់", Toast.LENGTH_SHORT).show();
                    } else {
                        checkChangeCode(DeviceID.getDeviceId(getBaseContext()),
                                RequestParams.getTokenRequestParams(getBaseContext()),
                                RequestParams.getSignatureRequestParams(getBaseContext()),
                                UserSession.getUserSession(getBaseContext()), edtCodeScan.getText().toString().trim());
                    }

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!code.equals("")) {
            if(destinationChangeId.equals("")){
                checkChangeCode(DeviceID.getDeviceId(this),
                        RequestParams.getTokenRequestParams(this),
                        RequestParams.getSignatureRequestParams(this),
                        UserSession.getUserSession(this), code);
            }
        }
    }

    private void registerOnClick(View.OnClickListener clickListener) {
        ButtonSelectBranch.setOnClickListener(clickListener);
        ButtonSelectDestinationTo.setOnClickListener(clickListener);
        imgScanQr.setOnClickListener(clickListener);
        btnSave.setOnClickListener(clickListener);
    }

    private static String convertObjectArrayToString(ArrayList<String> arr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : arr)
            sb.append(obj.toString()).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_select_branch:
                gotoRequestBrand();
                break;
            case R.id.button_select_destination_to:
                gotoRequestLocation();
                break;
            case R.id.bottom_save:
                if (!arrayNum.isEmpty()){
                    if (destinationChangeId.isEmpty()){
                        Toast.makeText(this, "សូមជ្រើសរើសទិសដៅទៅ", Toast.LENGTH_SHORT).show();
                    }else{
                        processSave(DeviceID.getDeviceId(this), RequestParams.getTokenRequestParams(this),
                                RequestParams.getSignatureRequestParams(this),
                                UserSession.getUserSession(this),
                                goodsTransferId,
                                branchId,
                                "" + destinationChangeId,
                                customerReceiverId,
                                convertObjectArrayToString(arrayNum, ",")
                        );
                    }
                }else{
                    Toast.makeText(this, "សូមជ្រើសរើសលេខកូដ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgScanQr:
                if (branchId.isEmpty()) {
                    Toast.makeText(this, "សូមជ្រើសរើសសាខា", Toast.LENGTH_SHORT).show();
                } else {
                    ScanChangeDestinationActivity.condition = "changeDestination";
                    startActivity(new Intent(this, ScanChangeDestinationActivity.class));
                }
                break;
        }
    }


    private void gotoRequestBrand() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_BRANCH_CODE);
        startActivityForResult(intent, Constants.REQUEST_BRANCH_CODE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        code = "";
        destinationChangeId = "";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkerPermission(String device, String token, String signature, String session) {
        progressDialog.setMessage("កំពុងដំណើរការ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<PermissionRespone> call = apiService.getPermission(device, token, signature, session);
        call.enqueue(new Callback<PermissionRespone>() {
            @Override
            public void onResponse(@NonNull Call<PermissionRespone> call, @NonNull Response<PermissionRespone> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            //Replace token
                            RequestParams.persistRequestParams(ChangeDestinationActivity.this
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
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<PermissionRespone> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void checkChangeCode(String device, String token, String signature, String session, String code) {
        progressDialog.setMessage("កំពុងដំណើរការ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ChangeResponse> call = apiService.checkChangeCode(device, token, signature, session, code, branchId, "1");
        call.enqueue(new Callback<ChangeResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ChangeResponse> call, @NonNull Response<ChangeResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            btnSave.setVisibility(View.VISIBLE);
                            //Replace token
                            RequestParams.persistRequestParams(ChangeDestinationActivity.this
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            llData.setVisibility(View.VISIBLE);

                            try {
                                Log.d("size==>", "" + response.body().getItemScan().size());
                            } catch (Exception e) {
                                Log.d("error===>", e.toString());
                            }
                            setupAreaAdapter(response.body().getItemScan(), response.body().getCode());
                            tvCode.setText(response.body().getCode());
                            tvSenderTel.setText(response.body().getSender());
                            tvReceiverTel.setText(response.body().getReceiver());
                            tvQty.setText("" + response.body().getQty());
                            destinationToId = response.body().getDestToId();

                            checkCode = true;
                            goodsTransferId = "" + response.body().getId();
                            customerReceiverId = "" + response.body().getCus_loc_id();
                            ButtonSelectDestinationTo.setText(response.body().getDestTo());
                            destinationChangeId =""+response.body().getDestToId();
                            ButtonSelectDestinationTo.setEnabled(false);
                        } else {
                            checkCode = false;
                            llData.setVisibility(View.GONE);
                            try {
                                InputMethod.hideSoftKeyboard(ChangeDestinationActivity.this);
                            }catch (Exception e){
                                Log.d("error==>",e.toString());
                            }
                            MediaPlayer sound = MediaPlayer.create(ChangeDestinationActivity.this, R.raw.wrong_voice);
                            sound.start();
                            Toast.makeText(ChangeDestinationActivity.this, "" + response.body().getInfo(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ChangeResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void processSave(String device, String token, String signature, String session,
                             String goodTransferId, String branchId, String destinationToId, String customerReceiverId, String num) {
        progressDialog.setMessage("កំពុងដំណើរការ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        btnSave.setText("កំពុងដំណើរការ...");
        btnSave.setEnabled(false);
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<SaveResponse> call = apiService.saveChangeDestination(device, token, signature, session, goodTransferId, branchId, destinationToId, customerReceiverId, num);
        call.enqueue(new Callback<SaveResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<SaveResponse> call, @NonNull Response<SaveResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            //Replace token
                            RequestParams.persistRequestParams(ChangeDestinationActivity.this
                                    , response.body().getToken()
                                    , response.body().getSignature());

                            destinationChangeId = "";
                            code = "";

                            SharedPreferences pref1 = getSharedPreferences("BluetoothVersion", MODE_PRIVATE);
                            String getVersion = pref1.getString("Version", "");

                            if(getVersion.equals("New") || !HomeActivity.printerWire.isEmpty()){
                                PrintLayoutActivity.desFromPint = response.body().getDestinationFrom();
                                PrintLayoutActivity.toPint = response.body().getDestinationTo();
                                PrintLayoutActivity.qrTotalAmountPrint = response.body().getTotal_amount();
                                PrintLayoutActivity.qrItemValuePrint = response.body().getDestinationFrom();
                                PrintLayoutActivity.cODPrintQr = response.body().getCollectCod();
                                Log.e("", "onResponse: "+  response.body().getCollectCod());
                                PrintLayoutActivity.destinationToPrint = response.body().getDestinationTo();
                                PrintLayoutActivity.qrItemCode = response.body().getItemCode();
                                PrintLayoutActivity.itemQty = response.body().getItemQty();
                                PrintLayoutActivity.receiverTel = response.body().getReceiverTel();
                                PrintLayoutActivity.transferCode = response.body().getTransferCode();
                                PrintLayoutActivity.deliveryArea = response.body().getDeliveryArea();
                                PrintLayoutActivity.itemName = response.body().getItemName();
                                PrintLayoutActivity.datePrint = response.body().getDatePrint();
                                PrintLayoutActivity.uom = response.body().getUom();
                                PrintLayoutActivity.desToCode = response.body().getDestToCode();
                                PrintLayoutActivity.condition = "changeDestination";
                                PrintLayoutActivity.location_from = response.body().getLocationFromLocation();
                                startActivity(new Intent(getBaseContext(), PrintLayoutActivity.class));
                            }else {
                                PrintLayoutOldActivity.desFromPint = response.body().getDestinationFrom();
                                PrintLayoutOldActivity.toPint = response.body().getDestinationTo();
                                PrintLayoutOldActivity.qrTotalAmountPrint = response.body().getTotal_amount();
                                PrintLayoutOldActivity.qrItemValuePrint = response.body().getDestinationFrom();
                                PrintLayoutOldActivity.cODPrintQr = response.body().getCollectCod();
                                Log.e("", "onResponse: "+  response.body().getCollectCod());
                                PrintLayoutOldActivity.destinationToPrint = response.body().getDestinationTo();
                                PrintLayoutOldActivity.qrItemCode = response.body().getItemCode();
                                PrintLayoutOldActivity.itemQty = response.body().getItemQty();
                                PrintLayoutOldActivity.receiverTel = response.body().getReceiverTel();
                                PrintLayoutOldActivity.transferCode = response.body().getTransferCode();
                                PrintLayoutOldActivity.deliveryArea = response.body().getDeliveryArea();
                                PrintLayoutOldActivity.itemName = response.body().getItemName();
                                PrintLayoutOldActivity.datePrint = response.body().getDatePrint();
                                PrintLayoutOldActivity.uom = response.body().getUom();
                                PrintLayoutOldActivity.desToCode = response.body().getDestToCode();
                                PrintLayoutOldActivity.condition = "changeDestination";
                                PrintLayoutOldActivity.location_from = response.body().getLocationFromLocation();
                                Log.e("", "onResponse: Mee" + response.body().getLocationFromLocation() );
                                startActivity(new Intent(getBaseContext(), PrintLayoutOldActivity.class));
                            }

                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                            btnSave.setText("រក្សាទុក");
                            btnSave.setEnabled(true);
                            Toast.makeText(ChangeDestinationActivity.this, ""+response.body().getInfo(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SaveResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                btnSave.setText("រក្សាទុក");
                btnSave.setEnabled(true);
            }
        });
    }

    private void gotoRequestLocation() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_CHANGE_DESTINATION_CODE);
        intent.putExtra("branch_id_location", ""+branchId);
        startActivityForResult(intent, Constants.REQUEST_CHANGE_DESTINATION_CODE);
    }


    @SuppressLint("SetTextI18n")
    private void setupAreaAdapter(List<Integer> itemData, String code) {
        arrayNum.clear();
        RecyclerView rc = findViewById(R.id.recyclerCode);
        ChangeAdapter changeAdapter = new ChangeAdapter(itemData, code);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rc.setLayoutManager(layoutManager);
        rc.setAdapter(changeAdapter);
        changeAdapter.setOnItemClickListener((position, checkBox) -> {
            if (checkBox) {
                if (arrayNum.size() == 0) {
                    arrayNum.add(0, "" +itemData.get(position));
                } else {
                    arrayNum.add("" + itemData.get(position));
                }
            } else {
                try {
                    if (arrayNum.size() == 1) {
                        arrayNum.remove(0);
                    } else {
                        for (int i =0 ; i <arrayNum.size();i++){
                            if (arrayNum.get(i).equals("" + itemData.get(position))){
                                arrayNum.remove(i);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("error===>", e.toString());
                }
            }
            Log.d("arrayNumber==>", "" + arrayNum);
            tvQty.setText("" + arrayNum.size());
        });
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_BRANCH_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionBranch = data.getParcelableExtra(Constants.REQUEST_BRANCH_KEY);
            assert selectionBranch != null;
            ButtonSelectBranch.setText(selectionBranch.getName());
            branchId = selectionBranch.getId();
            llData.setVisibility(View.GONE);
            edtTel.setText("");
            code = "";
        } else if (requestCode == Constants.REQUEST_CHANGE_DESTINATION_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectDestination = data.getParcelableExtra(Constants.REQUEST_CHANGE_DESTINATION_KEY);
            assert selectDestination != null;
            ButtonSelectDestinationTo.setText(selectDestination.getName());
            destinationChangeId = selectDestination.getId();
        }

    }
}