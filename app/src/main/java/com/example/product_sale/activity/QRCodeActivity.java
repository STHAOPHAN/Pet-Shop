package com.example.product_sale.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.product_sale.R;
import com.example.product_sale.service.SaleApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRCodeActivity extends AppCompatActivity {
    private ImageView qrImageView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        qrImageView = findViewById(R.id.qrImageView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        createQRCode(5);
    }

    private void createQRCode(int orderId) {
        progressBar.setVisibility(View.VISIBLE);
        SaleApiService saleApiService = SaleApiService.retrofit.create(SaleApiService.class);
        saleApiService.createQRCode(orderId).enqueue(new Callback<SaleApiService.QRCodeResponse>() {
            @Override
            public void onResponse(Call<SaleApiService.QRCodeResponse> call, Response<SaleApiService.QRCodeResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    String qrDataURL = response.body().getQrDataURL();
                    Bitmap qrBitmap = dataUriToBitmap(qrDataURL);
                    qrImageView.setImageBitmap(qrBitmap);
                } else {
                    Toast.makeText(QRCodeActivity.this, "Failed to create QR code" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaleApiService.QRCodeResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(QRCodeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap dataUriToBitmap(String dataUri) {
        try {
            String base64Image = dataUri.split(",")[1];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
