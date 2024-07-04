package com.example.product_sale.service;

import com.example.product_sale.models.Pet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface PetApiService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://petshopapi-env.eba-xz2mv5rq.ap-southeast-1.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @GET("api/pets")
    Call<List<Pet>> getPets();
}
