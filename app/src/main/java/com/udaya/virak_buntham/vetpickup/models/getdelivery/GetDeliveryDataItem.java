package com.udaya.virak_buntham.vetpickup.models.getdelivery;

import com.google.gson.annotations.SerializedName;
import com.udaya.virak_buntham.vetpickup.models.ListSearch.SearchListData;

import java.util.List;

public class GetDeliveryDataItem {

    @SerializedName("date")
    private String date;

    @SerializedName("series_code")
    private String seriesCode;

    @SerializedName("sender_tel")
    private String senderTel;

    @SerializedName("receiver_tel")
    private String receiverTel;

    @SerializedName("receiver_addr")
    private String receiverAddr;

    @SerializedName("receiver_lats")
    private String receiverLats;

    @SerializedName("receiver_longs")
    private String receiverLongs;

    @SerializedName("status")
    private String status;

    @SerializedName("transfer_fee")
    private String transfer_fee;

    @SerializedName("cod")
    private String cod;

    @SerializedName("code")
    private String code;

    @SerializedName("delivery_detail_id")
    private int delivery_detail_id;

    public String getDate() {
        return date;
    }

    public String getSeriesCode() {
        return seriesCode;
    }

    public String getSenderTel() {
        return senderTel;
    }

    public String getReceiverTel() {
        return receiverTel;
    }

    public String getReceiverAddr() {
        return receiverAddr;
    }

    public String getReceiverLats() {
        return receiverLats;
    }

    public String getReceiverLongs() {
        return receiverLongs;
    }

    public String getStatus() {
        return status;
    }

    public String getTransfer_fee() {
        return transfer_fee;
    }

    public String getCod() {
        return cod;
    }

    public String getCode() {
        return code;
    }

    public int getDelivery_detail_id() {
        return delivery_detail_id;
    }


}
