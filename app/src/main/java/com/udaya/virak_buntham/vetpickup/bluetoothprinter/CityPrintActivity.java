package com.udaya.virak_buntham.vetpickup.bluetoothprinter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.print.PrintPort;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.hztytech.printer.sdk.km_blebluetooth.KmBlebluetooth;
import com.hztytech.printer.sdk.km_bluetooth.Kmbluetooth;
import com.hztytech.printer.sdk.km_usb.KmUsb;
import com.printer.BtPrinterCommandTSPL;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.nav.ScaleLayoutActivity;
import com.udaya.virak_buntham.vetpickup.adapters.PrintQrCodeItemAdapter;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.BluetoothActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.KmCreate;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.Send;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.UseCase;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.print.PrintData;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceListActivity.EXTRA_DEVICE_ADDRESS;

public class CityPrintActivity extends AppCompatActivity implements OnInternetConnectionListener {

    private ProgressDialog progressDialog;
    /*private static PrintPort printPort;*/
    private static final boolean D = true;
    private static boolean isConnected = false;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private BluetoothAdapter mBluetoothAdapter = null;
    private static final String TAG = "CityPrintActivity";
    LinearLayout btn, btnFind;

    //intent DataFrom Printer Data
    PrintData printData;

    byte[] data = new byte[2];

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvCodePrint)
    TextView tvCode;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvSenderTelPrint)
    TextView tvSenderTelPrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvRecieverTelPrint)
    TextView tvReceiverTelPrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvFromDesPrint)
    TextView tvFromDesPrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvFromToPrint)
    TextView tvFromToPrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvBrandNameFromprint)
    TextView tvBrandNameFromPrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvBrandFromTelPrint)
    TextView tvBrandFromTelPrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvBrandNameToPrint)
    TextView tvBrandNameToPrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvBrandTelToPrint)
    TextView tvBrandTelToPrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvcreatedDate)
    TextView tvCreatedDate;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvTranferFeePrint)
    TextView tvTransferFeePrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvDeliveryFeePrint)
    TextView tvDeliveryFeePrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvTotalFeePrint)
    TextView tvTotalFeePrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvItemTypePrint)
    TextView tvItemTypePrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvCODPrint)
    TextView tvCODPrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvItemNamePrint)
    TextView tvItemNamePrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvItemValuePrint)
    TextView tvItemValuePrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.imgTranferCode)
    ImageView imgCode;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvDesFromPint)
    TextView tvDesFromPint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvToPint)
    TextView tvToPint;
    @BindView(R.id.tvDestinationCode)
    TextView tvDestinationCode;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvQrItemValuePrint)
    TextView tvQrItemValuePrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvQrTotalAmountPrint)
    TextView tvQrTotalAmountPrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvDestinaionToPrint)
    TextView tvDestinationToPrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvDatePrint)
    TextView tvDatePrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvUserNamePrint)
    TextView tvUserNamePrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.view_print_qr_item_code)
    TextView view_print_qr_item_code;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvQtyItemPrint)
    TextView tvQtyItemPrint;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.view_print_qr_receiver)
    TextView view_print_qr_receiver;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvCODPrintQr)
    TextView tvCODPrintQr;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvTitleCOD)
    TextView tvTitleCOD;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.imgPrintQRCode)
    ImageView imgPrintQRCode;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvTitleArrival)
    TextView tvTitleArrival;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llDelivery)
    LinearLayout llDelivery;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.imgVetLogistic)
    ImageView imgVetLogisticCity;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvDesFromNote)
    TextView tvDesFromNote;


    String valuePaid = " ";
    String valueCOD = " ";
    String totalAmount = "";
    String printCOD = "";
    String titleCOD = "";
    int point;

    //arrayRecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    ArrayList<QrCodePrintItem> itemQr = new ArrayList<>();
    public final static int QRCodeWidth = 800;
    LinearLayout ll;
    LinearLayout llQrItem;
    public static String deliveryArea = "";

    float value = 0.6f;
    EditText editSize;
    public static TextView tvName , express;
    public static String location_from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_print);
        ButterKnife.bind(this);

        tvDestinationCode.setVisibility(View.VISIBLE);
        mRecyclerView = findViewById(R.id.recyclerViewPrinter);
        tvName = findViewById(R.id.tvNamePrinterConnected);
        express = findViewById(R.id.txt_express);
        if(HomeActivity.addGoodsNew == 1){
            express.setVisibility(View.VISIBLE);
        } else {
            express.setVisibility(View.GONE);
        }
        tvName.setText("កំពុងដំណើរការ...");
        setRecyclerViewData(value);
        //Test resize layout\

        resizeLayout();
   //     connectedPrinter();
   //     getIntentData();

        try {
            getPrintData();
        } catch (WriterException e) {
            e.printStackTrace();
        }
        try {
            setDataToItem(1);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    //    Connected();

        //经典蓝牙
        Kmbluetooth.getInstance().openBluetoothAdapter(this);
        //低功耗蓝牙
        KmBlebluetooth.getInstance().openBluetoothAdapter(this);
        KmUsb.getInstance().openUsb(this);


        SharedPreferences pref = getSharedPreferences("PrinterName", MODE_PRIVATE);
        if(pref.getString("Name", "").equals("printer_wire")){
            if(HomeActivity.printerWire.isEmpty()){
                tvName.setText("ភ្ជាប់បរាជ័យ");
                Toast.makeText(this, "ការតភ្ជាប់បរាជ័យ", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "ការភ្ជាប់ជោគជ័យ", Toast.LENGTH_SHORT).show();
                tvName.setText(HomeActivity.printerWire);
            }
        }else {
            if(KmCreate.getInstance().connectType == 1){
                SharedPreferences prefs = getSharedPreferences("Printer", MODE_PRIVATE);
                String printerName = prefs.getString("PrinterType", "");
                tvName.setText(printerName);
                Toast.makeText(this, "ការភ្ជាប់ជោគជ័យ", Toast.LENGTH_SHORT).show();
            } else {
                tvName.setText("ភ្ជាប់បរាជ័យ");
                Toast.makeText(this, "ការតភ្ជាប់បរាជ័យ", Toast.LENGTH_SHORT).show();
            }
        }

        btn = findViewById(R.id.button_print);
        btnFind = findViewById(R.id.button_send);
        btnFind.setOnClickListener(v -> choosePrinter());
        btn.setOnClickListener(v -> {
            btn.setClickable(false);
            btn.setEnabled(false);
            Toast.makeText(CityPrintActivity.this, "Printing", Toast.LENGTH_SHORT).show();
            int sizeItem = Integer.parseInt(printData.getItemQTY());
            Log.d("sizeItem==>", "" + sizeItem);
            FrameLayout view = findViewById(R.id.dynamicLinear);

            Send.writeData( UseCase.tspl_case2(CityPrintActivity.this, loadBitmapFromView(view)), CityPrintActivity.this);

//            printBmpData(view);

            for (int i = 1; i <= sizeItem; i++) {
                try {
                    setDataQrItem(i);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }

//            startActivity(new Intent(getBaseContext(), HomeActivity.class));
//            printPort.disconnect();
//            isConnected = false;


        });
        //set scale layout for print
        setSizeDefault();
    }

/*    public void Connected() {
        SharedPreferences prefs = getSharedPreferences("Name", MODE_PRIVATE);
        String printerName = prefs.getString("printerName", "");
        if (printerName.equals("")) {
            Log.d("NoDevice==>", "No Device");
        } else {
            showDeviceList();
        }
    }*/

/*
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
*/

/*
    public void printBmpData(FrameLayout view) {
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
//                printPort.portSendCmd("https://homepages.cae.wisc.edu/~ece533/images/arctichare.png");
//            printPort.drawGraphic(0,0,0,0,bitmap);
        }
    }
*/

    public Bitmap loadBitmapFromView(View v) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels,
                View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels,
                        View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Log.d("Width==>", "" + v.getMeasuredWidth());
        Log.d("Height==>", "" + v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);
        return returnedBitmap;
    }

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        progressDialog.setMessage(getResources().getString(R.string.loadings));
//        progressDialog.setCancelable(false);
//        progressDialog.show();
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
                        try{
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
                        }catch (Exception e){
                             Log.d("error==>",""+e.toString());

                        }

                    }
                }
