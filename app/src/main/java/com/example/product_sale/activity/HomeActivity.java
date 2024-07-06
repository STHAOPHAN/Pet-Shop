package com.example.product_sale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
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
import com.example.product_sale.models.PetType;
import com.example.product_sale.service.PetApiService;
import com.example.product_sale.service.PetTypeApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rvPets;
    private PetAdapter petAdapter;
    private List<Pet> mListPet;
    private List<PetType> mPetTypeList;
    private ImageButton btnDetails;
    private AutoCompleteTextView spinnerPetType, spinnerBreed;
    private ImageButton btnSearch;

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
        petAdapter = new PetAdapter(this, mListPet, Cart.getInstance());
        rvPets.setAdapter(petAdapter);
        btnDetails = findViewById(R.id.btn_details);
        btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(Cart.getInstance().getCartItems()));
            startActivity(intent);
        });
/*
        spinnerPetType = findViewById(R.id.spinner_pet_type);
        spinnerBreed = findViewById(R.id.spinner_breed);
        btnSearch = findViewById(R.id.btn_search);

        loadPetTypes();
        loadBreeds();

        btnSearch.setOnClickListener(v -> performSearch());*/

        callApiGetPets();
    }
    private void loadPetTypes() {
        PetTypeApiService petTypeApiService = PetTypeApiService.retrofit.create(PetTypeApiService.class);
        petTypeApiService.getPetTypes().enqueue(new Callback<List<PetType>>() {
            @Override
            public void onResponse(Call<List<PetType>> call, Response<List<PetType>> response) {
                if (response.isSuccessful()) {
                    List<String> petTypeNames = new ArrayList<>();
                    for (PetType petType : response.body()) {
                        petTypeNames.add(petType.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_dropdown_item_1line, petTypeNames);
                    spinnerPetType.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<PetType>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Failed to load pet types", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBreeds() {
        // Tải danh sách các giống loài từ API hoặc từ cơ sở dữ liệu
        // Cập nhật spinnerBreed với danh sách giống loài tương tự như spinnerPetType
    }
    private void callApiGetPets() {
        PetApiService petApiService = PetApiService.retrofit.create(PetApiService.class);
        petApiService.getPets(null, null).enqueue(new Callback<List<Pet>>() {
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

    private void performSearch() {
        String selectedPetType = spinnerPetType.getText().toString();
        String selectedBreed = spinnerBreed.getText().toString();

        Integer petTypeId = null;
        if (!selectedPetType.isEmpty()) {
            for (PetType petType : mPetTypeList) {
                if (petType.getName().equals(selectedPetType)) {
                    petTypeId = petType.getId();
                    break;
                }
            }
        }

        PetApiService petApiService = PetApiService.retrofit.create(PetApiService.class);
        petApiService.getPets(petTypeId, selectedBreed).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                if (response.isSuccessful()) {
                    mListPet.clear();
                    mListPet.addAll(response.body());
                    petAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to get pets", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
