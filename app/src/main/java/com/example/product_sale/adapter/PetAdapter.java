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

import com.bumptech.glide.Glide;
import com.example.product_sale.R;
import com.example.product_sale.models.Cart;
import com.example.product_sale.models.CartItem;
import com.example.product_sale.models.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {
    private Context context;
    private List<Pet> petList;
    private List<Pet> petListFull; // Danh sách đầy đủ của tất cả các pet
    private Cart cart;

    public PetAdapter(Context context, List<Pet> petList, Cart cart) {
        this.context = context;
        this.petList = new ArrayList<>(petList);
        this.petListFull = new ArrayList<>(petList); // Sao chép toàn bộ danh sách
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
        holder.tvPetBreed.setText("Giống loài: " + pet.getBreed());
        holder.tvPetName.setText("Tên: " + pet.getName());
        holder.tvPetColor.setText("Màu: " + pet.getColor());
        holder.tvPetPrice.setText("Giá: " + pet.getPrice() + " VND");

        // Tìm resource ID của ảnh từ tên ảnh
        int imageResId = context.getResources().getIdentifier(pet.getImage(), "drawable", context.getPackageName());

        // Tải và hiển thị ảnh sử dụng Glide
        Glide.with(holder.itemView.getContext())
                .load(imageResId)
                .placeholder(R.drawable.default_pet_image) // Ảnh mặc định khi đang tải
                .error(R.drawable.default_pet_image) // Ảnh hiển thị khi có lỗi
                .into(holder.ivPet);

        holder.btnAddToCart.setOnClickListener(v -> {
            CartItem cartItem = new CartItem(pet, 1);
            cart.addItem(cartItem);
            Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    // Hàm lọc danh sách pet theo petTypeId và giống loài
    public void filter(String petTypeId, String breed) {
        petList.clear();
        if (petTypeId.isEmpty() && breed.isEmpty()) {
            petList.addAll(petListFull); // Nếu không có bộ lọc, hiển thị tất cả các pet
        } else {
            for (Pet pet : petListFull) {
                if ((petTypeId.isEmpty() || pet.getPetType().getName().equals(petTypeId)) &&
                        (breed.isEmpty() || pet.getBreed().equals(breed))) {
                    petList.add(pet);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class PetViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPet;
        TextView tvPetName, tvPetBreed, tvPetColor, tvPetPrice;
        ImageButton btnAddToCart;

        public PetViewHolder(View itemView) {
            super(itemView);
            ivPet = itemView.findViewById(R.id.iv_pet);
            tvPetName = itemView.findViewById(R.id.tv_pet_name);
            tvPetBreed = itemView.findViewById(R.id.tv_pet_breed);
            tvPetColor = itemView.findViewById(R.id.tv_pet_color);
            tvPetPrice = itemView.findViewById(R.id.tv_pet_price);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}
