package com.example.product_sale.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.adapter.OrderPetAdapter;
import com.example.product_sale.models.OrderPet;
import com.example.product_sale.models.Pet;
import com.example.product_sale.service.OrderPetApiService;
import com.example.product_sale.service.PetApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderPetActivity extends AppCompatActivity {
    private RecyclerView rvOrderPets;
    private OrderPetAdapter orderPetAdapter;
    private List<OrderPet> orderPetList;
    private List<Pet> petList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pet);
        this.getSupportActionBar().setTitle("Order Pet");

        rvOrderPets = findViewById(R.id.rv_order_pets);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvOrderPets.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvOrderPets.addItemDecoration(itemDecoration);

        orderPetAdapter = new OrderPetAdapter(this, petList);
        rvOrderPets.setAdapter(orderPetAdapter);

        // Get orderId from intent
        int orderId = getIntent().getIntExtra("orderId", -1); // -1 is default value if not found

        callApiGetOrderPets(orderId);
    }

    private void callApiGetOrderPets(int orderId) {
        OrderPetApiService orderPetApiService = OrderPetApiService.retrofit.create(OrderPetApiService.class);
        orderPetApiService.getOrderPets(orderId).enqueue(new Callback<List<OrderPet>>() {
            @Override
            public void onResponse(Call<List<OrderPet>> call, Response<List<OrderPet>> response) {
                if (response.isSuccessful()) {
                    orderPetList = response.body();
                    List<Integer> petIds = extractPetIds(orderPetList);
                    callApiGetPetsByIds(petIds);
                } else {
                    Toast.makeText(OrderPetActivity.this, "Failed to get order pets", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderPet>> call, Throwable t) {
                Toast.makeText(OrderPetActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Failure: " + t.getMessage());
            }
        });
    }

    private List<Integer> extractPetIds(List<OrderPet> orderPets) {
        List<Integer> petIds = new ArrayList<>();
        for (OrderPet orderPet : orderPets) {
            petIds.add(orderPet.getAnimalId());
        }
        return petIds;
    }

    private void callApiGetPetsByIds(List<Integer> petIds) {
        PetApiService petApiService = PetApiService.retrofit.create(PetApiService.class);

        for (int petId : petIds) {
            Map<String, Object> options = new HashMap<>();
            options.put("id", petId);

            petApiService.getPetsByIds(options).enqueue(new Callback<List<Pet>>() {
                @Override
                public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                    if (response.isSuccessful()) {
                        petList.addAll(response.body());
                        orderPetAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(OrderPetActivity.this, "Failed to get pets", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Pet>> call, Throwable t) {
                    Toast.makeText(OrderPetActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Failure: " + t.getMessage());
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_revert) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
