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
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import com.example.product_sale.R;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.product_sale.activity.BaseFragment;
import com.example.product_sale.activity.LoginActivity;
import com.example.product_sale.models.Customer;
import com.example.product_sale.service.CustomerApiService;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class ProfileFragment extends BaseFragment {

    private EditText edtFirstName, edtMobileNo, edtEmail, edtAddress;
    private CircleImageView ivProfileImage;
    private MaterialButton btnSaveProFile, btnChangePassword, btnDeactivate;
    private Customer customer;
    private String emailUser;
    private int idUser;

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Khởi tạo các View
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        edtFirstName = view.findViewById(R.id.edtFirstName);
        edtMobileNo = view.findViewById(R.id.edtMobileNo);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtAddress = view.findViewById(R.id.edtAddress);
        btnSaveProFile = view.findViewById(R.id.btnSaveProFile);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnDeactivate = view.findViewById(R.id.btnDeactivate);

        // Lấy dữ liệu profile và thiết lập giá trị cho các View
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
        emailUser = user.getEmail();
        customer = new Customer();
        loadProfileData();

        // Thiết lập sự kiện click cho nút lưu profile
        btnSaveProFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
                loadProfileData();
            }
        });

        // Thiết lập sự kiện click cho nút thay đổi mật khẩu
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        return view;
    }

    private void loadProfileData() {
        CustomerApiService customerApiService = CustomerApiService.retrofit.create(CustomerApiService.class);
        customerApiService.getCustomerByEmail(emailUser).enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful()) {
                    List<Customer> customers = response.body();
                    if (customers != null && !customers.isEmpty()) {
                        Customer customer = customers.get(0);
                        // Thiết lập giá trị cho các View
                        edtFirstName.setText(customer.getFullName());
                        edtMobileNo.setText(customer.getPhone());
                        edtEmail.setText(customer.getEmail());
                        edtAddress.setText(customer.getAddress());
                        idUser = customer.getId();
                    } else {
                        Toast.makeText(getActivity(), "No customer found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to get customer", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Log.e("API_ERROR", "Failure: " + t.getMessage());
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfileData() {
        // Lưu dữ liệu profile
        String fullName = edtFirstName.getText().toString();
        String mobileNo = edtMobileNo.getText().toString();
        String address = edtAddress.getText().toString();

        Customer editCustomer = new Customer();
        editCustomer.setFullName(fullName);
        editCustomer.setPhone(mobileNo);
        editCustomer.setAddress(address);


        // Lưu dữ liệu này vào cơ sở dữ liệu hoặc gửi tới API
        CustomerApiService customerApiService = CustomerApiService.retrofit.create(CustomerApiService.class);
        customerApiService.updateProfile(idUser, editCustomer).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Customer customer = response.body();
                    if (customer != null) {
                        // Thiết lập giá trị cho các View
                        edtFirstName.setText(customer.getFullName());
                        edtMobileNo.setText(customer.getPhone());
                        edtEmail.setText(customer.getEmail());
                        edtAddress.setText(customer.getAddress());
                        idUser = customer.getId();
                        Toast.makeText(getActivity(), "Update Profile Successful", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), "No customer found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to update customer", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("API_ERROR", "Failure: " + t.getMessage());
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changePassword() {
        NavOptions navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)    // Animation khi chuyển đến Fragment mới
                .setExitAnim(R.anim.slide_out_left)     // Animation khi Fragment hiện tại bị loại bỏ
                .setPopEnterAnim(R.anim.slide_in_left)  // Animation khi quay lại Fragment hiện tại
                .setPopExitAnim(R.anim.slide_out_right) // Animation khi Fragment hiện tại bị loại bỏ khi quay lại
                .build();

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_reset_password, null, navOptions);
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

