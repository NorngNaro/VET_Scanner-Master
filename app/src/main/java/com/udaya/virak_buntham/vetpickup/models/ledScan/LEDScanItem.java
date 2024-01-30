package com.udaya.virak_buntham.vetpickup.models.ledScan;

public class LEDScanItem {

    private String itemCode;
    private String tel;
    private String destinationTo;
    private String destinationName;
    private Integer image;
    private int photo;

    public LEDScanItem(String itemCode, String tel, String destinationTo, String destinationName, Integer image, int photo) {
        this.itemCode = itemCode;
        this.tel = tel;
        this.destinationTo = destinationTo;
        this.destinationName = destinationName;
        this.image = image;
        this.photo = photo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getTel() {
        return tel;
    }

    public String getDestinationTo() {
        return destinationTo;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public Integer getImage() {
        return image;
    }

    public int getPhoto() {
        return photo;
    }
}
