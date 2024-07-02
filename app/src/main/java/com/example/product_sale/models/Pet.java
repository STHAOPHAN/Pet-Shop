package com.example.product_sale.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;

public class Pet implements Parcelable {
    private int id;
    private int petTypeId;
    private String breed;
    private String name;
    private int age;
    private String gender;
    private String color;
    private BigDecimal price;
    private String image;
    private boolean isAvailable;

    public Pet() {
        this.id = id;
        this.petTypeId = petTypeId;
        this.breed = breed;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.color = color;
        this.price = price;
        this.image = image;
        this.isAvailable = isAvailable;
    }
// Getters và Setters

    protected Pet(Parcel in) {
        id = in.readInt();
        petTypeId = in.readInt();
        breed = in.readString();
        name = in.readString();
        age = in.readInt();
        gender = in.readString();
        color = in.readString();
        image = in.readString();
        isAvailable = in.readByte() != 0;
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPetTypeId() {
        return petTypeId;
    }

    public void setPetTypeId(int petTypeId) {
        this.petTypeId = petTypeId;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(petTypeId);
        dest.writeString(breed);
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(gender);
        dest.writeString(color);
        dest.writeString(image);
        dest.writeByte((byte) (isAvailable ? 1 : 0));
    }
}
