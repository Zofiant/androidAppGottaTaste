package com.example.datagottataste;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class RecipeBd implements Parcelable {
    public String id, name, cal, image_url;
    public RecipeBd() {
    }
    public RecipeBd(String id, String name, String cal, String image_url) {
        this.id = id;
        this.name = name;
        this.cal = cal;
        this.image_url = image_url;
    }


    protected RecipeBd(Parcel in) {
        id = in.readString();
        name = in.readString();
        cal = in.readString();
        image_url = in.readString();
    }

    public static final Creator<RecipeBd> CREATOR = new Creator<RecipeBd>() {
        @Override
        public RecipeBd createFromParcel(Parcel in) {
            return new RecipeBd(in);
        }

        @Override
        public RecipeBd[] newArray(int size) {
            return new RecipeBd[size];
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
        dest.writeString(image_url);
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCal() { return cal; }
    public void setCal(String cal) { this.cal = cal; }
    public String getImageUrl() { return image_url; }
    public void setImageUrl(String imageUrl) { this.image_url = imageUrl; }

}
