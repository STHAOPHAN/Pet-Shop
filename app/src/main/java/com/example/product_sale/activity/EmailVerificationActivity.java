package com.example.product_sale.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.product_sale.R;
import com.example.product_sale.models.Customer;
import com.example.product_sale.service.CustomerApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailVerificationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Handler handler = new Handler();
    private boolean isVerified = false;
    private static final int CHECK_INTERVAL = 3000; // 3 seconds
    private static final int TIMEOUT = 300000; // 5 minutes in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Customer customer = getIntent().getParcelableExtra("customer");
        if (customer == null) finish();
        if (mUser == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            showProgressBar();
            startEmailVerificationCheck(customer);
            startTimeoutCheck();
        }
    }

    private void showProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void startEmailVerificationCheck(Customer customer) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (mUser.isEmailVerified()) {
                            isVerified = true;
                            callApiInsertCustomer(customer);
                        } else {
                            if (!isVerified) {
                                startEmailVerificationCheck(customer);
                            }
                        }
                    }
                });
            }
        }, CHECK_INTERVAL); // Check every 3 seconds
    }

    private void callApiInsertCustomer(Customer customer) {
        CustomerApiService customerApiService = CustomerApiService.retrofit.create(CustomerApiService.class);
        Call<Customer> call = customerApiService.createCustomer(customer);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EmailVerificationActivity.this, "Account Created.",
                            Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EmailVerificationActivity.this, "Account registration failed.",
                            Toast.LENGTH_SHORT).show();
                    deleteFirebaseUser();
                }
            }
            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("API_ERROR", "Failure: " + t.getMessage());
                Toast.makeText(EmailVerificationActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void deleteFirebaseUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("FIREBASE_USER", "User account deleted.");
                        } else {
                            Log.e("FIREBASE_USER", "Failed to delete user account.");
                        }
                    }
                });
        }
    }

    private void startTimeoutCheck() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isVerified) {
                    showTimeoutDialog();
                }
            }
        }, TIMEOUT); // Timeout after 5 minutes
    }

    private void showTimeoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EmailVerificationActivity.this);
        builder.setTitle("Verification Timeout");
        builder.setMessage("It seems like you haven't verified your email. Please try again or register with a different email.");

        builder.setPositiveButton("Resend Verification Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resendVerificationEmail();
            }
        });

        builder.setNegativeButton("Register Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent intent = new Intent(EmailVerificationActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resendVerificationEmail() {
        mUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailVerificationActivity.this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EmailVerificationActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isVerified = true; // Stop the handler when activity is destroyed
    }
}
