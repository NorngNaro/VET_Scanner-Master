package com.udaya.virak_buntham.vetpickup.models.getItemNotReceive;

import com.google.gson.annotations.SerializedName;
import com.udaya.virak_buntham.vetpickup.models.getCustomer.CustomerDataItem;

import java.util.List;

public class ItemNotReceiveResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("signature")
    private String signature;

    @SerializedName("info")
    private String info;

    @SerializedName("token")
    private String token;

    @SerializedName("code")
    private String code;

    @SerializedName("sender_telephone")
    private String sender_telephone;

    @SerializedName("receiver_telephone")
    private String receiver_telephone;

    @SerializedName("item_qty")
    private int item_qty;

    @SerializedName("item_data")
    private List<ItemData> data;


    public String getSignature() {
        return signature;
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

    public String getCode() {
        return code;
    }

    public String getSender_telephone() {
        return sender_telephone;
    }

    public String getReceiver_telephone() {
        return receiver_telephone;
    }

    public int getItem_qty() {
        return item_qty;
    }

    public List<ItemData> getData() {
        return data;
    }
}
