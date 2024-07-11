package com.example.product_sale.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.product_sale.R;
import com.example.product_sale.models.Order;
import com.example.product_sale.service.OrderApiService;
import com.example.product_sale.service.SaleApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRCodeActivity extends AppCompatActivity {
    private ImageView qrImageView;
    private ProgressBar progressBar;
    private Handler handler = new Handler();
    private Runnable checkStatusRunnable;
    private static final int CHECK_INTERVAL = 5000; // 5 seconds
    private boolean isCheckingStatus = false;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        qrImageView = findViewById(R.id.qrImageView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút quay lại
        }
        Intent intent = getIntent();
        orderId = intent.getIntExtra("orderId", -1);
        if (orderId != -1) {
            createQRCode(orderId);
        } else {
            Toast.makeText(this, "Invalid order ID", Toast.LENGTH_SHORT).show();
        }
        startCheckingOrderStatus();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                    qrImageView.setImageBitmap(dataUriToBitmap(qrDataURL));
                } else {
                    Toast.makeText(QRCodeActivity.this, "Failed to create QR code: " + response.message(), Toast.LENGTH_SHORT).show();
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

    private void startCheckingOrderStatus() {
        if (isCheckingStatus) return;
        isCheckingStatus = true;
        checkStatusRunnable = new Runnable() {
            @Override
            public void run() {
                checkOrderStatus(orderId);
                handler.postDelayed(this, CHECK_INTERVAL); // Check every 5 seconds
            }
        };
        handler.postDelayed(checkStatusRunnable, CHECK_INTERVAL);
    }

    private void stopCheckingOrderStatus() {
        isCheckingStatus = false;
        handler.removeCallbacks(checkStatusRunnable);
    }

    private void checkOrderStatus(int orderId) {
        OrderApiService orderApiService = OrderApiService.retrofit.create(OrderApiService.class);
        orderApiService.getOrderById(orderId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    String status = response.body().get(0).getStatus();
                    if ("Confirmed".equals(status)) {
                        stopCheckingOrderStatus();
                        showPaymentSuccessDialog();
                    }
                } else {
                    Toast.makeText(QRCodeActivity.this, "Failed to check order status: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(QRCodeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPaymentSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeActivity.this);
        builder.setTitle("Payment Successful");
        builder.setMessage("Your payment was successful. Thank you!");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(QRCodeActivity.this, OrderActivity.class);
                startActivity(intent);
                finish(); // Close QRCodeActivity
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCheckingOrderStatus();
    }
}
