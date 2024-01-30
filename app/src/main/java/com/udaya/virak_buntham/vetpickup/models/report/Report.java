package com.udaya.virak_buntham.vetpickup.models.report;

import com.google.gson.annotations.SerializedName;

public class Report {
    @SerializedName("id")
    private String Id;
    @SerializedName("code")
    private String code;
    @SerializedName("fee")
    private String fee;
    @SerializedName("symbol")
    private String symbol;

    public String getId() {
        return Id;
    }

    public String getCode() {
        return code;
    }

    public String getFee() {
        return fee;
    }

    public String getSymbol() {
        return symbol;
    }
}
