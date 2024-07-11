package com.example.product_sale.adapter;

import android.text.Layout;
import android.view.Gravity;
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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private String currentUserId;

    public MessageAdapter(List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout containerMessageSender;
        LinearLayout containerMessageReceiver;
        TextView textMessageSender;
        TextView textMessageReceiver;
        TextView textTimestampSender;
        TextView textTimestampReceiver;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            containerMessageSender = itemView.findViewById(R.id.sender_message_container);
            containerMessageReceiver = itemView.findViewById(R.id.receiver_message_container);
            textMessageSender = itemView.findViewById(R.id.text_message_sender);
            textMessageReceiver = itemView.findViewById(R.id.text_message_receiver);
            textTimestampSender = itemView.findViewById(R.id.text_sender_timestamp);
            textTimestampReceiver = itemView.findViewById(R.id.text_receiver_timestamp);
        }

        public void bind(Message message) {
            if (message.getSenderId().equals(currentUserId)) {
                containerMessageSender.setVisibility(View.VISIBLE);
                containerMessageReceiver.setVisibility(View.GONE);
                textMessageSender.setText(message.getMessageText());
                textTimestampSender.setVisibility(View.VISIBLE);
                textTimestampReceiver.setVisibility(View.GONE);
                textTimestampSender.setText("Sent at " + message.getSentAt());
            } else {
                containerMessageSender.setVisibility(View.GONE);
                containerMessageReceiver.setVisibility(View.VISIBLE);
                textMessageReceiver.setText(message.getMessageText());
                textTimestampSender.setVisibility(View.GONE);
                textTimestampReceiver.setVisibility(View.VISIBLE);
                textTimestampReceiver.setText("Sent at " + message.getSentAt());
            }
        }
    }
}