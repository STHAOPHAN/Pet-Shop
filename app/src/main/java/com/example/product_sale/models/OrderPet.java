package com.example.product_sale.models;

public class OrderPet {
    private int orderId;
    private int petId;
    private int quantity;
    public OrderPet(int orderId, int petId, int quantity) {
        this.orderId = orderId;
        this.petId = petId;
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
        return petId;
    }

    public void setAnimalId(int animalId) {
        this.petId = animalId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
