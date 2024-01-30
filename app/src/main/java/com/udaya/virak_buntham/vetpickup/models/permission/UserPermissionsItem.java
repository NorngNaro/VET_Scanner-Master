package com.udaya.virak_buntham.vetpickup.models.permission;

import com.google.gson.annotations.SerializedName;

public class UserPermissionsItem {
    @SerializedName("access")
    private String access;

    @SerializedName("module")
    private String module;

    public String getAccess() {
        return access;
    }

    public String getModule() {
        return module;
    }
}
