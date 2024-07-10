package com.example.product_sale.service;
import com.example.product_sale.config.AppConfig;
import com.example.product_sale.models.Sale;

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
public interface SaleApiService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConfig.DATABASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("api/sales")
    Call<List<Sale>> getSales();

    @POST("api/sales")
    Call<Sale> createSale(@Body Sale sale);

    @PUT("api/sales/{id}")
    Call<Sale> updateSale(@Path("id") int id, @Body Sale sale);

    @GET("api/sales/{id}")
    Call<Sale> getSaleById(@Path("id") int id);
}
