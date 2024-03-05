package com.udaya.virak_buntham.vetpickup.activities.nav;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.printer.psdk.tspl.args.TText;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.ScanActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceListActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceShowListActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.BluetoothActivity;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.KmCreate;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.Send;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode.UseCase;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udaya.virak_buntham.vetpickup.bluetoothprinter.DeviceListActivity.EXTRA_DEVICE_ADDRESS;

public class ScaleLayoutActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    private ProgressDialog progressDialog;
    private static PrintPort printPort;

    private static final boolean D = true;
    public static boolean isConnected;
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

    byte[] data = new byte[2];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_layout);
        ButterKnife.bind(this);
        mToolbarTitle.setText("វិក្កយបត្រគំរូ");
        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        tvName = findViewById(R.id.tvNamePrinterConnected);
        tvName.setText("កំពុងដំណើរការ...");

       resizeLayout();

        btn = findViewById(R.id.button_print);
        btnFind = findViewById(R.id.button_send);
        btnFind.setOnClickListener(v -> choosePrinter());

        FrameLayout view = findViewById(R.id.dynamicLinear);
        FrameLayout viewQr = findViewById(R.id.dynamicQrLinear);


        btn.setOnClickListener(v -> {
            Toast.makeText(this, "Printing", Toast.LENGTH_SHORT).show();

            Send.writeData( UseCase.tspl_case2(ScaleLayoutActivity.this, loadBitmapFromViewSimple(view)), ScaleLayoutActivity.this);
            Send.writeData( UseCase.tspl_case2(ScaleLayoutActivity.this, loadBitmapFromViewSimple(viewQr)), ScaleLayoutActivity.this);
        });


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

        setSizeDefault();

    }

    public Bitmap loadBitmapFromViewSimple(View v) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels,
                        View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels,
                        View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(returnedBitmap);
        // c.rotate(180);
       // c.rotate(180, c.getWidth()/2, c.getHeight()/2);
        v.draw(c);
        return returnedBitmap;
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


    private void choosePrinter() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("ជ្រើសរើសប្រភេទ Printer");
        String[] items = {"Bluetooth"};
        int checkedItem = 0;

        alertDialog.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0) {

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

    @Override
    protected void onRestart() {
        super.onRestart();

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


}
