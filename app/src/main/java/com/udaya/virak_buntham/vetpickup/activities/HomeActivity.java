package com.udaya.virak_buntham.vetpickup.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;

import com.application.print.PrintPort;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hztytech.printer.sdk.km_bluetooth.Kmbluetooth;
import com.printer.psdk.device.adapter.ConnectedDevice;
import com.printer.psdk.device.bluetooth.Bluetooth;
import com.printer.psdk.device.bluetooth.ConnectListener;
import com.printer.psdk.device.bluetooth.Connection;
import com.printer.psdk.frame.father.listener.DataListener;
import com.printer.psdk.frame.father.listener.ListenAction;
import com.printer.psdk.frame.father.types.lifecycle.Lifecycle;
import com.printer.psdk.tspl.GenericTSPL;
import com.printer.psdk.tspl.TSPL;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.callToCustomer.CallToCustomer;
import com.udaya.virak_buntham.vetpickup.activities.changeDestination.ChangeDestinationMenuActivity;
import com.udaya.virak_buntham.vetpickup.activities.goodsLocal.GoodsTransferLocalActivity;
import com.udaya.virak_buntham.vetpickup.activities.goodsTransfer.GoodsTransferActivity;
import com.udaya.virak_buntham.vetpickup.activities.locker.LockerActivity;
import com.udaya.virak_buntham.vetpickup.activities.movetovan.ReceiveItemActivity;
import com.udaya.virak_buntham.vetpickup.activities.nav.ChangeLanguageActivity;
import com.udaya.virak_buntham.vetpickup.activities.nav.ChoosePrinterActivity;
import com.udaya.virak_buntham.vetpickup.activities.nav.ScaleLayoutOldActivity;
import com.udaya.virak_buntham.vetpickup.activities.outForDevliery.OutForDeliveryActivity;
import com.udaya.virak_buntham.vetpickup.activities.pickUp.PickUpActivity;
import com.udaya.virak_buntham.vetpickup.activities.receivedCodeByBranch.SearchActivity;
import com.udaya.virak_buntham.vetpickup.activities.report.MenuReportActivity;
import com.udaya.virak_buntham.vetpickup.activities.report.ReportViewActivity;
import com.udaya.virak_buntham.vetpickup.adapters.ExampleAdapter;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.LocalPrintOldActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.KmCreate;
import com.udaya.virak_buntham.vetpickup.custom.ClickableImageView;
import com.udaya.virak_buntham.vetpickup.fragment.ExampleItem;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.logout.LogoutResponse;
import com.udaya.virak_buntham.vetpickup.models.permission.PermissionRespone;
import com.udaya.virak_buntham.vetpickup.models.print.PrintData;
import com.udaya.virak_buntham.vetpickup.models.requesttoken.RequestTokenResponse;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;
import com.udaya.virak_buntham.vetpickup.utils.AppConfig;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.GpsTrackerLocation;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, OnInternetConnectionListener, LocationListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.button_add_goods_transfer)
    CardView buttonAddNewGoodsTransfer;
    @BindView(R.id.button_add_goods_transfer_local)
    CardView buttonAddNewGoodsTransferLocal;
    @BindView(R.id.button_customer_received)
    CardView buttonCustomerReceived;
    @BindView(R.id.button_report_goods_transfer)
    Button buttonReportGoodsTransfer;
    @BindView(R.id.button_route_maps)
    Button buttonRouteMaps;
    @BindView(R.id.button_move_item_to_van)
    CardView buttonMoveItemToVan;
    @BindView(R.id.contact)
    ImageView contact;
    @BindView(R.id.button_received_item)
    CardView buttonReceiveItem;
    @BindView(R.id.button_customer_received_Branch)
    CardView buttonReceiveItemByBranch;
    @BindView(R.id.button_pick_up)
    CardView btnPickUpGoods;
    @BindView(R.id.button_report)
    CardView btnReportMenu;

    @BindView(R.id.button_search_report)
    CardView btnSearchReport;

    @BindView(R.id.button_transit)
    CardView btnTransit;
    @BindView(R.id.button_out_for_delivery)
    CardView buttonOutForDelivery;
    @BindView(R.id.button_call_to_customer)
    CardView buttonCallToCustomer;
    @BindView(R.id.iv_button_setting)
    ClickableImageView buttonSetting;
    int accessPermission = 0;
    @BindView(R.id.tvUserName)
    TextView tvName;
    @BindView(R.id.layout_loading)
    FrameLayout layouLoading;

    @BindView(R.id.button_search)
    CardView btnSearch;
    String getName = "";

    double storeVersion;
    @BindView(R.id.tvAppVersion)
    TextView tvAppVersion;

    LocationManager locationManager;
    @BindView(R.id.tvConnetedPrinter)
    TextView tvConnectedPrinter;

    byte[] data = new byte[2];

    //BluetoothConnect
    private final BluetoothAdapter mBluetoothAdapter = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private PrintPort printPort;
    private static final boolean D = true;
    private boolean isConnected = false;
    private final String address = "";
    private final String name = "";

    //setRecycler
    private ArrayList<ExampleItem> menuList;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    //get location
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManagers;
    String latitude, longitude;

    int checkPermisson = 0;
    @RequiresApi(api = Build.VERSION_CODES.M)

    private GpsTrackerLocation gpsTracker;

    // new
    private ArrayList<BluetoothDevice> blueDevices = new ArrayList<>();
    private static final String ACTION_USB_PERMISSION = "com.raycloud.kmprint";
    private PendingIntent pendingIntent;
    private boolean check;
    public static int addGoodsNew = 0; // 1 is add goods Toin Jet , 0 is add goods

    boolean isVETSlot = true; // For LED function is true

    public static BluetoothDevice blueDevice;

    private GenericTSPL tspl;

    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        if (isVETSlot) {
            CheckDeviceActivity.location = "000";
            startActivity(new Intent(this, CheckDeviceActivity.class));
        }

