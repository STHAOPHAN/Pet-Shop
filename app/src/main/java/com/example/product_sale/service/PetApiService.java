package com.example.product_sale.service;

import com.example.product_sale.config.AppConfig;
import com.example.product_sale.models.Pet;

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

public interface PetApiService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConfig.DATABASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @GET("api/pets")
    Call<List<Pet>> getPets(@Query("petType") String petType, @Query("breed") String breed);

    @POST("api/pets")
    Call<Pet> createPet(@Body Pet pet);

    @PUT("api/pets/{id}")
    Call<Pet> updatePet(@Path("id") int id, @Body Pet pet);

    @DELETE("api/pets/{id}")
    Call<Void> deletePet(@Path("id") int id);

    // If you need to get a single pet by ID, you can add this method:
    @GET("api/pets/{id}")
    Call<Pet> getPetById(@Path("id") int id);
}
