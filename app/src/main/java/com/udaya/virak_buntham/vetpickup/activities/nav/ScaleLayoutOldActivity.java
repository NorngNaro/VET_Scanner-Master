package com.udaya.virak_buntham.vetpickup.activities.nav;

import static com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceListActivity.EXTRA_DEVICE_ADDRESS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.application.print.PrintPort;
import com.printer.BtPrinterCommandTSPL;
import com.printer.psdk.device.adapter.ConnectedDevice;
import com.printer.psdk.device.adapter.types.WroteReporter;
import com.printer.psdk.device.bluetooth.Bluetooth;
import com.printer.psdk.device.bluetooth.ConnectListener;
import com.printer.psdk.device.bluetooth.Connection;
import com.printer.psdk.frame.father.PSDK;
import com.printer.psdk.frame.father.listener.DataListener;
import com.printer.psdk.frame.father.listener.ListenAction;
import com.printer.psdk.frame.father.types.lifecycle.Lifecycle;
import com.printer.psdk.imagep.android.AndroidSourceImage;
import com.printer.psdk.tspl.GenericTSPL;
import com.printer.psdk.tspl.TSPL;
import com.printer.psdk.tspl.args.TDirection;
import com.printer.psdk.tspl.args.TImage;
import com.printer.psdk.tspl.args.TPage;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.ScanActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceListActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceShowListActivity;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScaleLayoutOldActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    private ProgressDialog progressDialog;
    private static PrintPort printPort;
    private static final boolean D = true;
    private static boolean isConnected = false;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private BluetoothAdapter mBluetoothAdapter = null;
    private static final String TAG = "LocalPrintActivity";
    LinearLayout btn, btnFind;
    float value = 0.6f;
    EditText editSize;
    LinearLayout ll;
    LinearLayout llQrItem;
   public static TextView tvName ;

    private GenericTSPL tspl;

    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_layout);
        ButterKnife.bind(this);
        mToolbarTitle.setText("វិក្កយបត្រគំរូ");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage(getResources().getString(R.string.loadings));
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        tvName = findViewById(R.id.tvNamePrinterConnected);
        tvName.setText("កំពុងដំណើរការ...");
        resizeLayout();
