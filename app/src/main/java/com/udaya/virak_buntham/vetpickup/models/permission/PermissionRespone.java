package com.udaya.virak_buntham.vetpickup.models.permission;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PermissionRespone {
    @SerializedName("status")
    private String status;
    @SerializedName("info")
    private String info;
    @SerializedName("token")
    private String token;
    @SerializedName("signature")
    private String signature;

    @SerializedName("branchId")
    private String branchId;

    @SerializedName("appVersion")
    private String appVersion;

    @SerializedName("branchName")
    private String branchName;

    @SerializedName("username")
    private String username;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("destFromId")
    private String destFromId;

    @SerializedName("destFromName")
    private String destFromName;

    @SerializedName("userPermissions")
    private List<UserPermissionsItem> userPermissions;

    public String getSymbol() {
        return symbol;
    }

    public String getDestFromId() {
        return destFromId;
    }

    public String getDestFromName() {
        return destFromName;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getBranchName() {
        return branchName;
    }

    public List<UserPermissionsItem> getUserPermissions() {
        return userPermissions;
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

    public String getUsername() {
        return username;
    }
}
