package com.udaya.virak_buntham.vetpickup.models.savegoodstransfer;

public class GoodsTransfer {
    private String device;
    private String token;
    private String signature;
    private String session;
    private String destinationFrom;
    private String destinationTo;
    private String sender;
    private String receiver;
    private String itemValue;
    private String itemCurrency;
    private String itemType;
    private String itemName;
    private String itemQTY;
    private String uom;
    private String transferFee;
    private String deliveryFee;
    private String deliveryArea;
    private String membership;
    private String membershipPercentage;
    private String discount;
    private String fee;
    private String longs;
    private String lats;
    private String gtRequestId;

    public GoodsTransfer(String device, String token, String signature, String session, String destinationFrom, String destinationTo, String sender, String receiver, String itemValue, String itemCurrency, String itemType, String itemName, String itemQTY, String uom, String transferFee, String deliveryFee, String deliveryArea, String membership, String membershipPercentage, String discount, String fee, String longs, String lats, String gtRequestId) {
        this.device = device;
        this.token = token;
        this.signature = signature;
        this.session = session;
        this.destinationFrom = destinationFrom;
        this.destinationTo = destinationTo;
        this.sender = sender;
        this.receiver = receiver;
        this.itemValue = itemValue;
        this.itemCurrency = itemCurrency;
        this.itemType = itemType;
        this.itemName = itemName;
        this.itemQTY = itemQTY;
        this.uom = uom;
        this.transferFee = transferFee;
        this.deliveryFee = deliveryFee;
        this.deliveryArea = deliveryArea;
        this.membership = membership;
        this.membershipPercentage = membershipPercentage;
        this.discount = discount;
        this.fee = fee;
        this.longs = longs;
        this.lats = lats;
        this.gtRequestId = gtRequestId;
    }

    public String getDevice() {
        return device;
    }

    public String getToken() {
        return token;
    }

    public String getSignature() {
        return signature;
    }

    public String getSession() {
        return session;
    }

    public String getDestinationFrom() {
        return destinationFrom;
    }

    public String getDestinationTo() {
        return destinationTo;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getItemValue() {
        return itemValue;
    }

    public String getItemCurrency() {
        return itemCurrency;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemQTY() {
        return itemQTY;
    }

    public String getUom() {
        return uom;
    }

    public String getTransferFee() {
        return transferFee;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public String getMembership() {
        return membership;
    }

    public String getMembershipPercentage() {
        return membershipPercentage;
    }

    public String getDiscount() {
        return discount;
    }

    public String getFee() {
        return fee;
    }

    public String getLongs() {
        return longs;
    }

    public String getLats() {
        return lats;
    }

    public String getGtRequestId() {
        return gtRequestId;
    }
}
