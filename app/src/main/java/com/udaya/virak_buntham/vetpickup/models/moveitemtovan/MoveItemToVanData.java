package com.udaya.virak_buntham.vetpickup.models.moveitemtovan;

import com.google.gson.annotations.SerializedName;

public class MoveItemToVanData {
    @SerializedName("status")
    private String status;

    @SerializedName("info")
    private String info;

    @SerializedName("token")
    private String token;

    @SerializedName("signature")
    private String signature;

    @SerializedName("code")
    private String code;

    @SerializedName("destination")
    private String destination;

    @SerializedName("telephone")
    private String telephone;

    @SerializedName("sys_code")
    private String sysCode;

    @SerializedName("dest_code")
    private String destCode;

    @SerializedName("sound")
    private String sound;

    @SerializedName("location_name")
    private String locationName;

    @SerializedName("led")
    private String led;

    public String getLed() {
        return led;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getSysCode() {
        return sysCode;
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

    public String getSignature() {
        return signature;
    }

    public String getCode() {
        return code;
    }

    public String getDestination() {
        return destination;
    }

    public String getDestCode() {
        return destCode;
    }

    public String getSound() {
        return sound;
    }
}
