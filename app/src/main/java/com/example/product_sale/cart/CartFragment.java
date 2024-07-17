package com.example.product_sale.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.activity.BaseFragment;
import com.example.product_sale.activity.LoginActivity;
import com.example.product_sale.activity.OrderActivity;
import com.example.product_sale.activity.OrderPetActivity;
import com.example.product_sale.activity.QRCodeActivity;
import com.example.product_sale.adapter.CartAdapter;
import com.example.product_sale.models.Cart;
import com.example.product_sale.models.CartItem;
import com.example.product_sale.models.Order;
import com.example.product_sale.models.Request.OrderModel;
import com.example.product_sale.service.OrderApiService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends BaseFragment {
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
                if (Cart.getInstance().getCartItems().isEmpty()){
                    Toast.makeText(getContext(), "There is nothing to checkout. Please add items to your cart.", Toast.LENGTH_SHORT).show();
                    return;
                }
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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
        String emailUser = user.getEmail();
        OrderModel orderModel = new OrderModel(emailUser, totalPrice, petIds);
        orderApiService.checkoutOrder(orderModel).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Toast.makeText(getContext(), "Checkout successful", Toast.LENGTH_SHORT).show();
                Cart.getInstance().clear();
                cartAdapter.notifyDataSetChanged();

                Intent intent = new Intent(getActivity(), OrderActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(getContext(), "Checkout failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
