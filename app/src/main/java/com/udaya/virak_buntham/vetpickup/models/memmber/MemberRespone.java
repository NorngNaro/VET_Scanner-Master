package com.udaya.virak_buntham.vetpickup.models.memmber;

import com.google.gson.annotations.SerializedName;

public class MemberRespone {
    @SerializedName("status")
    private String status;
    @SerializedName("info")
    private String info;

    @SerializedName("token")
    private String token;

    @SerializedName("signature")
    private String signature;

    @SerializedName("membership_id")
    private String membership_id;

    @SerializedName("percent")
    private String percent;

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

    public String getMembership_id() {
        return membership_id;
    }

    public String getPercent() {
        return percent;
    }
}
