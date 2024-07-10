package com.example.product_sale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.models.Sale;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.SaleViewHolder> {
    private Context context;
    private List<Sale> saleList;
    private ImageView ivSaleImage;
    public SaleAdapter(Context context, List<Sale> saleList) {
        this.context = context;
        this.saleList = saleList;
    }

    @NonNull
    @Override
    public SaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false);
        return new SaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleViewHolder holder, int position) {
        Sale sale = saleList.get(position);
        holder.tvSaleId.setText("Sale ID: " + sale.getId());
        holder.tvAnimalId.setText("Animal ID: " + sale.getAnimalId());

        // Split and get date part before 'T'
        String saleDate = sale.getSaleDate();
        if (saleDate != null && saleDate.contains("T")) {
            saleDate = saleDate.split("T")[0]; // This will keep only the date part
        }

        holder.tvSaleDate.setText("Sale Date: " + saleDate);
        holder.tvSalePrice.setText("Sale Price: " + sale.getSalePrice().toString());

        // Load image into ivPet using Picasso
        String imagePath = "android.resource://" + context.getPackageName() + "/drawable/img_pet_" + sale.getId();
        Picasso.get().load(imagePath)
                .placeholder(R.drawable.ic_launcher_background)  // Default image while loading
                .error(R.drawable.ic_delete)  // Image to show if error occurs
                .into(holder.ivPet);
    }



    @Override
    public int getItemCount() {
        return saleList.size();
    }

    public static class SaleViewHolder extends RecyclerView.ViewHolder {
        TextView tvSaleId, tvAnimalId, tvCustomerId, tvSaleDate, tvSalePrice;
        ImageView ivPet;

        public SaleViewHolder(View itemView) {
            super(itemView);
            tvSaleId = itemView.findViewById(R.id.tv_sale_id);
            tvAnimalId = itemView.findViewById(R.id.tv_animal_id);
            tvCustomerId = itemView.findViewById(R.id.tv_customer_id);
            tvSaleDate = itemView.findViewById(R.id.tv_sale_date);
            tvSalePrice = itemView.findViewById(R.id.tv_sale_price);
            ivPet = itemView.findViewById(R.id.iv_pet);
        }
    }
}
