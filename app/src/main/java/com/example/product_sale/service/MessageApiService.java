package com.example.product_sale.service;

import com.example.product_sale.models.Messages;
import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface MessageApiService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://petshopapi-env.eba-xz2mv5rq.ap-southeast-1.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @GET("api/messages")
    Call<List<Messages>> getMessages();

    @POST("api/messages")
    Call<Messages> createMessage(@Body Messages messages);

    @PUT("api/messages/{id}")
    Call<Messages> updateMessage(@Path("id") int id, @Body Messages messages);

}
