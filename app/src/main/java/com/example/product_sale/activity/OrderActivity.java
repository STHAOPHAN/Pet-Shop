package com.example.product_sale.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.adapter.OrderAdapter;
import com.example.product_sale.models.Order;
import com.example.product_sale.service.OrderApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;

    private List<Order> orderList;
    private ImageButton btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        rvOrders = findViewById(R.id.rv_orders);
        btnBack = findViewById(R.id.btn_back);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvOrders.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvOrders.addItemDecoration(itemDecoration);

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderList);
        rvOrders.setAdapter(orderAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        callApiGetOrders();
    }

    private void callApiGetOrders() {
        OrderApiService orderApiService = OrderApiService.retrofit.create(OrderApiService.class);
        orderApiService.getOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    orderAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(OrderActivity.this, "Failed to get orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Failure: " + t.getMessage());
            }
        });
    }
}
