package com.udaya.virak_buntham.vetpickup.models.qrcodeitem;

import android.graphics.Bitmap;

public class QrCodeItem {
    private Bitmap qrImage;

    private String tvFrom;
    private String tvTo;
    private String tvItemCOde;
    private String tvTotalAmount;
    private String tvItemType;
    private String tvQrDate;
    private String tvPaidCheck;
    private String tvCodCheck;
    private String tvQrReceiver;
    private String tvCOD;
    private String tvPaid;

    public String getTvCOD() {
        return tvCOD;
    }

    public String getTvPaid() {
        return tvPaid;
    }

    public String getTvFrom() {
        return tvFrom;
    }

    public String getTvTo() {
        return tvTo;
    }

    public String getTvItemCOde() {
        return tvItemCOde;
    }

    public String getTvTotalAmount() {
        return tvTotalAmount;
    }

    public String getTvItemType() {
        return tvItemType;
    }

    public String getTvQrDate() {
        return tvQrDate;
    }

    public String getTvPaidCheck() {
        return tvPaidCheck;
    }

    public String getTvCodCheck() {
        return tvCodCheck;
    }

    public String getTvQrReceiver() {
        return tvQrReceiver;
    }

    public QrCodeItem(Bitmap qrImage, String tvFrom, String tvTo, String tvItemCOde, String tvTotalAmount, String tvItemType, String tvQrDate, String tvPaidCheck, String tvCodCheck, String tvQrReceiver) {
        this.qrImage = qrImage;
        this.tvFrom = tvFrom;
        this.tvTo = tvTo;
        this.tvItemCOde = tvItemCOde;
        this.tvTotalAmount = tvTotalAmount;
        this.tvItemType = tvItemType;
        this.tvQrDate = tvQrDate;
        this.tvPaidCheck = tvPaidCheck;
        this.tvCodCheck = tvCodCheck;
        this.tvQrReceiver = tvQrReceiver;
    }

    public Bitmap getQrImage() {
        return qrImage;
    }

//    public QrCodeItem(int qrImage) {
//        this.qrImage = qrImage;
//    }
}
