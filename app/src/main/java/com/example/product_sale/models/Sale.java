package com.example.product_sale.models;

import java.math.BigDecimal;
import java.util.Date;

public class Sale {
    private int id;
    private int animalId;
    private int customerId;
    private Date saleDate;
    private BigDecimal salePrice;

    public Sale(int id, int animalId, int customerId, Date saleDate, BigDecimal salePrice) {
        this.id = id;
        this.animalId = animalId;
        this.customerId = customerId;
        this.saleDate = saleDate;
        this.salePrice = salePrice;
    }
    // Getters và Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }
}

