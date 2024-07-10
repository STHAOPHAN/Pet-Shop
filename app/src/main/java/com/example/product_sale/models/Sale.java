package com.example.product_sale.models;

import java.math.BigDecimal;

public class Sale {
    private int id;
    private int petId;
    private int customerId;
    private String saleDate; // Changed type to String
    private BigDecimal salePrice;

    public Sale(int id, int petId, int customerId, String saleDate, BigDecimal salePrice) {
        this.id = id;
        this.petId = petId;
        this.customerId = customerId;
        this.saleDate = saleDate;
        this.salePrice = salePrice;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnimalId() {
        return petId;
    }

    public void setAnimalId(int animalId) {
        this.petId = animalId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }
}
