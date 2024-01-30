package com.udaya.virak_buntham.vetpickup.models;

public class ReportItem {
    private int No;
    private String date;
    private String itemCode;
    private String fee;

    public ReportItem(int no, String date, String itemCode, String fee) {
        No = no;
        this.date = date;
        this.itemCode = itemCode;
        this.fee = fee;
    }

    public int getNo() {
        return No;
    }

    public String getDate() {
        return date;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getFee() {
        return fee;
    }
}
