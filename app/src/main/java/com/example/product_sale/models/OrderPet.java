package com.example.product_sale.models;

public class OrderPet {
    private int orderId;
    private int animalId;
    private int quantity;

    public OrderPet(int orderId, int animalId, int quantity) {
        this.orderId = orderId;
        this.animalId = animalId;
        this.quantity = quantity;
    }
    // Getters và Setters

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
