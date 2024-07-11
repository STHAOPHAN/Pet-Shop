package com.example.product_sale.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private Pet pet;

    public CartItem(Pet pet) {
        this.pet = pet;
    }

    protected CartItem(Parcel in) {
        pet = in.readParcelable(Pet.class.getClassLoader());
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



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(pet, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
