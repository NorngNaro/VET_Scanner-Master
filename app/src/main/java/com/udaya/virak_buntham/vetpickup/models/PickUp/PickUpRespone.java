package com.udaya.virak_buntham.vetpickup.models.PickUp;

import com.google.gson.annotations.SerializedName;
import com.udaya.virak_buntham.vetpickup.models.getdelivery.GetDeliveryDataItem;

import java.util.List;

public class PickUpRespone {

    @SerializedName("status")
    private String status;

    @SerializedName("info")
    private String info;

    @SerializedName("token")
    private String token;

    @SerializedName("signature")
    private String signature;

    @SerializedName("data")
    private List<PickUpDataItem> data;

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

    public List<PickUpDataItem> getData() {
        return data;
    }
}
