package com.example.product_sale.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.models.CartItem;
import com.example.product_sale.models.Pet;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private Context context;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        if (cartItem == null) {
            return;
        }
        Pet pet = cartItem.getPet();
        // Gán dữ liệu cho các view trong item_cart.xml
        holder.ivPet.setImageResource(R.drawable.ic_app_background); // Giả sử bạn có hình placeholder
        holder.tvPetName.setText(pet.getName());
        holder.tvPetColor.setText("Màu: " + pet.getColor());
        holder.tvPetPrice.setText("Giá: " + pet.getPrice() * cartItem.getQuantity());
        holder.tvPetQuantity.setText("Số lượng: " + cartItem.getQuantity());

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveConfirmationDialog(position);
            }
        });
    }
    private void showRemoveConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Remove Confirmation");
        builder.setMessage("Are you sure you want to remove this item from the cart??");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeItem(position);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void removeItem(int position) {
        cartItems.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPet;
        private TextView tvPetName;
        private TextView tvPetColor;
        private TextView tvPetPrice;
        private TextView tvPetQuantity;
        private ImageButton btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPet = itemView.findViewById(R.id.iv_pet);
            tvPetName = itemView.findViewById(R.id.tv_pet_name);
            tvPetColor = itemView.findViewById(R.id.tv_pet_color);
            tvPetPrice = itemView.findViewById(R.id.tv_pet_price);
            tvPetQuantity = itemView.findViewById(R.id.tv_pet_quantity);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }
    }
}
