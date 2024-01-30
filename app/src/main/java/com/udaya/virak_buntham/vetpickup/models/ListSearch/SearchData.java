package com.udaya.virak_buntham.vetpickup.models.ListSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchData {
    @SerializedName("status")
    private String status;

    @SerializedName("info")
    private String info;

    public String getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public String getSignature() {
        return signature;
    }

    public List<SearchListData> getData() {
        return data;
    }

    @SerializedName("signature")
    private String signature;

    public String getToken() {
        return token;
    }

    @SerializedName("data")
    private List<SearchListData> data;

    @SerializedName("token")
    private String token;

}
