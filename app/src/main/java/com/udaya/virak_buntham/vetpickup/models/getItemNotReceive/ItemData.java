package com.udaya.virak_buntham.vetpickup.models.getItemNotReceive;

import com.google.gson.annotations.SerializedName;

public class ItemData {

    @SerializedName("code")
    private String code;

    @SerializedName("num")
    private int num;

    @SerializedName("status")
    private int status;

    @SerializedName("is_checked")
    private int is_checked;

    public String getCode() {
        return code;
    }

    public int getNum() {
        return num;
    }

    public int getStatus() {
        return status;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getIs_checked() {
        return is_checked;
    }
}
