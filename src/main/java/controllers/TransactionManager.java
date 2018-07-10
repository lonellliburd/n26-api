package controllers;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private static double sum, avg, max, min;
    private static long count;


    public static boolean handleTransaction(Request request){
        List<String> errors = new ArrayList<>();
        validate(request, errors);

        return CollectionUtils.isEmpty(errors);
    }

    private static void validate(Request request, List<String> errors){
        if (moreThanSixtySeconds(request.getTimestamp())){
            errors.add("More than 60 seconds");
        }
    }

    private static boolean moreThanSixtySeconds(long time){
        return ((System.currentTimeMillis() % 1000) - time ) / 1000 > 60;

    }
}