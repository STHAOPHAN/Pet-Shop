package com.example.product_sale.models;

public class OrderPet {
    private int order_id;
    private int pet_id;
    private int quantity;

    public OrderPet(int order_id, int pet_id, int quantity) {
        this.order_id = order_id;
        this.pet_id = pet_id;
        this.quantity = quantity;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getPet_id() {
        return pet_id;
    }

    public void setPet_id(int pet_id) {
        this.pet_id = pet_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
