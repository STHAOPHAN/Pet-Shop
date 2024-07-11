package com.example.product_sale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.adapter.OrderAdapter;
import com.example.product_sale.models.Customer;
import com.example.product_sale.models.Order;
import com.example.product_sale.service.CustomerApiService;
import com.example.product_sale.service.OrderApiService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;
    private FirebaseAuth mAuth;
    private List<Order> orderList;
    private int idUser;
    private String emailUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        this.getSupportActionBar().setTitle("List Orders");

        rvOrders = findViewById(R.id.rv_orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvOrders.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvOrders.addItemDecoration(itemDecoration);

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderList);
        rvOrders.setAdapter(orderAdapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        emailUser = user.getEmail();
        loadProfileData();
    }

    private void callApiGetOrders(int customerId) {
        OrderApiService orderApiService = OrderApiService.retrofit.create(OrderApiService.class);
        orderApiService.getOrders(customerId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    orderList.clear();
                    if (response.body() != null) {
                        orderList.addAll(response.body());
                    }
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

    private void loadProfileData() {
        CustomerApiService customerApiService = CustomerApiService.retrofit.create(CustomerApiService.class);
        customerApiService.getCustomerByEmail(emailUser).enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful()) {
                    List<Customer> customers = response.body();
                    if (customers != null && !customers.isEmpty()) {
                        Customer customer = customers.get(0);
                        idUser = customer.getId();
                        callApiGetOrders(idUser);
                    } else {
                        Toast.makeText(OrderActivity.this, "No customer found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderActivity.this, "Failed to get customer", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Log.e("API_ERROR", "Failure: " + t.getMessage());
                Toast.makeText(OrderActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
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
