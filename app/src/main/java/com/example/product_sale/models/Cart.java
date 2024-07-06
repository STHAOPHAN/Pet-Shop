package com.example.product_sale.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance;
    private List<CartItem> cartItems;

    private Cart() {
        cartItems = new ArrayList<>();
    }

    public static synchronized Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addItem(CartItem item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getPet().getId() == item.getPet().getId()) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        cartItems.add(item);
    }

    public void removeItem(int petId) {
        cartItems.removeIf(cartItem -> cartItem.getPet().getId() == petId);
    }

    public void updateQuantity(int petId, int quantity) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getPet().getId() == petId) {
                cartItem.setQuantity(quantity);
                return;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getTotalPrice();
        }
        return total;
    }
    public void clear() {
        cartItems.clear();
    }
}
