package com.udaya.virak_buntham.vetpickup.activities.goodsLocal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.MembershipScannerActivity;
import com.udaya.virak_buntham.vetpickup.activities.PrintGTReceiptLocalActivity;
import com.udaya.virak_buntham.vetpickup.activities.SelectionLocalActivity;
import com.udaya.virak_buntham.vetpickup.activities.goodsTransfer.GoodsTransferActivity;
import com.udaya.virak_buntham.vetpickup.base.AppBaseActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.CityPrintActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.LocalPrintActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.LocalPrintOldActivity;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.membership.Membership;
import com.udaya.virak_buntham.vetpickup.models.memmber.MemberRespone;
import com.udaya.virak_buntham.vetpickup.models.print.PrintData;
import com.udaya.virak_buntham.vetpickup.models.savegoodstransfer.GoodsTransferResponse;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.DestinationFromResponse;
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
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoodsTransferLocalActivity extends AppBaseActivity implements View.OnClickListener, OnInternetConnectionListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.button_paid)
    Button buttonPaid;
    @BindView(R.id.button_select_destination_from)
    Button buttonSelectDestinationFrom;
    @BindView(R.id.button_select_destination_to)
    Button buttonSelectDestinationTo;
    @BindView(R.id.edt_sender_telephone)
    EditText edtSenderPhone;
    @BindView(R.id.edt_receiver_telephone)
    EditText edtReceiverPhone;
    @BindView(R.id.edt_address)
    EditText edtReceiverAddress;
    @BindView(R.id.edt_item_value)
    EditText edtItemValue;
    @BindView(R.id.button_select_currency)
    Button buttonSelectCurrency;
    @BindView(R.id.button_select_item_type)
    Button buttonSelectItemType;
    @BindView(R.id.edt_item_name)
    EditText edtItemName;
    @BindView(R.id.edt_item_qty)
    EditText edtItemQty;
    @BindView(R.id.button_select_uom)
    Button buttonSelectUOM;
    @BindView(R.id.button_select_customer)
    Button buttonSelectCustomer;
    @BindView(R.id.edt_api_reference)
    EditText edtApiReference;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.tvTitle_Customer)
    TextView tvTitleCustomer;

    @BindView(R.id.button_select_memberShip)
    Button buttonSelectMemberShip;
    @BindView(R.id.tvTitle_memberShip)
    TextView tvTitleMemberShip;

    @BindView(R.id.radioGenaral)
    RadioButton radioGeneral;
    @BindView(R.id.radioCustomer)
    RadioButton radioCustomer;
    @BindView(R.id.radioVipCard)
    RadioButton radioVipCard;
    @BindView(R.id.tvCustomer)
    TextView tvCustomer;
    @BindView(R.id.autoCompleteTextViewLocal)
    AutoCompleteTextView actvLocal;
    @BindView(R.id.llLayoutLocal)
    LinearLayout llLayoutLocal;

    RadioButton radioButton;
    //Alert dialog
    private Button buttonSelectAreaDeliver;


    private SelectionData selectionDestinationTo;
    private SelectionData selectionItemType;
    private SelectionData selectionUOM;
    private SelectionData selectCustomer;

    //Get data from dialog;
    private String totalAmount;
    Button dialogButtonCurrency;

    private ProgressDialog progressDialog;

    //Get current location
    GoogleApiClient mGoogleApiClient;
    Location currentLocation;
    LocationRequest mLocationRequest;

    private EditText edtMembership;
    private EditText edtMemberPercentage;
    private String checkCollect = "0";
    private EditText edtFee;
    private TextView edtTotalAmount;
    Button dialogBtnPaid, dialogBtnUnpaid;
    String memberShipId = "0";

    //get Printer Name
    String getName = "";
    double getLatitude, getLongitude;

    public static String branch = "";
    public static String branchId = "";

    public static String defaultSymbol = "";
    public static String defaultSymbolId = "1";
    public static String defaultItemValueSymbol = "$";

    private int customerType = 1;
    public static int customerId = 0;
    private int vipCustomerId;


    public static String referenceLocal = "";
    public static String customerLocalName = "";
    public static String customerLocalTel = "";
    public static String receiverLocalTel = "";
    public static String customerLocalId = "";
    public static int isDelivery = 0;

    public AlertDialog alertDialogRefer;
    private Button btnCancel, btnDelivery, btnDeliveryCustomer;
    String[] numberList ={"0","1","22","33","123","554","899","1111"};
    ArrayList<String> arrayListNumberLocal = new ArrayList<>();
    ArrayList<String> arrayListNameLocal = new ArrayList<>();

    List<SelectionData> selectionDataCustomer;
    int isFreeDelivery;

    private GpsTrackerLocation gpsTracker;

    public void getLocationData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gpsTracker = new GpsTrackerLocation(GoodsTransferLocalActivity.this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (gpsTracker.canGetLocation()) {
                double letData = gpsTracker.getLatitude();
                double longData = gpsTracker.getLongitude();
                AppConfig.setGetLatitude(letData);
                AppConfig.setGetLongitude(longData);
            //    Toast.makeText(getBaseContext(), "==>" + AppConfig.getGetLatitude() + "==" + AppConfig.getGetLongitude(), Toast.LENGTH_SHORT).show();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_transfer_local);
        ButterKnife.bind(this);
        radioGroup.check(radioGroup.getChildAt(0).getId());
        closeKeyboard();
        mToolbarTitle.setText("ព័ត៌មានបញ្ញើអីវ៉ាន់ថ្មី (តំបន់)");
        getLatitude = AppConfig.getGetLatitude();
        getLongitude = AppConfig.getGetLongitude();
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        Log.d("reference==>", "" + referenceLocal);
        Log.d("customer_id==>", "" + customerLocalId);
        Log.d("customer_name==>", "" + customerLocalName);
        Log.d("customer_telephone==>", "" + customerLocalTel);

        if (branch.isEmpty() && branchId.isEmpty()) {
            buttonSelectDestinationFrom.setEnabled(true);
            buttonSelectDestinationFrom.setText(getResources().getString(R.string.please_select));
            buttonSelectDestinationTo.setEnabled(false);
        } else {
            buttonSelectDestinationFrom.setText(branch);
            buttonSelectDestinationTo.setEnabled(true);
            buttonSelectDestinationFrom.setEnabled(false);
        }

        registerOnClick(this);
        progressDialog = new ProgressDialog(this);
        requestCustomer(DeviceID.getDeviceId(this)
                , RequestParams.getTokenRequestParams(this)
                , RequestParams.getSignatureRequestParams(this)
                , UserSession.getUserSession(this), ""
        );

        //Initial google api for get lats longs
        buildGoogleApiClient(this);
        setDataFromLocal();
    }

    private void setDataFromLocal() {
        Log.e("", "setDataFromLocal: "+  customerLocalTel);
        edtSenderPhone.setText(customerLocalTel);
        actvLocal.setText(customerLocalTel);
        edtReceiverPhone.setText(receiverLocalTel);
        edtSenderPhone.setText("sjdhfjhdgfhjdfhjdf");
        if (!customerLocalId.equals("")) {
            radioGroup.check(radioGroup.getChildAt(1).getId());
            customerType = 2;
            radioGeneral.setEnabled(false);
            radioVipCard.setEnabled(false);
            edtSenderPhone.setEnabled(false);
            buttonSelectMemberShip.setVisibility(View.VISIBLE);
            buttonSelectMemberShip.setEnabled(false);
            tvTitleCustomer.setVisibility(View.VISIBLE);
            buttonSelectMemberShip.setText(customerLocalName);
            customerId = Integer.parseInt(customerLocalId);
        }
    }

    private synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void registerOnClick(View.OnClickListener clickListener) {
        buttonPaid.setOnClickListener(clickListener);

        buttonSelectDestinationFrom.setOnClickListener(clickListener);
        buttonSelectDestinationTo.setOnClickListener(clickListener);
        buttonSelectCurrency.setOnClickListener(clickListener);
        buttonSelectItemType.setOnClickListener(clickListener);
        buttonSelectUOM.setOnClickListener(clickListener);
        buttonSelectCustomer.setOnClickListener(clickListener);
        buttonSelectMemberShip.setOnClickListener(clickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {//                onBackPressed();
            customerLocalId = "";
            referenceLocal = "";
            startActivity(new Intent(this, HomeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        customerLocalId = "";
        referenceLocal = "";
        startActivity(new Intent(this, HomeActivity.class));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLocationData();
        if (requestCode == Constants.REQUEST_DESTINATION_FROM_CODE && resultCode == RESULT_OK && data != null) {
            //Get selection
            SelectionData selectionDestinationFrom = data.getParcelableExtra(Constants.REQUEST_DESTINATION_FROM_KEY);
            assert selectionDestinationFrom != null;
            buttonSelectDestinationFrom.setText(selectionDestinationFrom.getName());
            branchId = selectionDestinationFrom.getId();
            defaultSymbol = selectionDestinationFrom.getSymbol();
            buttonSelectDestinationTo.setEnabled(true);
        }
        if (requestCode == Constants.REQUEST_DESTINATION_TO_CODE && resultCode == RESULT_OK && data != null) {
            selectionDestinationTo = data.getParcelableExtra(Constants.REQUEST_DESTINATION_TO_KEY);
            assert selectionDestinationTo != null;
            buttonSelectDestinationTo.setText(selectionDestinationTo.getName());
        }
        if (requestCode == Constants.REQUEST_CURRENCY_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionCurrency = data.getParcelableExtra(Constants.REQUEST_CURRENCY_KEY);
//            buttonSelectCurrency.setText(selectionCurrency.getName());
            assert selectionCurrency != null;
            dialogButtonCurrency.setText(Objects.requireNonNull(selectionCurrency).getName());
            defaultSymbolId = selectionCurrency.getId();
            defaultItemValueSymbol = selectionCurrency.getName();
        }

        if (requestCode == Constants.REQUEST_ITEM_TYPE_CODE && resultCode == RESULT_OK && data != null) {
            selectionItemType = data.getParcelableExtra(Constants.REQUEST_ITEM_TYPE_KEY);
            assert selectionItemType != null;
            buttonSelectItemType.setText(selectionItemType.getName());
        }

        if (requestCode == Constants.REQUEST_UOM_CODE && resultCode == RESULT_OK && data != null) {
            selectionUOM = data.getParcelableExtra(Constants.REQUEST_UOM_KEY);
            assert selectionUOM != null;
            buttonSelectUOM.setText(selectionUOM.getName());
        }

        if (requestCode == Constants.REQUEST_AREA_DELIVER_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionDeliverArea = data.getParcelableExtra(Constants.REQUEST_AREA_DELIVER_KEY);
            assert selectionDeliverArea != null;
            buttonSelectAreaDeliver.setText(selectionDeliverArea.getName());
        }

        if (requestCode == Constants.REQUEST_CUSTOMER_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionCustomer = data.getParcelableExtra(Constants.REQUEST_CUSTOMER_KEY);
            assert selectionCustomer != null;
            buttonSelectCustomer.setText(selectionCustomer.getName());
            edtSenderPhone.setText(selectionCustomer.getTel());
            edtSenderPhone.setEnabled(false);
            edtSenderPhone.setHintTextColor(getResources().getColor(R.color.colorBlack));
            if (selectionCustomer.getId().equals("0")) {
                edtSenderPhone.setEnabled(true);
            }
            edtSenderPhone.setText(selectionCustomer.getTel());
            edtSenderPhone.setEnabled(false);
            customerId = Integer.parseInt(selectionCustomer.getId());
        }

        if (requestCode == Constants.MEMBERSHIP_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Membership membership = data.getParcelableExtra(Constants.MEMBERSHIP_KEY);
            assert membership != null;
            requestMember(DeviceID.getDeviceId(getBaseContext())
                    , RequestParams.getTokenRequestParams(getBaseContext())
                    , RequestParams.getSignatureRequestParams(getBaseContext())
                    , UserSession.getUserSession(getBaseContext())
                    , membership.getMembershipCode(),
                    actvLocal.getText().toString().trim()
            );
        }

        if (requestCode == Constants.REQUEST_MEMBER_SHIP_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionMemberShip = data.getParcelableExtra(Constants.REQUEST_MEMBER_SHIP_KEY);
            assert selectionMemberShip != null;
            buttonSelectMemberShip.setText(selectionMemberShip.getName());
            edtSenderPhone.setText(selectionMemberShip.getTel());
            edtSenderPhone.setEnabled(false);
            vipCustomerId = Integer.parseInt(selectionMemberShip.getId());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_paid:
                checkUserInput();
                break;

            case R.id.button_select_destination_from:
                gotoRequestDestinationFrom();
                break;

            case R.id.button_select_destination_to:
                gotoRequestDestinationTo();
                break;

            case R.id.button_select_currency:
                gotoRequestCurrency();
                break;

            case R.id.button_select_item_type:
                gotoRequestItemType();
                break;

            case R.id.button_select_uom:
                gotoRequestUOM();
                break;
            case R.id.button_select_customer:
                gotoRequestCustomer();
                break;

            case R.id.button_select_memberShip:
                gotoRequestMemberShip();
                break;


        }
    }

    private void alertPaid() {

        if(tvCustomer.getText().equals("General")){
            GoodsTransferActivity.price = 6000.00;
        }else {
            GoodsTransferActivity.price = 4000.00;
        }

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_dialog_local, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        TextView transferFeeSymbol = dialogView.findViewById(R.id.transfer_fee_symbol);
        transferFeeSymbol.setText(defaultSymbol);
        dialogButtonCurrency = dialogView.findViewById(R.id.button_select_currency);
        dialogBtnUnpaid = dialogView.findViewById(R.id.button_unpaid);
        edtMembership = dialogView.findViewById(R.id.goods_transfer_membership_local);
        edtMemberPercentage = dialogView.findViewById(R.id.goods_transfer_percentage_local);
        edtFee = dialogView.findViewById(R.id.goods_transfer_fee);
        final TextView edtItemValue = dialogView.findViewById(R.id.edt_item_value);
        ImageView buttonScanMembership = dialogView.findViewById(R.id.button_scan_membership);
        buttonScanMembership.setOnClickListener(v -> gotoScanMembership());

        if(HomeActivity.addGoodsNew == 1){
            edtFee.setEnabled(false);
            edtFee.setText("0");
        }

        dialogBtnUnpaid.setOnClickListener(view -> {
            getChoosePrinter();
            int transferFeeTarget = 0;
            try {
                transferFeeTarget = Integer.parseInt(edtFee.getText().toString().trim());
            } catch (Exception e) {
                Log.d("error==>",""+e.getMessage());
            }
            if (edtFee.getText().toString().isEmpty() || edtItemValue.getText().toString().isEmpty()) {
                Toast.makeText(GoodsTransferLocalActivity.this, getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
            } else if (transferFeeTarget < GoodsTransferActivity.price || edtFee.getText().toString().isEmpty()) {
                AlertDialogUtil.dialogMoneyAlert(GoodsTransferLocalActivity.this);
            } else {
                if (referenceLocal.equals("")) {
                    dialogBtnUnpaid.setClickable(false);
                    dialogBtnUnpaid.setText(getResources().getString(R.string.loadings));
                    dialogBtnPaid.setClickable(false);
                    saveGoodsTransferLocale(
                            DeviceID.getDeviceId(GoodsTransferLocalActivity.this),
                            RequestParams.getTokenRequestParams(GoodsTransferLocalActivity.this),
                            RequestParams.getSignatureRequestParams(GoodsTransferLocalActivity.this),
                            UserSession.getUserSession(GoodsTransferLocalActivity.this),
                            branchId,
                            selectionDestinationTo.getId(),
                            actvLocal.getText().toString(),
                            edtReceiverPhone.getText().toString(),
                            edtItemValue.getText().toString(),
                            "1",
                            selectionItemType.getId(),
                            edtItemName.getText().toString(),
                            edtItemQty.getText().toString(),
                            selectionUOM.getId(),
                            edtFee.getText().toString(),
                            String.valueOf(AppConfig.getGetLatitude()),
                            String.valueOf(AppConfig.getGetLongitude()),
                            "" + checkCollect,
                            0,
                            edtReceiverAddress.getText().toString(),
                            referenceLocal,
                             /*customerType */0,
                           /* customerId*/0,
                           /* vipCustomerId*/0,
                            isDelivery
                    );
                } else {

                    checkReferLocal();
                    btnDelivery.setOnClickListener(v -> {
                        btnDelivery.setClickable(false);
                        btnDelivery.setText(getResources().getString(R.string.loadings));
                        btnDeliveryCustomer.setClickable(false);
                        btnCancel.setClickable(false);
                        saveGoodsTransferLocale(
                                DeviceID.getDeviceId(GoodsTransferLocalActivity.this),
                                RequestParams.getTokenRequestParams(GoodsTransferLocalActivity.this),
                                RequestParams.getSignatureRequestParams(GoodsTransferLocalActivity.this),
                                UserSession.getUserSession(GoodsTransferLocalActivity.this),
                                branchId,
                                selectionDestinationTo.getId(),
                                actvLocal.getText().toString(),
                                edtReceiverPhone.getText().toString(),
                                edtItemValue.getText().toString(),
                                "1",
                                selectionItemType.getId(),
                                edtItemName.getText().toString(),
                                edtItemQty.getText().toString(),
                                selectionUOM.getId(),
                                edtFee.getText().toString(),
                                String.valueOf(getLatitude),
                                String.valueOf(getLongitude),
                                "" + checkCollect,
                                0,
                                edtReceiverAddress.getText().toString(),
                                referenceLocal,
                                /*customerType */0,
                                /* customerId*/0,
                                /* vipCustomerId*/0,
                                0
                        );
                    });
                    btnDeliveryCustomer.setOnClickListener(v -> {
                        btnDeliveryCustomer.setClickable(false);
                        btnDeliveryCustomer.setText(getResources().getString(R.string.loadings));
                        btnDelivery.setClickable(false);
                        btnCancel.setClickable(false);
                        saveGoodsTransferLocale(
                                DeviceID.getDeviceId(GoodsTransferLocalActivity.this),
                                RequestParams.getTokenRequestParams(GoodsTransferLocalActivity.this),
                                RequestParams.getSignatureRequestParams(GoodsTransferLocalActivity.this),
                                UserSession.getUserSession(GoodsTransferLocalActivity.this),
                                branchId,
                                selectionDestinationTo.getId(),
                                actvLocal.getText().toString(),
                                edtReceiverPhone.getText().toString(),
                                edtItemValue.getText().toString(),
                                "1",
                                selectionItemType.getId(),
                                edtItemName.getText().toString(),
                                edtItemQty.getText().toString(),
                                selectionUOM.getId(),
                                edtFee.getText().toString(),
                                String.valueOf(getLatitude),
                                String.valueOf(getLongitude),
                                "" + checkCollect,
                                0,
                                edtReceiverAddress.getText().toString(),
                                referenceLocal,
                                /*customerType */0,
                                /* customerId*/0,
                                /* vipCustomerId*/0,
                                1
                        );
                    });
                }
            }
        });
        CheckBox chk = dialogView.findViewById(R.id.checkCollect);
        chk.setOnClickListener(v -> {
            boolean checked = ((CheckBox) v).isChecked();
            if (checked) {
                checkCollect = "1";
            } else {
                checkCollect = "0";
            }
        });
        dialogBtnPaid = dialogView.findViewById(R.id.button_paid);
        dialogBtnPaid.setOnClickListener(view -> {
            int transferFeeTarget = 0;
            try {
                transferFeeTarget = Integer.parseInt(edtFee.getText().toString().trim());
            } catch (Exception e) {
                Log.d("error==>",""+e.getMessage());
            }

            if (edtFee.getText().toString().isEmpty() || edtItemValue.getText().toString().isEmpty()) {
                Toast.makeText(GoodsTransferLocalActivity.this, getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
            } else if (transferFeeTarget < GoodsTransferActivity.price || edtFee.getText().toString().isEmpty()) {
                AlertDialogUtil.dialogMoneyAlert(GoodsTransferLocalActivity.this);
            } else {
                if (referenceLocal.equals("")) {
                    dialogBtnPaid.setClickable(false);
                    dialogBtnPaid.setText(getResources().getString(R.string.loadings));
                    dialogBtnUnpaid.setClickable(false);
                    getChoosePrinter();
                    saveGoodsTransferLocale(
                            DeviceID.getDeviceId(GoodsTransferLocalActivity.this),
                            RequestParams.getTokenRequestParams(GoodsTransferLocalActivity.this),
                            RequestParams.getSignatureRequestParams(GoodsTransferLocalActivity.this),
                            UserSession.getUserSession(GoodsTransferLocalActivity.this),
                            branchId,
                            selectionDestinationTo.getId(),
                            actvLocal.getText().toString(),
                            edtReceiverPhone.getText().toString(),
                            edtItemValue.getText().toString(),
                            "1",
                            selectionItemType.getId(),
                            edtItemName.getText().toString(),
                            edtItemQty.getText().toString(),
                            selectionUOM.getId(),
                            edtFee.getText().toString(),
                            String.valueOf(getLatitude),
                            String.valueOf(getLongitude),
                            checkCollect,
                            1,
                            edtReceiverAddress.getText().toString(),
                            referenceLocal,
                            /*customerType */0,
                            /* customerId*/0,
                            /* vipCustomerId*/0,
                            isDelivery
                    );
                } else {
                    checkReferLocal();
                    btnDelivery.setOnClickListener(v -> {
                        btnDelivery.setClickable(false);
                        btnDelivery.setText(getResources().getString(R.string.loadings));
                        btnDeliveryCustomer.setClickable(false);
                        btnCancel.setClickable(false);
                        saveGoodsTransferLocale(
                                DeviceID.getDeviceId(GoodsTransferLocalActivity.this),
                                RequestParams.getTokenRequestParams(GoodsTransferLocalActivity.this),
                                RequestParams.getSignatureRequestParams(GoodsTransferLocalActivity.this),
                                UserSession.getUserSession(GoodsTransferLocalActivity.this),
                                branchId,
                                selectionDestinationTo.getId(),
                                actvLocal.getText().toString(),
                                edtReceiverPhone.getText().toString(),
                                edtItemValue.getText().toString(),
                                "1",
                                selectionItemType.getId(),
                                edtItemName.getText().toString(),
                                edtItemQty.getText().toString(),
                                selectionUOM.getId(),
                                edtFee.getText().toString(),
                                String.valueOf(getLatitude),
                                String.valueOf(getLongitude),
                                "" + checkCollect,
                                0,
                                edtReceiverAddress.getText().toString(),
                                referenceLocal,
                                /*customerType */0,
                                /* customerId*/0,
                                /* vipCustomerId*/0,
                                0
                        );
                    });
                    btnDeliveryCustomer.setOnClickListener(v -> {
                        btnDeliveryCustomer.setClickable(false);
                        btnDeliveryCustomer.setText(getResources().getString(R.string.loadings));
                        btnDelivery.setClickable(false);
                        btnCancel.setClickable(false);
                        saveGoodsTransferLocale(
                                DeviceID.getDeviceId(GoodsTransferLocalActivity.this),
                                RequestParams.getTokenRequestParams(GoodsTransferLocalActivity.this),
                                RequestParams.getSignatureRequestParams(GoodsTransferLocalActivity.this),
                                UserSession.getUserSession(GoodsTransferLocalActivity.this),
                                branchId,
                                selectionDestinationTo.getId(),
                                actvLocal.getText().toString(),
                                edtReceiverPhone.getText().toString(),
                                edtItemValue.getText().toString(),
                                "1",
                                selectionItemType.getId(),
                                edtItemName.getText().toString(),
                                edtItemQty.getText().toString(),
                                selectionUOM.getId(),
                                edtFee.getText().toString(),
                                String.valueOf(getLatitude),
                                String.valueOf(getLongitude),
                                "" + checkCollect,
                                0,
                                edtReceiverAddress.getText().toString(),
                                referenceLocal,
                               /* customerType*/ 0 ,
                                /*customerId*/0,
                                /*vipCustomerId*/0,
                                1
                        );

                    });
                }

            }
        });
        dialogButtonCurrency.setOnClickListener(view -> gotoRequestCurrency());
        edtTotalAmount = dialogView.findViewById(R.id.goods_transfer_total_amount);
        edtFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    s = Constants.DEFAULT_VALUE;
                }
                String discountAmount = "0";
                try {
                    discountAmount = String.valueOf(getDiscount(Double.parseDouble(edtFee.getText().toString().trim()), Double.parseDouble(edtMemberPercentage.getText().toString())));
                    edtMembership.setText(discountAmount);
                } catch (Exception e) {
                    edtMembership.setText("0");
                }
                edtMembership.setText(discountAmount);
                String totalAmount = String.valueOf(getTotalAmount(Double.parseDouble(s.toString()), Double.parseDouble(edtMembership.getText().toString())));
                edtTotalAmount.setText(totalAmount);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        alertDialog.show();
    }

    private void gotoScanMembership() {
        Intent intent = new Intent(this, MembershipScannerActivity.class);
        startActivityForResult(intent, Constants.MEMBERSHIP_REQUEST_CODE);
    }

    private void gotoPrintReceipt(PrintData printData, Class className) {
        Intent intent = new Intent(this, className);
        intent.putExtra("printData", printData);
        startActivity(intent);
        progressDialog.cancel();
    }

    private void gotoRequestDestinationFrom() {
        Intent intent = new Intent(this, SelectionLocalActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_DESTINATION_FROM_CODE);
        startActivityForResult(intent, Constants.REQUEST_DESTINATION_FROM_CODE);
    }

    private void gotoRequestDestinationTo() {
        Intent intent = new Intent(this, SelectionLocalActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_DESTINATION_TO_CODE);
        intent.putExtra("destination_from_id", branchId);
        startActivityForResult(intent, Constants.REQUEST_DESTINATION_TO_CODE);
    }

    private void gotoRequestCurrency() {
        Intent intent = new Intent(this, SelectionLocalActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_CURRENCY_CODE);
        startActivityForResult(intent, Constants.REQUEST_CURRENCY_CODE);
    }

    private void gotoRequestItemType() {
        Intent intent = new Intent(this, SelectionLocalActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_ITEM_TYPE_CODE);
        startActivityForResult(intent, Constants.REQUEST_ITEM_TYPE_CODE);
    }

    private void gotoRequestUOM() {
        Intent intent = new Intent(this, SelectionLocalActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_UOM_CODE);
        startActivityForResult(intent, Constants.REQUEST_UOM_CODE);
    }

    private void gotoRequestCustomer() {
        Intent intent = new Intent(this, SelectionLocalActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_CUSTOMER_CODE);
        startActivityForResult(intent, Constants.REQUEST_CUSTOMER_CODE);
    }

    private void gotoRequestMemberShip() {
        Intent intent = new Intent(this, SelectionLocalActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_MEMBER_SHIP_CODE);
        startActivityForResult(intent, Constants.REQUEST_MEMBER_SHIP_CODE);
    }


    private void gotoRequestAreaToDeliver() {
        Intent intent = new Intent(this, SelectionLocalActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_AREA_DELIVER_CODE);
        startActivityForResult(intent, Constants.REQUEST_AREA_DELIVER_CODE);
    }

    private void checkUserInput() {
        if (branchId == null
                || selectionDestinationTo == null
                || actvLocal.getText().toString().isEmpty()
                || edtReceiverPhone.getText().toString().isEmpty()
                || selectionItemType == null
                || edtItemQty.getText().toString().isEmpty()
                || selectionUOM == null) {
            AlertDialogUtil.alertMessageInput(this, getResources().getString(R.string.please_fill));
        }
//        else if (customerType == 0) {
//            AlertDialogUtil.alertMessageInput(this, "សូមជ្រើសរើសសាខាប្រភេទអតិថិជន");
//        }
        else {
            alertPaid();
        }
    }


    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }

    private double getDiscount(double price, double percentage) {
        if (price > 0) {
            return (price * percentage) / 100;
        } else {
            return 0;
        }

    }

    private double getTotalAmount(double fullPrice, double discount) {
        if (fullPrice > 0) {
            return fullPrice - discount;
        } else {
            return 0;
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    protected void onResume() {
        super.onResume();
        getLocationData();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.hide();
    }

    private void saveGoodsTransferLocale(String device,
                                         String token,
                                         String signature,
                                         String session,
                                         String destinationFrom,
                                         String destinationTo,
                                         String sender,
                                         String receiver,
                                         String itemValue,
                                         String itemCurrency,
                                         String itemType,
                                         String itemName,
                                         String itemQTY,
                                         String uom,
                                         final String transferFee,
                                         String longs,
                                         String lats,
                                         String collectCod,
                                         int paid,
                                         String address,
                                         String reference,
                                         int customerType,
                                         int customerID,
                                         int vipCusId,
                                         int isDelivery
    ) {
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<GoodsTransferResponse> call = apiService.saveGoodsTransferLocal(device, token, signature, session, destinationFrom, destinationTo, sender, receiver, itemValue, itemCurrency, itemType, itemName, itemQTY, uom, transferFee, longs, lats, collectCod, paid, address, reference, isDelivery);
        call.enqueue(new Callback<GoodsTransferResponse>() {
            @Override
            public void onResponse(@NonNull Call<GoodsTransferResponse> call, @NonNull Response<GoodsTransferResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.i("requestTransfer", response.body().getInfo());
                    if (response.body() != null) {
                        if (response.body().getStatus().equals(Constants.STATUS_SUCCESS)) {
                            RequestParams.persistRequestParams(GoodsTransferLocalActivity.this, response.body().getToken(), response.body().getSignature());
                            PrintData printData = new PrintData();
                            printData.setBranchName(response.body().getBranchFromName());
                            printData.setDestinatioFrom(response.body().getDestination_from());
                            printData.setTransferCode(response.body().getTransferCode());
                            printData.setItemValue(response.body().getItemValue());
                            printData.setTransferDate(response.body().getDateInvoice());
                            printData.setItemType(selectionItemType.getName());
                            printData.setItemQTY(edtItemQty.getText().toString());
                            printData.setItemUOM(selectionUOM.getName());
                            printData.setItemValueSymbol(defaultItemValueSymbol);
                            printData.setNormalSymbol(defaultSymbol);
                            printData.setSender(actvLocal.getText().toString());
                            printData.setReceiver(edtReceiverPhone.getText().toString());
                            printData.setDestinationTo(response.body().getDestinationTo());
                            printData.setTransferFee(response.body().getTransferFee());
                            printData.setDeliveryFee(response.body().getDeliveryFee());
                            printData.setDiscount(response.body().getDiscount());
                            printData.setTotalAmount(response.body().getTotalAmount());
                            printData.setBranchFromTel(response.body().getBranchFromTel());
                            printData.setBranchToName(response.body().getBranchToName());
                            printData.setBranchToTel(response.body().getBranchToTel());
                            printData.setItemCode(response.body().getItemCode());
                            printData.setQrCode(response.body().getQrCode());
                            printData.setPrintDate(response.body().getDatePrint());
                            printData.setPaid(response.body().getPaid());
                            printData.setCollectCod(response.body().getCollectCod());
                            printData.setItemName(response.body().getItem_name());
                            printData.setPoint(response.body().getPoint());
                            printData.setCustomerName(response.body().getCustomerName());
                            printData.setDestinationToCode(response.body().getDestinationToCode());
                            printData.setLocation_type(response.body().getLocation_type());
                            customerLocalId = "";
                            referenceLocal = "";
                            getChoosePrinter();

                            SharedPreferences pref1 = getSharedPreferences("BluetoothVersion", MODE_PRIVATE);
                            String getVersion = pref1.getString("Version", "");

                            if (getName.equals("BlueTooth")) {
                                if(getVersion.equals("New")){
                                    gotoPrintReceipt(printData, LocalPrintActivity.class);
                                }else {
                                    gotoPrintReceipt(printData, LocalPrintOldActivity.class);
                                }
                            } else if(getName.equals("printer_wire")) {
                                gotoPrintReceipt(printData, CityPrintActivity.class);
                            }else {
                                gotoPrintReceipt(printData, PrintGTReceiptLocalActivity.class);
                            }
                            Log.d("codeDataCollect==>", "" + response.body().getCollectCod());
                        } else {
                            AlertDialogUtil.alertMessageError(GoodsTransferLocalActivity.this, response.body().getInfo());
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GoodsTransferResponse> call, @NonNull Throwable t) {
                Log.i("saveGoodsTransfer",""+t.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void requestMember(String device, String token, String signature, String session, String code, String senderTel) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<MemberRespone> call = apiService.checkMemember(device, token, signature, session, code, senderTel);
        call.enqueue(new Callback<MemberRespone>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<MemberRespone> call, Response<MemberRespone> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals(Constants.STATUS_SUCCESS)) {
                            RequestParams.persistRequestParams(getBaseContext()
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            String Percent = response.body().getPercent();
                            edtMemberPercentage.setText("" + Percent);
                            String discountAmount = String.valueOf(getDiscount(Double.parseDouble(edtFee.getText().toString().trim()), Double.parseDouble(edtMemberPercentage.getText().toString())));
                            edtMembership.setText(discountAmount);
                            String totalAmount = String.valueOf(getTotalAmount(Double.parseDouble(edtFee.getText().toString().trim()), Double.parseDouble(edtMembership.getText().toString())));
                            edtTotalAmount.setText(totalAmount);
                            memberShipId = response.body().getMembership_id();
                            Log.d("idMemberShip==>", response.body().getMembership_id());
                        } else {
                            showDialog();
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<MemberRespone> call, Throwable t) {
                progressDialog.hide();
            }
        });

    }

    private void showDialog() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        alertDialog.setTitleText(getResources().getString(R.string.message));
        alertDialog.setContentText(getResources().getString(R.string.invalid_membership));
        alertDialog.setConfirmText(getResources().getString(R.string.ok));
        alertDialog.setConfirmClickListener(sDialog -> alertDialog.dismissWithAnimation())
                .show();
        alertDialog.setCanceledOnTouchOutside(true);
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void getChoosePrinter() {
        SharedPreferences prefs = getSharedPreferences("PrinterName", MODE_PRIVATE);
        getName = prefs.getString("Name", "");
        System.out.println("namePrinter==>" + getName);
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        if (radioButton.getText().equals("ដៃគូសហការណ៍")) {
            buttonSelectCustomer.setVisibility(View.VISIBLE);
            tvTitleCustomer.setVisibility(View.VISIBLE);
            tvTitleMemberShip.setVisibility(View.GONE);
            buttonSelectMemberShip.setVisibility(View.GONE);

            edtSenderPhone.setText("");
            edtSenderPhone.setEnabled(true);
            buttonSelectMemberShip.setText(getResources().getString(R.string.please_select));
            customerType = 2;
            vipCustomerId = 0;
        } else if (radioButton.getText().equals("ទូទៅ")) {
            tvTitleCustomer.setVisibility(View.GONE);
            buttonSelectCustomer.setVisibility(View.GONE);
            tvTitleMemberShip.setVisibility(View.GONE);
            buttonSelectMemberShip.setVisibility(View.GONE);
            edtSenderPhone.setText("");
            edtSenderPhone.setEnabled(true);
            buttonSelectCustomer.setText(getResources().getString(R.string.please_select));
            buttonSelectMemberShip.setText(getResources().getString(R.string.please_select));
            customerType = 1;


        } else if (radioButton.getText().equals("កាតសមាជិក")) {
            tvTitleMemberShip.setVisibility(View.VISIBLE);
            buttonSelectMemberShip.setVisibility(View.VISIBLE);
            tvTitleCustomer.setVisibility(View.GONE);
            buttonSelectCustomer.setVisibility(View.GONE);
            customerId = 0;
            edtSenderPhone.setText("");
            edtSenderPhone.setEnabled(true);
            buttonSelectCustomer.setText(getResources().getString(R.string.please_select));
            customerType = 3;
        }
    }

    private void checkReferLocal() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_refer, null);
        dialogBuilder.setView(dialogView);
        alertDialogRefer = dialogBuilder.create();
        btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnDelivery = dialogView.findViewById(R.id.btn_delivery);
        btnDeliveryCustomer = dialogView.findViewById(R.id.btn_delivery_customer);
        btnCancel.setOnClickListener(v -> alertDialogRefer.hide());
        alertDialogRefer.show();
    }

    private void listSenderTel(ArrayList<String> ar) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_dropdown_item_1line, ar);

        actvLocal.setThreshold(1);
        actvLocal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCustomer.setText("General");
                isFreeDelivery=0;
                if (s.toString().length()>6){
                    actvLocal.setOnItemClickListener((parent, view, position, id) -> {
                        isFreeDelivery = 1;
                        String name = parent.getAdapter().getItem(position).toString().substring(14);
                        StringBuffer sb = new StringBuffer(name);
                        sb.deleteCharAt(sb.length() - 1);
                        tvCustomer.setText(sb);
                    });
                    actvLocal.setAdapter(adapter);
                    actvLocal.setTextColor(Color.GRAY);
                }else {
                    actvLocal.setAdapter(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        llLayoutLocal.setOnClickListener(v -> {
            if (isFreeDelivery != 1) {
                for (int i = 0; i < selectionDataCustomer.size(); i++) {
                    if (selectionDataCustomer.get(i).getTel().equals(actvLocal.getText().toString().trim())) {
                        tvCustomer.setText(selectionDataCustomer.get(i).getName());
                        isFreeDelivery = 1;
                        break;
                    }else {
                        isFreeDelivery=0;
                    }
                }
            }
        });

        edtReceiverPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (isFreeDelivery != 1) {
                    for (int i = 0; i < selectionDataCustomer.size(); i++) {
                        if (selectionDataCustomer.get(i).getTel().equals(actvLocal.getText().toString().trim())) {
                            tvCustomer.setText(selectionDataCustomer.get(i).getName());
                            isFreeDelivery = 1;
                            break;
                        }else {
                            isFreeDelivery=0;
                        }
                    }
                }
            }
        });
//        actvLocal.setOnItemClickListener((parent, view, position, id) -> {
//            String name = parent.getAdapter().getItem(position).toString().substring(13);
//            StringBuffer sb= new StringBuffer(name);
//            sb.deleteCharAt(sb.length()-1);
//            tvCustomer.setText(sb);
//        });
//        actvLocal.setAdapter(adapter);
//        actvLocal.setTextColor(Color.GRAY);
    }
    private void requestCustomer(String device, String token, String signature, String
            session, String searchText) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        } else {
            progressDialog.show();
        }

        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<DestinationFromResponse> call = apiService.getCustomer(device, token, signature, session, searchText);
        call.enqueue(new Callback<DestinationFromResponse>() {
            @Override
            public void onResponse(@NonNull Call<DestinationFromResponse> call, @NonNull Response<DestinationFromResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() == 0) {
                                //Replace request params
                                RequestParams.persistRequestParams(GoodsTransferLocalActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                            } else {
                                selectionDataCustomer = response.body().getData();
                                selectCustomer = new SelectionData();
                                for (int i = 0; i < response.body().getData().size() - 1; i++) {
                                    arrayListNumberLocal.add(response.body().getData().get(i).getTel() + "   ( " + response.body().getData().get(i).getName() + ")");
                                    arrayListNameLocal.add(response.body().getData().get(i).getName());
                                    selectCustomer.setTel(response.body().getData().get(i).getTel());
                                }
                                listSenderTel(arrayListNumberLocal);
                                //Replace request params
                                RequestParams.persistRequestParams(GoodsTransferLocalActivity.this
                                        , Objects.requireNonNull(response.body()).getToken()
                                        , Objects.requireNonNull(response.body()).getSignature());
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(GoodsTransferLocalActivity.this, response.body().getInfo());
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<DestinationFromResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", Objects.requireNonNull(t.getMessage()));
                progressDialog.hide();
            }
        });
    }
}


