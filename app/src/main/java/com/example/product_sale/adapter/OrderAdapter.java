package com.example.product_sale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.models.Order;
import com.example.product_sale.activity.OrderPetActivity;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderId.setText("Order ID: " + order.getId());
        holder.tvCustomerId.setText("Customer ID: " + order.getCustomerId());

        // Split and get date part before 'T'
        String orderDate = order.getOrderDate();
        if (orderDate != null && orderDate.contains("T")) {
            orderDate = orderDate.split("T")[0]; // This will keep only the date part
        }
        holder.tvOrderDate.setText("Order Date: " + orderDate);

        holder.tvTotalPrice.setText("Total Price: " + order.getTotalPrice().toString());
        holder.tvStatus.setText("Status: " + order.getStatus());

        // Handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to OrderPetActivity
                Intent intent = new Intent(context, OrderPetActivity.class);
                intent.putExtra("orderId", order.getId()); // Pass order ID or any other data needed
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvCustomerId, tvOrderDate, tvTotalPrice, tvStatus;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvCustomerId = itemView.findViewById(R.id.tv_customer_id);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            tvStatus = itemView.findViewById(R.id.tv_order_status);
        }
    }
}
