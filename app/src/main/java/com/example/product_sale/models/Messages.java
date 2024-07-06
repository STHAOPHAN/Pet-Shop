package com.example.product_sale.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Messages implements Parcelable {
    private int id;
    private int senderId;
    private int receiverId;
    private String message;
    private Date sentAt;
    private Customer receiver;
    private Customer sender;

    public Messages() {
    }

    protected Messages(Parcel in) {
        id = in.readInt();
        senderId = in.readInt();
        receiverId = in.readInt();
        message = in.readString();
        sentAt = new Date(in.readLong());
    }

    public static final Creator<Messages> CREATOR = new Creator<Messages>() {
        @Override
        public Messages createFromParcel(Parcel in) {
            return new Messages(in);
        }

        @Override
        public Messages[] newArray(int size) {
            return new Messages[size];
        }
    };

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public Customer getReceiver() {
        return receiver;
    }

    public void setReceiver(Customer receiver) {
        this.receiver = receiver;
    }

    public Customer getSender() {
        return sender;
    }

    public void setSender(Customer sender) {
        this.sender = sender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(senderId);
        dest.writeInt(receiverId);
        dest.writeString(message);
        dest.writeLong(sentAt != null ? sentAt.getTime() : -1);
    }
}