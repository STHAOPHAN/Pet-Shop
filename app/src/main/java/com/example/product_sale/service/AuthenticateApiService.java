package com.example.product_sale.service;

import com.example.product_sale.models.Request.LoginModel;
import com.example.product_sale.models.Respone.LoginResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticateApiService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://petshopapi-env.eba-xz2mv5rq.ap-southeast-1.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @POST("api/authenticate/login")
    Call<LoginResponse> login(@Body LoginModel loginModel);
}
