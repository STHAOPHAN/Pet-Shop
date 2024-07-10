package com.example.product_sale.service;
import com.example.product_sale.models.OrderPet;

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

public interface OrderPetApiService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://petshopapi-env.eba-xz2mv5rq.ap-southeast-1.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @GET("api/orderpets")
    Call<List<OrderPet>> getOrderPets(@Query("orderId") int orderId);

    @POST("api/orderpets")
    Call<OrderPet> createOrderPet(@Body OrderPet orderPet);

    @PUT("api/orderpets/{id}")
    Call<OrderPet> updateOrderPet(@Path("id") int id, @Body OrderPet orderPet);
}
