package com.udaya.virak_buntham.vetpickup.activities.changeDestination;

import static com.udaya.virak_buntham.vetpickup.activities.changeDestination.DeviceListChangeActivity.EXTRA_DEVICE_ADDRESS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.application.print.PrintPort;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
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
import com.udaya.virak_buntham.vetpickup.activities.nav.ScaleLayoutOldActivity;
import com.udaya.virak_buntham.vetpickup.utils.InputMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PrintLayoutOldActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.edtScaleType)
    EditText editSize;
    @BindView(R.id.llQrItem)
    LinearLayout llQrItem;
    @BindView(R.id.btnResize)
    Button btnResize;
    @BindView(R.id.llChoosePrinter)
    LinearLayout llChoosePrinter;
    @BindView(R.id.llPrint)
    LinearLayout llPrint;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    private ProgressDialog progressDialog;
    private static PrintPort printPort;
    private static final boolean D = true;
    private static boolean isConnected = false;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private BluetoothAdapter mBluetoothAdapter = null;

   public static TextView tvName ;

    @BindView(R.id.tvDesFromPint)
    TextView tvDesFromPint;
    @BindView(R.id.tvToPint)
    TextView tvToPint;
    @BindView(R.id.tvQrTotalAmountPrint)
    TextView tvQrTotalAmountPrint;
    @BindView(R.id.tvQrItemValuePrint)
    TextView tvQrItemValuePrint;
    @BindView(R.id.tvCODPrintQr)
    TextView tvCODPrintQr;
    @BindView(R.id.tvTitleCOD)
    TextView tvTitleCOD;
    @BindView(R.id.tvDestinaionToPrint)
    TextView tvDestinationToPrint;
    @BindView(R.id.view_print_qr_item_code)
    TextView tvQrItemCode;
    @BindView(R.id.tvUserNamePrint)
    TextView tvUserNamePrint;
    @BindView(R.id.tvDatePrint)
    TextView tvDatePrint;
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @BindView(R.id.tvQtyItemPrint)
    TextView tvQtyItemPrint;
    @BindView(R.id.view_print_qr_receiver)
    TextView tvQrReceiver;
    @BindView(R.id.llDelivery)
    LinearLayout llDelivery;
    @BindView(R.id.imgPrintQRCode)
    ImageView imgPrintQRCode;
    @BindView(R.id.tvCondition)
    TextView tvCondition;
    @BindView(R.id.tvDesFromNote)
    TextView tvDesFromNote;
    @BindView(R.id.tvDestinationCode)
    TextView tvDestinationCode;

    public final static int QRCodeWidth = 800;

    public static String desFromPint,toPint,qrTotalAmountPrint,qrItemValuePrint,cODPrintQr,destinationToPrint,qrItemCode,userNamePrint,
    datePrint, customerName,qtyItemPrint,receiverTel,transferCode,deliveryArea,itemName,uom, desToCode;
    public  static  int itemQty;
    public  static  String condition, location_from ;

    private GenericTSPL tspl;

    private Connection connection;


    @SuppressLint("SetTextI18n")
    private  void setValue(int i) throws WriterException {
        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        String name = prefs.getString("Username", "");
        tvDesFromPint.setText(desFromPint);
        tvToPint.setText(toPint);
        tvQrTotalAmountPrint.setText("ថ្លៃដឹក:"+qrTotalAmountPrint +" ៛(PAID)");
        tvQrItemValuePrint.setText(itemName+ "(" + itemQty + uom +  ")");
        Log.e("", "setValue: " + cODPrintQr );
        if(cODPrintQr.equals("0")){
            tvTitleCOD.setVisibility(View.GONE);
            tvCODPrintQr.setVisibility(View.GONE);
        }else{
            tvTitleCOD.setVisibility(View.VISIBLE);
            tvCODPrintQr.setText(cODPrintQr);
        }
        tvQrReceiver.setText(receiverTel);
        tvQrItemCode.setText(qrItemCode);
        tvUserNamePrint.setText(name);
        tvDatePrint.setText(datePrint);
        tvCustomerName.setText(customerName);
        tvQtyItemPrint.setText(qtyItemPrint);
        tvDesFromNote.setText(location_from);
        if(!Objects.equals(desToCode, "")){
            tvDestinationCode.setVisibility(View.VISIBLE);
        }
        tvDestinationCode.setText(desToCode);
        imgPrintQRCode.setImageBitmap(TextToImageEncode(transferCode + "," + receiverTel + "," + i));
        tvQtyItemPrint.setText("(" + i + "/" + itemQty+ ")");
        if(deliveryArea.isEmpty()){
            llDelivery.setVisibility(View.GONE);
        }else {
            llDelivery.setVisibility(View.VISIBLE);
            tvDestinationToPrint.setText(deliveryArea);
        }
        switch (condition) {
            case "return":
                tvCondition.setText("(ត្រឡប់)");
                break;
            case "changeBranch":
                tvCondition.setText("(ប្តូរសាខា)");
                break;
            case "changeDestination":
                tvCondition.setText("(ប្តូរទិសដៅ)");
                break;
            case "returnHQ":
                tvCondition.setText("(ត្រឡប់ HQ)");
                break;
        }
    }
    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRCodeWidth, QRCodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.colorBlack) : getResources().getColor(R.color.colorWhite);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 800, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_layout);
        ButterKnife.bind(this);
        registerOnClick(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();
         tvName = findViewById(R.id.tvNamePrinterConnected);
        try {
            setValue(1);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        //itemQrData
        llQrItem.setScaleX(1);
        llQrItem.setScaleY(1);
        llQrItem.setPivotX(0);
        llQrItem.setPivotY(0);

        setSizeDefault();
        connectedPrinter();
        Connected();
    }
    private void setDataQrItem(int i) throws WriterException {
        setValue(i);
        FrameLayout viewLayout = findViewById(R.id.frameLayout);
        printBmpQrData(viewLayout);
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
        Intent serverIntent = new Intent(this, DeviceListChangeActivity.class);
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
        Intent serverIntent = new Intent(this, DeviceListChangeActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        printPort = new PrintPort();
    }
    @SuppressLint("SetTextI18n")
    private void setSizeDefault() {
        SharedPreferences prefs = getSharedPreferences("scaleType", MODE_PRIVATE);
        float value = prefs.getFloat("scaleValue", 0);
        if (value == 0) {
            llQrItem.setScaleX(1);
            llQrItem.setScaleY(1);
            llQrItem.setPivotX(0);
            llQrItem.setPivotY(0);
            editSize.setText("100");
        } else {
            llQrItem.setScaleX(value);
            llQrItem.setScaleY(value);
            llQrItem.setPivotX(0);
            llQrItem.setPivotY(0);
            float data = value * 100;
            @SuppressLint("DefaultLocale") String strDouble = String.format("%.0f", data);
            editSize.setText("" + strDouble);
        }
    }

    private void registerOnClick(View.OnClickListener clickListener) {
        btnResize.setOnClickListener(clickListener);
        llChoosePrinter.setOnClickListener(clickListener);
        llPrint.setOnClickListener(clickListener);
    }

    private void resizeLayout() {
        InputMethod.hideSoftKeyboard(this);
        float value = Float.parseFloat(editSize.getText().toString());
        float sizeScale = value / 100;
        llQrItem.setScaleX(sizeScale);
        llQrItem.setScaleY(sizeScale);
        SharedPreferences.Editor editor = getSharedPreferences("scaleType", MODE_PRIVATE).edit();
        editor.putFloat("scaleValue", sizeScale);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnResize:
                resizeLayout();
                break;
            case R.id.llChoosePrinter:
                /*choosePrinter();*/
                blueToothConnect();
                break;
            case R.id.llPrint:
                for (int i = 1; i <=itemQty; i++) {
                    try {
                        setDataQrItem(i);
                    } catch (WriterException e) {
                        e.printStackTrace();
                        Log.d("errorPrintLayout===>",""+e.toString());
                    }
                    Log.d("loopPrint===>",""+i);
                }
                finish();
                startActivity(new Intent(this, HomeActivity.class));
             //   printPort.disconnect();
                isConnected = false;
                break;
        }
    }


    private void printBmpQrData(FrameLayout view) {

        BitmapFactory.Options mOptions = new BitmapFactory.Options();
        mOptions.inScaled = false;
        Bitmap bitmap = loadBitmapFromView(view);
        //   bitmap = Bitmap.createScaledBitmap(bitmap, 682, 1024, true);
        GenericTSPL _gtspl = tspl.page(TPage.builder().width(100).height(100).build())
                .direction(
                        TDirection.builder()
                                .direction(TDirection.Direction.UP_OUT)
                                .mirror(TDirection.Mirror.NO_MIRROR)
                                .build()
                )
                .gap(true)
                .cut(true)
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
        }*/
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
     //   printPort.disconnect();
        isConnected = false;
    }

    public Bitmap loadBitmapFromView(View v) {
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
    }
