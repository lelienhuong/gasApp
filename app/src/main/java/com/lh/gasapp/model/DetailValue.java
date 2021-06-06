package com.lh.gasapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailValue implements Parcelable {
    public String time;
    public int gasValue;

    public DetailValue(){
    }

    public DetailValue(String time, int gasValue) {
        this.time = time;
        this.gasValue = gasValue;
    }

    protected DetailValue(Parcel in) {
        time = in.readString();
        gasValue = in.readInt();
    }

    public static final Creator<DetailValue> CREATOR = new Creator<DetailValue>() {
        @Override
        public DetailValue createFromParcel(Parcel in) {
            return new DetailValue(in);
        }

        @Override
        public DetailValue[] newArray(int size) {
            return new DetailValue[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeInt(gasValue);
    }
}
