package com.example.product_sale.service;

import com.example.product_sale.models.Customer;
import com.example.product_sale.models.Pet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
public interface CustomerApiService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://petshopapi-env.eba-xz2mv5rq.ap-southeast-1.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("api/customers")
    Call<List<Customer>> getCustomers();

    @POST("api/customers")
    Call<Customer> createCustomer(@Body Customer customer);

    @PUT("api/customers/{id}")
    Call<Customer> updateCustomer(@Path("id") int id, @Body Customer customer);
}
