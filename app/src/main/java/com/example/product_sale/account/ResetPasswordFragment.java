package com.example.product_sale.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.product_sale.R;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.product_sale.activity.LoginActivity;
import com.example.product_sale.models.Customer;
import com.example.product_sale.models.Pet;
import com.example.product_sale.service.CustomerApiService;
import com.example.product_sale.service.PetApiService;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ResetPasswordFragment extends Fragment {
    private EditText currentPassword, newPassword, confirmPassword;
    private MaterialButton btnChangePassword;

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        currentPassword = view.findViewById(R.id.edtOldPwd);
        newPassword = view.findViewById(R.id.edtNewPwd);
        confirmPassword = view.findViewById(R.id.edtConfirmPwd);

        btnChangePassword = view.findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(v -> changePassword());

        return view;

    }
    private void changePassword() {
        String currentPass = currentPassword.getText().toString().trim();
        String newPass = newPassword.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()){
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPass.equals(confirmPass)){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null && user.getEmail() != null) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String email = user.getEmail();

                // Re-authenticate the user
                auth.signInWithEmailAndPassword(email, currentPass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        changePasswordInServer(email, currentPass, newPass);
                        // Change password
                        user.updatePassword(newPass).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Password change failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Current password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(getContext(), "The new password is not the same as the confirm password", Toast.LENGTH_SHORT).show();
        }

    }

    private void changePasswordInServer(String email, String oldPass, String newPass){
        CustomerApiService customerApiService = CustomerApiService.retrofit.create(CustomerApiService.class);
        customerApiService.changePassword(email, oldPass, newPass).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Customer customers = response.body();
                    if (customers == null) {
                        Toast.makeText(getActivity(), "No customer found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to change Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("API_ERROR", "Failure: " + t.getMessage());
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_account, menu); // Inflate the menu
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
