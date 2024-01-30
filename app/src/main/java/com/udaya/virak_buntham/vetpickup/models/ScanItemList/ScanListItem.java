package com.udaya.virak_buntham.vetpickup.models.ScanItemList;

public class ScanListItem {
    private String itemCode;
    private String tel;
    private String desctinationTo;
    private String desctinationName;
    private Integer image;
    private int photo;

    public String getDesctinationName() {
        return desctinationName;
    }

    public Integer getImage() {
        return image;
    }

    public int getPhoto() {
        return photo;
    }

    public ScanListItem(String itemCode, String tel, String desctinationTo, String desctinationName) {
        this.itemCode = itemCode;
        this.tel = tel;
        this.desctinationTo = desctinationTo;
        this.desctinationName = desctinationName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getTel() {
        return tel;
    }

    public String getDesctinationTo() {
        return desctinationTo;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setDesctinationTo(String desctinationTo) {
        this.desctinationTo = desctinationTo;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
