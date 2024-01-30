package com.udaya.virak_buntham.vetpickup.models.locker;

import com.google.gson.annotations.SerializedName;

public class LockerLabelData {

    @SerializedName("date")
    private String date;
    @SerializedName("code")
    private String code;
    @SerializedName("qr_code")
    private String qr_code;
    @SerializedName("destination_from")
    private String destination_from;
    @SerializedName("destination_to")
    private String destination_to;
    @SerializedName("dest_to_code")
    private String dest_to_code;
    @SerializedName("location_type")
    private String location_type;
    @SerializedName("delivery_area")
    private String delivery_area;
    @SerializedName("total_amount")
    private String total_amount;
    @SerializedName("item_name")
    private String item_name;
    @SerializedName("item_qty")
    private int item_qty;
   @SerializedName("receiver_telephone")
    private String receiver_telephone;
   @SerializedName("customer_name")
    private String customer_name;
   @SerializedName("username")
    private String username;

    public String getDate() {
        return date;
    }

    public String getCode() {
        return code;
    }

    public String getQr_code() {
        return qr_code;
    }

    public String getDestination_from() {
        return destination_from;
    }

    public String getDestination_to() {
        return destination_to;
    }

    public String getDest_to_code() {
        return dest_to_code;
    }

    public String getLocation_type() {
        return location_type;
    }

    public String getDelivery_area() {
        return delivery_area;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public String getItem_name() {
        return item_name;
    }

    public int getItem_qty() {
        return item_qty;
    }

    public String getReceiver_telephone() {
        return receiver_telephone;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getUsername() {
        return username;
    }
}
