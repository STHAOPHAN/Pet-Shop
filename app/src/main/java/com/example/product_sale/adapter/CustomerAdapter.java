package com.example.product_sale.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.models.Customer;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.UserViewHolder> {
    private List<Customer> userList;
    private OnUserClickListener onUserClickListener;

    public interface OnUserClickListener {
        void onUserClick(Customer user);
    }

    public CustomerAdapter (List<Customer> userList, OnUserClickListener onUserClickListener) {
        this.userList = userList;
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
        holder.itemView.setOnClickListener(v -> onUserClickListener.onUserClick(customer));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userEmailTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userEmailTextView = itemView.findViewById(R.id.userEmailTextView);
        }
    }
}