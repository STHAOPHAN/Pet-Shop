package com.example.product_sale.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.adapter.CartAdapter;
import com.example.product_sale.models.Cart;
import com.example.product_sale.models.CartItem;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private RecyclerView rvCart;
    private CartAdapter cartAdapter;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private ArrayList<CartItem> cartItems;

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

        cartItems = getIntent().getParcelableArrayListExtra("cartItems");

        cartAdapter = new CartAdapter(this, Cart.getInstance().getCartItems());
        rvCart.setAdapter(cartAdapter);

        tvTotalPrice.setText("Tổng tiền: " + Cart.getInstance().getTotalPrice());
    }
}
