package com.codingnew.rentbuysell;

public class getchats {
    public String message;
    public String receiveR;
    public String sendeR;
    public long time;
    public getchats() {

    }

    public getchats(String message, String receiveR, String sendeR, long time) {
        this.message = message;
        this.receiveR = receiveR;
        this.sendeR = sendeR;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiveR() {
        return receiveR;
    }

    public void setReceiveR(String receiveR) {
        this.receiveR = receiveR;
    }

    public String getSendeR() {
        return sendeR;
    }

    public void setSendeR(String sendeR) {
        this.sendeR = sendeR;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
