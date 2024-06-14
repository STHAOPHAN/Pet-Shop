package com.example.product_sale.models;

import java.math.BigDecimal;

public class Pet {
    private int pet_id;
    private String name;
    private int type_id;
    private String breed;
    private int age;
    private float weight;
    private BigDecimal price;
    private String description;
    private String image;

    public Pet(int pet_id, String name, int type_id, String breed, int age, float weight, BigDecimal price, String description, String image) {
        this.pet_id = pet_id;
        this.name = name;
        this.type_id = type_id;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public int getPet_id() {
        return pet_id;
    }

    public void setPet_id(int pet_id) {
        this.pet_id = pet_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
