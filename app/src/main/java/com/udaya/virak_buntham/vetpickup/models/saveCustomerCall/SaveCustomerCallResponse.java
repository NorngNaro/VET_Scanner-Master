package com.udaya.virak_buntham.vetpickup.models.saveCustomerCall;

import com.google.gson.annotations.SerializedName;

public class SaveCustomerCallResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("info")
    private String info;

    @SerializedName("token")
    private String token;

    @SerializedName("signature")
    private String signature;

    @SerializedName("scan_status")
    private boolean scan_status;

    @SerializedName("goods_transfer_id")
    private int goods_transfer_id;

    @SerializedName("num")
    private String num;


    public String getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public String getToken() {
        return token;
    }

    public String getSignature() {
        return signature;
    }

    public boolean isScan_status() {
        return scan_status;
    }

    public int getGoods_transfer_id() {
        return goods_transfer_id;
    }

    public String getNum() {
        return num;
    }
}
