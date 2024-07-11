package com.example.product_sale.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.product_sale.activity.BaseFragment;
import com.example.product_sale.adapter.MessageAdapter;
import com.example.product_sale.databinding.FragmentChatUserBinding;
import com.example.product_sale.models.Customer;
import com.example.product_sale.models.Message;
import com.example.product_sale.service.CustomerApiService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragmentUser extends BaseFragment {
    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private ImageButton buttonSendMessage;
    private DatabaseReference messagesRef;
    private FirebaseUser currentUser;
    private String userId;
    private boolean isUser;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private FragmentChatUserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.chatRecyclerView;
        editTextMessage = binding.messageEditText;
        buttonSendMessage = binding.sendMessageButton;

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Chat with Pet Shop");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return root;
        }
        buttonSendMessage.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessage(messageText);
                editTextMessage.setText("");
            }
        });
        callApiGetCustomerByEmail(currentUser.getEmail());
        return root;
    }

    private void callApiGetCustomerByEmail(String email) {
        CustomerApiService customerApiService = CustomerApiService.retrofit.create(CustomerApiService.class);
        customerApiService.getCustomers(email).enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Customer customer = response.body().get(0);
                    userId = String.valueOf(customer.getId());
                    initializeFirebase();
                } else {
                    Toast.makeText(getActivity(), "Failed to get customer info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Log.e("API_ERROR", "Failure: " + t.getMessage());
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeFirebase() {
        messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(userId);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, currentUser.getUid());
        recyclerView.setAdapter(messageAdapter);
        addFirebaseListener();
    }

    private void addFirebaseListener() {
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                if (message != null) {
                    messageList.add(message);
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle message update if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle message removal if needed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle message movement if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }


    private void sendMessage(String messageText) {
        String messageId = messagesRef.push().getKey();
        if (messageId != null) {
            Date currentDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
            String formattedDateTime = formatter.format(currentDate);
            Message message = new Message(messageId, currentUser.getUid(), messageText, formattedDateTime);
            messagesRef.child(messageId).setValue(message);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
