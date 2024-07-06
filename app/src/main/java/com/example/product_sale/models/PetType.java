package com.example.product_sale.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PetType implements Parcelable {
    private int id;
    private String name;
    private List<Pet> pets;

    public PetType() {
        pets = new ArrayList<>();
    }

    protected PetType(Parcel in) {
        id = in.readInt();
        name = in.readString();
        pets = new ArrayList<>();
        in.readList(pets, Pet.class.getClassLoader());
    }

    public static final Creator<PetType> CREATOR = new Creator<PetType>() {
        @Override
        public PetType createFromParcel(Parcel in) {
            return new PetType(in);
        }

        @Override
        public PetType[] newArray(int size) {
            return new PetType[size];
        }
    };

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeList(pets);
    }
}