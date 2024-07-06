package com.example.product_sale.service;
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
            .baseUrl("http://petshopapi-env.eba-xz2mv5rq.ap-southeast-1.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @GET("api/pettypes")
    Call<List<PetType>> getPetTypes();

    @POST("api/pettypes")
    Call<PetType> createPetType(@Body PetType petType);

    @PUT("api/pettypes/{id}")
    Call<PetType> updatePetType(@Path("id") int id, @Body PetType petType);
}