/*
        SharedPreferences.Editor editor = getSharedPreferences("PrinterName", MODE_PRIVATE).edit();
        editor.putString("BluetoothName", "");
        editor.apply();
*/

        //check let long
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        ProgressDialog progressDialog = new ProgressDialog(this);
        checkerPermissionCamera();
        setArray();
        checkerPermission(DeviceID.getDeviceId(this), RequestParams.getTokenRequestParams(this), RequestParams.getSignatureRequestParams(this), UserSession.getUserSession(this));
        mToolbarTitle.setText("VET Scanner");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        registerOnClick(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.home_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        goToContact();

        //        setLocation();
        //Disable button pick up maps
        buttonRouteMaps.setEnabled(false);
        tvAppVersion.setText("កំណែ " + AppConfig.getAppVersion());
        setConnectedPrinter();

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.BLUETOOTH_SCAN},
                1);


    }

    public void getLocationData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gpsTracker = new GpsTrackerLocation(HomeActivity.this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (gpsTracker.canGetLocation()) {
                double letData = gpsTracker.getLatitude();
                double longData = gpsTracker.getLongitude();
                AppConfig.setGetLatitude(letData);
                AppConfig.setGetLongitude(longData);
                //  Toast.makeText(this, "==>" + AppConfig.getGetLatitude() + "==" + AppConfig.getGetLongitude(), Toast.LENGTH_SHORT).show();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }

    private void setArray() {
        menuList = new ArrayList<>();
        menuList.add(new ExampleItem(R.drawable.ic_inventory, "បញ្ចូលបញ្ញើថ្មី\n(ខេត្ដ/ក្រុង)"));
        menuList.add(new ExampleItem(R.drawable.ic_toin_jet, "បញ្ចូលបញ្ញើថ្មី\n(ទាន់ចិត្ត)"));
        menuList.add(new ExampleItem(R.drawable.food_delivery_ic, "បញ្ចូលបញ្ញើថ្មី\n(តំបន់)"));
        menuList.add(new ExampleItem(R.drawable.ic_move_tovan_color, "ផ្ទេរអីវ៉ាន់ឡើងឡាន"));
        menuList.add(new ExampleItem(R.drawable.ic_transit, "ផ្ទេរតជើង"));
        menuList.add(new ExampleItem(R.drawable.move_from_van_color, "ទទួលអីវ៉ាន់\nពីឡាន"));
        menuList.add(new ExampleItem(R.drawable.customer_service, "តេទៅអតិថិជន"));
        menuList.add(new ExampleItem(R.drawable.customer_receive_color, "អតិថិជនទទួលអីវ៉ាន់(សាខា)"));
        menuList.add(new ExampleItem(R.drawable.ic_customer_receive, "អតិថិជនទទួលអីវ៉ាន់(ដឹកជញ្ជូន)"));
        menuList.add(new ExampleItem(R.drawable.branch_pick_color, "យកអីវ៉ាន់ដល់ផ្ទះ (App request)"));
        menuList.add(new ExampleItem(R.drawable.delivery_home_color, "ដឹកអីវ៉ាន់ដល់ផ្ទះ"));
        menuList.add(new ExampleItem(R.drawable.locker, "Locker"));
        menuList.add(new ExampleItem(R.drawable.ic_change, "ប្តូរ (Change)"));
        menuList.add(new ExampleItem(R.drawable.ic_led, "LED Scan"));
    }


    private void setRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);

/*
        ImageView imageView = findViewById(R.id.view);
*/
/*
        Bitmap bitmap = imageView.getDrawingCache();

        data = UseCase.tspl_case2(HomeActivity.this, bitmap);
*//*


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  Bitmap bitmap = imageView.getDrawingCache();
                Log.e("", "onClick: "+ loadBitmapFromView(imageView) );

                Send.writeData( UseCase.tspl_case2(HomeActivity.this, loadBitmapFromView(imageView)), HomeActivity.this);
            }
        });
*/


        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(menuList);
        mAdapter.setOnItemClickListener((position, textView) -> {
            switch (menuList.get(position).getText1()) {
                case "បញ្ចូលបញ្ញើថ្មី\n(ខេត្ដ/ក្រុង)":
                    addGoodsNew = 0;
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                 /*   Toast.makeText(this, "Me", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "" + getLocation(), Toast.LENGTH_SHORT).show();*/
                    if (getLocation()) {
                        gotoAddNewGoodsTransfer();
                    }

//                    if (getLocation()) {
//                        getLocationData();
//                        gotoAddNewGoodsTransfer();
//                    } else {
//                        getLocation();
//                    }
                    break;

                case "បញ្ចូលបញ្ញើថ្មី\n(ទាន់ចិត្ត)":
                    addGoodsNew = 1;
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                    if (getLocation()) {
                        gotoAddNewGoodsTransfer();
                    }
//                    if (getLocation()) {
//                        getLocationData();
//                        gotoAddNewGoodsTransfer();
//                    } else {
//                        getLocation();
//                    }
                    break;

                case "បញ្ចូលបញ្ញើថ្មី\n(តំបន់)":
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                    if (getLocation()) {
                        gotoAddNewGoodsTransferLocal();
                    } else {
                        getLocation();
                    }

                    break;
                case "ផ្ទេរអីវ៉ាន់ឡើងឡាន":
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
//                    gotoMoveItemToVan();
                    if (getLocation()) {
                        gotoMoveItemToVan();
                    }
                    break;
                case "ទទួលអីវ៉ាន់\nពីឡាន":
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }

                    if (getLocation()) {
                        gotoReceivedItem();
                    }

                    break;
                case "អតិថិជនទទួលអីវ៉ាន់(សាខា)":
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                    if (getLocation()) {
                        gotoSearchReceiveItemByBranch();
                    }
                    break;
                case "អតិថិជនទទួលអីវ៉ាន់(ដឹកជញ្ជូន)":
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                    if (getLocation()) {
                        gotoScanQr();
                    }
                    break;
                case "យកអីវ៉ាន់ដល់ផ្ទះ (App request)":
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                    gotoPickUpGoods();
                    break;
                case "ដឹកអីវ៉ាន់ដល់ផ្ទះ":
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                    gotoOutForDelivery();
                    break;
                case "របាយការណ៍":
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                    gotoReportGoodsTransfer();
                    break;
                case "តេទៅអតិថិជន":
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                    gotoCallToCustomer();
                    break;
                case "ផ្ទេរតជើង":
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                    if (getLocation()) {
                        gotoTransit();
                    } else {
                        getLocation();
                    }
                    break;
                case "ប្តូរ (Change)":
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                    if (getLocation()) {
                        gotoChangeDestination();
                    } else {
                        getLocation();
                    }
                    break;
                case "Locker":
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                    if (getLocation()) {
                        startActivity(new Intent(HomeActivity.this, LockerActivity.class));
                    } else {
                        getLocation();
                    }
                    break;
                case "LED Scan":
                    startActivity(new Intent(this, LEDScanActivity.class));
                    break;
 /*               case "ស្វែងរក":
                    if (printPort != null) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                    goToSearch("ស្វែងរក", "search");
                    break;*/
                default:
//                    if (printPort != null) {
//                        printPort.disconnect();
//                        isConnected = false;
//                    }
//                    gotoCallToCustomer();
                    break;
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void registerOnClick(View.OnClickListener clickListener) {
        buttonSetting.setOnClickListener(clickListener);
        buttonAddNewGoodsTransfer.setOnClickListener(clickListener);
        buttonReportGoodsTransfer.setOnClickListener(clickListener);
        buttonRouteMaps.setOnClickListener(clickListener);
        buttonAddNewGoodsTransferLocal.setOnClickListener(clickListener);
        buttonCustomerReceived.setOnClickListener(clickListener);
        buttonMoveItemToVan.setOnClickListener(clickListener);
        buttonReceiveItem.setOnClickListener(clickListener);
        buttonReceiveItemByBranch.setOnClickListener(clickListener);
        btnReportMenu.setOnClickListener(clickListener);
        btnPickUpGoods.setOnClickListener(clickListener);
        buttonOutForDelivery.setOnClickListener(clickListener);
        buttonCallToCustomer.setOnClickListener(clickListener);
        btnSearch.setOnClickListener(clickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_button_setting:
                buttonSetting.showAnimation(this);
                gotoSetting();
                break;
            case R.id.button_add_goods_transfer:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                if (getLocation()) {
                    gotoAddNewGoodsTransfer();
                } else {
                    getLocation();
                }
                break;
            case R.id.button_add_goods_transfer_local:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                if (getLocation()) {
                    gotoAddNewGoodsTransferLocal();
                } else {
                    getLocation();
                }
                break;
            case R.id.button_customer_received:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                gotoScanQr();
                break;
            case R.id.button_report_goods_transfer:
                printPort.disconnect();
                isConnected = false;
                gotoReportGoodsTransfer();
                break;
            case R.id.button_route_maps:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                gotoMaps();
                break;
            case R.id.button_move_item_to_van:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                gotoMoveItemToVan();
                break;
            case R.id.button_received_item:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                gotoReceivedItem();
                break;
            case R.id.button_customer_received_Branch:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                gotoSearchReceiveItemByBranch();
                break;

            case R.id.button_report:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                gotoMenuReport();
                break;
            case R.id.button_pick_up:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                gotoPickUpGoods();
                break;
            case R.id.button_out_for_delivery:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                gotoOutForDelivery();
                break;
            case R.id.button_call_to_customer:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                gotoCallToCustomer();
                break;
            case R.id.button_search_report:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                goToSearch("ស្វែងរក", "26");
                break;
            case R.id.button_transit:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                gotoTransit();
                break;
            case R.id.button_search:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                goToSearch("ស្វែងរក", "26");
                break;

        }
    }

    private void gotoChangePassword() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    private void gotoChangeLanguage() {
        Intent intent = new Intent(this, ChangeLanguageActivity.class);
        startActivity(intent);
    }

    private void gotoLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void gotoAddNewGoodsTransfer() {
        try {
            if (getName.equals("BlueTooth")) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    // Get a set of currently paired devices
                    if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                            return;
                        }
                    }
                    mBluetoothAdapter.enable();
                    Intent intent = new Intent(this, GoodsTransferActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, GoodsTransferActivity.class);
                    startActivity(intent);
                }
            } else {
                Intent intent = new Intent(this, GoodsTransferActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            Intent intent = new Intent(this, GoodsTransferActivity.class);
            startActivity(intent);
        }

    }

    private void gotoTransit() {

        Intent intent = new Intent(this, TransitActivity.class);
        startActivity(intent);

   /*     if (getName.equals("BlueTooth")) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                // Get a set of currently paired devices
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                        return;
                    }
                }
                mBluetoothAdapter.enable();
                Intent intent = new Intent(this, TransitActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, TransitActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(this, TransitActivity.class);
            startActivity(intent);
        }*/

    }

    private void gotoChangeDestination() {
        if (getName.equals("BlueTooth")) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                // Get a set of currently paired devices
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                        return;
                    }
                }
                mBluetoothAdapter.enable();
                Intent intent = new Intent(this, ChangeDestinationMenuActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, ChangeDestinationMenuActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(this, ChangeDestinationMenuActivity.class);
            startActivity(intent);
        }
    }

    private void gotoAddNewGoodsTransferLocal() {
        if (getName.equals("BlueTooth")) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) {

                // Get a set of currently paired devices
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                        return;
                    }
                }

                mBluetoothAdapter.enable();
                Intent intent = new Intent(this, GoodsTransferLocalActivity.class);
                //   Intent intent = new Intent(this, SelectionLocalFunctionActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, GoodsTransferLocalActivity.class);
                //   Intent intent = new Intent(this, SelectionLocalFunctionActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(this, GoodsTransferLocalActivity.class);
            //    Intent intent = new Intent(this, SelectionLocalFunctionActivity.class);
            startActivity(intent);
        }
    }

    private void gotoScanQr() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);
        } else {
            startActivity(new Intent(this, SearchReceiveCodeActivity.class));
        }

    }

    private void gotoReportGoodsTransfer() {
        Intent intent = new Intent(this, MenuReportActivity.class);
        startActivity(intent);
    }

    private void goToSearch(String name, String id) {
        Intent intent = new Intent(this, ReportViewActivity.class);
        intent.putExtra("TitleName", name);
        intent.putExtra("Device", DeviceID.getDeviceId(this));
        intent.putExtra("session", UserSession.getUserSession(this));
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void gotoMaps() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void gotoSetting() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    private void gotoMoveItemToVan() {
        Intent intent = new Intent(this, MoveItemToVanActivity.class);
        startActivity(intent);
    }

    private void gotoReceivedItem() {
        Intent intent = new Intent(this, ReceiveItemActivity.class);
        startActivity(intent);
    }

    private void gotoSearchReceiveItemByBranch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);
        } else {
//            startActivity(new Intent(this, SearchReceivedCodeByBranchActivity.class));
            startActivity(new Intent(this, SearchActivity.class));
        }
    }

    private void gotoMenuReport() {
        Intent intent = new Intent(this, MenuReportActivity.class);
        startActivity(intent);
    }

    private void gotoPickUpGoods() {
        Intent intent = new Intent(this, PickUpActivity.class);
        startActivity(intent);
    }

    private void gotoOutForDelivery() {
        Intent intent = new Intent(this, OutForDeliveryActivity.class);
        startActivity(intent);
    }

    private void gotoCallToCustomer() {
        Intent intent = new Intent(this, CallToCustomer.class);
        startActivity(intent);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_change_password:
                gotoChangePassword();
                break;
            case R.id.nav_change_language:
                gotoChangeLanguage();
                break;
            case R.id.nav_setting:
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                Kmbluetooth.getInstance().stopBluetoothDevicesDiscovery();
                startActivity(new Intent(this, ChoosePrinterActivity.class));

              /*  PrintData printData = new PrintData();
                printData.setBranchName("sdfdf");
                printData.setDestinatioFrom("sdfdf");
                printData.setTransferCode("sdfdf");
                printData.setItemValue("sdfdf");
                printData.setTransferDate("sdfdf");
                printData.setItemType("sdfdf");
                printData.setItemQTY("sdfdf");
                printData.setItemUOM("sdfdf");
                printData.setItemValueSymbol("sdfdf");
                printData.setNormalSymbol("sdfdf");
                printData.setSender("sdfdf");
                printData.setReceiver("sdfdf");
                printData.setDestinationTo("sdfdf");
                printData.setTransferFee("sdfdf");
                printData.setDeliveryFee("sdfdf");
                printData.setDiscount("sdfdf");
                printData.setTotalAmount("sdfdf");
                printData.setBranchFromTel("sdfdf");
                printData.setBranchToName("sdfdf");
                printData.setBranchToTel("sdfdf");
                printData.setItemCode("sdfdf");
                printData.setQrCode("sdfdf");
                printData.setPrintDate("sdfdf");
                printData.setPaid("sdfdf");
                printData.setCollectCod("sdfdf");
                printData.setItemName("sdfdf");
                printData.setPoint(1);
                printData.setCustomerName("sdfdf");
                printData.setDestinationToCode("sdfdf");
                printData.setLocation_type("sdfdf");

                Intent intent = new Intent(this, LocalPrintOldActivity.class);
                intent.putExtra("printData", printData);
                startActivity(intent);*/


                break;

            case R.id.nav_logout:
                requestLogout(DeviceID.getDeviceId(this), RequestParams.getTokenRequestParams(this), RequestParams.getSignatureRequestParams(this), UserSession.getUserSession(this));
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void requestLogout(String device, String token, String signature, String session) {
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<LogoutResponse> call = apiService.requestLogout(device, token, signature, session);
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(@NonNull Call<LogoutResponse> call, @NonNull Response<LogoutResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals(Constants.STATUS_SUCCESS)) {

                            //Replace token
                            RequestParams.persistRequestParams(HomeActivity.this
                                    , response.body().getToken()
                                    , response.body().getSignature());

                            UserSession.clearSession(HomeActivity.this);
                            gotoLogin();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LogoutResponse> call, @NonNull Throwable t) {
                Log.i("requestInfo", "" + t.getMessage());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {
        AlertDialogUtil.alertMessageInternetConnection(this);
    }

    private void checkerPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);
        } else {
            Log.e("permission", "checkerPermissionCamera:True ");
        }
    }

    private void removeItem(String nameCard) {
        for (int j = 0; j < menuList.size(); j++) {
            if (menuList.get(j).getText1().equals(nameCard)) {
                menuList.remove(j);
            }
        }
    }

    private void addItem(String nameCard) {
        for (int j = 0; j < menuList.size(); j++) {
            if (menuList.get(j).getText1().equals(nameCard)) {
                menuList.add(new ExampleItem(R.drawable.report_color, "របាយការណ៍"));
            }
        }
    }

    private void checkerPermission(String device, String token, String signature, String session) {
        layouLoading.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<PermissionRespone> call = apiService.getPermission(device, token, signature, session);
        call.enqueue(new Callback<PermissionRespone>() {
            @Override
            public void onResponse(@NonNull Call<PermissionRespone> call, @NonNull Response<PermissionRespone> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            //Replace token
                            RequestParams.persistRequestParams(HomeActivity.this
                                    , response.body().getToken()
                                    , response.body().getSignature());
                            int permission = response.body().getUserPermissions().size();
                            String userName = response.body().getUsername();
                            tvName.setText(userName);
                            SharedPreferencesUserName(userName);
                            String destFromId = response.body().getDestFromId();
                            String destFromName = response.body().getDestFromName();

                            String brandId = response.body().getBranchId();
                            String brandName = response.body().getBranchName();

                            RequestParams.persistBranch(HomeActivity.this
                                    , brandId
                                    , brandName);

                            String symbol = response.body().getSymbol();

                            GoodsTransferActivity.branchId = destFromId;
                            GoodsTransferActivity.branch = destFromName;
                            GoodsTransferActivity.defaultSymbol = symbol;
                            GoodsTransferActivity.defaultSymbolId = symbol;
                            GoodsTransferActivity.defaultItemValueSymbol = symbol;

                            GoodsTransferLocalActivity.branchId = destFromId;
                            GoodsTransferLocalActivity.branch = destFromName;
                            GoodsTransferLocalActivity.defaultSymbol = symbol;
                            GoodsTransferLocalActivity.defaultSymbolId = symbol;
                            GoodsTransferLocalActivity.defaultItemValueSymbol = symbol;

                            MoveItemToVanActivity.branchId = brandId;
                            MoveItemToVanActivity.branch = brandName;

                            ReceiveItemActivity.branchId = brandId;
                            ReceiveItemActivity.branch = brandName;

                            CallToCustomer.branchId = brandId;
                            CallToCustomer.branch = brandName;
                            int checkReport = 0;

                            if (checkPermisson == 0) {
                                for (int i = 0; i < permission; i++) {

                                    if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 1) {
                                        accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                        if (accessPermission != 1) {
                                            removeItem("បញ្ចូលបញ្ញើថ្មី\n(ខេត្ដ/ក្រុង)");
                                        }
                                    } else if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 2) {
                                        accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                        if (accessPermission != 1) {
                                            removeItem("បញ្ចូលបញ្ញើថ្មី\n(តំបន់)");
                                        }
                                    } else if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 3) {
                                        accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                        if (accessPermission != 1) {
                                            removeItem("ផ្ទេរអីវ៉ាន់ឡើងឡាន");
                                        }
                                    } else if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 4) {
                                        accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                        if (accessPermission != 1) {
                                            removeItem("ទទួលអីវ៉ាន់\nពីឡាន");
                                        }
                                    } else if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 5) {
                                        accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                        removeItem("អតិថិជនទទួលអីវ៉ាន់(ដឹកជញ្ជូន)");
                                    } else if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 6) {
                                        accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                        if (accessPermission != 1) {
                                            removeItem("អតិថិជនទទួលអីវ៉ាន់(សាខា)");
                                        }
                                    } else if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 11) {
                                        accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                        if (accessPermission != 1) {
                                            removeItem("ដឹកអីវ៉ាន់ដល់ផ្ទះ");
                                        }
                                    } else if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 13) {
                                        accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                        if (accessPermission != 1) {
                                            removeItem("យកអីវ៉ាន់ដល់ផ្ទះ (App request)");
                                        }
                                    } else if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 14) {
                                        accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                        if (accessPermission != 1) {
                                            removeItem("តេទៅអតិថិជន");
                                        }
                                    } else if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 25) {
                                        accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                        if (accessPermission != 1) {
                                            removeItem("ផ្ទេរតជើង");
                                        }
                                    } else if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 27) {
                                        accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                        if (accessPermission != 1) {
                                            //  removeItem("Locker");
                                        }
                                    }
                                    if (
                                            Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 7 ||
                                                    Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 8 ||
                                                    Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 9 ||
                                                    Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 12 ||
                                                    Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 15 ||
                                                    Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 18 ||
                                                    Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 19 ||
                                                    Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 20 ||
                                                    Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 21
                                    ) {
                                        accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
                                        if (accessPermission == 1) {
                                            if (checkReport == 0) {
                                                menuList.add(new ExampleItem(R.drawable.report_color, "របាយការណ៍"));
                                                checkReport = 1;
                                            }
                                        }
                                    }