//                progressDialog.hide();
                break;
            case REQUEST_ENABLE_BT:
        }
    }
*/

/*    private void SharedPreferencesType(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("Printer", MODE_PRIVATE).edit();
        editor.putString("PrinterType", name);
        editor.apply();
    }*/

/*    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            printData = getIntent().getParcelableExtra("printData");
            assert printData != null;
            System.out.println("senderName==>" + printData.getSender());
            System.out.println("ReceiverName==>" + printData.getReceiver());
        }
    }*/

    private void getPrintData() throws WriterException {
        if (getIntent().getExtras() != null) {
            printData = getIntent().getParcelableExtra("printData");
            assert printData != null;
            tvCode.setText(printData.getTransferCode());
            try {
                imgCode.setImageBitmap(TextToImageEncode(printData.getTransferCode()));
            } catch (WriterException e) {
                e.printStackTrace();
            }
            tvSenderTelPrint.setText(printData.getSender());
            tvReceiverTelPrint.setText(printData.getReceiver());
            tvFromDesPrint.setText(printData.getBranchName());
            tvFromToPrint.setText(printData.getDestinationTo());
            tvBrandNameFromPrint.setText(printData.getBranchName());
            tvBrandFromTelPrint.setText(printData.getBranchFromTel());
            tvBrandNameToPrint.setText(printData.getBranchToName());
            tvBrandTelToPrint.setText(printData.getBranchToTel());
            tvCreatedDate.setText(printData.getPrintDate());
            tvDesFromNote.setText(location_from);
            tvTransferFeePrint.setText(printData.getTransferFee() + "៛");
            tvDeliveryFeePrint.setText(printData.getDeliveryFee() + "៛");
            tvItemValuePrint.setText(printData.getItemValue() + "$ (ទទួលបានពីអតិថិជន)" );
            valuePaid = printData.getPaid();
            point = printData.getPoint();
            if (point == 0) {
                if (valuePaid.equals("1")) {
                    tvTotalFeePrint.setText(printData.getTotalAmount() + "(PAID)");
                } else {
                    tvTotalFeePrint.setText(printData.getTotalAmount() + "(UNPAID)");
                }
            } else {
                tvTotalFeePrint.setText("0៛ (Saving Point)");

            }
            valueCOD = printData.getCollectCod();
            if (valueCOD.equals("1")) {
                tvCODPrint.setText(printData.getItemValue() + "$");
            } else {
                tvCODPrint.setText(" ");
            }
            tvItemNamePrint.setText(printData.getItemName() + " ចំនួន: " + printData.getItemQTY() + printData.getItemUOM());
//            try {
//                imgVetLogisticCity.setImageBitmap(TextToImageEncode("http://onelink.to/36dvpn"));
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }

    private void setRecyclerViewData(float scaleValue) {
        itemQr = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new PrintQrCodeItemAdapter(itemQr, scaleValue, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {

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

/*
    public void printBmpDataRecycler(RecyclerView view) {
        if (isConnected) {
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

//    private void setPrintLayout() {
//        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
//        String name = prefs.getString("Username", "");
//        if (valuePaid.equals("1")) {
//            totalAmount = "ថ្លៃដឹក:" + printData.getTotalAmount() + "៛" + "(PAID)";
//        } else {
//            totalAmount = "ថ្លៃដឹក:" + printData.getTotalAmount() + "៛" + "(UNPAID)";
//        }
//        if (valueCOD.equals("1")) {
//            printCOD = "COD(" + printData.getItemValue() + "$" + ")";
//            titleCOD = "ថ្លៃទំនិញ";
//        } else {
//            titleCOD = " ";
//            printCOD = " ";
//        }
//        int sizeItem = Integer.parseInt(printData.getItemQTY());
//        for (int i = 1; i <= sizeItem; i++) {
//            try {
//                itemQr.add(new QrCodePrintItem(
//                        printData.getBranchName(),
//                        printData.getDestinationTo(),
//                        printData.getReceiver(),
//                        printData.getItemName(),
//                        printData.getItemCode() + "/" + i,
//                        totalAmount,
//                        printCOD,
//                        titleCOD,
//                        "(" + i + "/" + printData.getItemQTY() + ")",
//                        TextToImageEncode(printData.getTransferCode() + "," + printData.getReceiver() + "," + i),
//                        name
//                ));
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences pref = getSharedPreferences("PrinterName", MODE_PRIVATE);
        if(pref.getString("Name", "").equals("printer_wire")){
            if(HomeActivity.printerWire.isEmpty()){
                tvName.setText("ភ្ជាប់បរាជ័យ");
                Toast.makeText(this, "ការតភ្ជាប់បរាជ័យ", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "ការភ្ជាប់ជោគជ័យ", Toast.LENGTH_SHORT).show();
                tvName.setText(HomeActivity.printerWire);
            }
        }else {
            if(KmCreate.getInstance().connectType == 1){
                SharedPreferences prefs = getSharedPreferences("Printer", MODE_PRIVATE);
                String printerName = prefs.getString("PrinterType", "");
                tvName.setText(printerName);
                Toast.makeText(this, "ការភ្ជាប់ជោគជ័យ", Toast.LENGTH_SHORT).show();
            } else {
                tvName.setText("ភ្ជាប់បរាជ័យ");
                Toast.makeText(this, "ការតភ្ជាប់បរាជ័យ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Back(View view) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(this.getResources().getString(R.string.information));
        sweetAlertDialog.setContentText(this.getResources().getString(R.string.do_you_want_exit));
        sweetAlertDialog.setConfirmClickListener(sDialog -> {
            startActivity(new Intent(getBaseContext(), HomeActivity.class));
          //  printPort.disconnect();
            isConnected = false;
        });
        sweetAlertDialog.setCancelButton(this.getResources().getString(R.string.cancel), SweetAlertDialog::dismissWithAnimation).show();
        Button btn = sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
        btn.setText(getBaseContext().getResources().getString(R.string.ok));
    }

    private void choosePrinter() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CityPrintActivity.this);
        alertDialog.setTitle("ជ្រើសរើសប្រភេទ Printer");
        String[] items = {"Bluetooth"};
        int checkedItem = 0;

        alertDialog.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0) {
               /* showDeviceDataList();
                SharedPreferences.Editor editor = getSharedPreferences("Name", MODE_PRIVATE).edit();
                editor.putString("selectedType", "Bluetooth");
                editor.apply();
                dialog.dismiss();*/

                dialog.dismiss();
                Intent intent = new Intent(this, BluetoothActivity.class);
                intent.putExtra("type","经典");
                startActivityForResult(intent,1);
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

/*
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
*/

/*
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
     //   printPort = new PrintPort();
    }
*/

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
            setRecyclerViewData(1f);
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

    //print QrItem
/*
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

    private void setDataQrItem(int i) throws WriterException {
        setDataToItem(i);
        FrameLayout viewLayout = findViewById(R.id.dynamicQrLinear);
        Send.writeData( UseCase.tspl_case2(CityPrintActivity.this, loadBitmapFromView(viewLayout)), CityPrintActivity.this);

       // printBmpQrData(viewLayout);
    }

    @SuppressLint("SetTextI18n")
    private void setDataToItem(int i) throws WriterException {
        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        String name = prefs.getString("Username", "");
        if (valuePaid.equals("1")) {
            totalAmount = "ថ្លៃដឹក:" + printData.getTotalAmount() + "៛" + "(PAID)";
        } else {
            totalAmount = "ថ្លៃដឹក:" + printData.getTotalAmount() + "៛" + "(UNPAID)";
        }
        if (valueCOD.equals("1")) {
            printCOD = "COD(" + printData.getItemValue() + "$" + ")";
            titleCOD = "ថ្លៃទំនិញ";
        } else {
            titleCOD = " ";
            printCOD = " ";
        }
        Log.d("getDelivery==>", "" + deliveryArea);
        if (deliveryArea.isEmpty()) {
            llDelivery.setVisibility(View.GONE);
        } else {
            tvDestinationToPrint.setText(deliveryArea);
        }
//        tvTitleArrival.setText("");
//        tvDestinationToPrint.setText("");
        tvDesFromPint.setText(printData.getDestinatioFrom());
        tvToPint.setText(printData.getDestinationTo());
        tvDestinationCode.setText(printData.getDestinationToCode());
        tvQrItemValuePrint.setText(printData.getItemName() + "(" + printData.getItemQTY() + printData.getItemUOM() + ")");
        view_print_qr_item_code.setText(printData.getItemCode() + "/" + i);
        tvQrTotalAmountPrint.setText(totalAmount);
        tvUserNamePrint.setText(name);
        tvQtyItemPrint.setText("(" + i + "/" + printData.getItemQTY() + ")");
        view_print_qr_receiver.setText(printData.getReceiver());
        tvCODPrintQr.setText(printCOD);
        tvTitleCOD.setText(titleCOD);
        tvDatePrint.setText(printData.getPrintDate());
        imgPrintQRCode.setImageBitmap(TextToImageEncode(printData.getTransferCode() + "," + printData.getReceiver() + "," + i));
        Log.d("code==>", "" + printData.getTransferCode() + "," + printData.getReceiver() + "," + i);
        tvCustomerName.setText(printData.getCustomerName());
    }

}