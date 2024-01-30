package com.udaya.virak_buntham.vetpickup.activities.nav;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.application.print.PrintPort;
import com.hztytech.printer.sdk.km_blebluetooth.KmBlebluetooth;
import com.hztytech.printer.sdk.km_bluetooth.Kmbluetooth;
import com.hztytech.printer.sdk.km_bluetooth.KmbluetoothAdapter;
import com.hztytech.printer.sdk.km_usb.KmUsb;
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
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.ScanActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceListActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceShowListActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.BluetoothActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.KmCreate;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceListActivity.EXTRA_DEVICE_ADDRESS;

public class ChoosePrinterActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.button_mobile_printer)
    Button btnMobilePrinter;
    @BindView(R.id.button_bluetooth)
    Button btnBluetooth;
    @BindView(R.id.button_bluetooth_old)
    Button btnBluetoothOld;
    @BindView(R.id.button_test_print)
    Button btnTestPrint;
    String getName = "";
    @BindView(R.id.llBluetoothPrinter)
    LinearLayout llBluetoothPrinter;
    @BindView(R.id.btnChooesPrinter)
    Button btnChoosePrinter;
    TextView tvName;

    private BluetoothAdapter mBluetoothAdapter = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private PrintPort printPort;
    private static final boolean D = true;
    private boolean isConnected = false;

    private ProgressDialog progressDialog;

    public static String printer_name = "";

    SharedPreferences pref1;
    String getVersion;

    private GenericTSPL tspl;

    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooes_printer);
        ButterKnife.bind(this);

        getVersionPrinter();

        if(getVersion.equals("New")){
            //经典蓝牙
            Kmbluetooth.getInstance().openBluetoothAdapter(this);
            //低功耗蓝牙
            KmBlebluetooth.getInstance().openBluetoothAdapter(this);
            KmUsb.getInstance().openUsb(this);
        }

        progressDialog = new ProgressDialog(this);

        mToolbarTitle.setText(getResources().getString(R.string.setting));
        tvName = findViewById(R.id.tvNameDisplay);

        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        registerOnClick(this);

        getChoosePrinter();

        if(KmCreate.getInstance().connectType == 1){
            SharedPreferences prefs = getSharedPreferences("Printer", MODE_PRIVATE);
            String printerName = prefs.getString("PrinterType", "");
            tvName.setText(printerName);
            btnTestPrint.setVisibility(View.VISIBLE);
        }

        btnChoosePrinter.setOnClickListener(v -> choosePrinter());

        btnTestPrint.setOnClickListener(view -> {
            Log.e("", "onCreate: "+ getVersion );
            if(getVersion.equals("New")){
                startActivity(new Intent(this, ScaleLayoutActivity.class));
            }else {
                startActivity(new Intent(this, ScaleLayoutOldActivity.class));
            }
        });

    }

    private void getVersionPrinter(){
        pref1 = getSharedPreferences("BluetoothVersion", MODE_PRIVATE);
        getVersion = pref1.getString("Version", "");

    }

    private void registerOnClick(View.OnClickListener clickListener) {
        btnMobilePrinter.setOnClickListener(clickListener);
        btnBluetooth.setOnClickListener(clickListener);
        btnBluetoothOld.setOnClickListener(clickListener);
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
        if (printPort == null) {
            Log.d("printPort==>","null");
        } else {
            printPort.disconnect();
            isConnected = false;
        }
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(getVersion.equals("New")){
            if (!printer_name.equals("")) {
                tvName.setText(printer_name);
                btnTestPrint.setVisibility(View.VISIBLE);
                isConnected = true;
                ScaleLayoutActivity.isConnected = true;
            } else {
                tvName.setText("មិនទាន់ភ្ជាប់ម៉ាស៊ីនព្រីន");
                isConnected = false;
                ScaleLayoutActivity.isConnected = false;
            }
        }else {
            if (!printer_name.equals("")) {
                tvName.setText(printer_name);
                btnTestPrint.setVisibility(View.VISIBLE);
                isConnected = true;
                ScaleLayoutActivity.isConnected = true;
            } else {
                tvName.setText("មិនទាន់ភ្ជាប់ម៉ាស៊ីនព្រីន");
                isConnected = false;
                ScaleLayoutActivity.isConnected = false;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_mobile_printer:
                if (printPort == null) {
                    visibleButtonMobilePrinter();
                    choosePrinter("MobilePrinter");
                    gotoHome();
                } else {
                    visibleButtonMobilePrinter();
                    choosePrinter("MobilePrinter");
                    gotoHome();
                    printPort.disconnect();
                    isConnected = false;
                }
                break;

            case R.id.button_bluetooth:
                llBluetoothPrinter.setVisibility(View.VISIBLE);
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                visibleButtonBluetooth();
                choosePrinter("BlueTooth");
                bluetoothVersion("New");
                getVersionPrinter();
                //shareBluetoothName("");
                SharedPreferencesType("");
                tvName.setText("មិនទាន់ភ្ជាប់ម៉ាស៊ីនព្រីន");
                break;

            case R.id.button_bluetooth_old:
                llBluetoothPrinter.setVisibility(View.VISIBLE);
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                visibleButtonBluetoothOld();
                choosePrinter("BlueTooth");
                bluetoothVersion("Old");
                getVersionPrinter();
               // shareBluetoothName("");
                SharedPreferencesType("");
                tvName.setText("មិនទាន់ភ្ជាប់ម៉ាស៊ីនព្រីន");
                break;
        }
    }

    private void visibleButtonMobilePrinter() {
        btnMobilePrinter.setBackground(getDrawable(R.color.colorAccent));
        btnBluetooth.setBackground(getDrawable(R.color.colorAsbestos));
        btnBluetoothOld.setBackground(getDrawable(R.color.colorAsbestos));
    }

    private void visibleButtonBluetooth() {
        btnMobilePrinter.setBackgroundColor(getResources().getColor(R.color.colorAsbestos));
        btnBluetooth.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        btnBluetoothOld.setBackgroundColor(getResources().getColor(R.color.colorAsbestos));
    }

    private void visibleButtonBluetoothOld() {
        btnMobilePrinter.setBackgroundColor(getResources().getColor(R.color.colorAsbestos));
        btnBluetooth.setBackgroundColor(getResources().getColor(R.color.colorAsbestos));
        btnBluetoothOld.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    private void gotoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void choosePrinter(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("PrinterName", MODE_PRIVATE).edit();
        editor.putString("Name", name);
        editor.apply();
        Log.e("", "choosePrinter: " + name );
    }

    private void bluetoothVersion(String version) {
        SharedPreferences.Editor editor = getSharedPreferences("BluetoothVersion", MODE_PRIVATE).edit();
        editor.putString("Version", version);
        editor.apply();
    }


    @SuppressLint("MissingPermission")
    private void getChoosePrinter() {
        SharedPreferences prefs = getSharedPreferences("PrinterName", MODE_PRIVATE);
        getName = prefs.getString("Name", "");

        Log.d("NamePrinter==>", "" + getName);
        if (getName.equals("BlueTooth")) {
            btnMobilePrinter.setBackground(getDrawable(R.color.colorAsbestos));
            if(getVersion.equals("New")){
                btnBluetooth.setBackground(getDrawable(R.color.colorAccent));
                btnBluetoothOld.setBackground(getDrawable(R.color.colorAsbestos));
            }else {
                btnBluetooth.setBackground(getDrawable(R.color.colorAsbestos));
                btnBluetoothOld.setBackground(getDrawable(R.color.colorAccent));
            }
            llBluetoothPrinter.setVisibility(View.VISIBLE);
            // start screen select printer

            if(getVersion.equals("New")){
                if(KmCreate.getInstance().connectType != 1){
                    Intent intent = new Intent(this, BluetoothActivity.class);
                    intent.putExtra("type","经典");
                    startActivityForResult(intent,1);
                }
            } else {
              //  connectedPrinter();
                //  showDeviceDataList();

                if((HomeActivity.blueDevice) == null){
                    Intent intent = new Intent(this, ScanActivity.class);
                    intent.putExtra("type","经典");
                    startActivityForResult(intent,1);
                }else {
                    progressDialog.show();
                    connection = Bluetooth.getInstance().createConnectionClassic( HomeActivity.blueDevice, new ConnectListener() {
                        @Override
                        public void onConnectSuccess (ConnectedDevice connectedDevice){
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("", "run: "+ msg );
                                        if (msg.equals("STATE_CONNECTED")) {
                                            tvName.setText(HomeActivity.blueDevice.getName());
                                            btnTestPrint.setVisibility(View.VISIBLE);
                                            checkSelect();
                                            SharedPreferencesType(HomeActivity.blueDevice.getAddress());
                                            progressDialog.dismiss();
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
                }
            }

           // connectedPrinter();
        } else if (getName.equals("NoDevice")) {
            btnMobilePrinter.setBackground(getDrawable(R.color.colorAsbestos));
            if(getVersion.equals("New")){
                btnBluetooth.setBackground(getDrawable(R.color.colorAccent));
                btnBluetoothOld.setBackground(getDrawable(R.color.colorAsbestos));
            }else {
                btnBluetooth.setBackground(getDrawable(R.color.colorAsbestos));
                btnBluetoothOld.setBackground(getDrawable(R.color.colorAccent));
            }
            llBluetoothPrinter.setVisibility(View.VISIBLE);
            // start screen select printer
            if(getVersion.equals("New")){
                if(KmCreate.getInstance().connectType != 1){
                    Intent intent = new Intent(this, BluetoothActivity.class);
                    intent.putExtra("type","经典");
                    startActivityForResult(intent,1);
                }
            } else {
                Intent intent = new Intent(this, ScanActivity.class);
                intent.putExtra("type","经典");
                startActivityForResult(intent,1);
               // connectedPrinter();
               // showDeviceDataList();
            }
        } else {
            llBluetoothPrinter.setVisibility(View.GONE);
            btnMobilePrinter.setBackground(getDrawable(R.color.colorAccent));
            btnBluetoothOld.setBackground(getDrawable(R.color.colorAsbestos));
            btnBluetooth.setBackground(getDrawable(R.color.colorAsbestos));

/*            if(getVersion.equals("New")){
                btnBluetooth.setBackground(getDrawable(R.color.colorAccent));
                btnBluetoothOld.setBackground(getDrawable(R.color.colorAsbestos));
            }else {
                btnBluetooth.setBackground(getDrawable(R.color.colorAsbestos));
                btnBluetoothOld.setBackground(getDrawable(R.color.colorAccent));
            }*/
        }
    }


    private void choosePrinter() {
        SharedPreferences prefs = getSharedPreferences("BluetoothVersion", MODE_PRIVATE);
        String getVersion = prefs.getString("Version", "");
        if(getVersion.equals("New")){
            Intent intent = new Intent(this, BluetoothActivity.class);
            intent.putExtra("type","经典");
            startActivityForResult(intent,1);
        } else {
           // showDeviceDataList();
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra("type","经典");
            startActivityForResult(intent,1);
            SharedPreferences.Editor editor = getSharedPreferences("Name", MODE_PRIVATE).edit();
            editor.putString("selectedType", "Bluetooth");
            editor.apply();
        }

    }

    public  void connected(){
        System.out.println("Printer connected");
        Intent data = new Intent();
        setResult(22,data);
        finish();
    }


    // old

    private void showDeviceDataList() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //If the Bluetooth adapter is not supported,programmer is over
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        Intent serverIntent = new Intent(this, DeviceShowListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
        printPort = new PrintPort();
    }

    private void checkSelect() {
        if (tvName.getText().toString().equals(getResources().getString(R.string.no_device_connecting))) {
            choosePrinter("MobilePrinter");
        } else {
            if (printPort != null) {
                printPort.disconnect();
                isConnected = false;
            }
            if(getVersion.equals("New")){
                visibleButtonBluetooth();
            } else {
                visibleButtonBluetoothOld();
            }

            choosePrinter("BlueTooth");
        }
    }

    /*public void connectedPrinter() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //If the Bluetooth adapter is not supported,programmer is over
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
        printPort = new PrintPort();
    }*/

    private void shareBluetoothName(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("PrinterName", MODE_PRIVATE).edit();
        editor.putString("BluetoothName", name);
        editor.apply();
    }

    private void SharedPreferencesType(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("Printer", MODE_PRIVATE).edit();
        editor.putString("PrinterType", name);
        editor.apply();
    }

/*
    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        progressDialog.hide();
        if (D) {
            Log.d("this", "onActivityResult " + resultCode);
        }
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    if (isConnected & (printPort != null)) {
                        printPort.disconnect();
                        isConnected = false;
                    }
                    String dataValue = data.getExtras().getString(EXTRA_DEVICE_ADDRESS);
                    String address = dataValue.substring(dataValue.length() - 17);
                    String name = dataValue.substring(0, (dataValue.length() - 17));
                    if (!isConnected) {

                        Log.e("TAG", "onActivityResult: " + dataValue );
                        Log.e("TAG", "onActivityResult: " + address );
                        Log.e("TAG", "onActivityResult: " + name );


                        if (printPort.connect(address)) {
                            isConnected = true;
                            tvName.setText(name);
                            shareBluetoothName(name);
                            SharedPreferencesType(dataValue);
                            checkSelect();
                            btnTestPrint.setVisibility(View.VISIBLE);
                        } else {
                            Log.e("", "onActivityResult: 2 " );
                            tvName.setText("មិនទាន់ភ្ជាប់ម៉ាស៊ីនព្រីន");
                            isConnected = false;
                            printPort.disconnect();
                        }
                    }
                    progressDialog.hide();
                }
                break;
            case REQUEST_ENABLE_BT:
        }
    }
*/

    private void dataListen(ConnectedDevice connectedDevice) {
        DataListener.with(connectedDevice).listen(new ListenAction() {
            @Override
            public void action(byte[] bytes) {
                //固件回传的数据
            }
        }).start();
    }


}
