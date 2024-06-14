package com.example.product_sale.models;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private int order_id;
    private String customer_name;
    private String customer_phone;
    private String customer_email;
    private Date order_date;
    private BigDecimal total_price;
    private String status;

}
