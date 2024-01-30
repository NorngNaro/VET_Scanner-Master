package com.udaya.virak_buntham.vetpickup.models.selectionlist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DestinationFromResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("info")
    private String info;
    @SerializedName("data")
    private List<SelectionData> data;
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

    public List<SelectionData> getData() {
        return data;
    }

    public String getToken() {
        return token;
    }

    public String getSignature() {
        return signature;
    }

    public DestinationFromResponse(String status, String info, List<SelectionData> data, String token, String signature) {
        this.status = status;
        this.info = info;
        this.data = data;
        this.token = token;
        this.signature = signature;
    }
}
