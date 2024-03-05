package com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode;


import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hztytech.printer.sdk.km_blebluetooth.KmBlebluetooth;
import com.hztytech.printer.sdk.km_blebluetooth.KmBlebluetoothAdapter;
import com.hztytech.printer.sdk.km_bluetooth.Kmbluetooth;
import com.hztytech.printer.sdk.km_bluetooth.KmbluetoothAdapter;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.nav.ChoosePrinterActivity;
import com.udaya.virak_buntham.vetpickup.activities.nav.ScaleLayoutActivity;

import java.util.ArrayList;

public class BluetoothActivity extends Activity {

    private static final String ACTION_USB_PERMISSION = "com.raycloud.kmprint";

    private ArrayList<BluetoothDevice> blueDevices = new ArrayList<>();

    private ListView lv_blue;
    private DeviceAdapter deviceAdapter;
    private PendingIntent pendingIntent;
    private String type;
    TextView tv_current;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list1);
        Intent intent = this.getIntent();
        type = intent.getStringExtra("type");
        /* System.out.println("-------" + type);*/


        lv_blue = findViewById(R.id.lv_blue);

        deviceAdapter = new DeviceAdapter();
        lv_blue.setAdapter(deviceAdapter);

        // back action
        Button button = findViewById(R.id.bar_btn);
        button.setOnClickListener(v -> {
            System.out.println("返回----");
            Kmbluetooth.getInstance().stopBluetoothDevicesDiscovery();
            finish();
        });

   //     pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        lv_blue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                HomeActivity.blueDevice = blueDevices.get(position);

                if (type.equals("BLE")) {
                    Kmbluetooth.getInstance().getConnectDeviceOnly(blueDevices.get(position),new KmbluetoothAdapter(){
                        @Override
                        public void connectSuccess() {
                            System.out.println("连接成功");
                            KmCreate.getInstance().connectType = 1;

                            if (ContextCompat.checkSelfPermission(BluetoothActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    ActivityCompat.requestPermissions(BluetoothActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                                    return;
                                }
                            }

                            KmCreate.getInstance().connectName = blueDevices.get(position).getName();

                            Log.e("", "connectSuccess:  Meeeeee" );
                            connected();
                        }

                    });
                    /*KmBlebluetooth.getInstance().getConnectDevice(blueDevices.get(position), new KmBlebluetoothAdapter() {
                        @Override
                        public void connectSuccess() {
                            super.connectSuccess();
                            System.out.println("BLE连接成功");
                            KmCreate.getInstance().connectType = 2;

                            if (ContextCompat.checkSelfPermission(BluetoothActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    ActivityCompat.requestPermissions(BluetoothActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                                    return;
                                }
                            }

                            KmCreate.getInstance().connectName = blueDevices.get(position).getName();
                            connected();
                        }
                    });*/
                } else {
                    Kmbluetooth.getInstance().getConnectDeviceOnly(blueDevices.get(position), new KmbluetoothAdapter() {
                        @Override
                        public void connectSuccess() {
                            System.out.println("连接成功");
                            KmCreate.getInstance().connectType = 1;
                            if (ContextCompat.checkSelfPermission(BluetoothActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    ActivityCompat.requestPermissions(BluetoothActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                                    return;
                                }
                            }
                            KmCreate.getInstance().connectName = blueDevices.get(position).getName();
                            connected();

                            // can get name of printer over here
                            ChoosePrinterActivity.printer_name = blueDevices.get(position).getName();
                            SharedPreferencesType(blueDevices.get(position).getName());
                            SharedPreferencesMac(blueDevices.get(position).toString());
                        }
                    });
                }
            }
        });

        if (type.equals("BLE")) {
            KmBlebluetooth.getInstance().startBluetoothDevicesDiscovery(new KmBlebluetoothAdapter() {
                @Override
                public void findDevice(BluetoothDevice bluetoothDevice) {
                    super.findDevice(bluetoothDevice);
                    blueDevices.add(bluetoothDevice);
                    deviceAdapter.notifyDataSetChanged();
                }
            });
        } else {
            Kmbluetooth.getInstance().startBluetoothDevicesDiscovery(new KmbluetoothAdapter() {
                @Override
                public void findDevice(BluetoothDevice bluetoothDevice) {
                    blueDevices.add(bluetoothDevice);
                    deviceAdapter.notifyDataSetChanged();
                }
            });
        }
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

    public void connected() {
        Toast.makeText(this, "ម៉ាស៊ីនព្រីនភ្ជាប់ជោគជ័យ", Toast.LENGTH_SHORT).show();
        Intent data = new Intent();
        setResult(22, data);
        finish();
    }

    class DeviceAdapter extends BaseAdapter {

        LayoutInflater layoutInflater = LayoutInflater.from(BluetoothActivity.this);

        @Override
        public int getCount() {
            return blueDevices.size();
        }

        @Override
        public BluetoothDevice getItem(int i) {
            return blueDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.device_item1, null);
            }

            TextView tvName = convertView.findViewById(R.id.tv_name);
            TextView tvAddress = convertView.findViewById(R.id.tv_address);
            BluetoothDevice bluetoothDevice = getItem(i);

            if (ContextCompat.checkSelfPermission(BluetoothActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(BluetoothActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                }
            }
            tvName.setText(bluetoothDevice.getName());
            tvAddress.setText(bluetoothDevice.getAddress());
            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Kmbluetooth.getInstance().stopBluetoothDevicesDiscovery();
    }
}
