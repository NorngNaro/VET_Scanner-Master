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
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iposprinter.iposprinterservice.IPosPrinterCallback;
import com.iposprinter.iposprinterservice.IPosPrinterService;
import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.adapters.ReportItemAdapter;
import com.udaya.virak_buntham.vetpickup.aildhelper.iposprinter.ThreadPoolManager;
import com.udaya.virak_buntham.vetpickup.aildhelper.iposprinter.Utils.HandlerUtils;
import com.udaya.virak_buntham.vetpickup.base.BaseApp;
import com.udaya.virak_buntham.vetpickup.listeners.OnInternetConnectionListener;
import com.udaya.virak_buntham.vetpickup.models.report.Report;
import com.udaya.virak_buntham.vetpickup.models.report.ReportResponse;
import com.udaya.virak_buntham.vetpickup.preferences.DeviceID;
import com.udaya.virak_buntham.vetpickup.preferences.RequestParams;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;
import com.udaya.virak_buntham.vetpickup.printerutils.AidlUtil;
import com.udaya.virak_buntham.vetpickup.rest.ApiClient;
import com.udaya.virak_buntham.vetpickup.rest.ApiInterface;
import com.udaya.virak_buntham.vetpickup.utils.AlertDialogUtil;
import com.udaya.virak_buntham.vetpickup.utils.Constants;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportGoodsTransferActivity extends AppCompatActivity implements OnInternetConnectionListener {

    private final String TAG = ReportGoodsTransferActivity.class.getSimpleName();

    BaseApp baseApp;


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.rv_report_item_container)
    RecyclerView rvReportItemContainer;
    @BindView(R.id.linear_layout_print)
    LinearLayout linearLayoutPrint;
    @BindView(R.id.test_image)
    ImageView testImage;
    @BindView(R.id.report_branch)
    TextView branch;
    @BindView(R.id.report_username)
    TextView username;
    @BindView(R.id.report_date)
    TextView date;
    @BindView(R.id.report_total)
    TextView total;
    @BindView(R.id.report_symbol)
    TextView symbol;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_goods_transfer);
        ButterKnife.bind(this);

        baseApp = (BaseApp) getApplication();

        mToolbarTitle.setText(R.string.report_goods_transfer);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);

        requestReport(DeviceID.getDeviceId(this)
                , RequestParams.getTokenRequestParams(this)
                , RequestParams.getSignatureRequestParams(this)
                , UserSession.getUserSession(this));

        // Initial new printer
        handler = new HandlerUtils.MyHandler(iHandlerIntent);
        callback = new IPosPrinterCallback.Stub() {

            @Override
            public void onRunResult(final boolean isSuccess) {
                Log.i(TAG, "result:" + isSuccess + "\n");
            }

            @Override
            public void onReturnString(final String value) {
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

    private void setupReportAdapter(List<Report> reportList) {
        ReportItemAdapter reportItemAdapter = new ReportItemAdapter(reportList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvReportItemContainer.setLayoutManager(layoutManager);
        rvReportItemContainer.setItemAnimator(new DefaultItemAnimator());
        rvReportItemContainer.addItemDecoration(getDivider());
        rvReportItemContainer.setNestedScrollingEnabled(false);
        rvReportItemContainer.setAdapter(reportItemAdapter);
    }

    private void requestReport(String device, String token, String signature, String session) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        ApiInterface apiService = ApiClient.getClient(this, this).create(ApiInterface.class);
        Call<ReportResponse> call = apiService.getReport(device, token, signature, session);
        call.enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReportResponse> call, @NonNull Response<ReportResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals(Constants.STATUS_SUCCESS)) {
                            RequestParams.persistRequestParams(ReportGoodsTransferActivity.this
                                    , response.body().getToken()
                                    , response.body().getSignature());

                            branch.setText(response.body().getBranch());
                            username.setText(response.body().getUser());
                            date.setText(response.body().getDate());
                            total.setText(response.body().getTotal());
                            symbol.setText(response.body().getSymbol());
                            setupReportAdapter(response.body().getData());
                        } else {
                            AlertDialogUtil.alertMessageError(ReportGoodsTransferActivity.this, response.body().getInfo());
                        }
                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<ReportResponse> call, @NonNull Throwable t) {
                progressDialog.hide();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.report_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_print:
                Bitmap bitmap = getViewBitmap(linearLayoutPrint);
                if (bitmap != null) {
                    if (Build.MODEL.equalsIgnoreCase("Q1")) {
                        printBitmap(scaleImage(bitmap));
                    } else if (Build.MODEL.equalsIgnoreCase("V1s-G")) {
                        printInvoice(scaleImage(bitmap));
                    } else {
                        printInvoice(scaleImage(bitmap));
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private DividerItemDecoration getDivider() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider));
        return dividerItemDecoration;
    }

    public Bitmap getViewBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private Bitmap scaleImage(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) 384) / width;
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

    @Override
    public void onInternetAvailable() {

    }

    @Override
    public void onInternetUnavailable() {

    }


    // Initial new printer

    private final HandlerUtils.IHandlerIntent iHandlerIntent = new HandlerUtils.IHandlerIntent() {
        @Override
        public void handlerIntent(Message msg) {
            switch (msg.what) {
                case MSG_IS_BUSY:
                    Toast.makeText(ReportGoodsTransferActivity.this, R.string.printer_is_working, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_LESS:
                    Toast.makeText(ReportGoodsTransferActivity.this, R.string.out_of_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_EXISTS:
                    Toast.makeText(ReportGoodsTransferActivity.this, R.string.exists_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_THP_HIGH_TEMP:
                    Toast.makeText(ReportGoodsTransferActivity.this, R.string.printer_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_MOTOR_HIGH_TEMP:
                    Toast.makeText(ReportGoodsTransferActivity.this, R.string.motor_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(MSG_MOTOR_HIGH_TEMP_INIT_PRINTER, 180000);  //马达高温报警，等待3分钟后复位打印机
                    break;
                case MSG_MOTOR_HIGH_TEMP_INIT_PRINTER:
                    printerInit();
                    break;
                case MSG_CURRENT_TASK_PRINT_COMPLETE:
                    Toast.makeText(ReportGoodsTransferActivity.this, R.string.printer_current_task_print_complete, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private final BroadcastReceiver IPosPrinterStatusListener = new BroadcastReceiver() {
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

    private final ServiceConnection connectService = new ServiceConnection() {
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
        ThreadPoolManager.getInstance().executeTask(() -> {
            try {
                mIPosPrinterService.printerInit(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
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
        ThreadPoolManager.getInstance().executeTask(() -> {
            try {
                mIPosPrinterService.printBitmap(0, 16, mBitmap, callback);
                mIPosPrinterService.printerPerformPrint(160, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
