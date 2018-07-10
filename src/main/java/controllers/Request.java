package controllers;

public class Request {
    private double amount;
    private long timestamp;

    public Request() {
    }

    public double getAmount() {
        return amount;
    }

    public Request setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Request setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}