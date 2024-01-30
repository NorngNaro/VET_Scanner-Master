
package com.udaya.virak_buntham.vetpickup.models.selectionlist;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectionData implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("symbol")
    @Expose
    private String symbol;

    @SerializedName("tel")
    @Expose
    private String tel;

    @SerializedName("allow_delivery")
    @Expose
    private int allowDelivery;

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("is_free_delivery")
    @Expose
    private int isFreeDelivery;



    public String getTel() {
        return tel;
    }

    public static Creator<SelectionData> getCREATOR() {
        return CREATOR;
    }

    public final static Creator<SelectionData> CREATOR = new Creator<SelectionData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SelectionData createFromParcel(Parcel in) {
            return new SelectionData(in);
        }

        public SelectionData[] newArray(int size) {
            return (new SelectionData[size]);
        }

    }
    ;

    protected SelectionData(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.symbol = ((String) in.readValue((String.class.getClassLoader())));
        this.tel = ((String) in.readValue((String.class.getClassLoader())));
        this.allowDelivery = ((int) in.readValue((int.class.getClassLoader())));
        this.type = ((int) in.readValue((int.class.getClassLoader())));
    }

    public SelectionData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(symbol);
        dest.writeValue(tel);
        dest.writeValue(allowDelivery);
        dest.writeValue(type);
    }

    public int describeContents() {
        return  0;
    }

    public int getAllowDelivery() {
        return allowDelivery;
    }

    public int getType() {
        return type;
    }

    public void setAllowDelivery(int allowDelivery) {
        this.allowDelivery = allowDelivery;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsFreeDelivery() {
        return isFreeDelivery;
    }
}
