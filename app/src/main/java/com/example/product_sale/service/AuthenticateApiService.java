package com.example.product_sale.service;

import com.example.product_sale.config.AppConfig;
import com.example.product_sale.models.Request.LoginModel;
import com.example.product_sale.models.Respone.LoginResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticateApiService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConfig.DATABASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @POST("api/authenticate/login")
    Call<LoginResponse> login(@Body LoginModel loginModel);
}
