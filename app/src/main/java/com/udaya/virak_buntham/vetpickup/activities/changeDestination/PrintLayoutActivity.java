package com.udaya.virak_buntham.vetpickup.activities.changeDestination;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.CityPrintActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.LocalPrintActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.BluetoothActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.KmCreate;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.Send;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.UseCase;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;
import com.udaya.virak_buntham.vetpickup.utils.InputMethod;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.udaya.virak_buntham.vetpickup.activities.changeDestination.DeviceListChangeActivity.EXTRA_DEVICE_ADDRESS;

public class PrintLayoutActivity extends AppCompatActivity implements View.OnClickListener {

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

    public  static String desFromPint,toPint,qrTotalAmountPrint,qrItemValuePrint,cODPrintQr,destinationToPrint,qrItemCode,userNamePrint,
    datePrint, customerName,qtyItemPrint,receiverTel,transferCode,deliveryArea,itemName,uom, desToCode;
    public  static  int itemQty;
    public  static  String condition, location_from ;

    @SuppressLint("SetTextI18n")
    private  void setValue(int i) throws WriterException {
        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        String name = prefs.getString("Username", "");
        tvDesFromPint.setText(desFromPint);
        tvToPint.setText(toPint);
        tvQrTotalAmountPrint.setText("ថ្លៃដឹក:"+qrTotalAmountPrint +" ៛(PAID)");
        tvQrItemValuePrint.setText(itemName+ "(" + itemQty + uom +  ")");
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

        //经典蓝牙
        Kmbluetooth.getInstance().openBluetoothAdapter(this);
        //低功耗蓝牙
        KmBlebluetooth.getInstance().openBluetoothAdapter(this);
        KmUsb.getInstance().openUsb(this);


        if(KmCreate.getInstance().connectType == 1){
            SharedPreferences pref = getSharedPreferences("Printer", MODE_PRIVATE);
            String printerName = pref.getString("PrinterType", "");
            tvName.setText(printerName);
            Toast.makeText(this, "ការភ្ជាប់ជោគជ័យ", Toast.LENGTH_SHORT).show();
        } else {
            tvName.setText("ភ្ជាប់បរាជ័យ");
            Toast.makeText(this, "ការតភ្ជាប់បរាជ័យ", Toast.LENGTH_SHORT).show();
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
/*        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.setCancelable(false);
        progressDialog.show();*/
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
       // connectedPrinter();
      //  Connected();
    }
    private void setDataQrItem(int i) throws WriterException {
        setValue(i);
        FrameLayout viewLayout = findViewById(R.id.frameLayout);
        //printBmpQrData(viewLayout);
        Send.writeData( UseCase.tspl_case2(PrintLayoutActivity.this, loadBitmapFromView(viewLayout)), PrintLayoutActivity.this);
    }

/*
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
    */

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
                choosePrinter();
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
                isConnected = false;
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(KmCreate.getInstance().connectType == 1){
            SharedPreferences pref = getSharedPreferences("Printer", MODE_PRIVATE);
            String printerName = pref.getString("PrinterType", "");
            tvName.setText(printerName);
            Toast.makeText(this, "ការភ្ជាប់ជោគជ័យ", Toast.LENGTH_SHORT).show();
        } else {
            tvName.setText("ភ្ជាប់បរាជ័យ");
            Toast.makeText(this, "ការតភ្ជាប់បរាជ័យ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
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

    private void choosePrinter() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PrintLayoutActivity.this);
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
            isConnected = false;
        });
        sweetAlertDialog.setCancelButton(this.getResources().getString(R.string.cancel), SweetAlertDialog::dismissWithAnimation).show();
        Button btn = sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
        btn.setText(getBaseContext().getResources().getString(R.string.ok));
    }

}