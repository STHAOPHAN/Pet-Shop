package com.example.product_sale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.product_sale.R;
import com.example.product_sale.models.Pet;
import com.squareup.picasso.Picasso;

public class PetDetailActivity extends BaseActivity {
    private ImageView ivPetImage;
    private TextView tvPetId, tvPetName, tvPetBreed, tvPetColor, tvPetPrice, tvPetAge, tvPetGender;  // Add TextView for gender

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setTitle("Pet Detail");
        setContentView(R.layout.activity_pet_detail);

        ivPetImage = findViewById(R.id.iv_pet_image);
        tvPetId = findViewById(R.id.tv_pet_id);
        tvPetName = findViewById(R.id.tv_pet_name);
        tvPetBreed = findViewById(R.id.tv_pet_breed);  // TextView for breed
        tvPetColor = findViewById(R.id.tv_pet_color);
        tvPetPrice = findViewById(R.id.tv_pet_price);
        tvPetAge = findViewById(R.id.tv_pet_age);  // TextView for age
        tvPetGender = findViewById(R.id.tv_pet_gender);  // Initialize TextView for gender

        Intent intent = getIntent();
        Pet pet = intent.getParcelableExtra("pet");

        if (pet != null) {
            tvPetId.setText("ID: " + pet.getId());
            tvPetName.setText("Tên: " +pet.getName());
            tvPetBreed.setText("Giống loài: " + pet.getBreed());  // Set breed text
            tvPetColor.setText("Màu: " + pet.getColor());
            tvPetPrice.setText("Price: $" + pet.getPrice());
            tvPetAge.setText("Tuổi: " + pet.getAge());  // Set age text
            tvPetGender.setText("Giới tính: " + pet.getGender());  // Set gender text

            String imageName = pet.getImage(); // Đây là tên tệp hình ảnh, ví dụ: img_pet_1.jpg
            String imagePath = "android.resource://" + getPackageName() + "/drawable/" + imageName;

            Picasso.get().load(imagePath)
                    .placeholder(R.drawable.ic_launcher_background)  // Ảnh mặc định khi đang tải
                    .error(R.drawable.ic_delete)        // Ảnh hiển thị khi có lỗi
                    .into(ivPetImage);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_revert) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
