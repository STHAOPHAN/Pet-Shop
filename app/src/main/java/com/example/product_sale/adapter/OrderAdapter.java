package com.example.product_sale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.activity.QRCodeActivity;
import com.example.product_sale.models.Order;
import com.example.product_sale.activity.OrderPetActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orderList;
    private Button btnPay;


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
        holder.tvOrderId.setText("Mã hàng: " + order.getId());
        holder.tvCustomerId.setText("Mã người dùng: " + order.getCustomerId());

        // Format the order date
        String orderDate = order.getOrderDate();
        orderDate = formatDateString(orderDate);

        holder.tvOrderDate.setText("Ngày đặt: " + orderDate);

        holder.tvTotalPrice.setText("Tổng tiền: " + order.getTotalPrice().toString() + " VND");
        holder.tvStatus.setText("Trạng thái: " + order.getStatus());

        if (order.getStatus().equals("Paid")){
            holder.btnPay.setVisibility(View.GONE);
        }

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

        holder.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến QRCodeActivity và truyền orderId
                Intent intent = new Intent(context, QRCodeActivity.class);
                intent.putExtra("orderId", order.getId()); // Pass order ID or any other data needed
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // Method to format the date string
    private String formatDateString(String dateString) {
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date = fromUser.parse(dateString);
            return myFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // return original date string if parsing fails
        }
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvCustomerId, tvOrderDate, tvTotalPrice, tvStatus;

        Button btnPay;
        public OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvCustomerId = itemView.findViewById(R.id.tv_customer_id);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            tvStatus = itemView.findViewById(R.id.tv_order_status);
            btnPay = itemView.findViewById(R.id.btn_pay);
        }
    }
}
