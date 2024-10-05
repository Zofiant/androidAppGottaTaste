package com.example.datagottataste;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class User implements Parcelable {
    private String id;
    private String name;
    private String surname;
    private String description;
    private List<String> recipes;
    private Boolean isCreator;
    private String userImageUrl;

    public User(String id, String name, String surname,
                Integer age, String description,
                List<String> recipes, Boolean isCreator,
                String userImageUrl) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.description = description;
        this.recipes = recipes;
        this.isCreator = isCreator;
        this.userImageUrl = userImageUrl;
    }

    public User() {

    }

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        surname = in.readString();
        description = in.readString();
        recipes = in.createStringArrayList();
        byte tmpIsCreator = in.readByte();
        isCreator = tmpIsCreator == 0 ? null : tmpIsCreator == 1;
        userImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(description);
        dest.writeStringList(recipes);
        dest.writeByte((byte) (isCreator == null ? 0 : isCreator ? 1 : 2));
        dest.writeString(userImageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<String> recipes) {
        this.recipes = recipes;
    }

    public Boolean isCreator() {
        return isCreator;
    }

    public void setCreator(Boolean creator) {
        isCreator = creator;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }
}
