package com.udaya.virak_buntham.vetpickup.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.hztytech.printer.sdk.km_bluetooth.Kmbluetooth;
import com.printer.psdk.device.bluetooth.Bluetooth;
import com.printer.psdk.device.bluetooth.BluetoothStateListen;
import com.printer.psdk.device.bluetooth.DiscoveryListen;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.nav.ChoosePrinterActivity;
import com.udaya.virak_buntham.vetpickup.activities.nav.ScaleLayoutActivity;

import java.util.ArrayList;
import java.util.List;


public class ScanActivity extends AppCompatActivity {

    private myAdapter listAdapter;
    private TextView tvEmpty;
    private Button tvScan;
    private EditText edit_name;
    private final List<Device> devList = new ArrayList<>();
    private final List<Device> searchList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        initViews();

        Bluetooth.getInstance().initialize(getApplication());
        Bluetooth.getInstance().setDiscoveryListener(discoveryListener);
        Bluetooth.getInstance().setBluetoothStateListener(bluetoothStateListen);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                1);
    }

    @SuppressLint("MissingPermission")
    private void initViews() {

        ListView lv = findViewById(R.id.lv);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvScan = findViewById(R.id.tvScan);
        edit_name = findViewById(R.id.edit_name);
        listAdapter = new myAdapter(this, devList);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            HomeActivity.blueDevice = devList.get(position).device;
            // can get name of printer over here
            ChoosePrinterActivity.printer_name = devList.get(position).device.getName();
            SharedPreferencesType(devList.get(position).device.getName());
            SharedPreferencesMac(devList.get(position).device.toString());
            Intent data = new Intent();
            setResult(22, data);
            finish();

        });

        Button button = findViewById(R.id.bar_btn);
        button.setOnClickListener(v -> {
            System.out.println("返回----");
            Kmbluetooth.getInstance().stopBluetoothDevicesDiscovery();
            finish();
        });

        tvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Bluetooth.getInstance().isInitialized()) {
                    if (tvScan.getText().toString().equals("ស្វែងរក")) {
                        doStartDiscovery();

                    } else {
                        Bluetooth.getInstance().stopDiscovery();
                        tvScan.setText("ស្វែងរក");
                    }
                }
            }
        });
        edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchList.clear();
                if (editable.toString().equals("")) {
                    listAdapter = new myAdapter(ScanActivity.this, devList);
                    lv.setAdapter(listAdapter);
                } else {
                    for (Device device : devList) {
                        if (device.getName() != null) {
                            if (device.getName().contains(editable.toString())) {
                                searchList.add(device);
                            }
                        }
                    }
                    listAdapter = new myAdapter(ScanActivity.this, searchList);
                    lv.setAdapter(listAdapter);
                }
            }
        });
    }


    private final DiscoveryListen discoveryListener = new DiscoveryListen() {
        @Override
        public void onDiscoveryStart() {
            invalidateOptionsMenu();
        }

        @Override
        public void onDiscoveryStop() {
            invalidateOptionsMenu();
        }

        @Override
        public void onDiscoveryError(int errorCode, String errorMsg) {
            Log.e("", "onDiscoveryError: " + errorMsg  + errorCode);
            switch (errorCode) {
                case DiscoveryListen.ERROR_LACK_LOCATION_PERMISSION://缺少定位权限
                    ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
                    Log.e("", "ERROR_LACK_LOCATION_PERMISSION: ");
                    break;
                case DiscoveryListen.ERROR_LOCATION_SERVICE_CLOSED://位置服务未开启
                    Log.e("", "ERROR_LOCATION_SERVICE_CLOSED: ");
                    break;
                case DiscoveryListen.ERROR_LACK_SCAN_PERMISSION://缺少搜索权限
                    Log.e("", "ERROR_LACK_SCAN_PERMISSION: ");
                    break;
                case DiscoveryListen.ERROR_SCAN_FAILED://搜索失败
                    Log.e("", "ERROR_SCAN_FAILED: ");
                    break;
            }
        }


        @Override
        public void onDeviceFound(BluetoothDevice device, int rssi) {
            tvEmpty.setVisibility(View.INVISIBLE);
            Device dev = new Device(device, rssi);
            if (!devList.contains(dev) && dev.getName() != null) {
                if (!dev.getName().trim().equals("") && !dev.getName().isEmpty() && !dev.getName().endsWith("_BLE") && !dev.getName().endsWith("-LE")) {
                    devList.add(dev);
                    listAdapter.notifyDataSetChanged();
                }
            }
        }
    };
    private final BluetoothStateListen bluetoothStateListen = new BluetoothStateListen() {

        @Override
        public void onBluetoothAdapterStateChanged(int i) {

        }
    };

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        if (Bluetooth.getInstance().isInitialized()) {
            if (Bluetooth.getInstance().isEnabledBluetooth()) {
                doStartDiscovery();
            } else {
                startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Bluetooth.getInstance().isInitialized()) {
            Bluetooth.getInstance().stopDiscovery();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Bluetooth.getInstance().isInitialized()) {
            Bluetooth.getInstance().stopDiscovery();
        }
    }

    private void doStartDiscovery() {
        devList.clear();
        listAdapter.notifyDataSetChanged();
        tvEmpty.setVisibility(View.VISIBLE);
        Bluetooth.getInstance().startDiscovery();
        tvScan.setText("ស្វែងរក");
    }

    public class myAdapter extends BaseAdapter {

        private List<Device> mList;
        private Context mContext;
        private LayoutInflater mInflater;

        public myAdapter(Context mContext, List<Device> mList) {
            this.mList = mList;
            this.mContext = mContext;
            this.mInflater = LayoutInflater.from(mContext);

        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            //如果缓存convertView为空，则需要创建View
            if (convertView == null) {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.item_scan, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
                holder.tvAddr = (TextView) convertView.findViewById(R.id.tvAddr);
                holder.tvRssi = (TextView) convertView.findViewById(R.id.tvRssi);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(TextUtils.isEmpty(mList.get(i).getName()) ? "N/A" : mList.get(i).getName());
            holder.tvAddr.setText(mList.get(i).device.getAddress());
            holder.tvRssi.setText(mList.get(i).rssi + "");

            return convertView;

        }

    }

    //ViewHolder静态类
    static class ViewHolder {
        public TextView tvName;
        public TextView tvAddr;
        public TextView tvRssi;
    }

    private void SharedPreferencesType(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("Printer", MODE_PRIVATE).edit();
        editor.putString("PrinterType", name);
        editor.apply();
    }

    private void SharedPreferencesMac(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("PrinterMacAddress", MODE_PRIVATE).edit();
        editor.putString("PrinterMac", name);
        editor.apply();
    }



}
