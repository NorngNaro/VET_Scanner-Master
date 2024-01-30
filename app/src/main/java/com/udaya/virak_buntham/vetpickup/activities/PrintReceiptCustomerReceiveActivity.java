package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.udaya.virak_buntham.vetpickup.adapters.QrCodeAdapter;
import com.udaya.virak_buntham.vetpickup.aildhelper.iposprinter.ThreadPoolManager;
import com.udaya.virak_buntham.vetpickup.aildhelper.iposprinter.Utils.HandlerUtils;
import com.udaya.virak_buntham.vetpickup.base.BaseApp;
import com.udaya.virak_buntham.vetpickup.models.print.PrintData;
import com.udaya.virak_buntham.vetpickup.models.qrcodeitem.QrCodeItem;
import com.udaya.virak_buntham.vetpickup.printerutils.AidlUtil;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrintReceiptCustomerReceiveActivity extends AppCompatActivity implements View.OnClickListener {

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

    //    @SuppressLint("NonConstantResourceId")@BindView(R.id.txt_branch_from)
//    TextView branchFrom;
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
    @BindView(R.id.tvPad)
    TextView viewPad;
    @BindView(R.id.tvCollectCod)
    TextView viewCollectCod;
    @BindView(R.id.view_print_qr_COD_Check)
    TextView viewCollectCodValue;
    @BindView(R.id.view_print_qr_Paid_Check)
    TextView viewCollectPaidCheck;


    BaseApp baseApp;

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


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int itemValueQrCode = 0;
    PrintData printData;

    private String valueQrCodeData;
    public final static int QRcodeWidth = 500;
    Bitmap bitmap;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_gtreceipt_customer);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
