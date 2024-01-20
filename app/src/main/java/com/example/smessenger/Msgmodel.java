package com.example.smessenger;

public class Msgmodel {
    String message, senderid;
    long time;

    public Msgmodel() {
    }

    public Msgmodel(String message, String senderid, long time) {
        this.message = message;
        this.senderid = senderid;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
