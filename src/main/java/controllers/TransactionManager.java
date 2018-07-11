package controllers;

import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;

public class TransactionManager {
    private static PriorityBlockingQueue<Request> queue = new PriorityBlockingQueue<>(11, (r1, r2)
            ->  new Long(r1.getTimestamp()).compareTo(r2.getTimestamp()));

    public static boolean handleTransaction(Request request){
        queue.add(request);
        return lessThanSixtySeconds(request.getTimestamp());
    }

    public static Response getStatistics(){
        Response response = new Response();
        Iterator<Request> iterator = queue.iterator();
        while (iterator.hasNext()){
            Request r = iterator.next();
            if (lessThanSixtySeconds(r.getTimestamp())) {
                calculate(response, r.getAmount());
            }
        }
        return response;
    }

    private static boolean lessThanSixtySeconds(long time){
        return ((System.currentTimeMillis() - time ) / 1000) < 60;
    }

    private static void calculate(Response response, double amount){
        double sum;
        long count;

        if (amount >= response.getMax()){
            response.setMax(amount);
        }

        if (amount <= response.getMin()){
            response.setMin(amount);
        }

        sum = response.getSum() + amount;
        response.setSum(sum);

        count = response.getCount() + 1;
        response.setCount(count);

        response.setAvg(sum/count);
    }

    public static void reset(){
        queue = new PriorityBlockingQueue<>(11, (r1, r2)
                ->  new Long(r1.getTimestamp()).compareTo(r2.getTimestamp()));
    }
}