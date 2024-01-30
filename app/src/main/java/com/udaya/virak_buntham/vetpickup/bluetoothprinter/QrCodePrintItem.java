package com.udaya.virak_buntham.vetpickup.bluetoothprinter;

import android.graphics.Bitmap;

public class QrCodePrintItem {
    private final String tvDesFrom;
    private final String tvDesTo;
    private final String receiverTelephone;
    private final String itemName;
    private final String itemCode;
    private final String totalAmount;
    private final String COD;
    private final String titleCOD;
    private final String tvQty;
    private final Bitmap bitmap;
    private final String tvName;

    public String getTvDesFrom() {
        return tvDesFrom;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getTvDesTo() {
        return tvDesTo;
    }

    public String getReceiverTelephone() {
        return receiverTelephone;
    }

    public String getItemName() {
        return itemName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getCOD() {
        return COD;
    }

    public String getTitleCOD() {
        return titleCOD;
    }

    public String getTvQty() {
        return tvQty;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getTvName() {
        return tvName;
    }

    public QrCodePrintItem(String tvDesFrom, String tvDesTo, String receiverTelephone, String itemName, String itemCode, String totalAmount, String COD, String titleCOD, String tvQty, Bitmap bitmap, String tvName) {
        this.tvDesFrom = tvDesFrom;
        this.tvDesTo = tvDesTo;
        this.receiverTelephone = receiverTelephone;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.totalAmount = totalAmount;
        this.COD = COD;
        this.titleCOD = titleCOD;
        this.tvQty = tvQty;
        this.bitmap = bitmap;
        this.tvName = tvName;
    }
}