/*        connectedPrinter();
        Connected();*/
        btn = findViewById(R.id.button_print);
        btnFind = findViewById(R.id.button_send);
        btnFind.setOnClickListener(v -> choosePrinter());
        btn.setOnClickListener(v -> {
            Toast.makeText(this, "Printing", Toast.LENGTH_SHORT).show();
            FrameLayout view = findViewById(R.id.dynamicLinear);
            FrameLayout viewQr = findViewById(R.id.dynamicQrLinear);

            printBmpData(view);
            printBmpData(viewQr);
        });
        setSizeDefault();

        Log.e(TAG, "onCreate: " + HomeActivity.blueDevice );

        blueToothConnect();

    }

    void blueToothConnect(){
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
                            //    Toast.makeText(ScaleLayoutActivity.this, msg, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onConnectionStateChanged: " + msg );

                            if (msg.equals("STATE_CONNECTED")) {
                                Toast.makeText(ScaleLayoutOldActivity.this, "ការភ្ជាប់ជោគជ័យ", Toast.LENGTH_SHORT).show();
                                tvName.setText(HomeActivity.blueDevice.getName());
                            }else {
                                if(HomeActivity.blueDevice == null){
                                    tvName.setText("ភ្ជាប់បរាជ័យ");
                                    Toast.makeText(ScaleLayoutOldActivity.this, "ការតភ្ជាប់បរាជ័យ", Toast.LENGTH_SHORT).show();

                                    // Open for connection
                                    Intent intent = new Intent(ScaleLayoutOldActivity.this, ScanActivity.class);
                                    startActivity(intent);
                                }
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

    @Override
    protected void onRestart() {
        super.onRestart();

        blueToothConnect();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    private void setSizeDefault() {
        SharedPreferences prefs = getSharedPreferences("scaleType", MODE_PRIVATE);
        float value = prefs.getFloat("scaleValue", 0);
        if (value == 0) {
            ll.setScaleX(1);
            ll.setScaleY(1);
            ll.setPivotX(0);
            ll.setPivotY(0);
            llQrItem.setScaleX(1);
            llQrItem.setScaleY(1);
            llQrItem.setPivotX(0);
            llQrItem.setPivotY(0);
            editSize.setText("100");
        } else {
            ll.setScaleX(value);
            ll.setScaleY(value);
            ll.setPivotX(0);
            ll.setPivotY(0);
            llQrItem.setScaleX(value);
            llQrItem.setScaleY(value);
            llQrItem.setPivotX(0);
            llQrItem.setPivotY(0);
            float data = value * 100;
            @SuppressLint("DefaultLocale") String strDouble = String.format("%.0f", data);
            editSize.setText("" + strDouble);
        }
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
        printPort = new PrintPort();
    }

    private void choosePrinter() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("ជ្រើសរើសប្រភេទ Printer");
        String[] items = {"Bluetooth"};
        int checkedItem = 0;

        alertDialog.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0) {
                showDeviceDataList();
                SharedPreferences.Editor editor = getSharedPreferences("Name", MODE_PRIVATE).edit();
                editor.putString("selectedType", "Bluetooth");
                editor.apply();
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public void Connected() {
        SharedPreferences prefs = getSharedPreferences("Name", MODE_PRIVATE);
        String printerName = prefs.getString("printerName", "");
        if (printerName.equals("")) {
            Log.d("NoDevice==>","No Device");
        } else {
            showDeviceList();
        }
    }

    private void showDeviceList() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //If the Bluetooth adapter is not supported,programmer is over
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        printPort = new PrintPort();
    }

    private void connectedPrinter() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //If the Bluetooth adapter is not supported,programmer is over
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        printPort = new PrintPort();
    }

    private void resizeLayout() {
        editSize = findViewById(R.id.edtScaleType);
        Button btnSide = findViewById(R.id.btnResize);
        final FrameLayout layout = findViewById(R.id.dynamicLinear);
        final FrameLayout layouts = findViewById(R.id.dynamicQrLinear);
        ll = findViewById(R.id.layoutLinear);
        llQrItem = findViewById(R.id.llQrItem);

        //itemData
        ll.setScaleX(1);
        ll.setScaleY(1);
        ll.setPivotX(0);
        ll.setPivotY(0);

        //itemQrData
        llQrItem.setScaleX(1);
        llQrItem.setScaleY(1);
        llQrItem.setPivotX(0);
        llQrItem.setPivotY(0);

        btnSide.setOnClickListener(view -> {
            closeKeyboard();
            float value = Float.parseFloat(editSize.getText().toString());
            float sizeScale = value / 100;
            ll.setScaleX(sizeScale);
            ll.setScaleY(sizeScale);
            llQrItem.setScaleX(sizeScale);
            llQrItem.setScaleY(sizeScale);
            Log.d("ReLoadData==>", "ChangeValue");
            SharedPreferences.Editor editor = getSharedPreferences("scaleType", MODE_PRIVATE).edit();
            editor.putFloat("scaleValue", sizeScale);
            editor.apply();
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public Bitmap rotateBitmap(Bitmap original, float degrees) {
        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);
        Bitmap rotatedBitmap = Bitmap.createBitmap(original , 0, 0, original .getWidth(), original .getHeight(), matrix, true);
        return rotatedBitmap;
    }

    public void printBmpData(FrameLayout view) {

        Bitmap bitmap = loadBitmapFromView(view);
        GenericTSPL _gtspl = tspl.page(TPage.builder().width(100).height(100).build())
                .direction(
                        TDirection.builder()
                                .direction(TDirection.Direction.UP_OUT)
                                .mirror(TDirection.Mirror.NO_MIRROR)
                                .build()
                )
                .gap(false)
                .cut(false)
                .cls()
                .image(
                         TImage.builder()
                                .image(new AndroidSourceImage(bitmap))
                                .compress(true)
                                .build()
                )
                .print(1);
        safeWrite(_gtspl);


        /*if (isConnected) {

//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic6);
//            FrameLayout view = (FrameLayout) findViewById(R.id.dynamicLinear);
//            LinearLayout view = (LinearLayout) findViewById(R.id.dynamicLinear);
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap bitmap = view.getDrawingCache();
            Canvas c = new Canvas(bitmap);
            c.drawColor(0, PorterDuff.Mode.CLEAR);
            ArrayList<byte[]> data = new ArrayList<>();
//            byte[] wakeup = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            BtPrinterCommandTSPL printer = new BtPrinterCommandTSPL();
//            data.add(wakeup);
            data.add(printer.BtPrinter_CreatePage(100, 100));
            // Set printing direction
            data.add(printer.BtPrinter_Direction(1, 0));
//             Can only cut the knife, automatically cut the paper without finishing a sheet
            data.add(printer.BtPrinter_Cut(false));
//             Set gap positioning
            data.add(printer.BtPrinter_SetGap(false));
            // Set speed 3
            data.add(printer.BtPrinter_Speed(6));
            // Set density
            data.add(printer.BtPrinter_Density(15));
//             Clear page buffer
            data.add(printer.BtPrinter_Cls());
            data.add(printer.BtPrinter_DrawPic(0, 0, loadBitmapFromView(view)));
            data.add(printer.BtPrinter_PrintPage(1));
            printPort.portSendCmd(data);
//                printPort.portSendCmd("https://homepages.cae.wisc.edu/~ece533/images/arctichare.png");
//            printPort.drawGraphic(0,0,0,0,bitmap);


        }*/
    }

    public Bitmap loadBitmapFromView(View v) {
        Context context = v.getContext();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
        v.layout(2, 2, v.getMeasuredWidth(), v.getMeasuredHeight());

        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);

        v.draw(c);

        return returnedBitmap;
    }

/*    public Bitmap loadBitmapFromView(View v) {

        DisplayMetrics dm = getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels,
                View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels,
                        View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Log.d("Width==>", "" + v.getMeasuredWidth());
        Log.d("Height==>", "" + v.getMeasuredHeight());
        Bitmap returnedBitmap =
                Bitmap.createBitmap(v.getMeasuredWidth(),
                        v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);

        return returnedBitmap;
    }*/



/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView tvName = findViewById(R.id.tvNamePrinterConnected);
        if (D) {
            Log.d(TAG, "onActivityResult " + resultCode);
        }
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    if (isConnected & (printPort != null)) {
                        printPort.disconnect();
                        isConnected = false;
                    }

                    String dataValue = data.getExtras().getString(EXTRA_DEVICE_ADDRESS);
//                    String data = "QR-386432703A"
                    String address = dataValue.substring(dataValue.length() - 17);
                    String name = dataValue.substring(0, (dataValue.length() - 17));

                    if (!isConnected) {
                        if (printPort.connect(address)) {
                            isConnected = true;
                            tvName.setText(name);
                            SharedPreferencesType(dataValue);
                            Toast.makeText(this, "ការភ្ជាប់ជោគជ័យ", Toast.LENGTH_SHORT).show();
                        } else {
                            printPort.disconnect();
                            isConnected = false;
                            tvName.setText("ភ្ជាប់បរាជ័យ");
                            Toast.makeText(this, "ការតភ្ជាប់បរាជ័យ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
//                progressDialog.hide();
                break;
            case REQUEST_ENABLE_BT:
        }
    }
*/

/*
    private void SharedPreferencesType(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("Printer", MODE_PRIVATE).edit();
        editor.putString("PrinterType", name);
        editor.apply();
    }

    private void printBmpQrData(FrameLayout view) {
        if (isConnected) {
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic6);
//            FrameLayout view = (FrameLayout) findViewById(R.id.dynamicLinear);
//            LinearLayout view = (LinearLayout) findViewById(R.id.dynamicLinear);
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap bitmap = view.getDrawingCache();
            Canvas c = new Canvas(bitmap);
            c.drawColor(0, PorterDuff.Mode.CLEAR);
            ArrayList<byte[]> data = new ArrayList<>();
//            byte[] wakeup = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            BtPrinterCommandTSPL printer = new BtPrinterCommandTSPL();
//            data.add(wakeup);
            data.add(printer.BtPrinter_CreatePage(100, 100));
            // Set printing direction
            data.add(printer.BtPrinter_Direction(1, 0));
//             Can only cut the knife, automatically cut the paper without finishing a sheet
            data.add(printer.BtPrinter_Cut(false));
//             Set gap positioning
            data.add(printer.BtPrinter_SetGap(false));
            // Set speed 3
            data.add(printer.BtPrinter_Speed(6));
            // Set density
            data.add(printer.BtPrinter_Density(15));
//             Clear page buffer
            data.add(printer.BtPrinter_Cls());
            data.add(printer.BtPrinter_DrawPic(0, 0, loadBitmapFromView(view)));
            data.add(printer.BtPrinter_PrintPage(1));

            printPort.portSendCmd(data);
        }
    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
     //   printPort.disconnect();
        isConnected = false;
    }


    private void dataListen(ConnectedDevice connectedDevice) {
        DataListener.with(connectedDevice).listen(new ListenAction() {
            @Override
            public void action(byte[] bytes) {
                //固件回传的数据
            }
        }).start();
    }

    private void safeWrite(PSDK psdk) {
        try {
            WroteReporter reporter = psdk.write();
            if (!reporter.isOk()) {
                throw new IOException("写入数据失败", reporter.getException());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
