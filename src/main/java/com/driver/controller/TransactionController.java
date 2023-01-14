package com.driver.controller;

import com.driver.services.TransactionService;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(name="/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @PostMapping("/issueBook")
    public ResponseEntity<String> issueBook(@RequestParam("cardId") int cardId, @RequestParam("bookId") int bookId) throws Exception{
        transactionService.issueBook(cardId,bookId);
        return new ResponseEntity<>("transaction completed", HttpStatus.ACCEPTED);
    }

    @PostMapping("/returnBook")
    public ResponseEntity<String> returnBook(@RequestParam("cardId") int cardId, @RequestParam("bookId") int bookId) throws Exception{
        transactionService.returnBook(cardId,bookId);
        return new ResponseEntity<>("transaction completed", HttpStatus.ACCEPTED);
    }
}
