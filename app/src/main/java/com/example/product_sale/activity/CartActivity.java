package com.example.product_sale.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.adapter.CartAdapter;
import com.example.product_sale.models.Cart;
import com.example.product_sale.models.CartItem;
import com.example.product_sale.models.Request.OrderModel;
import com.example.product_sale.service.OrderApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private RecyclerView rvCart;
    private CartAdapter cartAdapter;
    private TextView tvTotalPrice;
    private Button btnCheckout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvCart = findViewById(R.id.rv_cart);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnCheckout = findViewById(R.id.btn_checkout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCart.setLayoutManager(linearLayoutManager);


        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvCart.addItemDecoration(itemDecoration);

        cartAdapter = new CartAdapter(this, Cart.getInstance().getCartItems(), tvTotalPrice);
        rvCart.setAdapter(cartAdapter);

        tvTotalPrice.setText("Tổng tiền: " + Cart.getInstance().getTotalPrice());
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutOrder();
            }
        });
    }
    private void checkoutOrder() {
        OrderApiService orderApiService = OrderApiService.retrofit.create(OrderApiService.class);
        double totalPrice = Cart.getInstance().getTotalPrice();
        List<Integer> petIds = new ArrayList<>();
        for (CartItem item : Cart.getInstance().getCartItems()) {
            petIds.add(item.getPet().getId());
        }
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        OrderModel orderModel = new OrderModel(sharedPreferences.getInt("Id", -1), totalPrice, petIds);
        orderApiService.checkoutOrder(orderModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(CartActivity.this, "Checkout successful", Toast.LENGTH_SHORT).show();
                Cart.getInstance().clear();
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Checkout failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
