package controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    @RequestMapping(value = "/statistic", method = RequestMethod.GET)
    public Response getStatistics(){
        return new Response().setAvg(200).setCount(20).setMax(100).setMin(10);
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity handleTransactions(@RequestBody Request request){
        boolean val = false;
        return val == true ? new ResponseEntity(HttpStatus.CREATED): new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}