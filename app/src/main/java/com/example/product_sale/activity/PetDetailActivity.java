package com.example.product_sale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.product_sale.R;
import com.example.product_sale.models.Pet;
import com.squareup.picasso.Picasso;

public class PetDetailActivity extends AppCompatActivity {
    private ImageView ivPetImage;
    private TextView tvPetId, tvPetName, tvPetColor, tvPetPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        ivPetImage = findViewById(R.id.iv_pet_image);
        tvPetId = findViewById(R.id.tv_pet_id);
        tvPetName = findViewById(R.id.tv_pet_name);
        tvPetColor = findViewById(R.id.tv_pet_color);
        tvPetPrice = findViewById(R.id.tv_pet_price);

        Intent intent = getIntent();
        Pet pet = intent.getParcelableExtra("pet");

        if (pet != null) {
            tvPetId.setText("ID: " + pet.getId());
            tvPetName.setText(pet.getName());
            tvPetColor.setText("Color: " + pet.getColor());
            tvPetPrice.setText("Price: $" + pet.getPrice());
            Picasso.get().load(pet.getImage()).into(ivPetImage);
        }
    }
}
