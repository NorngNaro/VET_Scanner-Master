package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
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
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceListActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceShowListActivity;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceListActivity.EXTRA_DEVICE_ADDRESS;

public class ChooesPrinterActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooes_printer);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);

        mToolbarTitle.setText(getResources().getString(R.string.chooes_printer));
        tvName = findViewById(R.id.tvNameDisplay);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        registerOnClick(this);
        getChoosePrinter();
        btnChoosePrinter.setOnClickListener(v -> choosePrinter());
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
            Log.d("printPort==>", "null");
        } else {
            printPort.disconnect();
            isConnected = false;
        }
        startActivity(new Intent(this, HomeActivity.class));
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
                Log.e("", "onClick: asdfdf");
                llBluetoothPrinter.setVisibility(View.VISIBLE);
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                visibleButtonBluetooth();
                choosePrinter("BlueTooth");
                break;

            case R.id.button_bluetooth_old:
                Log.e("", "onClick: asdfdf");
                llBluetoothPrinter.setVisibility(View.VISIBLE);
                if (printPort != null) {
                    printPort.disconnect();
                    isConnected = false;
                }
                Toast.makeText(this, "askjdff", Toast.LENGTH_SHORT).show();
                visibleButtonBluetoothOld();
                choosePrinter("BlueTooth");
                break;
        }
    }

    private void checkSelect() {
        if (tvName.getText().toString().equals(getResources().getString(R.string.no_device_connecting))) {
            choosePrinter("MobilePrinter");
        } else {
            if (printPort != null) {
                printPort.disconnect();
                isConnected = false;
            }
            visibleButtonBluetooth();
            choosePrinter("BlueTooth");
        }
    }

    private void visibleButtonMobilePrinter() {
        btnMobilePrinter.setBackground(getDrawable(R.color.colorAccent));
        btnBluetooth.setBackground(getDrawable(R.color.colorAsbestos));
        btnBluetoothOld.setBackgroundColor(getResources().getColor(R.color.colorAsbestos));
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

//    private void SharedPreferencesType(String name) {
//        SharedPreferences.Editor editor = getSharedPreferences("Printer", MODE_PRIVATE).edit();
//        editor.putString("PrinterType", name);
//        editor.apply();
//    }

    private void choosePrinter(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("PrinterName", MODE_PRIVATE).edit();
        editor.putString("Name", name);
        editor.apply();
    }

    private void shareBluetoothName(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("PrinterName", MODE_PRIVATE).edit();
        editor.putString("BluetoothName", name);
        editor.apply();
    }

    private void getChoosePrinter() {
        SharedPreferences prefs = getSharedPreferences("PrinterName", MODE_PRIVATE);
        getName = prefs.getString("Name", "");
        Log.d("NamePrinter==>", "" + getName);
        if (getName.equals("BlueTooth")) {
            btnMobilePrinter.setBackground(getDrawable(R.color.colorAsbestos));
            btnBluetooth.setBackground(getDrawable(R.color.colorAccent));
            llBluetoothPrinter.setVisibility(View.VISIBLE);
            connectedPrinter();
        } else if (getName.equals("NoDevice")) {
            btnMobilePrinter.setBackground(getDrawable(R.color.colorAsbestos));
            btnBluetooth.setBackground(getDrawable(R.color.colorAccent));
            llBluetoothPrinter.setVisibility(View.VISIBLE);
            connectedPrinter();
        } else {
            llBluetoothPrinter.setVisibility(View.GONE);
            btnMobilePrinter.setBackground(getDrawable(R.color.colorAccent));
            btnBluetooth.setBackground(getDrawable(R.color.colorAsbestos));
        }
    }

    public void connectedPrinter() {
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
    }


    private void choosePrinter() {
        showDeviceDataList();
        SharedPreferences.Editor editor = getSharedPreferences("Name", MODE_PRIVATE).edit();
        editor.putString("selectedType", "Bluetooth");
        editor.apply();
    }

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

    private void SharedPreferencesType(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("Printer", MODE_PRIVATE).edit();
        editor.putString("PrinterType", name);
        editor.apply();
    }

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
                    String sData = data.getExtras().getString(EXTRA_DEVICE_ADDRESS);
//                    String sData = "QR-386432703A"
                    String address = sData.substring(sData.length() - 17);
                    String name = sData.substring(0, (sData.length() - 17));
                    if (!isConnected) {
                        if (printPort.connect(address)) {
                            isConnected = true;
                            tvName.setText(name);
                            shareBluetoothName(name);
                            SharedPreferencesType(sData);
                            checkSelect();
                        } else {
                            choosePrinter("NoDevice");
                            tvName.setText("Fail To Connected");
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
}
