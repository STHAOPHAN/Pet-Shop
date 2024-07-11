package com.example.product_sale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.product_sale.R;
import com.example.product_sale.adapter.MessageAdapter;
import com.example.product_sale.models.Cart;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private ImageButton buttonSendMessage;
    private DatabaseReference messagesRef;
    private FirebaseUser currentUser;
    private String userId;
    private String fullName;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.chatRecyclerView);
        editTextMessage = findViewById(R.id.messageEditText);
        buttonSendMessage = findViewById(R.id.sendMessageButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        userId = getIntent().getStringExtra("userId");
        fullName = getIntent().getStringExtra("fullName");
        if (userId == null || !isInteger(userId) || fullName == null) {
            finish();
            return;
        }
        this.getSupportActionBar().setTitle(fullName);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            finish();
            return;
        }

        buttonSendMessage.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessage(messageText);
                editTextMessage.setText("");
            }
        });
        initializeFirebase();
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void initializeFirebase() {
        runOnUiThread(() -> {
            messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(userId);
            messageList = new ArrayList<>();
            messageAdapter = new MessageAdapter(messageList, currentUser.getUid());
            recyclerView.setAdapter(messageAdapter);
            addFirebaseListener();
        });
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
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
            String formattedDateTime = formatter.format(currentTime);
            Message message = new Message(messageId, currentUser.getUid(), messageText, formattedDateTime);
            messagesRef.child(messageId).setValue(message);
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