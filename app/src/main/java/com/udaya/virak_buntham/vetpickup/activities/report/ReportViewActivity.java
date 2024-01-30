package com.udaya.virak_buntham.vetpickup.activities.report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.utils.AppConfig;
import com.udaya.virak_buntham.vetpickup.utils.RegisterActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportViewActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    WebView webView;
    private ProgressDialog progressDialog;

    String device, session, id;
    String urlLoad = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        String name = intent.getStringExtra("TitleName");
        device = intent.getStringExtra("Device");
        session = intent.getStringExtra("session");
        id = intent.getStringExtra("id");
        mToolbarTitle.setText(name);
        RegisterActionBar.registerSupportToolbar(this, mToolbar);
        setWebView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebView() {
        webView = findViewById(R.id.webView);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loadings));
        progressDialog.show();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
//        webView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                progressDialog.show();
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, final String url) {
//                progressDialog.hide();
//            }
//        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("Failure Url :", failingUrl);
                progressDialog.dismiss();
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.d("Ssl Error:", handler.toString() + "error:" + error);
                progressDialog.dismiss();
                handler.cancel();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                progressDialog.dismiss();
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        });
        switch (id) {
            case "1":
                urlLoad = AppConfig.getUrlReport() + "goodsByAgency?device=" + device + "&session=" + session;
                break;
            case "2":
                urlLoad = AppConfig.getUrlReport() + "itemReceiveAgency?device=" + device + "&session=" + session;
                break;
            case "3":
                urlLoad = AppConfig.getUrlReport() + "agencyRevenue?device=" + device + "&session=" + session;
                break;
            case "4":
                urlLoad = AppConfig.getUrlReport() + "goodsReport?device=" + device + "&session=" + session;
                break;
            case "5":
                urlLoad = AppConfig.getUrlReport() + "reportOutForDelivery?device=" + device + "&session=" + session;
                break;
            case "6":
                urlLoad = AppConfig.getUrlReport() + "franchiesGoodsTransfer?device=" + device + "&session=" + session;
                break;
            case "7":
                urlLoad = AppConfig.getUrlReport() + "franchiesMoveToVan?device=" + device + "&session=" + session;
                break;
            case "8":
                urlLoad = AppConfig.getUrlReport() + "franchiesReceiveItem?device=" + device + "&session=" + session;
                break;
            case "9":
                urlLoad = AppConfig.getUrlReport() + "franchiesRevenue?device=" + device + "&session=" + session;
                break;
            case "10":
                urlLoad = AppConfig.getUrlReport() + "stockIn?device=" + device + "&session=" + session;
                break;
            case "20":
                urlLoad = AppConfig.getUrlReport() + "franchiesCustomerReceived?device=" + device + "&session=" + session;
                break;
            case "21":
                urlLoad = AppConfig.getUrlReport() + "stockNotReceive?device=" + device + "&session=" + session;
                break;
            case "22":
                urlLoad = AppConfig.getUrlReport() + "goodsReturn?device=" + device + "&session=" + session;
                break;
            case "23":
                urlLoad = AppConfig.getUrlReport() + "franchiesRevenueOld?device=" + device + "&session=" + session;
                break;
            case "24":
                urlLoad = AppConfig.getUrlReport() + "franchiesReceiveItemCod?device=" + device + "&session=" + session;
                break;
            case "contact":
                urlLoad = AppConfig.getUrlReport() + "contact";
                break;
            case "26":
                urlLoad = AppConfig.getUrlReport() + "searchGoods?device=" + device + "&session=" + session;
                break;
        }
        webView.loadUrl(urlLoad);
        Log.d("url==>", urlLoad);
    }
}

