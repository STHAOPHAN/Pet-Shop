package com.example.product_sale.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.adapter.PetAdapter;
import com.example.product_sale.models.Cart;
import com.example.product_sale.models.Customer;
import com.example.product_sale.models.Pet;
import com.example.product_sale.service.PetApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rvPets;
    private PetAdapter petAdapter;
    private List<Pet> mListPet;
    private List<Customer> mListCustomer;

    private Cart cart;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rvPets = findViewById(R.id.rv_pets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPets.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvPets.addItemDecoration(itemDecoration);

        mListPet = new ArrayList<>();
        cart = new Cart();
        petAdapter = new PetAdapter(this, mListPet, cart);
        rvPets.setAdapter(petAdapter);

        callApiGetPets();
    }

/*    private void callApiGetPets() {

        PetApiService.apiService.getPets().enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                mListPet = response.body();
                PetAdapter petAdapter1 = new PetAdapter(mListPet);
                rvPets.setAdapter(petAdapter1);
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Log.e("API_ERROR", "Failure: " + t.getMessage());
            }
        });
    }*/

    private void callApiGetPets() {
        PetApiService petApiService = PetApiService.retrofit.create(PetApiService.class);
        petApiService.getPets().enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                if (response.isSuccessful()) {
                    mListPet.clear(); // Xóa danh sách cũ
                    mListPet.addAll(response.body()); // Thêm danh sách mới từ response
                    petAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to get pets", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Log.e("API_ERROR", "Failure: " + t.getMessage());
                Toast.makeText(HomeActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
