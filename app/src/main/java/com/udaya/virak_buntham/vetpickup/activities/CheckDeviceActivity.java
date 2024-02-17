package com.udaya.virak_buntham.vetpickup.activities;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.udaya.virak_buntham.vetpickup.listeners.SerialListener;
import com.udaya.virak_buntham.vetpickup.serial.SerialService;
import com.udaya.virak_buntham.vetpickup.serial.SerialSocket;
import com.udaya.virak_buntham.vetpickup.utils.ConstantUtil;
import com.udaya.virak_buntham.vetpickup.utils.CustomProber;

import java.util.ArrayList;
import java.util.Locale;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.utils.TextUtil;

public class CheckDeviceActivity extends ListActivity implements ServiceConnection, SerialListener {

    private SerialService service;

    private boolean initialStart = true;
    int index = 0;
    boolean connect = false;
    private int baudRate = 9600;

    private UsbSerialPort usbSerialPort;

    public static int deviceId, portNum;
    public static String location;

    private CheckDeviceActivity.Connected connected = CheckDeviceActivity.Connected.False;

    private enum Connected {False, Pending, True}

    private ArrayAdapter<CheckDeviceActivity.ListItem> listAdapter;
    private final ArrayList<CheckDeviceActivity.ListItem> listItems = new ArrayList<>();


    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if (initialStart) {
            initialStart = false;
            this.runOnUiThread(this::connect);
        }
    }

    private void connect() {
        connect(null);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }


    @Override
    public void onSerialConnect() {
        connected = CheckDeviceActivity.Connected.True;
    }

    @Override
    public void onSerialConnectError(Exception e) {
        disconnect();
        startActivity(new Intent(this, CheckDeviceActivity.class));
        finishAffinity();
    }

    @Override
    public void onSerialRead(byte[] data) {
        finish();
    }

    @Override
    public void onSerialIoError(Exception e) {

    }

    private void connect(Boolean permissionGranted) {
        UsbDevice device = null;
        UsbManager usbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);
        for (UsbDevice v : usbManager.getDeviceList().values())
            if (v.getDeviceId() == deviceId)
                device = v;
        if (device == null) {
            Toast.makeText(this, "Connection Null", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if (driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        if (driver == null) {
            Toast.makeText(this, "Driver Null", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (driver.getPorts().size() < portNum) {
            Toast.makeText(this, "Port problem", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        usbSerialPort = driver.getPorts().get(portNum);

        UsbDeviceConnection usbConnection = usbManager.openDevice(driver.getDevice());
        if (usbConnection == null && permissionGranted == null && !usbManager.hasPermission(driver.getDevice())) {
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(this, 0,
                    new Intent(ConstantUtil.INTENT_ACTION_GRANT_USB), PendingIntent.FLAG_MUTABLE);
            usbManager.requestPermission(driver.getDevice(), usbPermissionIntent);
            return;
        }

        connected = CheckDeviceActivity.Connected.Pending;

        try {
            usbSerialPort.open(usbConnection);
            usbSerialPort.setParameters(baudRate, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            SerialSocket socket = new SerialSocket(this.getApplicationContext(), usbConnection, usbSerialPort);
            service.connect(socket);
            onSerialConnect();
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }


    private void disconnect() {
        connected = CheckDeviceActivity.Connected.False;
        service.disconnect();
        usbSerialPort = null;
    }

    static class ListItem {
        UsbDevice device;
        int port;
        UsbSerialDriver driver;

        ListItem(UsbDevice device, int port, UsbSerialDriver driver) {
            this.device = device;
            this.port = port;
            this.driver = driver;
        }
    }

    LinearLayout loading, problem;

    boolean check = false;
    boolean noInternet = false;
    android.app.AlertDialog alertDialog;
    ViewGroup viewGroup;

    private boolean hexEnabled = false;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_device);

        // USB
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ConstantUtil.INTENT_ACTION_GRANT_USB.equals(intent.getAction())) {
                    Boolean granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);
                    connect(true);
                }
            }
        };

        this.bindService(new Intent(this, SerialService.class), this, Context.BIND_AUTO_CREATE);


        listAdapter = new ArrayAdapter<CheckDeviceActivity.ListItem>(this, 0, listItems) {
            @SuppressLint("SetTextI18n")
            @NonNull
            @Override
            public View getView(int position, View view, @NonNull ViewGroup parent) {
                CheckDeviceActivity.ListItem item = listItems.get(position);
                if (view == null)
                    view = getWindow().getLayoutInflater().inflate(R.layout.device_list_item, parent, false);
                TextView text1 = view.findViewById(R.id.text1);
                TextView text2 = view.findViewById(R.id.text2);
                if (item.driver == null)
                    text1.setText("<no driver>");
                else if (item.driver.getPorts().size() == 1)
                    text1.setText(item.driver.getClass().getSimpleName().replace("SerialDriver", ""));
                else
                    text1.setText(item.driver.getClass().getSimpleName().replace("SerialDriver", "") + ", Port " + item.port);
                text2.setText(String.format(Locale.US, "Vendor %04X, Product %04X", item.device.getVendorId(), item.device.getProductId()));

                return view;
            }
        };

        Log.e("", "onCreate: " + listAdapter.getCount());
        setListAdapter(listAdapter);

    }


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public void onResume() {
        super.onResume();
        refresh();
        setDevice();
        try {
            this.registerReceiver(broadcastReceiver, new IntentFilter(ConstantUtil.INTENT_ACTION_GRANT_USB));
            if (initialStart && service != null) {
                initialStart = false;
                this.runOnUiThread(this::connect);
            }
        } catch (Exception e) {
            Log.e("", "onResume: " + e);
        }
    }

    @SuppressLint("UnsafeIntentLaunch")
    void setDevice() {
        if (!connect) {
            try {
                CheckDeviceActivity.ListItem item = listItems.get(index);
                if (item.driver == null) {
                    index = index + 1;
                    setDevice();
                    if (index == listItems.size()) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                    Log.e("", "setDevice: No Device");
                    //  Toast.makeText(this, "No device", Toast.LENGTH_SHORT).show();
                } else {
                    connect = true;
                    index = 0;
                    CheckDeviceActivity.portNum = item.port;
                    CheckDeviceActivity.deviceId = item.device.getDeviceId();

                    Handler handler = new Handler();
                    handler.postDelayed(() -> send(location + ",005"), 500);

                    ScanReceiveTransitActivity.fromLed = true;
                    finish();

                }
            } catch (Exception e) {
                Log.d("error==>", "" + e);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (service != null)
            service.attach(this);
        else
            this.startService(new Intent(this, SerialService.class)); // prevents service destroy on unbind from recreated activity caused by orientation change
    }

    private void send(String str) {
        try {
            String msg;
            if (hexEnabled) {
                StringBuilder sb = new StringBuilder();
                TextUtil.toHexString(sb, TextUtil.fromHexString(str));
                msg = sb.toString();
            } else {
                msg = str;
            }
            SpannableStringBuilder spn = new SpannableStringBuilder(msg + '\n');
            spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSendText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            service.write(msg.getBytes());
        } catch (Exception e) {
            onSerialIoError(e);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            refresh();
            return true;
        } else if (id == R.id.baud_rate) {
            final String[] baudRates = getResources().getStringArray(R.array.baud_rates);
            int pos = java.util.Arrays.asList(baudRates).indexOf(String.valueOf(baudRate));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Baud rate");
            builder.setSingleChoiceItems(baudRates, pos, (dialog, item1) -> {
                baudRate = Integer.parseInt(baudRates[item1]);
                dialog.dismiss();
            });
            builder.create().show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    void refresh() {
        UsbManager usbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);
        UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();
        UsbSerialProber usbCustomProber = CustomProber.getCustomProber();
        listItems.clear();
        for (UsbDevice device : usbManager.getDeviceList().values()) {
            UsbSerialDriver driver = usbDefaultProber.probeDevice(device);
            if (driver == null) {
                driver = usbCustomProber.probeDevice(device);
            }
            if (driver != null) {
                for (int port = 0; port < driver.getPorts().size(); port++)
                    listItems.add(new CheckDeviceActivity.ListItem(device, port, driver));
            } else {
                listItems.add(new CheckDeviceActivity.ListItem(device, 0, null));
            }
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CheckDeviceActivity.ListItem item = listItems.get(position - 1);
        if (item.driver == null) {
            Toast.makeText(this, "no driver", Toast.LENGTH_SHORT).show();
        } else {
          /*  Handler handler = new Handler();
            handler.postDelayed(() -> {
                send("002,005");
            }, 3000);*/
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        check = false;
        //   checkConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        check = true;
        unregisterReceiver(broadcastReceiver);
    }
}