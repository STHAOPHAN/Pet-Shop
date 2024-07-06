package com.example.product_sale.cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class CartFragment extends Fragment {
    private RecyclerView rvCart;
    private CartAdapter cartAdapter;
    private TextView tvTotalPrice;
    private Button btnCheckout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCart = root.findViewById(R.id.rv_cart);
        tvTotalPrice = root.findViewById(R.id.tv_total_price);
        btnCheckout = root.findViewById(R.id.btn_checkout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvCart.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvCart.addItemDecoration(itemDecoration);

        cartAdapter = new CartAdapter(getContext(), Cart.getInstance().getCartItems(), tvTotalPrice);
        rvCart.setAdapter(cartAdapter);

        tvTotalPrice.setText("Tổng tiền: " + Cart.getInstance().getTotalPrice());
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutOrder();
            }
        });

        return root;
    }

    private void checkoutOrder() {
        OrderApiService orderApiService = OrderApiService.retrofit.create(OrderApiService.class);
        double totalPrice = Cart.getInstance().getTotalPrice();
        List<Integer> petIds = new ArrayList<>();
        for (CartItem item : Cart.getInstance().getCartItems()) {
            petIds.add(item.getPet().getId());
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        OrderModel orderModel = new OrderModel(sharedPreferences.getString("Email", ""), totalPrice, petIds);
        orderApiService.checkoutOrder(orderModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "Checkout successful", Toast.LENGTH_SHORT).show();
                Cart.getInstance().clear();
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Checkout failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
