package com.example.datagottataste;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class RecipeDate implements Parcelable {
    public String id, name, cal, imageId, userId,dateOfDiet;
    public RecipeDate() {
    }
    public RecipeDate(String id, String name, String cal, String imageId, String userId, String dateOfDiet) {
        this.id = id;
        this.name = name;
        this.cal = cal;
        this.imageId = imageId;
        this.userId = userId;
        this.dateOfDiet = dateOfDiet;
    }


    protected RecipeDate(Parcel in) {
        id = in.readString();
        name = in.readString();
        cal = in.readString();
        imageId = in.readString();
        userId = in.readString();
        dateOfDiet = in.readString();
    }

    public static final Creator<RecipeDate> CREATOR = new Creator<RecipeDate>() {
        @Override
        public RecipeDate createFromParcel(Parcel in) {
            return new RecipeDate(in);
        }

        @Override
        public RecipeDate[] newArray(int size) {
            return new RecipeDate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(cal);
        dest.writeString(imageId);
        dest.writeString(userId);
        dest.writeString(dateOfDiet);
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCal() { return cal; }
    public void setCal(String cal) { this.cal = cal; }
    public String getImageUrl() { return imageId; }
    public void setImageUrl(String imageId) { this.imageId = imageId; }
    public String getUserId() { return  userId; };
    public void setUserId(String userId) { this.userId = userId; };
    public String getDateOfDiet() { return dateOfDiet; };
    public void setDateOfDiet(String dateOfDiet) { this.dateOfDiet = dateOfDiet; };

}
