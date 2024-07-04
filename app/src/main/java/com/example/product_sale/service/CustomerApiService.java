package com.example.product_sale.service;

import com.example.product_sale.models.Customer;
import com.example.product_sale.models.Pet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface CustomerApiService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyy").create()           ;
    CustomerApiService apiService = new Retrofit.Builder()
            .baseUrl("http://unitask-api-env.eba-way5nszv.ap-southeast-1.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CustomerApiService.class);
    @GET("api/users")
    Call<List<Customer>> getUsers();
}
