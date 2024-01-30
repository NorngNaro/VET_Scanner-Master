package com.udaya.virak_buntham.vetpickup.models.getdelivery;

import com.google.gson.annotations.SerializedName;
import com.udaya.virak_buntham.vetpickup.models.ListSearch.SearchListData;

import java.util.List;

public class GetDeliveryRespone {

    @SerializedName("status")
    private String status;

    @SerializedName("info")
    private String info;

    @SerializedName("token")
    private String token;

    @SerializedName("signature")
    private String signature;

    @SerializedName("data")
    private List<GetDeliveryDataItem> data;

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

    public List<GetDeliveryDataItem> getData() {
        return data;
    }
}
