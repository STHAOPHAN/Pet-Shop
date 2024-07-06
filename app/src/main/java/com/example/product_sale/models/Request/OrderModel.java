package com.example.product_sale.models.Request;

import java.util.List;

public class OrderModel {
    private int customerId;
    private double totalPrice;
    private List<Integer> petIds;

    public OrderModel(int customerId, double totalPrice, List<Integer> petIds) {
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.petIds = petIds;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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
