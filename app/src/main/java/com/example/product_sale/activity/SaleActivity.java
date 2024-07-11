package com.example.product_sale.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.adapter.SaleAdapter;
import com.example.product_sale.models.Pet;
import com.example.product_sale.models.Sale;
import com.example.product_sale.service.PetApiService;
import com.example.product_sale.service.SaleApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleActivity extends AppCompatActivity {
    private RecyclerView rvSales;
    private SaleAdapter saleAdapter;

    private List<Sale> mListSale;
    private List<Pet> mListPet;
    private ImageButton btnBack;
    private ImageView ivSaleImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        rvSales = findViewById(R.id.rv_sales);
        btnBack = findViewById(R.id.btn_back);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvSales.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvSales.addItemDecoration(itemDecoration);

        mListSale = new ArrayList<>();
        saleAdapter = new SaleAdapter(this, mListSale);
        rvSales.setAdapter(saleAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        callApiGetSales();

    }




    private void callApiGetSales() {
        SaleApiService saleApiService = SaleApiService.retrofit.create(SaleApiService.class);
        saleApiService.getSales().enqueue(new Callback<List<Sale>>() {
            @Override
            public void onResponse(Call<List<Sale>> call, Response<List<Sale>> response) {
                if (response.isSuccessful()) {
                    mListSale.clear();
                    mListSale.addAll(response.body());
                    saleAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SaleActivity.this, "Failed to get sales", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Sale>> call, Throwable t) {
                Toast.makeText(SaleActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
