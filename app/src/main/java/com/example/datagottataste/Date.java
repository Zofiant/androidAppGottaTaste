package com.example.datagottataste;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Date implements Parcelable {
    private String userId;
    private String RecipeId;
    private String DateOfDiet;


    protected Date(Parcel in) {
        userId = in.readString();
        RecipeId = in.readString();
        DateOfDiet = in.readString();
    }

    public static final Creator<Date> CREATOR = new Creator<Date>() {
        @Override
        public Date createFromParcel(Parcel in) {
            return new Date(in);
        }

        @Override
        public Date[] newArray(int size) {
            return new Date[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(RecipeId);
        dest.writeString(DateOfDiet);
    }
}