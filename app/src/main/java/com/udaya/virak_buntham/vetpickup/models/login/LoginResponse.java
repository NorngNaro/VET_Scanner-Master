package com.udaya.virak_buntham.vetpickup.models.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("info")
    private String info;
    @SerializedName("user_name")
    private String username;
    @SerializedName("token")
    private String token;
    @SerializedName("signature")
    private String signature;
    @SerializedName("session")
    private String session;


    public String getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public String getSignature() {
        return signature;
    }

    public String getSession() {
        return session;
    }
}
