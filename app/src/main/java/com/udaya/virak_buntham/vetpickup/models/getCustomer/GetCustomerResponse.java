package com.udaya.virak_buntham.vetpickup.models.getCustomer;

import com.google.gson.annotations.SerializedName;
import com.udaya.virak_buntham.vetpickup.models.PickUp.PickUpDataItem;

import java.util.List;

public class GetCustomerResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("info")
    private String info;

    @SerializedName("token")
    private String token;

    @SerializedName("signature")
    private String signature;

    @SerializedName("data")
    private List<CustomerDataItem> data;

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

    public List<CustomerDataItem> getData() {
        return data;
    }
}
