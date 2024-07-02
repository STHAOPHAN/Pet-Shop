package com.example.product_sale.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.product_sale.R;
import com.example.product_sale.models.Pet;

public class PetDetailActivity extends AppCompatActivity {

    ImageView petImageView;
    TextView petNameTextView;
    TextView petBreedTextView;
    TextView petAgeTextView;
    TextView petWeightTextView;
    TextView petPriceTextView;
    TextView petDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        petImageView = findViewById(R.id.pet_image_detail);
        petNameTextView = findViewById(R.id.pet_name_detail);
        petBreedTextView = findViewById(R.id.pet_breed_detail);
        petAgeTextView = findViewById(R.id.pet_age_detail);
        petWeightTextView = findViewById(R.id.pet_weight_detail);
        petPriceTextView = findViewById(R.id.pet_price_detail);
        petDescriptionTextView = findViewById(R.id.pet_description_detail);

        // Nhận đối tượng Pet từ Intent
        Pet pet = getIntent().getParcelableExtra("pet");

        if (pet != null) {
            petNameTextView.setText(pet.getName());
            petBreedTextView.setText(pet.getBreed());
            petAgeTextView.setText(String.valueOf(pet.getAge()));
            petWeightTextView.setText(String.valueOf(pet.getWeight()));
            petPriceTextView.setText(String.valueOf(pet.getPrice()));
            petDescriptionTextView.setText(pet.getDescription());

            // Bạn có thể thêm hình ảnh cụ thể bằng cách sử dụng pet.getImage() nếu có URL hoặc resource id
            petImageView.setImageResource(R.drawable.ic_pet_placeholder);
        }
    }
}

