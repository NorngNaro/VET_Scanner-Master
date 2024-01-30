
package com.udaya.virak_buntham.vetpickup.models.print;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrintData implements Parcelable {

    @SerializedName("branch_name")
    @Expose
    private String branchName;
    @SerializedName("transfer_code")
    @Expose
    private String transferCode;
    @SerializedName("item_value")
    @Expose
    private String itemValue;
    @SerializedName("transfer_date")
    @Expose
    private String transferDate;
    @SerializedName("item_type")
    @Expose
    private String itemType;
    @SerializedName("item_qty")
    @Expose
    private String itemQTY;
    @SerializedName("item_uom")
    @Expose
    private String itemUOM;
    @SerializedName("item_value_symbol")
    @Expose
    private String itemValueSymbol;
    @SerializedName("normal_symbol")
    @Expose
    private String normalSymbol;

    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("receiver")
    @Expose
    private String receiver;
    @SerializedName("destination_to")
    @Expose
    private String destinationTo;
    @SerializedName("transfer_fee")
    @Expose
    private String transferFee;
    @SerializedName("delivery_fee")
    @Expose
    private String deliveryFee;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("branch_from_tel")
    @Expose
    private String branchFromTel;
    @SerializedName("branch_to_name")
    @Expose
    private String branchToName;
    @SerializedName("branch_to_tel")
    @Expose
    private String branchToTel;
    @SerializedName("print_date")
    @Expose
    private String printDate;
    @SerializedName("item_code")
    @Expose
    private String itemCode;
    @SerializedName("qr_code")
    @Expose
    private String qrCode;

    @SerializedName("collect_cod")
    private String collectCod;

    @SerializedName("destination_from")
    private String destinatioFrom;

    @SerializedName("paid")
    private String paid;

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

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDestinationToCode() {
        return destinationToCode;
    }

    public void setDestinationToCode(String destinationToCode) {
        this.destinationToCode = destinationToCode;
    }

    @SerializedName("item_name")
    private String itemName;

    @SerializedName("delivery_area")
    private String deliveryArea;

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(String deliveryArea) {
        this.deliveryArea = deliveryArea;
    }

    public String getDestinatioFrom() {
        return destinatioFrom;
    }

    public final static Creator<PrintData> CREATOR = new Creator<PrintData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PrintData createFromParcel(Parcel in) {
            return new PrintData(in);
        }

        public PrintData[] newArray(int size) {
            return (new PrintData[size]);
        }

    };

    protected PrintData(Parcel in) {
        this.branchName = ((String) in.readValue((String.class.getClassLoader())));
        this.transferCode = ((String) in.readValue((String.class.getClassLoader())));
        this.itemValue = ((String) in.readValue((String.class.getClassLoader())));
        this.transferDate = ((String) in.readValue((String.class.getClassLoader())));
        this.itemType = ((String) in.readValue((String.class.getClassLoader())));
        this.itemQTY = ((String) in.readValue((String.class.getClassLoader())));
        this.itemUOM = ((String) in.readValue((String.class.getClassLoader())));
        this.itemValueSymbol = ((String) in.readValue((String.class.getClassLoader())));
        this.normalSymbol = ((String) in.readValue((String.class.getClassLoader())));
        this.sender = ((String) in.readValue((String.class.getClassLoader())));
        this.receiver = ((String) in.readValue((String.class.getClassLoader())));
        this.destinationTo = ((String) in.readValue((String.class.getClassLoader())));
        this.transferFee = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryFee = ((String) in.readValue((String.class.getClassLoader())));
        this.discount = ((String) in.readValue((String.class.getClassLoader())));
        this.percentage = ((String) in.readValue((String.class.getClassLoader())));
        this.totalAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.branchFromTel = ((String) in.readValue((String.class.getClassLoader())));
        this.branchToName = ((String) in.readValue((String.class.getClassLoader())));
        this.branchToTel = ((String) in.readValue((String.class.getClassLoader())));
        this.printDate = ((String) in.readValue((String.class.getClassLoader())));
        this.itemCode = ((String) in.readValue((String.class.getClassLoader())));
        this.qrCode = ((String) in.readValue((String.class.getClassLoader())));
        this.paid = ((String) in.readValue((String.class.getClassLoader())));
        this.collectCod = ((String) in.readValue((String.class.getClassLoader())));
        this.itemQTY = ((String) in.readValue((String.class.getClassLoader())));
        this.itemName = ((String) in.readValue((String.class.getClassLoader())));
        this.destinatioFrom=((String) in.readValue((String.class.getClassLoader())));
        this.point=((Integer) in.readValue((Integer.class.getClassLoader())));
        this.customerName=((String) in.readValue((String.class.getClassLoader())));
        this.destinationToCode=((String) in.readValue((String.class.getClassLoader())));

    }

    public void setDestinatioFrom(String destinatioFrom) {
        this.destinatioFrom = destinatioFrom;
    }

    public PrintData() {
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getTransferCode() {
        return transferCode;
    }

    public void setTransferCode(String transferCode) {
        this.transferCode = transferCode;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemQTY() {
        return itemQTY;
    }

    public void setItemQTY(String itemQTY) {
        this.itemQTY = itemQTY;
    }

    public String getItemUOM() {
        return itemUOM;
    }

    public void setItemUOM(String itemUOM) {
        this.itemUOM = itemUOM;
    }

    public String getItemValueSymbol() {
        return itemValueSymbol;
    }

    public void setItemValueSymbol(String itemValueSymbol) {
        this.itemValueSymbol = itemValueSymbol;
    }

    public String getNormalSymbol() {
        return normalSymbol;
    }

    public void setNormalSymbol(String normalSymbol) {
        this.normalSymbol = normalSymbol;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDestinationTo() {
        return destinationTo;
    }

    public void setDestinationTo(String destinationTo) {
        this.destinationTo = destinationTo;
    }

    public String getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(String transferFee) {
        this.transferFee = transferFee;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getDiscount() {
        return discount;
    }


    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBranchFromTel() {
        return branchFromTel;
    }

    public void setBranchFromTel(String branchFromTel) {
        this.branchFromTel = branchFromTel;
    }

    public String getBranchToName() {
        return branchToName;
    }

    public void setBranchToName(String branchToName) {
        this.branchToName = branchToName;
    }

    public String getBranchToTel() {
        return branchToTel;
    }

    public void setBranchToTel(String branchToTel) {
        this.branchToTel = branchToTel;
    }

    public String getPrintDate() {
        return printDate;
    }

    public void setCollectCod(String collectCod) {
        this.collectCod = collectCod;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(branchName);
        dest.writeValue(transferCode);
        dest.writeValue(itemValue);
        dest.writeValue(transferDate);
        dest.writeValue(itemType);
        dest.writeValue(itemQTY);
        dest.writeValue(itemUOM);
        dest.writeValue(itemValueSymbol);
        dest.writeValue(normalSymbol);
        dest.writeValue(sender);
        dest.writeValue(receiver);
        dest.writeValue(destinationTo);
        dest.writeValue(transferFee);
        dest.writeValue(deliveryFee);
        dest.writeValue(discount);
        dest.writeValue(percentage);
        dest.writeValue(totalAmount);
        dest.writeValue(branchFromTel);
        dest.writeValue(branchToName);
        dest.writeValue(branchToTel);
        dest.writeValue(printDate);
        dest.writeValue(itemCode);
        dest.writeValue(qrCode);
        dest.writeValue(paid);
        dest.writeValue(collectCod);
        dest.writeValue(itemQTY);
        dest.writeValue(itemName);
        dest.writeValue(destinatioFrom);
        dest.writeValue(point);
        dest.writeValue(customerName);
        dest.writeValue(destinationToCode);
    }

    public int describeContents() {
        return 0;
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

    public void setPoint(int point) {
        this.point = point;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
