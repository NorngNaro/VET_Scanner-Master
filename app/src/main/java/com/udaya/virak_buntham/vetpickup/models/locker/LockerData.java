package com.udaya.virak_buntham.vetpickup.models.locker;

import com.google.gson.annotations.SerializedName;

public class LockerData {

    @SerializedName("id")
    private int id;
    @SerializedName("date")
    private String date;

    public int getId() {
        return id;
    }

    @SerializedName("code")
    private String code;
    @SerializedName("reference")
    private String reference;
    @SerializedName("sender_name")
    private String sender_name;
    @SerializedName("sender_telephone")
    private String sender_telephone;
    @SerializedName("receiver_name")
    private String receiver_name;
    @SerializedName("receiver_telephone")
    private String receiver_telephone;
    @SerializedName("locker_name")
    private String locker_name;
    @SerializedName("lats")
    private String lats;
    @SerializedName("longs")
    private String longs;
    @SerializedName("status")
    private int status;

    @SerializedName("is_print")
    private int is_print;



    public String getDate() {
        return date;
    }

    public String getCode() {
        return code;
    }

    public String getReference() {
        return reference;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getReceiver_telephone() {
        return receiver_telephone;
    }

    public String getSender_telephone() {
        return sender_telephone;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public String getLocker_name() {
        return locker_name;
    }

    public String getLats() {
        return lats;
    }

    public int getIs_print() {
        return is_print;
    }

    public String getLongs() {
        return longs;
    }

    public int getStatus() {
        return status;
    }
}
