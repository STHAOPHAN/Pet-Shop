package com.example.product_sale.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.models.Pet;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Pet> mListPet;

    public CartAdapter(List<Pet> mListPet) {
        this.mListPet = mListPet;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Pet pet = mListPet.get(position);
        if (pet == null) {
            return;
        }
        // Gán dữ liệu cho các view trong item_cart.xml
        holder.ivPet.setImageResource(R.drawable.ic_app_background); // Giả sử bạn có hình placeholder
        holder.tvPetName.setText(pet.getName());
        holder.tvPetColor.setText("Màu: " + pet.getColor());
        holder.tvPetPrice.setText("Giá: " + pet.getPrice());
        holder.tvPetId.setText("ID: " + pet.getId());

        // Xử lý sự kiện khi bấm nút xóa khỏi giỏ hàng
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListPet.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                // Cập nhật lại tổng tiền nếu cần
                // ((CartActivity) holder.itemView.getContext()).updateTotalPrice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListPet != null ? mListPet.size() : 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPet;
        private TextView tvPetName;
        private TextView tvPetColor;
        private TextView tvPetPrice;
        private TextView tvPetId;
        private ImageButton btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPet = itemView.findViewById(R.id.iv_pet);
            tvPetName = itemView.findViewById(R.id.tv_pet_name);
            tvPetColor = itemView.findViewById(R.id.tv_pet_color);
            tvPetPrice = itemView.findViewById(R.id.tv_pet_price);
            tvPetId = itemView.findViewById(R.id.tv_pet_id);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }
    }
}
