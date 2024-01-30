package com.udaya.virak_buntham.vetpickup.models.ListSearch;

import com.google.gson.annotations.SerializedName;

public class SearchListData {
    public int getId() {
        return id;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("series_code")
    private String series_code;

    @SerializedName("date")
    private String date;

    @SerializedName("code")
    private String code;

    @SerializedName("receiver_telephone")
    private String receiver_telephone;

    @SerializedName("sender_telephone")
    private String sender_telephone;

    @SerializedName("arrival_date")
    private String arrival_date;

    @SerializedName("branch")
    private String branch;

    @SerializedName("qty")
    private String qty;

    @SerializedName("call_status")
    private String call_status;

    @SerializedName("status")
    private String status;

    @SerializedName("destination_from")
    private String destination_from;

    @SerializedName("item_name")
    private String item_name;

    @SerializedName("received_date")
    private String received_date;


    public String getQty() {
        return qty;
    }

    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }

    @SerializedName("location")
    private String location;

    public String getSeries_code() {
        return series_code;
    }

    public void setSeries_code(String series_code) {
        this.series_code = series_code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCall_status() {
        return call_status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReceiver_telephone() {
        return receiver_telephone;
    }

    public void setReceiver_telephone(String receiver_telephone) {
        this.receiver_telephone = receiver_telephone;
    }
    @SerializedName("permission")
    private String permission;

    public String getPermission() {
        return permission;
    }

    public String getSender_telephone() {
        return sender_telephone;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public String getBranch() {
        return branch;
    }

    public String getStatus() {
        return status;
    }

    public String getDestination_from() {
        return destination_from;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getReceived_date() {
        return received_date;
    }

}
