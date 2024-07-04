package com.example.product_sale.models;

public class CartItem {
    private Pet pet;
    private int quantity;
    public CartItem(Pet pet, int quantity) {
        this.pet = pet;
        this.quantity = quantity;
    }
    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return pet.getPrice() * quantity;
    }
}