//        if (getIntent().getExtras() != null) {
//
//            printData = getIntent().getParcelableExtra("printData");
//
//            branchFrom.setText(printData.getBranchName());
//            transferCode.setText(printData.getTransferCode());
//            itemValue.setText(getFeeWithSymbol(printData.getItemValue(), printData.getItemValueSymbol()));
//            transferDate.setText(printData.getTransferDate());
//            itemType.setText(getFullItemType(printData.getItemType(), printData.getItemQTY(), printData.getItemUOM()));
//            sender.setText(printData.getSender());
//            receiver.setText(printData.getReceiver());
//            destinationTo.setText(printData.getDestinationTo());
//            viewPad.setText(printData.getPaid());
//            itemValueQrCode = Integer.parseInt(printData.getItemQTY());
//            Log.d("ItemQtyValue", "" + printData.getItemQTY());
//
//            if (printData.getPaid().equals("0")) {
//                viewPad.setText("មិនទាន់ទូទាត់");
//                viewCollectPaidCheck.setText("មិនទាន់ទូទាត់");
//            } else {
//                viewPad.setText("ទូទាត់រួច");
//                viewCollectPaidCheck.setText("ទូទាត់រួច");
//            }
//
//            Log.d("CollectCode==>", "" + printData.getCollectCod());
//            if (printData.getCollectCod().equals("0")) {
//                viewCollectCod.setText("គ្មាន");
////                viewCollectCodValue.setVisibility(View.GONE);
//            } else {
//                viewCollectCodValue.setText("COD(" + getFeeWithSymbol(printData.getItemValue(), printData.getItemValueSymbol()) + ")");
//            }
////            if (!printData.getCollectCod().equals("0")) {
////                viewCollectCod.setText("មាន");
////                viewCollectCodValue.setText("COD(" + getFeeWithSymbol(printData.getItemValue(), printData.getItemValueSymbol()) + ")");
////            } else {
//////                Toast.makeText(baseApp, "not 0", Toast.LENGTH_SHORT).show();
////                viewCollectCod.setText("គ្មាន");
////                viewCollectCodValue.setVisibility(View.GONE);
////            }
//            transferFee.setText(getFeeWithSymbol(printData.getTransferFee(), printData.getNormalSymbol()));
//            deliverFee.setText(getFeeWithSymbol(printData.getDeliveryFee(), printData.getNormalSymbol()));
//            discount.setText(getFeeWithSymbol(printData.getDiscount(), printData.getNormalSymbol()));
//            labelDiscount.append(getStrPercentage(printData.getPercentage()));
//            totalAmount.setText(getFeeWithSymbol(printData.getTotalAmount(), printData.getNormalSymbol()));
//            totalPaid.setText(getFeeWithSymbol(printData.getTotalAmount(), printData.getNormalSymbol()));
//            branchFromName.setText(printData.getBranchName());
//            branchFromTel.setText(printData.getBranchFromTel());
////            branchToName.setText(printData.getBranchToName());
////            branchToTel.setText(printData.getBranchToTel());
//            printDate.setText(printData.getPrintDate());
//
//            //QR receipt for print
//            printQrBranchFrom.setText(printData.getBranchName());
//            printQrBranchTo.setText(printData.getBranchToName());
//            printQrItemCode.setText(printData.getItemCode());
//            printQrTotalAmount.setText(getFeeWithSymbol(printData.getTotalAmount(), printData.getNormalSymbol()));
//            printQrItemType.setText(printData.getItemType());
//            GlideApp.with(this).load(printData.getQrCode()).into(imageQrCode);
//            qrDate.setText(printData.getTransferDate());
//            qrReceiver.setText(printData.getReceiver());
//
//            //QR receipt for display
//            viewPrintQrBranchFrom.setText(printData.getBranchName());
////            viewPrintQrBranchTo.setText(printData.getBranchToName());
//            viewPrintQrBranchTo.setText(printData.getDestinationTo());
//            viewPrintQrItemCode.setText(printData.getItemCode());
//            viewPrintQrTotalAmount.setText(getFeeWithSymbol(printData.getTotalAmount(), printData.getNormalSymbol()));
//            viewPrintQrItemType.setText(printData.getItemType());
//            GlideApp.with(this).load(printData.getQrCode()).into(viewImageQrCode);
//            viewQrDate.setText(printData.getTransferDate());
//            viewQrReceiver.setText(printData.getReceiver());
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage(getResources().getString(R.string.loadings));
//            progressDialog.show();
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    // Do something after 5s = 5000ms
//                    getArrayItem();
//                    progressDialog.hide();
//                }
//            }, 5000);
//
//
//        }
        getBundle();
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
                    Toast.makeText(PrintReceiptCustomerReceiveActivity.this, R.string.printer_is_working, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_LESS:
                    Toast.makeText(PrintReceiptCustomerReceiveActivity.this, R.string.out_of_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_EXISTS:
                    Toast.makeText(PrintReceiptCustomerReceiveActivity.this, R.string.exists_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_THP_HIGH_TEMP:
                    Toast.makeText(PrintReceiptCustomerReceiveActivity.this, R.string.printer_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_MOTOR_HIGH_TEMP:
                    Toast.makeText(PrintReceiptCustomerReceiveActivity.this, R.string.motor_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(MSG_MOTOR_HIGH_TEMP_INIT_PRINTER, 180000);  //马达高温报警，等待3分钟后复位打印机
                    break;
                case MSG_MOTOR_HIGH_TEMP_INIT_PRINTER:
                    printerInit();
                    break;
                case MSG_CURRENT_TASK_PRINT_COMPLETE:
                    Toast.makeText(PrintReceiptCustomerReceiveActivity.this, R.string.printer_current_task_print_complete, Toast.LENGTH_SHORT).show();
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

    private void getArrayItem() {
        String paidValue;
        String codValue;
        ArrayList<QrCodeItem> qrImageList = new ArrayList<>();
        for (int i = 0; i < itemValueQrCode; i++) {
            if (printData.getPaid().equals("0")) {
                paidValue = "មិនទាន់ទូទាត់";
            } else {
                paidValue = "ទូទាត់រួច";
            }
            if (printData.getCollectCod().equals("0")) {
                viewCollectCod.setText("គ្មាន");
                codValue = " ";
            } else {
                codValue = "COD(" + getFeeWithSymbol(printData.getItemValue(), printData.getItemValueSymbol()) + ")";
            }
            valueQrCodeData = printData.getTransferCode() + "," + printData.getReceiver() + "," + i;
            try {
                bitmap = TextToImageEncode(valueQrCodeData);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            Integer countNum = i + 1;
            qrImageList.add(new QrCodeItem(
                    bitmap,
                    printData.getBranchName(),
                    printData.getDestinationTo(),
                    printData.getItemCode() + "/" + countNum,
                    getFeeWithSymbol(printData.getTotalAmount(), printData.getNormalSymbol()),
                    printData.getItemType(),
                    printData.getTransferDate(),
                    paidValue,
                    codValue,
                    printData.getReceiver()
            ));
        }
        mRecyclerView = findViewById(R.id.recyclerQrImage);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new QrCodeAdapter(qrImageList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        String Cod = bundle.getString("CODValue");
        String Fee = bundle.getString("FEEValue");
        String code = bundle.getString("CODEValue");
        String name = bundle.getString("NameValue");
        Log.d("CODValue==>", Cod);
        Log.d("FeeValue==>", Fee);
        TextView tvCODValue = findViewById(R.id.tvCOD);
        TextView tvFeeValue = findViewById(R.id.tvFee);
        TextView tvCodeValue = findViewById(R.id.tvCodeCustomer);
        TextView tvDateValue = findViewById(R.id.tvDateCustomer);
        TextView tvName = findViewById(R.id.tvReceivedName);
        LinearLayout llCOD = findViewById(R.id.linearCOD);
        LinearLayout llFEE = findViewById(R.id.linearFee);
        tvCODValue.setText(Cod);
        tvFeeValue.setText(Fee);
        tvCodeValue.setText(code);
        tvName.setText(name);
        if (tvCODValue.getText().toString().trim().equals(getResources().getString(R.string.none))) {
            llCOD.setVisibility(View.GONE);
        }
        if (tvFeeValue.getText().toString().trim().equals(getResources().getString(R.string.paid))) {
            llFEE.setVisibility(View.GONE);
        }
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        tvDateValue.setText(currentDate + " " + currentTime);

    }
}
