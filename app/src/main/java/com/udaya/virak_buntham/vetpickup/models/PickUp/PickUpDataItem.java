package com.udaya.virak_buntham.vetpickup.models.PickUp;

import com.google.gson.annotations.SerializedName;

public class PickUpDataItem {

    @SerializedName("date")
    private String date;

    @SerializedName("sender_tel")
    private String senderTel;

    @SerializedName("receiver_tel")
    private String receiverTel;

    @SerializedName("sender_addr")
    private String senderAddr;

    @SerializedName("sender_lats")
    private String senderLats;

    @SerializedName("sender_longs")
    private String senderLongs;

    @SerializedName("status")
    private String status;

    @SerializedName("code")
    private String code;

    @SerializedName("cus_id")
    private String cusId;

    @SerializedName("reference")
    private String reference;

    @SerializedName("cus_name")
    private String cus_name;

    public String getNote() {
        return note;
    }

    @SerializedName("cus_tel")
    private String cus_tel;
    @SerializedName("note")
    private String note;

    public String getCus_tel() {
        return cus_tel;
    }

    public String getCus_name() {
        return cus_name;
    }

    public String getDate() {
        return date;
    }

    public String getSenderTel() {
        return senderTel;
    }

    public String getSenderAddr() {
        return senderAddr;
    }

    public String getSenderLats() {
        return senderLats;
    }

    public String getSenderLongs() {
        return senderLongs;
    }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getCusId() {
        return cusId;
    }

    public String getReceiverTel() {
        return receiverTel;
    }

    public String getReference() {
        return reference;
    }
}


