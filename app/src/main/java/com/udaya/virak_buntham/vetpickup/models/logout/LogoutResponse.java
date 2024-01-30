package com.udaya.virak_buntham.vetpickup.models.logout;

import com.google.gson.annotations.SerializedName;

public class LogoutResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("info")
    private String info;
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

    public String getToken() {
        return token;
    }

    public String getSignature() {
        return signature;
    }
}
