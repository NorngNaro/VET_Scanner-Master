package com.udaya.virak_buntham.vetpickup.activities.goodsTransfer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.udaya.virak_buntham.vetpickup.activities.PrintGTReceiptActivity;
import com.udaya.virak_buntham.vetpickup.activities.SelectionActivity;
import com.udaya.virak_buntham.vetpickup.activities.SelectionLocalActivity;
import com.udaya.virak_buntham.vetpickup.base.AppBaseActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.CityPrintActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.CityPrintOldActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.LocalPrintActivity;
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
import com.udaya.virak_buntham.vetpickup.utils.InputMethod;
import com.udaya.virak_buntham.vetpickup.utils.NumberFormatter;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoodsTransferActivity extends AppBaseActivity implements View.OnClickListener, OnInternetConnectionListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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

    @BindView(R.id.autoCompleteTextView)
    AutoCompleteTextView autoCompleteTextView;

    @BindView(R.id.llLayout)
    LinearLayout llLayout;


    //Alert dialog
    private Button buttonSelectAreaDeliver;

    private int deliveryArea = 0;

    private SelectionData selectionDestinationTo;
    private SelectionData selectionItemType;
    private SelectionData selectionUOM;
    private SelectionData selectCustomer;

    //Get data from dialog;
    private String totalAmount;

    private ProgressDialog progressDialog;

    //Get current location
    GoogleApiClient mGoogleApiClient;
    Location currentLocation;
    LocationRequest mLocationRequest;

    private EditText edtMembership;
    private EditText edtMemberPercentage;
    private EditText edtFee;
    private TextView edtTotalAmount;
    String memberShipId = "0";
    private String checkCollect = "0";
    private String isTransit = "0";
    String getName = "";

    double getLatitude, getLongitude;

    public static String defaultSymbol = "";
    public static String defaultSymbolId = "1";
    public static String defaultItemValueSymbol = "$";


    public static String branch = "";
    public static String branchId = "";
    private int type = 0;
    private int allowDelivery = 0;
    private EditText txtDeliverFee;


    @BindView(R.id.radioGroups)
    RadioGroup radioGroup;


    @BindView(R.id.button_select_customer)
    Button buttonSelectCustomer;

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

    private int customerType = 1;
    public int customerId;
    private int vipCustomerId;
    public static double price = 4000.0;

    public static String referenceLocal = "";
    public static String customerLocalName = "";
    public static String customerLocalTel = "";
    public static String receiverLocalTel = "";
    public static String customerLocalId = "";
    ArrayList<String> arrayListNumber = new ArrayList<>();
    ArrayList<String> arrayListName = new ArrayList<>();
    TextView tvTitleDelivery;
    int isFreeDelivery;
    List<SelectionData> selectionDataCustomer;
    LinearLayout llNote;
    EditText edtNote;
    Button buttonSave;

    private GpsTrackerLocation gpsTracker;

    public void getLocationData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gpsTracker = new GpsTrackerLocation(GoodsTransferActivity.this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (gpsTracker.canGetLocation()) {
                double letData = gpsTracker.getLatitude();
                double longData = gpsTracker.getLongitude();
                AppConfig.setGetLatitude(letData);
                AppConfig.setGetLongitude(longData);
//                Toast.makeText(getBaseContext(), "==>" + AppConfig.getGetLatitude() + "==" + AppConfig.getGetLongitude(), Toast.LENGTH_SHORT).show();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_transfer);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        radioGroup.check(radioGroup.getChildAt(0).getId());
        getLatitude = AppConfig.getGetLatitude();
        getLongitude = AppConfig.getGetLongitude();
        if(HomeActivity.addGoodsNew == 0){
            mToolbarTitle.setText("បញ្ចូលបញ្ញើអីវ៉ាន់ថ្មី(ខេត្ដ/ក្រុង)");
        } else {
            mToolbarTitle.setText("បញ្ចូលបញ្ញើអីវ៉ាន់ថ្មី(ទាន់ចិត្ត)");
        }

        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        requestCustomer(DeviceID.getDeviceId(this)
                , RequestParams.getTokenRequestParams(this)
                , RequestParams.getSignatureRequestParams(this)
                , UserSession.getUserSession(this), ""
        );
        registerOnClick(this);
        if (branch.isEmpty() && branchId.isEmpty()) {
            buttonSelectDestinationFrom.setEnabled(true);
            buttonSelectDestinationFrom.setText(getResources().getString(R.string.please_select));
            buttonSelectDestinationTo.setEnabled(false);
        } else {
            buttonSelectDestinationFrom.setText(branch);
            buttonSelectDestinationTo.setEnabled(true);
            buttonSelectDestinationFrom.setEnabled(false);
        }

        //Initial google api for get lats longs
        buildGoogleApiClient(this);
        setDataFrom();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocationData();
    }

    private void setDataFrom() {
        edtSenderPhone.setText(customerLocalTel);
        autoCompleteTextView.setText(customerLocalTel);
        edtReceiverPhone.setText(receiverLocalTel);
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
        if (item.getItemId() == android.R.id.home) {
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLocationData();

        if(HomeActivity.addGoodsNew == 1 ){
            isFreeDelivery = 1;
        }

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
            type = selectionDestinationTo.getType();
            allowDelivery = selectionDestinationTo.getAllowDelivery();
        }

        if (requestCode == Constants.REQUEST_CURRENCY_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionCurrency = data.getParcelableExtra(Constants.REQUEST_CURRENCY_KEY);
            assert selectionCurrency != null;
            buttonSelectCurrency.setText(selectionCurrency.getName());

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
            if (buttonSelectAreaDeliver.getText().toString().trim().equals("Other")) {
                llNote.setVisibility(View.VISIBLE);
                edtNote.setText("");
            } else {
                llNote.setVisibility(View.GONE);
            }
            double priceDelivery = Double.parseDouble(edtFee.getText().toString().trim()) * 30 / 100;
            if (isFreeDelivery == 1) {
                txtDeliverFee.setText("0");
            } else {
                if (priceDelivery < 2000) {
                    txtDeliverFee.setText("" + 2000);
                } else {
                    txtDeliverFee.setText("" + priceDelivery);
                }
                txtDeliverFee.setEnabled(false);
            }
            deliveryArea = Integer.parseInt(selectionDeliverArea.getId());
        }

        if (requestCode == Constants.MEMBERSHIP_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Membership membership = data.getParcelableExtra(Constants.MEMBERSHIP_KEY);
            assert membership != null;
            requestMember(DeviceID.getDeviceId(getBaseContext())
                    , RequestParams.getTokenRequestParams(getBaseContext())
                    , RequestParams.getSignatureRequestParams(getBaseContext())
                    , UserSession.getUserSession(getBaseContext())
                    , membership.getMembershipCode()
                    , edtSenderPhone.getText().toString().trim());
        }

        if (requestCode == Constants.REQUEST_MEMBER_SHIP_CODE && resultCode == RESULT_OK && data != null) {
            SelectionData selectionMemberShip = data.getParcelableExtra(Constants.REQUEST_MEMBER_SHIP_KEY);
            assert selectionMemberShip != null;
            buttonSelectMemberShip.setText(selectionMemberShip.getName());
            edtSenderPhone.setText(selectionMemberShip.getTel());
            edtSenderPhone.setEnabled(false);
            vipCustomerId = Integer.parseInt(selectionMemberShip.getId());
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

    @SuppressLint("SetTextI18n")
    private void alertPaid() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_dialog, null);
        dialogBuilder.setView(dialogView);

        TextView transferFeeSymbol = dialogView.findViewById(R.id.transfer_fee_symbol);
        TextView deliverFeeSymbol = dialogView.findViewById(R.id.delivery_fee_symbol);
        TextView totalAmountSymbol = dialogView.findViewById(R.id.total_amount_symbol);



        LinearLayout llCheckBox = dialogView.findViewById(R.id.llCheckBox);
        LinearLayout llDeliveryToHome = dialogView.findViewById(R.id.llDeliveryToHome);
        if (type != 2 && type != 4) {
            llCheckBox.setVisibility(View.VISIBLE);
            if (allowDelivery == 0) {
                llDeliveryToHome.setVisibility(View.GONE);
            } else {
                llDeliveryToHome.setVisibility(View.VISIBLE);
            }
        } else {
            llCheckBox.setVisibility(View.GONE);
            llDeliveryToHome.setVisibility(View.GONE);
        }


        llNote = dialogView.findViewById(R.id.llNote);
        edtNote = dialogView.findViewById(R.id.edtNote);
        transferFeeSymbol.setText(defaultSymbol);
        deliverFeeSymbol.setText(defaultSymbol);
        totalAmountSymbol.setText(defaultSymbol);


        edtFee = dialogView.findViewById(R.id.goods_transfer_fee);
        txtDeliverFee = dialogView.findViewById(R.id.goods_transfer_deliver_fee);
        edtMembership = dialogView.findViewById(R.id.goods_transfer_membership);
        edtMemberPercentage = dialogView.findViewById(R.id.goods_transfer_percentage);
        edtTotalAmount = dialogView.findViewById(R.id.goods_transfer_total_amount);
        buttonSelectAreaDeliver = dialogView.findViewById(R.id.button_select_area_deliver);
        ImageView buttonScanMembership = dialogView.findViewById(R.id.button_scan_membership);

        buttonScanMembership.setOnClickListener(v -> gotoScanMembership());

        if(HomeActivity.addGoodsNew == 1){
            txtDeliverFee.setEnabled(false);
            txtDeliverFee.setVisibility(View.GONE);
            deliverFeeSymbol.setVisibility(View.GONE);
        }

        edtFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    s = Constants.DEFAULT_VALUE;
                    buttonSelectAreaDeliver.setEnabled(false);
                    buttonSelectAreaDeliver.setClickable(false);

                } else {
                    buttonSelectAreaDeliver.setEnabled(true);
                    buttonSelectAreaDeliver.setClickable(true);

                    if(HomeActivity.addGoodsNew == 1){
                        txtDeliverFee.setEnabled(false);
                        txtDeliverFee.setText("0");
                    }
                    double totalAmount = Double.parseDouble(txtDeliverFee.getText().toString().trim()) + Double.parseDouble(s.toString());
                    if(HomeActivity.addGoodsNew == 1){
                        totalAmount = totalAmount * 2;
                    }
                    edtTotalAmount.setText( "" + totalAmount + "0" );

                    Log.e("", "onTextChanged: "+ totalAmount);

                    //set price delivery
                    if (!buttonSelectAreaDeliver.getText().toString().trim().equals(getResources().getString(R.string.please_select))) {
                        double priceDelivery = Double.parseDouble(s.toString().trim()) * 30 / 100;
                        if (priceDelivery < 2000) {
                            txtDeliverFee.setText("" + 2000);
                        } else {
                            txtDeliverFee.setText("" + priceDelivery);
                        }

                    }
                }
//                String discountAmount = "0";
//                try {
//                    discountAmount = String.valueOf(getDiscount(Double.parseDouble(edtFee.getText().toString().trim()), Double.parseDouble(edtMemberPercentage.getText().toString())));
//                    edtMembership.setText(discountAmount);
//                } catch (Exception e) {
//                    edtMembership.setText("0");
//                }
//                edtMembership.setText(discountAmount);
//                String totalAmount = String.valueOf(getTotalAmount(Double.parseDouble(s.toString()), Double.parseDouble(edtMembership.getText().toString())));
//                String totalAmount = String.valueOf(getTotalAmount(Double.parseDouble(s.toString()), Double.parseDouble(txtDeliverFee.getText().toString())));

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtDeliverFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    s = Constants.DEFAULT_VALUE;
                }
                double total = Double.parseDouble(edtFee.getText().toString().trim()) + Double.parseDouble(s.toString());
                if(HomeActivity.addGoodsNew == 1){
                    total = total * 2;
                }
                edtTotalAmount.setText("" + total);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtMemberPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    s = Constants.DEFAULT_VALUE;
                }
                if (edtFee.getText().toString().isEmpty()) {
                    edtFee.setText(Constants.DEFAULT_VALUE);
                }
                String totalAmount = String.valueOf(getTotalAmount(Double.parseDouble(edtFee.getText().toString()), getDiscount(Double.parseDouble(edtFee.getText().toString()), Double.parseDouble(s.toString()))));
                if(HomeActivity.addGoodsNew == 1){
                    totalAmount = String.valueOf((Double.parseDouble(totalAmount) * 2));
                }
                edtTotalAmount.setText(NumberFormatter.separateThousandFormat(totalAmount));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        tvTitleDelivery = dialogView.findViewById(R.id.tvTitleDelivery);
        if (isFreeDelivery == 1) {
            tvTitleDelivery.setText("(Free)");
        } else {
            tvTitleDelivery.setText(" ");
        }

        buttonSelectAreaDeliver.setOnClickListener(v -> gotoRequestAreaToDeliver());
        CheckBox chk = dialogView.findViewById(R.id.checkCollectTranfer);
        chk.setOnClickListener(v -> {
            boolean checked = ((CheckBox) v).isChecked();
            if (checked) {
                checkCollect = "1";
            } else {
                checkCollect = "0";
            }
        });

        CheckBox checkBaseFee = dialogView.findViewById(R.id.checkBaseFee);
        checkBaseFee.setOnClickListener(v -> {
            boolean checked = ((CheckBox) v).isChecked();
            if (checked) {
                isTransit = "1";
                edtFee.setEnabled(false);
                edtTotalAmount.setEnabled(false);
                if (!edtFee.getText().toString().trim().isEmpty()) {
                    double totalAmount = Double.parseDouble("0") + Double.parseDouble(edtFee.getText().toString());
                    double baseFee = totalAmount * 2;
                    if(HomeActivity.addGoodsNew == 1){
                        baseFee = baseFee * 2;
                        edtFee.setText("" + baseFee/2);
                    }else {
                        edtFee.setText("" + baseFee);
                    }
                    edtTotalAmount.setText("" + baseFee);

                }
            } else {
                isTransit = "0";
                edtFee.setEnabled(true);
                edtTotalAmount.setEnabled(true);
                if (!edtFee.getText().toString().trim().isEmpty()) {
                    double totalAmount = Double.parseDouble("0") + Double.parseDouble(edtFee.getText().toString());
                    double baseFee = totalAmount / 2;
                    if(HomeActivity.addGoodsNew == 1){
                        baseFee = baseFee * 2;
                        edtFee.setText("" + baseFee/2);
                    }else {
                        edtFee.setText("" + baseFee);
                    }
                    edtTotalAmount.setText("" + baseFee);
                }
            }
        });
        buttonSave = dialogView.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(v -> {
            if(tvCustomer.getText().equals("General")){
                price = 6000.00;
            }else {
                price = 4000.00;
            }
            double transferFeeTarget;
            Log.d("PriceValue==>", "" + edtFee.getText().toString().trim());
            try {
                transferFeeTarget = Double.parseDouble(edtFee.getText().toString().trim());
            } catch (Exception e) {
                transferFeeTarget = 0;
            }
            if (edtFee.getText().toString().isEmpty() && edtTotalAmount.getText().toString().isEmpty() && buttonSelectAreaDeliver.getText().equals(getResources().getString(R.string.please_select))) {
                AlertDialogUtil.alertMessageInput(GoodsTransferActivity.this, getResources().getString(R.string.please_fill));
            } else if (transferFeeTarget < price || edtFee.getText().toString().trim().isEmpty()) {
                Log.d("price==>", "" + transferFeeTarget);
                AlertDialogUtil.dialogMoneyAlert(GoodsTransferActivity.this);
            } else {
//                String deliverId = "0";
//                if (buttonSelectAreaDeliver.getText().equals(getResources().getString(R.string.please_select))) {
//                    deliverId = "0";
//                } else {
//                    deliverId = selectionDeliverArea.getId();
//                }
                try {
                    InputMethod.hideSoftKeyboard(this);
                    buttonSave.setClickable(false);
                    buttonSave.setText(getResources().getString(R.string.loadings));
                    buttonSave.setClickable(false);
                    getChoosePrinter();
                    Log.d("defaultSymbolId==>", "" + defaultSymbolId);
                    saveGoodsTransfer(
                            DeviceID.getDeviceId(GoodsTransferActivity.this),
                            RequestParams.getTokenRequestParams(GoodsTransferActivity.this),
                            RequestParams.getSignatureRequestParams(GoodsTransferActivity.this),
                            UserSession.getUserSession(GoodsTransferActivity.this),
                            branchId,
                            selectionDestinationTo.getId(),
                            autoCompleteTextView.getText().toString(),
                            edtReceiverPhone.getText().toString(),
                            edtItemValue.getText().toString(),
                            "1",
                            selectionItemType.getId(),
                            edtItemName.getText().toString(),
                            edtItemQty.getText().toString(),
                            selectionUOM.getId(),
                            getFee(),
                            /*customerType */0,
                            /* customerId*/0,
                            /* vipCustomerId*/0,
                            referenceLocal,
                            deliveryArea,
                            String.valueOf(getTotalAmount(Double.parseDouble(edtFee.getText().toString()), getDiscount(Double.parseDouble(edtFee.getText().toString()), Double.parseDouble(edtMemberPercentage.getText().toString())))),
                            String.valueOf(AppConfig.getGetLatitude()),
                            String.valueOf(AppConfig.getGetLongitude()),
                            txtDeliverFee.getText().toString(),
                            checkCollect,
                            isTransit,
                            isFreeDelivery,
                            edtNote.getText().toString().trim()
                    );
                    totalAmount = String.valueOf(getTotalAmount(Double.parseDouble(edtFee.getText().toString()), getDiscount(Double.parseDouble(edtFee.getText().toString()), Double.parseDouble(edtMemberPercentage.getText().toString()))));
                } catch (Exception e) {
                    Log.d("error", "" + e.getMessage());
                }
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    String getFee(){
        if(HomeActivity.addGoodsNew == 1){
            return String.valueOf(getTotalAmount(Double.parseDouble(edtFee.getText().toString()), getDiscount(Double.parseDouble(edtFee.getText().toString()), Double.parseDouble(edtMemberPercentage.getText().toString()))));
        }else {
            return edtFee.getText().toString();
        }
    }

    private void gotoScanMembership() {
        Intent intent = new Intent(this, MembershipScannerActivity.class);
        startActivityForResult(intent, Constants.MEMBERSHIP_REQUEST_CODE);
    }

    private void gotoPrintReceipt(PrintData printData, Class className) {
        Intent intent = new Intent(GoodsTransferActivity.this, className);
        intent.putExtra("printData", printData);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void gotoRequestDestinationFrom() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_DESTINATION_FROM_CODE);
        startActivityForResult(intent, Constants.REQUEST_DESTINATION_FROM_CODE);
    }

    private void gotoRequestDestinationTo() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_DESTINATION_TO_CODE);
        intent.putExtra("destination_from_id", branchId);
        startActivityForResult(intent, Constants.REQUEST_DESTINATION_TO_CODE);
    }

    private void gotoRequestCurrency() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_CURRENCY_CODE);
        startActivityForResult(intent, Constants.REQUEST_CURRENCY_CODE);
    }

    private void gotoRequestItemType() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_ITEM_TYPE_CODE);
        startActivityForResult(intent, Constants.REQUEST_ITEM_TYPE_CODE);
    }

    private void gotoRequestUOM() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_UOM_CODE);
        startActivityForResult(intent, Constants.REQUEST_UOM_CODE);
    }

    private void gotoRequestAreaToDeliver() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(Constants.REQUEST_SELECTION_KEY, Constants.REQUEST_AREA_DELIVER_CODE);
        startActivityForResult(intent, Constants.REQUEST_AREA_DELIVER_CODE);
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

    private void checkUserInput() {
        if(HomeActivity.addGoodsNew == 1){
            isFreeDelivery = 1;
        }
        if (branchId == null
                || selectionDestinationTo == null
                || autoCompleteTextView.getText().toString().isEmpty()
                || edtReceiverPhone.getText().toString().isEmpty()
                || edtItemValue.getText().toString().isEmpty()
                || selectionItemType == null
                || edtItemQty.getText().toString().isEmpty()
                || selectionUOM == null) {
            AlertDialogUtil.alertMessageInput(this, getResources().getString(R.string.please_fill_the_information));
        } else {
            if (isFreeDelivery != 1) {
                for (int i = 0; i < selectionDataCustomer.size(); i++) {
                    if (selectionDataCustomer.get(i).getTel().equals(autoCompleteTextView.getText().toString().trim())) {
                        tvCustomer.setText(selectionDataCustomer.get(i).getName());
                        isFreeDelivery = 1;
                        break;
                    } else {
                        isFreeDelivery = 0;
                    }
                }
            }
            alertPaid();
        }
    }

    private void saveGoodsTransfer(String device,
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
                                   final int customerType,
                                   int customerId,
                                   int vipCusId,
                                   String reference,
                                   int deliveryArea,
                                   String fee,
                                   String longs,
                                   String lats,
                                   String requestId,
                                   String cod,
                                   String isTransit,
                                   int isDeliveryFee,
                                   String deliveryAddress) {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<GoodsTransferResponse> call = apiService.saveGoodsTransfer(
                device,
                token,
                signature,
                session,
                destinationFrom,
                destinationTo,
                sender,
                receiver,
                itemValue,
                itemCurrency,
                itemType,
                itemName,
                itemQTY,
                uom,
                transferFee,
                reference,
                deliveryArea,
                fee,
                longs,
                lats,
                requestId,
                cod,
                isTransit,
                isDeliveryFee,
                deliveryAddress );

        call.enqueue(new Callback<GoodsTransferResponse>() {
            @Override
            public void onResponse(@NonNull Call<GoodsTransferResponse> call, @NonNull Response<GoodsTransferResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.i("requestTransfer", response.body().getInfo());
                    if (response.body() != null) {
                        RequestParams.persistRequestParams(GoodsTransferActivity.this
                                , Objects.requireNonNull(response.body()).getToken()
                                , Objects.requireNonNull(response.body()).getSignature());
                        if (response.body().getStatus().equals(Constants.STATUS_SUCCESS)) {
                            RequestParams.persistRequestParams(GoodsTransferActivity.this, response.body().getToken(), response.body().getSignature());
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
                            printData.setSender(autoCompleteTextView.getText().toString());
                            printData.setReceiver(edtReceiverPhone.getText().toString());
                            printData.setDestinationTo(selectionDestinationTo.getName());
                            printData.setTransferFee(response.body().getTransferFee());
                            printData.setDeliveryFee(response.body().getDeliveryFee());
                            printData.setDiscount(response.body().getDiscount());
                            printData.setPercentage(edtMemberPercentage.getText().toString());
                            printData.setTotalAmount(response.body().getTotalAmount());
                            printData.setBranchFromTel(response.body().getBranchFromTel());
                            printData.setBranchToName(response.body().getBranchToName());
                            printData.setBranchToTel(response.body().getBranchToTel());
                            printData.setItemCode(response.body().getItemCode());
                            printData.setQrCode(response.body().getQrCode());
                            printData.setPrintDate(response.body().getDatePrint());
                            printData.setCollectCod(response.body().getCollectCod());
                            printData.setPaid(response.body().getPaid());
                            printData.setItemName(response.body().getItem_name());
                            printData.setDeliveryArea(response.body().getDelivery_area());
                            printData.setPoint(response.body().getPoint());
                            printData.setCustomerName(response.body().getCustomerName());
                            printData.setDestinationToCode(response.body().getDestinationToCode());
                            printData.setLocation_type(response.body().getLocation_type());

                            CityPrintOldActivity.location_from = response.body().getLocation_type();
                            CityPrintActivity.location_from = response.body().getLocation_type();
                            LocalPrintActivity.location_from = response.body().getLocation_type();
                            PrintGTReceiptActivity.location_from = response.body().getLocation_type();

                            Log.d("location type==>", "" + response.body().getLocation_type());
                            Log.d("responseArea==>", "" + response.body().getDelivery_area());
                            Log.d("deliveryAra==> ", "" + printData.getDeliveryArea());
                            PrintGTReceiptActivity.deliveryArea = response.body().getDelivery_area();
                            CityPrintActivity.deliveryArea = response.body().getDelivery_area();
                            CityPrintOldActivity.deliveryArea = response.body().getDelivery_area();
                            LocalPrintActivity.deliveryArea = response.body().getDelivery_area();
                            customerLocalId = "";
                            referenceLocal = "";
                            SharedPreferences pref1 = getSharedPreferences("BluetoothVersion", MODE_PRIVATE);
                            String getVersion = pref1.getString("Version", "");

                            getChoosePrinter();
//                            gotoPrintReceipt(printData);
                            if (getName.equals("BlueTooth")) {
                                if(getVersion.equals("New")){
                                    gotoPrintReceipt(printData, CityPrintActivity.class);
                                } else {
                                    gotoPrintReceipt(printData, CityPrintOldActivity.class);
                                }
                            } else if(getName.equals("printer_wire")) {
                                gotoPrintReceipt(printData, CityPrintActivity.class);
                            }
                            else {
                                gotoPrintReceipt(printData, PrintGTReceiptActivity.class);
                            }
                        } else {
                            AlertDialogUtil.alertMessageError(GoodsTransferActivity.this, response.body().getInfo());
                        }
                    }
                }

                Log.i("saveGoodsTransfer", "Unsuccessful");

            }

            @Override
            public void onFailure(@NonNull Call<GoodsTransferResponse> call, @NonNull Throwable t) {
                Log.i("saveGoodsTransfer", "" + t.getMessage());
                buttonSave.setClickable(true);
                buttonSave.setText(getResources().getString(R.string.save));
                buttonSave.setClickable(true);
            }
        });
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
        if(HomeActivity.addGoodsNew == 1){
            return (fullPrice - discount)*2;
        }else {
            if (fullPrice > 0) {
                return fullPrice - discount;
            } else {
                return 0;
            }
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
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.hide();
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
                            if(HomeActivity.addGoodsNew == 1){
                                totalAmount = String.valueOf(Double.parseDouble(totalAmount) * 2);
                            }
                            edtTotalAmount.setText(totalAmount);
                            progressDialog.hide();

                            memberShipId = response.body().getMembership_id();
                            Log.d("idMemberShip ==>", response.body().getMembership_id());
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

    public void ClickRadio(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);
        if (radioButton.getText().equals("ដៃគូសហការណ៍")) {
            buttonSelectCustomer.setVisibility(View.VISIBLE);
            tvTitleCustomer.setVisibility(View.VISIBLE);
            tvTitleMemberShip.setVisibility(View.GONE);
            buttonSelectMemberShip.setVisibility(View.GONE);

            edtSenderPhone.setText("");
            edtSenderPhone.setEnabled(true);
            buttonSelectMemberShip.setText(getResources().getString(R.string.please_select));
            customerType = 2;
            customerId = 0;
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

            edtSenderPhone.setText("");
            edtSenderPhone.setEnabled(true);
            buttonSelectCustomer.setText(getResources().getString(R.string.please_select));
            customerType = 3;
        }
    }

    private void listSenderTel(ArrayList<String> ar) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_dropdown_item_1line, ar);

        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCustomer.setText("General");
                isFreeDelivery = 0;
                if (s.toString().length() > 6) {
                    autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                        isFreeDelivery = 1;
                        String name = parent.getAdapter().getItem(position).toString().substring(14);
                        StringBuffer sb = new StringBuffer(name);
                        sb.deleteCharAt(sb.length() - 1);
                        tvCustomer.setText(sb);
                    });
                    autoCompleteTextView.setAdapter(adapter);
                    autoCompleteTextView.setTextColor(Color.GRAY);
                } else {
                    autoCompleteTextView.setAdapter(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        ArrayAdapter<String> adapter = new ArrayAdapter<>
//                (this, android.R.layout.simple_dropdown_item_1line, ar);
//
//        autoCompleteTextView.setThreshold(1);
//
//        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
//            isFreeDelivery = 1;
//            String name = parent.getAdapter().getItem(position).toString().substring(13);
//            StringBuffer sb = new StringBuffer(name);
//            sb.deleteCharAt(sb.length() - 1);
//            tvCustomer.setText(sb);
//        });
//        autoCompleteTextView.setAdapter(adapter);
//        autoCompleteTextView.setTextColor(Color.GRAY);

        llLayout.setOnClickListener(v -> {
            if (isFreeDelivery != 1) {
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                } else {
//                    progressDialog.show();
//                }
                for (int i = 0; i < selectionDataCustomer.size(); i++) {
                    if (selectionDataCustomer.get(i).getTel().equals(autoCompleteTextView.getText().toString().trim())) {
                        tvCustomer.setText(selectionDataCustomer.get(i).getName());
                        isFreeDelivery = 1;
//                        progressDialog.dismiss();
                        break;
                    } else {
                        isFreeDelivery = 0;
                    }
                }
            }
        });
        edtReceiverPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (isFreeDelivery != 1) {
//                    if (progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    } else {
//                        progressDialog.show();
//                    }
                    for (int i = 0; i < selectionDataCustomer.size(); i++) {
                        if (selectionDataCustomer.get(i).getTel().equals(autoCompleteTextView.getText().toString().trim())) {
                            tvCustomer.setText(selectionDataCustomer.get(i).getName());
                            isFreeDelivery = 1;
//                            progressDialog.dismiss();
                            break;
                        } else {
                            isFreeDelivery = 0;
//                            progressDialog.show();
                        }
                    }
                }
            }

        });
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
        Call<DestinationFromResponse> call = apiService.getCustomerAuto(device, token, signature, session, searchText);
        call.enqueue(new Callback<DestinationFromResponse>() {
            @Override
            public void onResponse(@NonNull Call<DestinationFromResponse> call, @NonNull Response<DestinationFromResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatus().equals(Constants.STATUS_SUCCESS)) {
                            if (response.body().getData().size() != 0) {
                                selectionDataCustomer = response.body().getData();
                                selectCustomer = new SelectionData();
                                for (int i = 0; i < response.body().getData().size() - 1; i++) {
                                    arrayListNumber.add(response.body().getData().get(i).getTel() + "   ( " + response.body().getData().get(i).getName() + ")");
                                    arrayListName.add(response.body().getData().get(i).getName());
//                                    selectCustomer.setName(response.body().getData().get(i).getName());
                                    selectCustomer.setTel(response.body().getData().get(i).getTel());
//                                    selectCustomer.setAllowDelivery(response.body().getData().get(i).getAllowDelivery());
                                }
                                listSenderTel(arrayListNumber);
                                //Replace request params
                            }  //Replace request params

                            RequestParams.persistRequestParams(GoodsTransferActivity.this
                                    , Objects.requireNonNull(response.body()).getToken()
                                    , Objects.requireNonNull(response.body()).getSignature());
                        } else {
                            AlertDialogUtil.alertMessageError(GoodsTransferActivity.this, response.body().getInfo());
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


