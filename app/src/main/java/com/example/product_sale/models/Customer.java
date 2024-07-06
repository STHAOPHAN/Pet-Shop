package com.example.product_sale.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Customer implements Parcelable {
    private int id;
    private String fullName;
    private String password;
    private String email;
    private String phone;
    private String address;
    private List<Object> messageReceivers;
    private List<Object> messageSenders;
    private List<Order> orders;
    private List<Sale> sales;

    public Customer() {
    }

    public Customer(int id, String fullName, String password, String email, String phone, String address) {
        this.id = id;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public Customer(String fullName, String password, String email, String phone, String address) {
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    protected Customer(Parcel in) {
        id = in.readInt();
        fullName = in.readString();
        password = in.readString();
        email = in.readString();
        phone = in.readString();
        address = in.readString();
        // Note: Object and List<Object> handling can be complex and is not implemented here.
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Object> getMessageReceivers() {
        return messageReceivers;
    }

    public void setMessageReceivers(List<Object> messageReceivers) {
        this.messageReceivers = messageReceivers;
    }

    public List<Object> getMessageSenders() {
        return messageSenders;
    }

    public void setMessageSenders(List<Object> messageSenders) {
        this.messageSenders = messageSenders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fullName);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(address);
        // Note: Object and List<Object> handling can be complex and is not implemented here.
    }
}