package com.udaya.virak_buntham.vetpickup.models.ScanQr;

import com.google.gson.annotations.SerializedName;

public class ScanQrRespone {
    @SerializedName("status")
    private String status;

    @SerializedName("info")
    private String info;

    @SerializedName("token")
    private String token;

    @SerializedName("signature")
    private String signature;


    public String getCod() {
        return cod;
    }

    public String getCod_currency() {
        return cod_currency;
    }

    @SerializedName("code")
    private String code;

    @SerializedName("sender")
    private String sender;

    public String getTransferFee() {
        return transferFee;
    }

    @SerializedName("receiver")
    private String receiver;
    @SerializedName("destination_to")
    private String destination_to;
    @SerializedName("transfer_fee")
    private String transferFee;
    @SerializedName("cod")
    private String cod;
    @SerializedName("cod_currency")
    private String cod_currency;
    @SerializedName("username")
    private String username;

    public String getUsername() {
        return username;
    }

    public String getCode() {
        return code;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getDestination_to() {
        return destination_to;
    }


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
}
