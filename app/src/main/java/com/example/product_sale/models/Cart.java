package com.example.product_sale.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private static Cart instance;
    private Map<Integer, CartItem> cartItems;
    public enum AddItemStatus {
        ITEM_ALREADY_EXISTS,
        ITEM_ADDED_SUCCESSFULLY
    }

    private Cart() {
        cartItems = new HashMap<>();
    }

    public static synchronized Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public AddItemStatus addItem(CartItem item) {
        int petId = item.getPet().getId();
        if (cartItems.containsKey(petId)) {
            return AddItemStatus.ITEM_ALREADY_EXISTS;
        }
        cartItems.put(petId, item);
        return AddItemStatus.ITEM_ADDED_SUCCESSFULLY;
    }

    public void removeItem(int petId) {
        cartItems.remove(petId);
    }


    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems.values());
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem cartItem : cartItems.values()) {
            total += cartItem.getPet().getPrice();
        }
        return total;
    }
    public void clear() {
        cartItems.clear();
    }
}
