package com.udaya.virak_buntham.vetpickup.models;

public class CustomerItem {
    private String name;
    private String tel;
    private int deliveryStatus;

    public CustomerItem(String name, String tel, int deliveryStatus) {
        this.name = name;
        this.tel = tel;
        this.deliveryStatus = deliveryStatus;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
