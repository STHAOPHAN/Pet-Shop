package com.example.product_sale.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance;
    private List<CartItem> cartItems;
    public enum AddItemStatus {
        ITEM_ALREADY_EXISTS,
        ITEM_ADDED_SUCCESSFULLY
    }

    private Cart() {
        cartItems = new ArrayList<>();
    }

    public static synchronized Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public AddItemStatus addItem(CartItem item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getPet().getId() == item.getPet().getId()) {
                return AddItemStatus.ITEM_ALREADY_EXISTS;
            }
        }
        cartItems.add(item);
        return AddItemStatus.ITEM_ADDED_SUCCESSFULLY;
    }

    public void removeItem(int petId) {
        cartItems.removeIf(cartItem -> cartItem.getPet().getId() == petId);
    }


    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getPet().getPrice();
        }
        return total;
    }
    public void clear() {
        cartItems.clear();
    }
}