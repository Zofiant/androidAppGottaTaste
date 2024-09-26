package com.example.datagottataste;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class RecipeItem implements Parcelable {
    String name,id,cal, quantity;

    String imageUrl;
    boolean box;

    public RecipeItem(String name, String id, String cal,String quantity, String imageUrl, boolean box){
        this.name = name;
        this.id = id;
        this.cal = cal;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.box = box;
    }
    protected RecipeItem(Parcel in) {
        name = in.readString();
        id = in.readString();
        cal = in.readString();
        quantity = in.readString();
        imageUrl = in.readString();

        //imageUrl = in.readParcelable(Bitmap.class.getClassLoader());
        box = in.readByte() !=0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(cal);
        dest.writeString(quantity);
        dest.writeString(imageUrl);
        //dest.writeParcelable(imageUrl,flags);
        dest.writeByte((byte)(box ? 1 : 0));
    }

    public static final Creator<RecipeItem> CREATOR = new Creator<RecipeItem>() {
        @Override
        public RecipeItem createFromParcel(Parcel in) {
            return new RecipeItem(in);
        }

        @Override
        public RecipeItem[] newArray(int size) {
            return new RecipeItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCal() { return cal; }
    public void setCal(String cal) { this.cal = cal; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }



}