/*    private void choosePrinter() {
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
    }*/


/*    private void showDeviceDataList() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //If the Bluetooth adapter is not supported,programmer is over
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        Intent serverIntent = new Intent(this, DeviceShowListChangeActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        printPort = new PrintPort();
    }
  */
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (D) {
            Log.d("code", "onActivityResult " + resultCode);
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
                        Log.d("address==>",""+address);
//                    DC:1D:30:8D:2F:01
                    if (!isConnected) {
                        if (printPort.connect(address)) {
                            isConnected = true;
                            tvName.setText(name);
                            SharedPreferencesType(dataValue);
                            Toast.makeText(this, "ការភ្ជាប់ជោគជ័យ", Toast.LENGTH_SHORT).show();
                        } else {
                            tvName.setText("ភ្ជាប់បរាជ័យ");
                            Toast.makeText(this, "ការតភ្ជាប់បរាជ័យ", Toast.LENGTH_SHORT).show();
                            printPort.disconnect();
                            isConnected = false;
                            printPort = new PrintPort();
                        }
                    }
                }
                progressDialog.dismiss();
                break;
            case REQUEST_ENABLE_BT:
        }
    }
*/

    private void SharedPreferencesType(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("Printer", MODE_PRIVATE).edit();
        editor.putString("PrinterType", name);
        editor.apply();
    }

    public void Back(View view) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(this.getResources().getString(R.string.information));
        sweetAlertDialog.setContentText(this.getResources().getString(R.string.do_you_want_exit));
        sweetAlertDialog.setConfirmClickListener(sDialog -> {
            finish();
            startActivity(new Intent(getBaseContext(), HomeActivity.class));
          //  printPort.disconnect();
            isConnected = false;
        });
        sweetAlertDialog.setCancelButton(this.getResources().getString(R.string.cancel), SweetAlertDialog::dismissWithAnimation).show();
        Button btn = sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
        btn.setText(getBaseContext().getResources().getString(R.string.ok));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        blueToothConnect();
    }

    void blueToothConnect(){
        if(HomeActivity.blueDevice != null){
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
                                Log.e("", "onConnectionStateChanged: " + msg );

                                if (msg.equals("STATE_CONNECTED")) {
                                    Toast.makeText(PrintLayoutOldActivity.this, "ការភ្ជាប់ជោគជ័យ", Toast.LENGTH_SHORT).show();
                                    tvName.setText(HomeActivity.blueDevice.getName());
                                }else {
                                    if(HomeActivity.blueDevice == null){
                                        tvName.setText("ភ្ជាប់បរាជ័យ");
                                        Toast.makeText(PrintLayoutOldActivity.this, "ការតភ្ជាប់បរាជ័យ", Toast.LENGTH_SHORT).show();

                                        // Open for connection
                                        Intent intent = new Intent(PrintLayoutOldActivity.this, ScanActivity.class);
                                        startActivity(intent);
                                    }
                                }
                                progressDialog.dismiss();
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
        }else {
            progressDialog.dismiss();
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra("type","经典");
            startActivityForResult(intent,1);
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