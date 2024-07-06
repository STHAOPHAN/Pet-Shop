package com.example.product_sale.models;

import java.util.Date;

public class Message {
    private String messageId;
    private String senderId;
    private String messageText;
    private String sentAt;

    public Message() {
    }

    public Message(String messageId, String senderId, String messageText, String sentAt) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.messageText = messageText;
        this.sentAt = sentAt;
    }

    // Getter methods

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getSentAt() {
        return sentAt;
    }
}
