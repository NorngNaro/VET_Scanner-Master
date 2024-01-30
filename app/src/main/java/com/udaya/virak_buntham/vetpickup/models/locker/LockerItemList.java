package com.udaya.virak_buntham.vetpickup.models.locker;

import com.google.gson.annotations.SerializedName;


public class LockerItemList {


    @SerializedName("date")
    private String date;

    @SerializedName("series_code")
    private String seriesCode;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSeriesCode() {
        return seriesCode;
    }

    public void setSeriesCode(String seriesCode) {
        this.seriesCode = seriesCode;
    }

    public LockerItemList(String date, String seriesCode) {
        this.date = date;
        this.seriesCode = seriesCode;
    }
}
