package com.example.product_sale.chat;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.activity.ChatActivity;
import com.example.product_sale.adapter.CustomerAdapter;
import com.example.product_sale.databinding.FragmentChatAdminBinding;
import com.example.product_sale.models.Customer;
import com.example.product_sale.service.CustomerApiService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragmentAdmin extends Fragment {
    private RecyclerView userRecyclerView;
    private CustomerAdapter customerAdapter;
    private List<Customer> userList;
    private List<Customer> filteredUserList;
    private EditText searchEditText;
    private FragmentChatAdminBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        userList = new ArrayList<>();
        filteredUserList = new ArrayList<>();
        callApiGetCustomers();
        customerAdapter = new CustomerAdapter(userList, this::openChatWithUser);
        userRecyclerView = binding.userRecyclerView;
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userRecyclerView.setAdapter(customerAdapter);
        searchEditText = binding.searchEditText;
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return root;
    }

    private void callApiGetCustomers() {
        CustomerApiService customerApiService = CustomerApiService.retrofit.create(CustomerApiService.class);
        customerApiService.getCustomers().enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful()) {
                    userList.clear(); // Xóa danh sách cũ
                    userList.addAll(response.body()); // Thêm danh sách mới từ response
                    customerAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                } else {
                    Toast.makeText(getActivity(), "Failed to get customers", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Log.e("API_ERROR", "Failure: " + t.getMessage());
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterUsers(String query) {
        filteredUserList.clear();
        for (Customer user : userList) {
            if (user.getEmail().toLowerCase().contains(query.toLowerCase()) || ("" + user.getId()).contains(query.toLowerCase())) {
                filteredUserList.add(user);
            }
        }
        customerAdapter = new CustomerAdapter(filteredUserList, this::openChatWithUser);
        userRecyclerView.setAdapter(customerAdapter);
        customerAdapter.notifyDataSetChanged();
    }

    private void openChatWithUser(Customer user) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("userId", ""+user.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
