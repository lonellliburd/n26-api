package controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    @RequestMapping(value = "/statistic", method = RequestMethod.GET)
    public Response getStatistics(){
        return TransactionManager.getStatistics();
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity handleTransactions(@RequestBody Request request){
        return TransactionManager.handleTransaction(request) ?
                new ResponseEntity(HttpStatus.CREATED): new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}