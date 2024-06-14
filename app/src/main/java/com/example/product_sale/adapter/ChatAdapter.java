package com.example.product_sale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.product_sale.R;
import com.example.product_sale.models.Message;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private List<Message> messageList;

    public ChatAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messageList.get(position);

        // Ẩn tất cả các container tin nhắn trước
        holder.userMessageContainer.setVisibility(View.GONE);
        holder.shopMessageContainer.setVisibility(View.GONE);

        // Hiển thị tin nhắn dựa trên loại
        if (message.isUser()) {
            holder.userMessageContainer.setVisibility(View.VISIBLE);
            holder.textUserMessage.setText(message.getText());
        } else {
            holder.shopMessageContainer.setVisibility(View.VISIBLE);
            holder.textShopMessage.setText(message.getText());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        LinearLayout userMessageContainer, shopMessageContainer;
        TextView textUserMessage, textShopMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            userMessageContainer = itemView.findViewById(R.id.user_message_container);
            shopMessageContainer = itemView.findViewById(R.id.shop_message_container);
            textUserMessage = itemView.findViewById(R.id.text_user_message);
            textShopMessage = itemView.findViewById(R.id.text_shop_message);
        }
    }
}