package com.example.product_sale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.activity.PetDetailActivity;
import com.example.product_sale.models.Pet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private Context context;
    private List<Pet> petList;

    public PetAdapter(List<Pet> petList) {
        this.petList = petList;
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
        holder.bind(pet);
        holder.petName.setText(pet.getName());
        holder.petBreed.setText(pet.getBreed());
        // Nếu bạn có hình ảnh thật sự, bạn có thể sử dụng thư viện như Glide hoặc Picasso để tải ảnh
        holder.petImage.setImageResource(R.drawable.ic_pet_placeholder);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PetDetailActivity.class);
            intent.putExtra("pet", pet); // Truyền đối tượng Pet vào Intent
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {

        private TextView petName;
        private TextView petBreed;
        private ImageView petImage;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            petName = itemView.findViewById(R.id.pet_name);
            petBreed = itemView.findViewById(R.id.pet_breed);
            petImage = itemView.findViewById(R.id.pet_image);
        }

        public void bind(Pet pet) {
            petName.setText(pet.getName());
            petBreed.setText(pet.getBreed());

            // Load image using Picasso
            Picasso.get().load(pet.getImage())
                    .placeholder(R.drawable.ic_pet_placeholder)
                    .error(R.drawable.ic_pet_placeholder)
                    .fit()
                    .centerCrop()
                    .into(petImage);
        }
    }
}
