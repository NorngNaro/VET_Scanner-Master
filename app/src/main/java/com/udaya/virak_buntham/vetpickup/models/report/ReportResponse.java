package com.udaya.virak_buntham.vetpickup.models.report;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("info")
    private String info;
    @SerializedName("branch")
    private String branch;
    @SerializedName("user")
    private String user;
    @SerializedName("date")
    private String date;
    @SerializedName("total")
    private String total;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("data")
    private List<Report> data;
    @SerializedName("token")
    private String token;
    @SerializedName("signature")
    private String signature;

    public String getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public String getBranch() {
        return branch;
    }

    public String getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }

    public String getTotal() {
        return total;
    }

    public String getSymbol() {
        return symbol;
    }

    public List<Report> getData() {
        return data;
    }

    public String getToken() {
        return token;
    }

    public String getSignature() {
        return signature;
    }
}
