package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.iposprinter.iposprinterservice.IPosPrinterCallback;
import com.iposprinter.iposprinterservice.IPosPrinterService;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.aildhelper.iposprinter.ThreadPoolManager;
import com.udaya.virak_buntham.vetpickup.aildhelper.iposprinter.Utils.HandlerUtils;
import com.udaya.virak_buntham.vetpickup.base.BaseApp;
import com.udaya.virak_buntham.vetpickup.models.print.PrintData;
import com.udaya.virak_buntham.vetpickup.printerutils.AidlUtil;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrintGTReceiptActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = ReportGoodsTransferActivity.class.getSimpleName();

    @BindView(R.id.layout_receipt_print)
    LinearLayout layoutPrintReceipt;
    @BindView(R.id.layout_print_qr)
    LinearLayout layoutPrintQR;
    @BindView(R.id.button_print_receipt)
    Button buttonPrintReceipt;
    @BindView(R.id._button_print_receipt)
    LinearLayout _buttonPrintReceipt;
    @BindView(R.id._button_print_qr)
    LinearLayout _buttonPrintQR;
    @BindView(R.id.button_close)
    Button buttonClose;

    @BindView(R.id.txt_branch_from)
    TextView branchFrom;
    @BindView(R.id.txt_transfer_code)
    TextView transferCode;
    @BindView(R.id.txt_item_value)
    TextView itemValue;
    @BindView(R.id.txt_transfer_date)
    TextView transferDate;
    @BindView(R.id.txt_item_type)
    TextView itemType;
    @BindView(R.id.txt_sender_phone)
    TextView sender;
    @BindView(R.id.txt_receiver_phone)
    TextView receiver;
    @BindView(R.id.txt_destination_to)
    TextView destinationTo;
    @BindView(R.id.txt_transfer_fee)
    TextView transferFee;
    @BindView(R.id.txt_discount)
    TextView discount;
    @BindView(R.id.lbl_discount)
    TextView labelDiscount;
    @BindView(R.id.txt_delivery_fee)
    TextView deliverFee;
    @BindView(R.id.txt_total_amount)
    TextView totalAmount;
    @BindView(R.id.txt_total_paid)
    TextView totalPaid;
    @BindView(R.id.txt_branch_from_name)
    TextView branchFromName;
    @BindView(R.id.txt_branch_from_tel)
    TextView branchFromTel;
    @BindView(R.id.txt_branch_to_name)
    TextView branchToName;
    @BindView(R.id.txt_branch_to_tel)
    TextView branchToTel;
    @BindView(R.id.txt_print_date)
    TextView printDate;

    @BindView(R.id.print_qr_branch_from)
    TextView printQrBranchFrom;
    @BindView(R.id.print_qr_branch_to)
    TextView printQrBranchTo;
    @BindView(R.id.print_qr_item_code)
    TextView printQrItemCode;
    @BindView(R.id.print_qr_total_amount)
    TextView printQrTotalAmount;
    @BindView(R.id.print_qr_item_type)
    TextView printQrItemType;
    @BindView(R.id.print_qr_code_img_qr)
    ImageView imageQrCode;
    @BindView(R.id.print_qr_date)
    TextView qrDate;
    @BindView(R.id.print_qr_receiver)
    TextView qrReceiver;

    @BindView(R.id.view_print_qr_branch_from)
    TextView viewPrintQrBranchFrom;
    @BindView(R.id.view_print_qr_branch_to)
    TextView viewPrintQrBranchTo;
    @BindView(R.id.view_print_qr_item_code)
    TextView viewPrintQrItemCode;
    @BindView(R.id.view_print_qr_total_amount)
    TextView viewPrintQrTotalAmount;
    @BindView(R.id.view_print_qr_item_type)
    TextView viewPrintQrItemType;
    @BindView(R.id.view_print_qr_code_img_qr)
    ImageView viewImageQrCode;
    @BindView(R.id.view_print_qr_date)
    TextView viewQrDate;
    @BindView(R.id.view_print_qr_receiver)
    TextView viewQrReceiver;

    @BindView(R.id.llSmallCOD)
    LinearLayout linearsmallCod;

    @BindView(R.id.llBigCOD)
    LinearLayout linearBigcod;


    @BindView(R.id.view_print_qr_COD_Check)
    TextView viewCollectCodValue;
    @BindView(R.id.view_qr_total_amount)
    TextView viewQrTotalAmount;
    @BindView(R.id.view_print_qr_Paid_Check)
    TextView viewCollectPaidCheck;
    @BindView(R.id.view_print_qr_COD_Check_print)
    TextView viewCollectCodValuePrint;
    @BindView(R.id.view_print_total_amount)
    TextView viewPrintTotalAmount;
    @BindView(R.id.view_print_qr_Paid_Check_print)
    TextView viewCollectPaidCheckPrint;

    @BindView(R.id.tvDeliveryToBig)
    TextView tvDeliveryToBigLayout;
    @BindView(R.id.tvDeliveryToSmall)
    TextView tvDeliveryToSmallLayout;

    @BindView(R.id.tvDesFromNote)
    TextView tvDesFromNote;
    @BindView(R.id.locationCode)
    TextView locationCode;

    BaseApp baseApp;
    public static String location_from;

