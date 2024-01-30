package com.udaya.virak_buntham.vetpickup.models.getCustomer;

import com.google.gson.annotations.SerializedName;

public class CustomerDataItem {

    @SerializedName("id")
    private int id;
    @SerializedName("code")
    private String code;
    @SerializedName("arrival_date")
    private String arrivalDate;
    @SerializedName("sender_telephone")
    private String senderTelephone;
    @SerializedName("receiver_telephone")
    private String receiverTelephone;
    @SerializedName("destination_from")
    private String destinationFrom;
    @SerializedName("item_qty")
    private String itemQty;
    @SerializedName("wrong_arrival")
    private boolean wrongArrival;
    @SerializedName("type")
    private int type;
    @SerializedName("type_lbl")
    private String typeLbl;
    @SerializedName("status")
    private int status;
    @SerializedName("status_lbl")
    private String statusLbl;
    @SerializedName("location")
    private String location;
    @SerializedName("tran_status")
    private int tranStatus;
    @SerializedName("remark")
    private String remark;
    @SerializedName("goods_transfer_id")
    private int goodsTransferId;
    @SerializedName("is_cal_his")
    private int is_cal_his;


    public int getGoodsTransferId() {
        return goodsTransferId;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getSenderTelephone() {
        return senderTelephone;
    }

    public String getReceiverTelephone() {
        return receiverTelephone;
    }

    public String getDestinationFrom() {
        return destinationFrom;
    }

    public String getItemQty() {
        return itemQty;
    }

    public boolean isWrongArrival() {
        return wrongArrival;
    }

    public int getType() {
        return type;
    }

    public String getTypeLbl() {
        return typeLbl;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusLbl() {
        return statusLbl;
    }

    public String getLocation() {
        return location;
    }

    public int getTranStatus() {
        return tranStatus;
    }

    public String getRemark() {
        return remark;
    }

    public int getIs_cal_his() {
        return is_cal_his;
    }
}
