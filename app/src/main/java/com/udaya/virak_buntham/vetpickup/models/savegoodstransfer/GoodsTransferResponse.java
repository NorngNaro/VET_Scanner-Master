package com.udaya.virak_buntham.vetpickup.models.savegoodstransfer;

import com.google.gson.annotations.SerializedName;

public class GoodsTransferResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("info")
    private String info;
    @SerializedName("date_print")
    private String datePrint;
    @SerializedName("date_invoice")
    private String dateInvoice;
    @SerializedName("branch_from_name")
    private String branchFromName;
    @SerializedName("branch_from_tel")
    private String branchFromTel;
    @SerializedName("branch_to_name")
    private String branchToName;
    @SerializedName("branch_to_tel")
    private String branchToTel;
    @SerializedName("transfer_code")
    private String transferCode;
    @SerializedName("item_value")
    private String itemValue;
    @SerializedName("transfer_fee")
    private String transferFee;
    @SerializedName("delivery_fee")
    private String deliveryFee;
    @SerializedName("discount")
    private String discount;
    @SerializedName("total_amount")
    private String totalAmount;
    @SerializedName("item_code")
    private String itemCode;
    @SerializedName("qr_code")
    private String qrCode;
    @SerializedName("token")
    private String token;
    @SerializedName("signature")
    private String signature;
    @SerializedName("collect_cod")
    private String collectCod;
    @SerializedName("paid")
    private String paid;
    @SerializedName("receiver_address")
    private String receiveraddress;
    @SerializedName("destination_to")
    private String destinationTo;
    @SerializedName("item_qty")
    private String item_qty;
    @SerializedName("item_name")
    private String item_name;
    @SerializedName("delivery_area")
    private String delivery_area;
    @SerializedName("destination_from")
    private String destination_from;
    @SerializedName("point")
    private int point;
    @SerializedName("customerName")
    private String customerName;
    @SerializedName("dest_to_code")
    private String destinationToCode;
    @SerializedName("location_type")
    private String location_type;

    public String getLocation_type() {
        return location_type;
    }

    public String getDestination_from() {
        return destination_from;
    }

    public String getDelivery_area() {
        return delivery_area;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_qty() {
        return item_qty;
    }

    public String getDestinationTo() {
        return destinationTo;
    }

    public String getReceiveraddress() {
        return receiveraddress;
    }

    public String getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public String getDatePrint() {
        return datePrint;
    }

    public String getDateInvoice() {
        return dateInvoice;
    }

    public String getBranchFromName() {
        return branchFromName;
    }

    public String getBranchFromTel() {
        return branchFromTel;
    }

    public String getBranchToName() {
        return branchToName;
    }

    public String getBranchToTel() {
        return branchToTel;
    }

    public String getTransferCode() {
        return transferCode;
    }

    public String getItemValue() {
        return itemValue;
    }

    public String getTransferFee() {
        return transferFee;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public String getDiscount() {
        return discount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public String getToken() {
        return token;
    }

    public String getSignature() {
        return signature;
    }

    public String getCollectCod() {
        return collectCod;
    }

    public String getPaid() {
        return paid;
    }

    public int getPoint() {
        return point;
    }

    public String getDestinationToCode() {
        return destinationToCode;
    }

    public String getCustomerName() {
        return customerName;
    }
}
