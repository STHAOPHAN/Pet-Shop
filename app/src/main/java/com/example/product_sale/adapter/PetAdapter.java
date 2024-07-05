package com.example.product_sale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.models.Cart;
import com.example.product_sale.models.CartItem;
import com.example.product_sale.models.Pet;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {
    private Context context;
    private List<Pet> petList;
    private Cart cart;

    public PetAdapter(Context context, List<Pet> petList, Cart cart) {
        this.context = context;
        this.petList = petList;
        this.cart = cart;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        Pet pet = petList.get(position);
        holder.tvPetName.setText(pet.getName());
        holder.tvPetPrice.setText("$" + pet.getPrice());
        // Đặt hình ảnh pet nếu có
        // holder.ivPet.setImageResource(R.drawable.default_pet_image);
        holder.btnAddToCart.setOnClickListener(v -> {
            CartItem cartItem = new CartItem(pet, 1);
            cart.addItem(cartItem);
            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public static class PetViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPet;
        TextView tvPetName, tvPetPrice;
        ImageButton btnAddToCart;

        public PetViewHolder(View itemView) {
            super(itemView);
            ivPet = itemView.findViewById(R.id.iv_pet);
            tvPetName = itemView.findViewById(R.id.tv_pet_name);
            tvPetPrice = itemView.findViewById(R.id.tv_pet_price);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}

