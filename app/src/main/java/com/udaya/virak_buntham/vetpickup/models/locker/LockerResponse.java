package com.udaya.virak_buntham.vetpickup.models.locker;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LockerResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("info")
    private String info;

    @SerializedName("token")
    private String token;

    @SerializedName("signature")
    private String signature;

    @SerializedName("data")
    private List<LockerData> data;

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

    public List<LockerData> getData() {
        return data;
    }

}


