package com.example.product_sale.models;

public class PetType {
    private int type_id;
    private String name;
    private String description;

    public PetType(int type_id, String name, String description) {
        this.type_id = type_id;
        this.name = name;
        this.description = description;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
