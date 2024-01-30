
package com.udaya.virak_buntham.vetpickup.models.membership;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Membership implements Parcelable
{
    private String membershipCode;
    private String percentage;
    public final static Creator<Membership> CREATOR = new Creator<Membership>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Membership createFromParcel(Parcel in) {
            return new Membership(in);
        }

        public Membership[] newArray(int size) {
            return (new Membership[size]);
        }

    }
    ;

    protected Membership(Parcel in) {
        this.membershipCode = ((String) in.readValue((String.class.getClassLoader())));
        this.percentage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Membership() {
    }

    public Membership(String membershipCode, String percentage) {
        this.membershipCode = membershipCode;
        this.percentage = percentage;
    }

    public String getMembershipCode() {
        return membershipCode;
    }

    public void setMembershipCode(String membershipCode) {
        this.membershipCode = membershipCode;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(membershipCode);
        dest.writeValue(percentage);
    }

    public int describeContents() {
        return  0;
    }

}
