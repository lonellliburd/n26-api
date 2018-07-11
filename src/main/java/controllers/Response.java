package controllers;

public class Response {

    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;

    public Response() {
        sum = 0;
        avg = 0;
        max = -9999999;
        min = 9999999;
        count = 0;
    }

    public double getSum() {
        return sum;
    }

    public Response setSum(double sum) {
        this.sum = sum;
        return this;
    }

    public double getAvg() {
        return avg;
    }

    public Response setAvg(double avg) {
        this.avg = avg;
        return this;
    }

    public double getMax() {
        return max;
    }

    public Response setMax(double max) {
        this.max = max;
        return this;
    }

    public double getMin() {
        return min;
    }

    public Response setMin(double min) {
        this.min = min;
        return this;
    }

    public long getCount() {
        return count;
    }

    public Response setCount(long count) {
        this.count = count;
        return this;
    }
}