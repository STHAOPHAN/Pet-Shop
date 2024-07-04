package com.example.product_sale.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.adapter.CartAdapter;
import com.example.product_sale.models.Pet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView rvCart;
    private CartAdapter cartAdapter;
    private List<Pet> mListCart;
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

        mListCart = new ArrayList<>();
        // Giả sử bạn có danh sách các pet trong giỏ hàng
        // mListCart = getCartItems();

        // Tạo adapter và set cho RecyclerView
        cartAdapter = new CartAdapter(mListCart);
        rvCart.setAdapter(cartAdapter);

        // Tính tổng tiền
        double totalPrice = calculateTotalPrice(mListCart);
        tvTotalPrice.setText("Tổng tiền: " + totalPrice);
    }

    private double calculateTotalPrice(List<Pet> listCart) {
        double total = 0;
        for (Pet pet : listCart) {
            total = total + (pet.getPrice());
        }
        return total;
    }

    // Hàm để lấy danh sách các pet trong giỏ hàng (tùy vào cách bạn lưu trữ dữ liệu giỏ hàng)
    // private List<Pet> getCartItems() {
    //     return // Lấy danh sách pet từ SharedPreferences, Database, hoặc bất cứ nơi nào bạn lưu trữ
    // }
}
