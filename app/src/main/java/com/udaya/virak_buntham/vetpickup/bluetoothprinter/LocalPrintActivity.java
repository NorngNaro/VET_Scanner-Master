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

import androidx.appcompat.app.AppCompatActivity;

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
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.BluetoothActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.KmCreate;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.Send;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.UseCase;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.print.PrintData;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceListActivity.EXTRA_DEVICE_ADDRESS;

public class LocalPrintActivity extends AppCompatActivity implements OnInternetConnectionListener {

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
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvDesFromNote)
    TextView tvDesFromNote;

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
    public static String location_from;

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

        // connectedPrinter();

        getIntentData();

        getPrintData();

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

        try {
            setDataToItem(1);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        tvDestinationCode.setVisibility(View.GONE);
        // Connected();
        btn = findViewById(R.id.button_print);
        btnFind = findViewById(R.id.button_send);
        btnFind.setOnClickListener(v -> choosePrinter());
        btn.setOnClickListener(v -> {
            btn.setClickable(false);
            btn.setEnabled(false);
            Toast.makeText(LocalPrintActivity.this, "Printing", Toast.LENGTH_SHORT).show();
            int sizeItem = Integer.parseInt(printData.getItemQTY());
            FrameLayout view = findViewById(R.id.dynamicLinear);
            //printBmpData(view);
            Send.writeData( UseCase.tspl_case2(LocalPrintActivity.this, loadBitmapFromView(view)), LocalPrintActivity.this);
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
            tvDesFromNote.setText(location_from);
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
            printPort.disconnect();
            isConnected = false;
        });
        sweetAlertDialog.setCancelButton(this.getResources().getString(R.string.cancel), SweetAlertDialog::dismissWithAnimation).show();
        Button btn = sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
        btn.setText(getBaseContext().getResources().getString(R.string.ok));
    }

    private void choosePrinter() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LocalPrintActivity.this);
        alertDialog.setTitle("ជ្រើសរើសប្រភេទ Printer");
        String[] items = {"Bluetooth"};
        int checkedItem = 0;
        alertDialog.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0) {

        /*        showDeviceDataList();
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

    private void setDataQrItem(int i) throws WriterException {
        setDataToItem(i);
        FrameLayout viewLayout = findViewById(R.id.dynamicQrLinear);
        Send.writeData( UseCase.tspl_case2(LocalPrintActivity.this, loadBitmapFromView(viewLayout)), LocalPrintActivity.this);

        //printBmpQrData(viewLayout);
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
}