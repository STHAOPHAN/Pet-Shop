package com.example.product_sale.models.Request;

import java.util.List;

public class OrderModel {
    private String email;
    private double totalPrice;
    private List<Integer> petIds;

    public OrderModel(String email, double totalPrice, List<Integer> petIds) {
        this.email = email;
        this.totalPrice = totalPrice;
        this.petIds = petIds;
    }

    public String getEmail() {
        return email;
    }

    public void setCustomerId(String email) {
        this.email = email;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Integer> getPetIds() {
        return petIds;
    }

    public void setPetIds(List<Integer> petIds) {
        this.petIds = petIds;
    }

}
