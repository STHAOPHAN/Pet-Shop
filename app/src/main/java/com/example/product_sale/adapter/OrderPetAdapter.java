package com.example.product_sale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.models.Pet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderPetAdapter extends RecyclerView.Adapter<OrderPetAdapter.OrderPetViewHolder> {

    private Context context;
    private List<Pet> petList;

    public OrderPetAdapter(Context context, List<Pet> petList) {
        this.context = context;
        this.petList = petList;
    }

    @NonNull
    @Override
    public OrderPetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_pet, parent, false);
        return new OrderPetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderPetViewHolder holder, int position) {
        Pet pet = petList.get(position);
        holder.tvPetName.setText(pet.getName());
        holder.tvPetPrice.setText(String.valueOf(pet.getPrice()));
        Picasso.get().load(pet.getImage()).into(holder.ivPetImage);
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public void setPets(List<Pet> pets) {
        this.petList = pets;
        notifyDataSetChanged();
    }

    public static class OrderPetViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPetImage;
        TextView tvPetName, tvPetPrice;

        public OrderPetViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPetImage = itemView.findViewById(R.id.iv_pet);
            tvPetName = itemView.findViewById(R.id.tv_pet_name);
            tvPetPrice = itemView.findViewById(R.id.tv_pet_price);
        }
    }
}
