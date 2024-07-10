package com.example.product_sale.service;
import com.example.product_sale.config.AppConfig;
import com.example.product_sale.models.PetType;

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
public interface PetTypeApiService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConfig.DATABASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @GET("api/pettypes")
    Call<List<PetType>> getPetTypes();

    @POST("api/pettypes")
    Call<PetType> createPetType(@Body PetType petType);

    @PUT("api/pettypes/{id}")
    Call<PetType> updatePetType(@Path("id") int id, @Body PetType petType);
}
