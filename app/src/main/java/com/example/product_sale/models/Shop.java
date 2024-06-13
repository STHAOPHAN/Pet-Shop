package com.example.product_sale.models;

public class Shop {
    private int shop_id;
    private String name;
    private String address;
    private String phone;
    private String email;

    public Shop(int shop_id, String name, String address, String phone, String email) {
        this.shop_id = shop_id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public int getShop_id() {
        return shop_id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
