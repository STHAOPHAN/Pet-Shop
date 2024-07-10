package com.example.product_sale.models;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private int id;
    private int customerId;
    private String orderDate;
    private BigDecimal totalPrice;
    private String status;

    public Order(int id, int customerId, String orderDate, BigDecimal totalPrice, String status) {
        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }
// Getters và Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
