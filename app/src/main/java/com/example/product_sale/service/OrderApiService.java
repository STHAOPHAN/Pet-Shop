package com.example.product_sale.service;

import com.example.product_sale.config.AppConfig;
import com.example.product_sale.models.Order;
import com.example.product_sale.models.Request.OrderModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderApiService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConfig.DATABASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @GET("api/orders")
    Call<List<Order>> getOrders(@Query("customerId") int customerId);

    @POST("api/orders")
    Call<Order> createOrder(@Body Order order);

    @PUT("api/orders/{id}")
    Call<Order> updateOrder(@Path("id") int id, @Body Order order);

    @DELETE("api/orders/{id}")
    Call<Void> deleteOrder(@Path("id") int id);
    @POST("api/orders/checkout")
    Call<Order> checkoutOrder(@Body OrderModel orderModel);

    @GET("api/orders")
    Call<List<Order>> getOrderById(@Query("id") int id);
}
