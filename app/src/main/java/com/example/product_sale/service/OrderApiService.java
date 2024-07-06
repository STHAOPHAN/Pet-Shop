package com.example.product_sale.service;
import com.example.product_sale.models.Order;

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
public interface OrderApiService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://petshopapi-env.eba-xz2mv5rq.ap-southeast-1.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @GET("api/orders")
    Call<List<Order>> getOrders();

    @POST("api/orders")
    Call<Order> createOrder(@Body Order order);

    @PUT("api/orders/{id}")
    Call<Order> updateOrder(@Path("id") int id, @Body Order order);

    @DELETE("api/orders/{id}")
    Call<Void> deleteOrder(@Path("id") int id);
}
