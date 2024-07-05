package com.example.product_sale.models;

import android.os.Parcelable;
import android.os.Parcel;

import java.util.List;

public class Pet implements Parcelable {
    private int id;
    private int petTypeId;
    private String breed;
    private String name;
    private int age;
    private String gender;
    private String color;
    private double price;
    private String image;
    private boolean isAvailable;
    private Object petType;
    private List<Object> orderPets;
    private List<Object> sales;

    public Pet(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Pet(int id, int petTypeId, String breed, String name, int age, String gender, String color, double price, String image, boolean isAvailable, Object petType, List<Object> orderPets, List<Object> sales) {
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
        this.petType = petType;
        this.orderPets = orderPets;
        this.sales = sales;
    }

    protected Pet(Parcel in) {
        id = in.readInt();
        petTypeId = in.readInt();
        breed = in.readString();
        name = in.readString();
        age = in.readInt();
        gender = in.readString();
        color = in.readString();
        price = in.readDouble();
        image = in.readString();
        isAvailable = in.readByte() != 0;
        // Note: Object and List<Object> handling can be complex and is not implemented here.
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public Object getPetType() {
        return petType;
    }

    public void setPetType(Object petType) {
        this.petType = petType;
    }

    public List<Object> getOrderPets() {
        return orderPets;
    }

    public void setOrderPets(List<Object> orderPets) {
        this.orderPets = orderPets;
    }

    public List<Object> getSales() {
        return sales;
    }

    public void setSales(List<Object> sales) {
        this.sales = sales;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(petTypeId);
        dest.writeString(breed);
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(gender);
        dest.writeString(color);
        dest.writeDouble(price);
        dest.writeString(image);
        dest.writeByte((byte) (isAvailable ? 1 : 0));
        // Note: Object and List<Object> handling can be complex and is not implemented here.
    }
}