// Initial new printer

    private final String PRINTER_NORMAL_ACTION = "com.iposprinter.iposprinterservice.NORMAL_ACTION";
    private final String PRINTER_PAPERLESS_ACTION = "com.iposprinter.iposprinterservice.PAPERLESS_ACTION";
    private final String PRINTER_PAPEREXISTS_ACTION = "com.iposprinter.iposprinterservice.PAPEREXISTS_ACTION";
    private final String PRINTER_THP_HIGHTEMP_ACTION = "com.iposprinter.iposprinterservice.THP_HIGHTEMP_ACTION";
    private final String PRINTER_THP_NORMALTEMP_ACTION = "com.iposprinter.iposprinterservice.THP_NORMALTEMP_ACTION";
    private final String PRINTER_MOTOR_HIGHTEMP_ACTION = "com.iposprinter.iposprinterservice.MOTOR_HIGHTEMP_ACTION";
    private final String PRINTER_BUSY_ACTION = "com.iposprinter.iposprinterservice.BUSY_ACTION";
    private final String PRINTER_CURRENT_TASK_PRINT_COMPLETE_ACTION = "com.iposprinter.iposprinterservice.CURRENT_TASK_PRINT_COMPLETE_ACTION";

    private final int MSG_TEST = 1;
    private final int MSG_IS_NORMAL = 2;
    private final int MSG_IS_BUSY = 3;
    private final int MSG_PAPER_LESS = 4;
    private final int MSG_PAPER_EXISTS = 5;
    private final int MSG_THP_HIGH_TEMP = 6;
    private final int MSG_THP_TEMP_NORMAL = 7;
    private final int MSG_MOTOR_HIGH_TEMP = 8;
    private final int MSG_MOTOR_HIGH_TEMP_INIT_PRINTER = 9;
    private final int MSG_CURRENT_TASK_PRINT_COMPLETE = 10;

    private IPosPrinterService mIPosPrinterService;
    private IPosPrinterCallback callback = null;
    private HandlerUtils.MyHandler handler;

    private String valueQrCodeData;
    public final static int QRcodeWidth = 500;
    Bitmap bitmap;

    TextView express;

    public static String deliveryArea = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_gtreceipt);
        ButterKnife.bind(this);

        express = findViewById(R.id.txt_express);

        if(HomeActivity.addGoodsNew == 1){
            express.setVisibility(View.VISIBLE);
        } else {
            express.setVisibility(View.GONE);
        }

        if (getIntent().getExtras() != null) {
            PrintData printData = getIntent().getParcelableExtra("printData");
            branchFrom.setText(printData.getBranchName());
            transferCode.setText(printData.getTransferCode());
            tvDesFromNote.setText(location_from);
            if(printData.getDestinationToCode() == null){
                locationCode.setVisibility(View.GONE);
            }else {
                locationCode.setText(printData.getDestinationToCode());
            }

//            itemValue.setText(getFeeWithSymbol(printData.getItemValue(), printData.getItemValueSymbol()));
            itemValue.setText(getFeeWithSymbol(printData.getItemValue(), " $"));
            transferDate.setText(printData.getTransferDate());
            itemType.setText(getFullItemType(printData.getItemType(), printData.getItemQTY(), printData.getItemUOM()));
            sender.setText(printData.getSender());
            receiver.setText(printData.getReceiver());
            destinationTo.setText(printData.getDestinationTo());
            transferFee.setText(getFeeWithSymbol(printData.getTransferFee(), printData.getNormalSymbol()));
            deliverFee.setText(getFeeWithSymbol(printData.getDeliveryFee(), printData.getNormalSymbol()));
            discount.setText(getFeeWithSymbol(printData.getDiscount(), printData.getNormalSymbol()));
            labelDiscount.append(getStrPercentage(printData.getPercentage()));
            totalAmount.setText(getFeeWithSymbol(printData.getTotalAmount(), printData.getNormalSymbol()));
            if (printData.getPoint()==0){
                totalPaid.setText(getFeeWithSymbol(printData.getTotalAmount(), printData.getNormalSymbol()));
            }else {
                totalPaid.setText("0៛ (Saving Point)");
            }
            branchFromName.setText(printData.getBranchName());
            branchFromTel.setText(printData.getBranchFromTel());
            branchToName.setText(printData.getBranchToName());
            branchToTel.setText(printData.getBranchToTel());
            printDate.setText(printData.getPrintDate());
            valueQrCodeData = printData.getQrCode();
            Log.d("QrCodeData==>", "" + printData.getQrCode());
            try {
                bitmap = TextToImageEncode(valueQrCodeData);
                viewImageQrCode.setImageBitmap(bitmap);
                imageQrCode.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            //QR receipt for print
            printQrBranchFrom.setText(printData.getDestinatioFrom());
            printQrBranchTo.setText(printData.getDestinationTo());
            printQrItemCode.setText(printData.getItemCode());
            printQrTotalAmount.setText(getFeeWithSymbol(printData.getTotalAmount(), printData.getNormalSymbol()));
            printQrItemType.setText(printData.getItemType());
//            GlideApp.with(this).load(printData.getQrCode()).into(imageQrCode);
            qrDate.setText(printData.getTransferDate());
            qrReceiver.setText(printData.getReceiver());

            Log.d("getCollectCode==>", "" + printData.getCollectCod());
            Log.d("getPaid==>", "" + printData.getPaid());

            if (printData.getCollectCod().equals("0")) {
                linearsmallCod.setVisibility(View.GONE);
                linearBigcod.setVisibility(View.GONE);
            } else {
                viewCollectCodValue.setText("COD(" + getFeeWithSymbol(printData.getItemValue(), printData.getItemValueSymbol()) + ")");
                viewCollectCodValuePrint.setText("COD(" + getFeeWithSymbol(printData.getItemValue(), printData.getItemValueSymbol()) + ")");
            }
//            if (printData.getPaid().equals("0")) {
//                viewCollectPaidCheck.setText("(UNPAID)");
//                viewCollectPaidCheckPrint.setText("(UNPAID)");
//            } else {
//                viewCollectPaidCheck.setText("(PAID)");
//                viewCollectCodValuePrint.setText("ទូទាត់រួច");
//                viewCollectPaidCheckPrint.setText("(PAID)");
//            }

            //QR receipt for display
            viewPrintQrBranchFrom.setText(printData.getDestinatioFrom());
            viewPrintQrBranchTo.setText(printData.getDestinationTo());
            viewPrintQrItemCode.setText(printData.getItemCode());
            Log.d("deliveryArea==> ", "" + printData.getItemCode());
            if (deliveryArea.isEmpty()) {
                tvDeliveryToBigLayout.setVisibility(View.GONE);
                tvDeliveryToSmallLayout.setVisibility(View.GONE);
            } else {
                tvDeliveryToBigLayout.setText("ដឹកដល់ផ្ទះ : " + deliveryArea);
                tvDeliveryToSmallLayout.setText("ដឹកដល់ផ្ទះ : " + deliveryArea);
                tvDeliveryToBigLayout.setVisibility(View.VISIBLE);
                tvDeliveryToSmallLayout.setVisibility(View.VISIBLE);
            }
//            Toast.makeText(getBaseContext(), "delivery Fee==> " + printData.getDeliveryFee(), Toast.LENGTH_SHORT).show();
            viewPrintQrTotalAmount.setText(getFeeWithSymbol(printData.getTotalAmount(), printData.getNormalSymbol()));
            viewPrintQrItemType.setText(printData.getItemType());
//            GlideApp.with(this).load(printData.getQrCode()).into(viewImageQrCode);
            viewPrintTotalAmount.setText(getFeeWithSymbol(printData.getTotalAmount(), printData.getNormalSymbol()));
            viewQrDate.setText(printData.getTransferDate());
            viewQrReceiver.setText(printData.getReceiver());
            viewQrTotalAmount.setText(getFeeWithSymbol(printData.getTotalAmount(), printData.getNormalSymbol()));
        }

        baseApp = (BaseApp) getApplication();
        registerClickListener(this);

        // Initial new printer
        handler = new HandlerUtils.MyHandler(iHandlerIntent);
        callback = new IPosPrinterCallback.Stub() {
            @Override
            public void onRunResult(final boolean isSuccess) throws RemoteException {
                Log.i(TAG, "result:" + isSuccess + "\n");
            }

            @Override
            public void onReturnString(final String value) throws RemoteException {
                Log.i(TAG, "result:" + value + "\n");
            }
        };

        Intent intent = new Intent();
        intent.setPackage("com.iposprinter.iposprinterservice");
        intent.setAction("com.iposprinter.iposprinterservice.IPosPrintService");
        bindService(intent, connectService, Context.BIND_AUTO_CREATE);

        IntentFilter printerStatusFilter = new IntentFilter();
        printerStatusFilter.addAction(PRINTER_NORMAL_ACTION);
        printerStatusFilter.addAction(PRINTER_PAPERLESS_ACTION);
        printerStatusFilter.addAction(PRINTER_PAPEREXISTS_ACTION);
        printerStatusFilter.addAction(PRINTER_THP_HIGHTEMP_ACTION);
        printerStatusFilter.addAction(PRINTER_THP_NORMALTEMP_ACTION);
        printerStatusFilter.addAction(PRINTER_MOTOR_HIGHTEMP_ACTION);
        printerStatusFilter.addAction(PRINTER_BUSY_ACTION);

        registerReceiver(IPosPrinterStatusListener, printerStatusFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void registerClickListener(View.OnClickListener clickListener) {
        buttonPrintReceipt.setOnClickListener(clickListener);
        _buttonPrintReceipt.setOnClickListener(clickListener);
        _buttonPrintQR.setOnClickListener(clickListener);
        buttonClose.setOnClickListener(clickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id._button_print_receipt:
                Bitmap _bitmapReceipt = getViewBitmap(layoutPrintReceipt);
                if (_bitmapReceipt != null) {
                    if (Build.MODEL.equalsIgnoreCase("Q1")) {
                        printBitmap(scaleImage(_bitmapReceipt, 1));
                    } else if (Build.MODEL.equalsIgnoreCase("V1s-G")) {
                        printInvoice(scaleImage(_bitmapReceipt, 1));
                    } else {
                        printInvoice(scaleImage(_bitmapReceipt, 1));
                    }
                }
                break;

            case R.id._button_print_qr:
                Bitmap _bitmapQR = getViewBitmap(layoutPrintQR);
                if (_bitmapQR != null) {
                    if (Build.MODEL.equalsIgnoreCase("Q1")) {
                        printBitmap(scaleImage(_bitmapQR, 2));
                    } else if (Build.MODEL.equalsIgnoreCase("V1s-G")) {
                        printInvoice(scaleImage(_bitmapQR, 2));
                    } else {
                        printInvoice(scaleImage(_bitmapQR, 2));
                    }
                }
                break;
            case R.id.button_close:
                AlertDialogUtil.dialogAlert(this);
//                gotoHome();
                break;
        }
    }

    public Bitmap getViewBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private String getStrPercentage(String percentage) {
        return "(" + percentage + "%)";
    }

    private String getFullItemType(String itemType, String itemQTY, String uom) {
        return itemType + " (" + itemQTY + uom + ")";
    }

    private Bitmap scaleImage(Bitmap bitmap, int printType) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth;
        if (printType == 1) {
            scaleWidth = ((float) 384) / width;
        } else {
            scaleWidth = ((float) width) / width;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, 1);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public void printInvoice(Bitmap bitmap) {
        baseApp.setAidl(true);
        if (baseApp.isAidl()) {
            AidlUtil.getInstance().printBitmap(bitmap);
        }
    }

    private void gotoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }

    private String getFeeWithSymbol(String value, String symbol) {
        return value + symbol;
    }


    // Initial new printer

    private HandlerUtils.IHandlerIntent iHandlerIntent = new HandlerUtils.IHandlerIntent() {
        @Override
        public void handlerIntent(Message msg) {
            switch (msg.what) {
                case MSG_IS_BUSY:
                    Toast.makeText(PrintGTReceiptActivity.this, R.string.printer_is_working, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_LESS:
                    Toast.makeText(PrintGTReceiptActivity.this, R.string.out_of_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_EXISTS:
                    Toast.makeText(PrintGTReceiptActivity.this, R.string.exists_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_THP_HIGH_TEMP:
                    Toast.makeText(PrintGTReceiptActivity.this, R.string.printer_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_MOTOR_HIGH_TEMP:
                    Toast.makeText(PrintGTReceiptActivity.this, R.string.motor_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(MSG_MOTOR_HIGH_TEMP_INIT_PRINTER, 180000);  //马达高温报警，等待3分钟后复位打印机
                    break;
                case MSG_MOTOR_HIGH_TEMP_INIT_PRINTER:
                    printerInit();
                    break;
                case MSG_CURRENT_TASK_PRINT_COMPLETE:
                    Toast.makeText(PrintGTReceiptActivity.this, R.string.printer_current_task_print_complete, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private BroadcastReceiver IPosPrinterStatusListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) return;
            switch (action) {
                case PRINTER_NORMAL_ACTION:
                    handler.sendEmptyMessageDelayed(MSG_IS_NORMAL, 0);
                    break;
                case PRINTER_PAPERLESS_ACTION:
                    handler.sendEmptyMessageDelayed(MSG_PAPER_LESS, 0);
                    break;
                case PRINTER_BUSY_ACTION:
                    handler.sendEmptyMessageDelayed(MSG_IS_BUSY, 0);
                    break;
                case PRINTER_PAPEREXISTS_ACTION:
                    handler.sendEmptyMessageDelayed(MSG_PAPER_EXISTS, 0);
                    break;
                case PRINTER_THP_HIGHTEMP_ACTION:
                    handler.sendEmptyMessageDelayed(MSG_THP_HIGH_TEMP, 0);
                    break;
                case PRINTER_THP_NORMALTEMP_ACTION:
                    handler.sendEmptyMessageDelayed(MSG_THP_TEMP_NORMAL, 0);
                    break;
                case PRINTER_MOTOR_HIGHTEMP_ACTION:
                    handler.sendEmptyMessageDelayed(MSG_MOTOR_HIGH_TEMP, 0);
                    break;
                case PRINTER_CURRENT_TASK_PRINT_COMPLETE_ACTION:
                    handler.sendEmptyMessageDelayed(MSG_CURRENT_TASK_PRINT_COMPLETE, 0);
                    break;
                default:
                    handler.sendEmptyMessageDelayed(MSG_TEST, 0);
                    break;
            }
        }
    };

    private ServiceConnection connectService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIPosPrinterService = IPosPrinterService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIPosPrinterService = null;
        }
    };

    public void printerInit() {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    mIPosPrinterService.printerInit(callback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(IPosPrinterStatusListener);
        unbindService(connectService);
        handler.removeCallbacksAndMessages(null);
    }

    public void printBitmap(final Bitmap mBitmap) {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    mIPosPrinterService.printBitmap(0, 16, mBitmap, callback);
                    mIPosPrinterService.printerPerformPrint(160, callback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
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

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
