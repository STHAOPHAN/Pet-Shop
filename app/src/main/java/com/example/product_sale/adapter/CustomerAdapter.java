package com.example.product_sale.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.activity.LoginActivity;
import com.example.product_sale.models.Customer;
import com.example.product_sale.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.UserViewHolder> {
    private List<Customer> userList;
    private Map<Integer, Message> customerLastMessageMap;
    private OnUserClickListener onUserClickListener;
    private FirebaseAuth auth;

    public interface OnUserClickListener {
        void onUserClick(Customer user);
    }

    public CustomerAdapter (List<Customer> userList, Map<Integer, Message> customerLastMessageMap, OnUserClickListener onUserClickListener) {
        this.userList = userList;
        this.customerLastMessageMap = customerLastMessageMap;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Customer customer = userList.get(position);
        holder.userEmailTextView.setText(customer.getFullName());
        if (customerLastMessageMap.get(customer.getId()) != null) {
            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            if (user == null){
                return;
            }
            String timeDifference = "";
            Date currentDate = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
            try {
                Date lastMessageDate = dateFormat.parse(customerLastMessageMap.get(customer.getId()).getSentAt());
                long diffInMillis = currentDate.getTime() - lastMessageDate.getTime();
                timeDifference = convertMillisToReadableTime(diffInMillis);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            String uid = user.getUid();
            if (uid.equals(customerLastMessageMap.get(customer.getId()).getSenderId())) {
                holder.latestMessageTextView.setText("You: "+trimStringTo40Chars(customerLastMessageMap.get(customer.getId()).getMessageText())+" · "+timeDifference);
            }
            else {
                holder.latestMessageTextView.setText("Customer:"+trimStringTo40Chars(customerLastMessageMap.get(customer.getId()).getMessageText())+"·"+timeDifference);
            }
        }

        holder.itemView.setOnClickListener(v -> onUserClickListener.onUserClick(customer));
    }

    public static String trimStringTo40Chars(String input) {
        if (input.length() <= 40) {
            return input;
        } else {
            return input.substring(0, 37) + "...";
        }
    }

    public static String convertMillisToReadableTime(long millis) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long weeks = days / 7;
        long months = days / 30;
        long years = days / 365;

        if (seconds < 60) {
            return seconds + "s";
        } else if (minutes < 60) {
            return minutes + "m";
        } else if (hours < 24) {
            return hours + "h";
        } else if (days < 7) {
            return days + "d";
        } else if (weeks < 4) {
            return weeks + "w";
        } else if (months < 12) {
            return months + "mo";
        } else {
            return years + "y";
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userEmailTextView;
        TextView latestMessageTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userEmailTextView = itemView.findViewById(R.id.userEmailTextView);
            latestMessageTextView = itemView.findViewById(R.id.latestMessageTextView);
        }
    }
}