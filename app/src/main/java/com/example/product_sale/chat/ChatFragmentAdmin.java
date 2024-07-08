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

import com.example.product_sale.activity.BaseFragment;
import com.example.product_sale.activity.ChatActivity;
import com.example.product_sale.adapter.CustomerAdapter;
import com.example.product_sale.databinding.FragmentChatAdminBinding;
import com.example.product_sale.models.Customer;
import com.example.product_sale.models.Message;
import com.example.product_sale.service.CustomerApiService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragmentAdmin extends BaseFragment {
    private RecyclerView userRecyclerView;
    private CustomerAdapter customerAdapter;
    private List<Customer> userList;
    private List<Customer> filteredUserList;
    private EditText searchEditText;
    private FragmentChatAdminBinding binding;
    SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    Map<Integer, Message> customerLastMessageMap = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        userList = new ArrayList<>();
        filteredUserList = new ArrayList<>();
        callApiGetCustomers();
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
                    for (Customer user : userList){
                        if (user.getEmail().equals("thaopsse162032@fpt.edu.vn")) userList.remove(user);
                    }
                    //customerAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                    fetchLatestMessages();
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

    private void fetchLatestMessages() {
        customerLastMessageMap.clear();
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot customerSnapshot : dataSnapshot.getChildren()) {
                    String customerId = customerSnapshot.getKey();
                    if (customerId != null) {
                        Query lastMessageQuery = messagesRef.child(customerId).orderByKey().limitToLast(1);
                        lastMessageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                    Message message = messageSnapshot.getValue(Message.class);
                                    if (message != null) {
                                        Date date = null;
                                        try {
                                            date = inputFormat.parse(message.getSentAt());
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                        String formattedDate = outputFormat.format(date);
                                        updateLastMessageDate(Integer.parseInt(customerId), formattedDate);
                                        customerLastMessageMap.put(Integer.parseInt(customerId), message);
                                    }
                                }
                                sortUserList();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("FIREBASE_ERROR", "Failed to fetch last message: " + databaseError.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FIREBASE_ERROR", "Failed to fetch messages: " + databaseError.getMessage());
            }
        });
    }

    private void setupRecyclerView() {
        customerAdapter = new CustomerAdapter(userList, customerLastMessageMap, this::openChatWithUser);
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
    }

    private void updateLastMessageDate(int customerId, String lastMessageDate) {
        CustomerApiService customerApiService = CustomerApiService.retrofit.create(CustomerApiService.class);
        Call<Customer> call = customerApiService.updateLastMessageDate(customerId, lastMessageDate);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (!response.isSuccessful()) {
                    Log.e("API_ERROR", "Failed to update last message date: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("API_ERROR", "Failure: " + t.getMessage());
            }
        });
    }

    private void sortUserList() {
        Collections.sort(userList, new Comparator<Customer>() {
            @Override
            public int compare(Customer u1, Customer u2) {
                if (u1.getLastMessageDate() == null && u2.getLastMessageDate() == null) {
                    return 0;
                } else if (u1.getLastMessageDate() == null) {
                    return 1; // u1 null, đẩy xuống cuối danh sách
                } else if (u2.getLastMessageDate() == null) {
                    return -1; // u2 null, đẩy xuống cuối danh sách
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    try {
                        Date date1 = dateFormat.parse(u1.getLastMessageDate());
                        Date date2 = dateFormat.parse(u2.getLastMessageDate());
                        return date2.compareTo(date1); // sắp xếp theo thời gian giảm dần
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            }
        });
        setupRecyclerView();
        customerAdapter.notifyDataSetChanged();
    }

    private void filterUsers(String query) {
        filteredUserList.clear();
        Map<Integer, Message> customerLastMessageMap2 = customerLastMessageMap;
        for (Customer user : userList) {
            if (user.getFullName().toLowerCase().contains(query.toLowerCase()) ||user.getEmail().toLowerCase().contains(query.toLowerCase()) || ("" + user.getId()).contains(query.toLowerCase())) {
                filteredUserList.add(user);
            }
            customerLastMessageMap2.remove(user.getId());
        }

        customerAdapter = new CustomerAdapter(filteredUserList, customerLastMessageMap2, this::openChatWithUser);
        userRecyclerView.setAdapter(customerAdapter);
        customerAdapter.notifyDataSetChanged();
    }

    private void openChatWithUser(Customer user) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("userId", ""+user.getId());
        intent.putExtra("fullName", ""+user.getFullName());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
