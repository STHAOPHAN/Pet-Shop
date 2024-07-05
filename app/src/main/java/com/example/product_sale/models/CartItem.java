package com.example.product_sale.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private Pet pet;
    private int quantity;

    public CartItem(Pet pet, int quantity) {
        this.pet = pet;
        this.quantity = quantity;
    }

    protected CartItem(Parcel in) {
        pet = in.readParcelable(Pet.class.getClassLoader());
        quantity = in.readInt();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(pet, flags);
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public double getTotalPrice() {
        return pet.getPrice() * quantity;
    }
}