//
//                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 1) {
//                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
//                                    if (accessPermission != 1) {
//                                        removeItem("បញ្ចូលបញ្ញើថ្មី\n(ខេត្ដ/ក្រុង)");
//                                    }
//                                }
//                                if (response.body().getUserPermissions().get(i).getModule().equals("2")) {
//                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
//                                    Toast.makeText(getBaseContext(), "work here", Toast.LENGTH_SHORT).show();
//                                    if (accessPermission != 1) {
//                                        removeItem("បញ្ចូលបញ្ញើថ្មី\n(តំបន់)");
//                                    }
//                                }
//                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 3) {
//                                    Toast.makeText(HomeActivity.this, "move to van", Toast.LENGTH_SHORT).show();
//                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
//                                    if (accessPermission != 1) {
//                                        removeItem("ផ្ទេរអីវ៉ាន់ឡើងឡាន");
//                                    }
//                                }
//                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 4) {
//                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
//                                    if (accessPermission != 1) {
//                                        removeItem("ទទួលអីវ៉ាន់\nពីឡាន");
//                                    }
//                                }
//                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 5) {
//                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
//                                    if (accessPermission != 1) {
//                                        removeItem("អតិថិជនទទួលអីវ៉ាន់(ដឹកជញ្ជូន)");
//                                    }
//                                }
//                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 6) {
//                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
//                                    if (accessPermission != 1) {
//                                        removeItem("អតិថិជនទទួលអីវ៉ាន់(សាខា)");
//                                    }
//
//                                }
//                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 7
//                                        || Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 8
//                                        || Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 9
//                                        || Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 12
//                                        || Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 15
//                                        || Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 16
//                                        || Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 17
//                                        || Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 18
//                                        || Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 19) {
//                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
//                                    if (accessPermission == 1) {
//                                        menuList.add(new ExampleItem(R.drawable.report_color, "របាយការណ៍"));
//                                        break;
//                                    } else {
//                                        removeItem("របាយការណ៍");
//                                    }
//                                }
//                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 11) {
//                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
//                                    if (accessPermission != 1) {
//                                        removeItem("ដឹកអីវ៉ាន់ដល់ផ្ទះ");
//                                    }
//
//                                }
//                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 13) {
//                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
//                                    if (accessPermission != 1) {
//                                        removeItem("យកអីវ៉ាន់ដល់ផ្ទះ");
//                                    }
//                                }
//                                if (Integer.parseInt(response.body().getUserPermissions().get(i).getModule() + "") == 14) {
//                                    accessPermission = Integer.parseInt(response.body().getUserPermissions().get(i).getAccess());
//                                    if (accessPermission != 1) {
//                                        removeItem("តេទៅអតិថិជន");
//                                    }
//                                }
//
//
                                }
                                setRecyclerView();
                                checkPermisson = 1;
                            }
                            storeVersion = Double.parseDouble(response.body().getAppVersion());
                            if (AppConfig.getAppVersion() < storeVersion) {
                                checkDialogUpdate("" + storeVersion);
                            }
                        } else {
                            finish();
                            startActivity(new Intent(getBaseContext(), SplashScreenActivity.class));
                        }
                    }
                } else {
                    finish();
                    startActivity(new Intent(getBaseContext(), SplashScreenActivity.class));
                }
                layouLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<PermissionRespone> call, @NonNull Throwable t) {
                finish();
                startActivity(new Intent(getBaseContext(), SplashScreenActivity.class));
                layouLoading.setVisibility(View.GONE);
            }
        });
    }

    private void requestToken(final String device) {
        Log.d("deviceId==>", "" + device);
        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<RequestTokenResponse> call = apiService.getToken(device);
        call.enqueue(new Callback<RequestTokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<RequestTokenResponse> call, @NonNull Response<RequestTokenResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals(Constants.STATUS_SUCCESS)) {
                            RequestParams.persistRequestParams(HomeActivity.this, response.body().getToken(), response.body().getSignature());
                            checkerPermission(DeviceID.getDeviceId(getBaseContext()), RequestParams.getTokenRequestParams(getBaseContext()), RequestParams.getSignatureRequestParams(getBaseContext()), UserSession.getUserSession(getBaseContext()));
                        }
                    } else {
                        Log.i("responseToken", "is null");
                    }
                } else {
                    gotoLogin();
                    Log.i("responseToken", "Request unsuccessful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<RequestTokenResponse> call, @NonNull Throwable t) {
                Log.i("requestToken", Objects.requireNonNull(t.getMessage()));
                //loading.setVisibility(View.GONE);
            }
        });
    }

    private void SharedPreferencesUserName(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
        editor.putString("Username", name);
        editor.apply();
    }

    private void checkDialogUpdate(String storeVersion) {
        AlertDialog.Builder dialogUpdate = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK).setTitle("បច្ចុប្បន្នភាពកំណែ" + storeVersion)
                .setIcon(getResources().getDrawable(android.R.drawable.stat_sys_download))
                .setMessage("សូមធ្វើបច្ចុប្បន្នភាពកំណែចុងក្រោយ")
                .setCancelable(false)
                .setNeutralButton("សូមចុចទីនេះដើម្បីធ្វើបច្ចុប្បន្នភាព", (dialog, which) -> {
                    // TODO Auto-generated method stub
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                    } catch (ActivityNotFoundException e) {
                        // TODO: handle exception
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                });
        AlertDialog updateDialog = dialogUpdate.create();
        updateDialog.show();
    }

    @Override
    protected void onResume() {
        //  setConnectedPrinter();
        getLocationData();
        setConnectedPrinter();
        super.onResume();
    }

    //get letLong
    @SuppressLint("MissingPermission")
    private boolean getLocation() {
        if (isGpsEnabled()) {
            if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 100);
            } else {
                try {
                    locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, HomeActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                    //  Toast.makeText(this, "GPS" + e, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
//            enableLocation();
            getLocationData();

        }
        return isGpsEnabled();
    }

    private LatLng latLng;

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(this, "" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
        Log.d("letLong==>", "" + location.getLatitude() + "," + location.getLongitude());
        AppConfig.setGetLatitude(location.getLatitude());
        AppConfig.setGetLongitude(location.getLongitude());
        if (location != null) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            Toast.makeText(this, "get==>" + latLng, Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "nono", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private boolean isGpsEnabled() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER) && service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void enableLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                // All location settings are satisfied. The client can initialize location
                // requests here.

            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(HomeActivity.this, 1);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        } catch (ClassCastException e) {
                            // Ignore, should be an impossible error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setConnectedPrinter() {

        SharedPreferences prefs1 = getSharedPreferences("PrinterMacAddress", MODE_PRIVATE);
        String printerMac = prefs1.getString("PrinterMac", "");

        SharedPreferences prefs = getSharedPreferences("Printer", MODE_PRIVATE);
        String printerName = prefs.getString("PrinterType", "");

        SharedPreferences pref2 = getSharedPreferences("BluetoothVersion", MODE_PRIVATE);
        String getVersion = pref2.getString("Version", "");

        SharedPreferences pref3 = getSharedPreferences("PrinterName", MODE_PRIVATE);
        String getPrinterType = pref3.getString("Name", "");


        if(getPrinterType.equals("MobilePrinter")){
            tvConnectedPrinter.setText("Sunmi Device");
        }else {
            if (getVersion.equals("New")) {
                if (KmCreate.getInstance().connectType == 1) {
                    tvConnectedPrinter.setText(printerName);
                } else if (!printerMac.equals("")) {
                    if (getVersion.equals("New")) {
                        tvConnectedPrinter.setText(printerName);
                    }

                } else {
                    tvConnectedPrinter.setText("មិនទាន់ភ្ជាប់ម៉ាស៊ីនព្រីន");
                }
            } else {
                if (HomeActivity.blueDevice != null) {
                    connection = Bluetooth.getInstance().createConnectionClassic(HomeActivity.blueDevice, new ConnectListener() {
                        @Override
                        public void onConnectSuccess(ConnectedDevice connectedDevice) {
                            tspl = TSPL.generic(connectedDevice);
                            TSPL.generic(Lifecycle.builder().connectedDevice(connectedDevice).build());
                            dataListen(connectedDevice);
                        }

                        @Override
                        public void onConnectFail(String s, Throwable throwable) {

                        }

                        @SuppressLint("MissingPermission")
                        @Override
                        public void onConnectionStateChanged(BluetoothDevice bluetoothDevice, int i) {

                            String msg;
                            switch (i) {
                                case Connection.STATE_CONNECTING:
                                    msg = "STATE_CONNECTING";
                                    break;
                                case Connection.STATE_PAIRING:
                                    msg = "STATE_PAIRING...";
                                    break;
                                case Connection.STATE_PAIRED:
                                    msg = "STATE_PAIRED";
                                    break;
                                case Connection.STATE_CONNECTED:
                                    msg = "STATE_CONNECTED";
                                    break;
                                case Connection.STATE_DISCONNECTED:
                                    msg = "STATE_DISCONNECTED";
                                    break;
                                case Connection.STATE_RELEASED:
                                    msg = "STATE_RELEASED";
                                    break;
                                default:
                                    msg = "";
                            }
                            if (!msg.isEmpty()) {
                                Log.e("", "onConnectionStateChanged: " + msg);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("", "run: meee" + msg);
                                        if (msg.equals("STATE_CONNECTED")) {
                                            tvConnectedPrinter.setText(HomeActivity.blueDevice.getName());
                                        } else {
                                            tvConnectedPrinter.setText("មិនទាន់ភ្ជាប់ម៉ាស៊ីនព្រីន");
                                        }
                                    }
                                });
                            }

                        }
                    });

                    if (connection == null) {
                        finish();
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            connection.connect(null);
                        }
                    }).start();
                } else {
                    tvConnectedPrinter.setText("មិនទាន់ភ្ជាប់ម៉ាស៊ីនព្រីន");
                }
            }
        }
    }

    private void dataListen(ConnectedDevice connectedDevice) {
        DataListener.with(connectedDevice).listen(new ListenAction() {
            @Override
            public void action(byte[] bytes) {
                //固件回传的数据
            }
        }).start();
    }

    private void showLetLog() {
        locationManagers = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManagers.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocations();
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", (dialog, which) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))).setNegativeButton("No", (dialog, which) -> dialog.cancel());
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocations() {
        if (ActivityCompat.checkSelfPermission(
                HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManagers.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                //   Toast.makeText(this, "" + "Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude, Toast.LENGTH_SHORT).show();
            } else {
//                buildAlertMessageNoGps();
                getLocation();
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        Log.d("dialog==>", "dialog alert check location display");
        try {
            alert.show();
        } catch (Exception e) {
            Log.d("error==>", "dialog error");
        }
    }

    private void goToContact() {
        contact.setVisibility(View.VISIBLE);
        contact.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReportViewActivity.class);
            intent.putExtra("TitleName", "លេខទំនាក់ទំនង");
            intent.putExtra("Device", DeviceID.getDeviceId(this));
            intent.putExtra("session", UserSession.getUserSession(this));
            intent.putExtra("id", "contact");
            startActivity(intent);
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (HomeActivity.blueDevice != null) {
            connection = Bluetooth.getInstance().createConnectionClassic(HomeActivity.blueDevice, new ConnectListener() {
                @Override
                public void onConnectSuccess(ConnectedDevice connectedDevice) {
                    tspl = TSPL.generic(connectedDevice);
                    TSPL.generic(Lifecycle.builder().connectedDevice(connectedDevice).build());
                    dataListen(connectedDevice);
                }

                @Override
                public void onConnectFail(String s, Throwable throwable) {

                }

                @SuppressLint("MissingPermission")
                @Override
                public void onConnectionStateChanged(BluetoothDevice bluetoothDevice, int i) {

                    String msg;
                    switch (i) {
                        case Connection.STATE_CONNECTING:
                            msg = "STATE_CONNECTING";
                            break;
                        case Connection.STATE_PAIRING:
                            msg = "STATE_PAIRING...";
                            break;
                        case Connection.STATE_PAIRED:
                            msg = "STATE_PAIRED";
                            break;
                        case Connection.STATE_CONNECTED:
                            msg = "STATE_CONNECTED";
                            break;
                        case Connection.STATE_DISCONNECTED:
                            msg = "STATE_DISCONNECTED";
                            break;
                        case Connection.STATE_RELEASED:
                            msg = "STATE_RELEASED";
                            break;
                        default:
                            msg = "";
                    }
                    if (!msg.isEmpty()) {
                        Log.e("", "onConnectionStateChanged: " + msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("", "run: meee" + msg);
                                if (msg.equals("STATE_CONNECTED")) {
                                    tvConnectedPrinter.setText(HomeActivity.blueDevice.getName());
                                } else {
                                    tvConnectedPrinter.setText("មិនទាន់ភ្ជាប់ម៉ាស៊ីនព្រីន");
                                }
                            }
                        });
                    }

                }
            });

            if (connection == null) {
                finish();
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    connection.connect(null);
                }
            }).start();
        } else {

            tvConnectedPrinter.setText("មិនទាន់ភ្ជាប់ម៉ាស៊ីនព្រីន");
        }
    }
}
