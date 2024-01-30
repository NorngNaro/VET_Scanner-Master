package com.udaya.virak_buntham.vetpickup.bluetoothprinter;

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
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
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
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.print.PrintData;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;

import java.io.IOException;
import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LocalPrintOldActivity extends AppCompatActivity implements OnInternetConnectionListener {

    private ProgressDialog progressDialog;
    private PrintPort printPort;
    private static final boolean D = true;
    private boolean isConnected = false;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private BluetoothAdapter mBluetoothAdapter = null;
    private static final String TAG = "LocalPrintActivity";
    LinearLayout btn, btnFind;

    //intent DataFrom Printer Data
    PrintData printData;
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
    @BindView(R.id.llDelivery)
    LinearLayout llDelivery;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvTitleArrival)
    TextView tvTitleArrival;

    public static TextView tvName;


    String valuePaid = " ";
    String valueCOD = " ";
    String totalAmount = "";
    String printCOD = "";
    String titleCOD = "";
    int point;
    public static String deliveryArea = "";
    ArrayList<QrCodePrintItem> itemQr = new ArrayList<>();
    public final static int QRCodeWidth = 800;
    LinearLayout ll;
    LinearLayout llQrItem;
    EditText editSize;
    Bitmap bitmap;

    private GenericTSPL tspl;

    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_print);
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage(getResources().getString(R.string.loadings));
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        ButterKnife.bind(this);
        resizeLayout();
        tvName= findViewById(R.id.tvNamePrinterConnected);
        tvName.setText("កំពុងដំណើរការ...");
      //  connectedPrinter();
        blueToothConnect();
        getIntentData();
        getPrintData();
        try {
            setDataToItem(1);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        tvDestinationCode.setVisibility(View.GONE);
      //  Connected();
        btn = findViewById(R.id.button_print);
        btnFind = findViewById(R.id.button_send);
        btnFind.setOnClickListener(v -> choosePrinter());
        btn.setOnClickListener(v -> {
            btn.setClickable(false);
            btn.setEnabled(false);
            Toast.makeText(LocalPrintOldActivity.this, "Printing", Toast.LENGTH_SHORT).show();
            int sizeItem = Integer.parseInt(printData.getItemQTY());
            FrameLayout view = findViewById(R.id.dynamicLinear);
            printBmpData(view);
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
            Log.d("no", "data");
        } else {
            showDeviceList();
        }
    }*/

/*    private void connectedPrinter() {
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
    }*/

    public void printBmpData(FrameLayout view) {

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
            BtPrinterCommandTSPL printer = new BtPrinterCommandTSPL();
            data.add(printer.BtPrinter_CreatePage(100, 100));
            data.add(printer.BtPrinter_Direction(1, 0));
            data.add(printer.BtPrinter_Cut(false));
            data.add(printer.BtPrinter_SetGap(false));
            data.add(printer.BtPrinter_Speed(6));
            data.add(printer.BtPrinter_Density(15));
            data.add(printer.BtPrinter_Cls());
            data.add(printer.BtPrinter_DrawPic(0, 0, loadBitmapFromView(view)));
            data.add(printer.BtPrinter_PrintPage(1));
            printPort.portSendCmd(data);
        }*/
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

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
//                    String dataValue = "QR-386432703A"
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

    private void SharedPreferencesType(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("Printer", MODE_PRIVATE).edit();
        editor.putString("PrinterType", name);
        editor.apply();
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            printData = getIntent().getParcelableExtra("printData");
            assert printData != null;
            System.out.println("senderName==>" + printData.getSender());
            System.out.println("ReceiverName==>" + printData.getReceiver());
        }
    }

    @SuppressLint("SetTextI18n")
    private void getPrintData() {
        if (getIntent().getExtras() != null) {
            printData = getIntent().getParcelableExtra("printData");
            assert printData != null;
            tvCode.setText(printData.getTransferCode());
//            try {
//                bitmap = TextToImageEncode(printData.getTransferCode());
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }
//            imgCode.setImageBitmap(bitmap);
//            try {
//                imgCode.setImageBitmap(TextToImageEncode(printData.getTransferCode()));
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }
            imgCode.setImageBitmap(stringToQRCode(printData.getTransferCode()));

            tvSenderTelPrint.setText(printData.getSender());
            tvReceiverTelPrint.setText(printData.getReceiver());
            tvFromDesPrint.setText(printData.getBranchName());
            tvFromToPrint.setText(printData.getDestinationTo());
            tvBrandNameFromPrint.setText(printData.getBranchName());
            tvBrandFromTelPrint.setText(printData.getBranchFromTel());
            tvBrandNameToPrint.setText(printData.getBranchToName());
            tvBrandTelToPrint.setText(printData.getBranchToTel());
            tvCreatedDate.setText(printData.getPrintDate());
            tvTransferFeePrint.setText(printData.getTransferFee() + "៛");
            tvDeliveryFeePrint.setText(printData.getDeliveryFee() + "៛");
            tvItemValuePrint.setText(printData.getItemValue() + "$ (ទទួលបានពីអតិថិជន)");
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
        }
    }

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

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


    private void setPrintLayout(int i) {
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
        try {
            bitmap = TextToImageEncode(printData.getTransferCode() + "," + printData.getReceiver() + "," + i);
        } catch (WriterException e) {
            e.printStackTrace();
            Log.d("error==>", "" + e.getMessage());
        }

        itemQr.add(new QrCodePrintItem(
                printData.getBranchName(),
                printData.getDestinationTo(),
                printData.getReceiver(),
                printData.getItemName(),
                printData.getItemCode() + "/" + i,
                totalAmount,
                printCOD,
                titleCOD,
                "(" + i + "/" + printData.getItemQTY() + ")",
//                bitmap,
                stringToQRCode(printData.getTransferCode() + "," + printData.getReceiver() + "," + i),
                name
        ));

    }

    public void Back(View view) {
        AlertDialogUtil.dialogAlert(this);
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(this.getResources().getString(R.string.information));
        sweetAlertDialog.setContentText(this.getResources().getString(R.string.do_you_want_exit));
        sweetAlertDialog.setConfirmClickListener(sDialog -> {
            startActivity(new Intent(getBaseContext(), HomeActivity.class));
         //   printPort.disconnect();
            isConnected = false;
        });
        sweetAlertDialog.setCancelButton(this.getResources().getString(R.string.cancel), SweetAlertDialog::dismissWithAnimation).show();
        Button btn = sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
        btn.setText(getBaseContext().getResources().getString(R.string.ok));
    }

    private void choosePrinter() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LocalPrintOldActivity.this);
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

    private void showDeviceList() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //If the Bluetooth adapter is not supported,programmer is over
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        } else {
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        }
        printPort = new PrintPort();
    }

    private void resizeLayout() {
        editSize = findViewById(R.id.edtScaleType);
        Button btnSide = findViewById(R.id.btnResize);
//        final FrameLayout layout = findViewById(R.id.dynamicLinear);
//        final FrameLayout layouts = findViewById(R.id.dynamicQrLinear);
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
            setPrintLayout(1);
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
            setPrintLayout(1);
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
            setPrintLayout(1);
            float data = value * 100;
            @SuppressLint("DefaultLocale") String strDouble = String.format("%.0f", data);
            editSize.setText("" + strDouble);
        }
    }

    //print QrItem
    public void printBmpQrData(FrameLayout view) {

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

       /* if (isConnected) {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap bitmap = view.getDrawingCache();
            Canvas c = new Canvas(bitmap);
            c.drawColor(0, PorterDuff.Mode.CLEAR);
            ArrayList<byte[]> data = new ArrayList<>();
            BtPrinterCommandTSPL printer = new BtPrinterCommandTSPL();
            data.add(printer.BtPrinter_CreatePage(100, 100));
            data.add(printer.BtPrinter_Direction(1, 0));
            data.add(printer.BtPrinter_Cut(false));
            data.add(printer.BtPrinter_SetGap(false));
            data.add(printer.BtPrinter_Speed(6));
            data.add(printer.BtPrinter_Density(15));
            data.add(printer.BtPrinter_Cls());
            data.add(printer.BtPrinter_DrawPic(0, 0, loadBitmapFromView(view)));
            data.add(printer.BtPrinter_PrintPage(1));
            printPort.portSendCmd(data);
        }*/
    }

    private void setDataQrItem(int i) throws WriterException {
        setDataToItem(i);
        FrameLayout viewLayout = findViewById(R.id.dynamicQrLinear);
        printBmpQrData(viewLayout);
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
        tvDestinationToPrint.setText(printData.getDestinationTo());
//        if (deliveryArea.isEmpty()) {
//            llDelivery.setVisibility(View.GONE);
//        } else {
//            tvDestinationToPrint.setText(printData.getDestinationTo());
//        }
        tvDesFromPint.setText(printData.getDestinatioFrom());
        tvToPint.setText(printData.getDestinationTo());
        tvQrItemValuePrint.setText(printData.getItemName() + "(" + printData.getItemQTY() + printData.getItemUOM() + ")");
        view_print_qr_item_code.setText(printData.getItemCode() + "/" + i);
        tvQrTotalAmountPrint.setText(totalAmount);
//        tvDestinationToPrint.setText(printData.getDestinationTo());
        tvUserNamePrint.setText(name);
        tvQtyItemPrint.setText("(" + i + "/" + printData.getItemQTY() + ")");
        view_print_qr_receiver.setText(printData.getReceiver());
        tvCODPrintQr.setText(printCOD);
        tvTitleCOD.setText(titleCOD);
        tvDatePrint.setText(printData.getPrintDate());
        try {
            bitmap = TextToImageEncode(printData.getTransferCode() + "," + printData.getReceiver() + "," + i);
        } catch (WriterException e) {
            e.printStackTrace();
            Log.d("error==>", "" + e.getMessage());
        }
//        imgPrintQRCode.setImageBitmap(bitmap);
        imgPrintQRCode.setImageBitmap(stringToQRCode(printData.getTransferCode() + "," + printData.getReceiver() + "," + i));
        tvCustomerName.setText(printData.getCustomerName());
    }

    private Bitmap stringToQRCode(String inputValue) {
        QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, 100);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);
        bitmap = qrgEncoder.getBitmap();
        return bitmap;
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
                                    Toast.makeText(LocalPrintOldActivity.this, "ការភ្ជាប់ជោគជ័យ", Toast.LENGTH_SHORT).show();
                                    tvName.setText(HomeActivity.blueDevice.getName());
                                }else {
                                    if(HomeActivity.blueDevice == null){
                                        tvName.setText("ភ្ជាប់បរាជ័យ");
                                        Toast.makeText(LocalPrintOldActivity.this, "ការតភ្ជាប់បរាជ័យ", Toast.LENGTH_SHORT).show();

                                        // Open for connection
                                        Intent intent = new Intent(LocalPrintOldActivity.this, ScanActivity.class);
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
        }else {
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