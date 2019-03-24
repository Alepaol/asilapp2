package asilapp.sms.com.measure.model;

import android.support.annotation.NonNull;

public class AsilRoom implements Comparable<AsilRoom>{
    private String sender;
    private String receiver;
    private String senderUid;
    private String receiverUid;
    private String message;
    private long timestamp;

    public AsilRoom() {

    }

    public AsilRoom(String sender, String receiver, String senderUid, String receiverUid, String message, long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.timestamp = timestamp;

    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public String getSender() {
        return sender;
    }

    public String getSenderUid() {
        return senderUid;
    }


    @Override
    public int compareTo(@NonNull AsilRoom asilRoom) {
        return Long.compare(this.timestamp,asilRoom.timestamp);
    }
}
