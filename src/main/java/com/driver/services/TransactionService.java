package com.driver.services;

import com.driver.models.*;
import com.driver.repositories.BookRepository;
import com.driver.repositories.CardRepository;
import com.driver.repositories.TransactionRepository;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

    @Autowired
    private BookRepository bookRepository5;

    @Autowired
    private CardRepository cardRepository5;

    @Autowired
    private TransactionRepository transactionRepository5;

    @Value("${books.max_allowed}")
    public int max_allowed_books;

    @Value("${books.max_allowed_days}")
    public int getMax_allowed_days;

    @Value("${books.fine.per_day}")
    public int fine_per_day;

    public String issueBook(int cardId, int bookId) throws Exception {
        Book book=bookRepository5.findById(bookId).get();
        Card card=cardRepository5.findById(cardId).get();

        Transaction transaction=new Transaction();
        transaction.setBook(book);
        transaction.setCard(card);
        transaction.setIssueOperation(true);

        if(book==null || !book.isAvailable()){
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository5.save(transaction);
            throw new Exception("Book is either unavailable or not present");
        }
        if(card==null || card.getCardStatus().equals(CardStatus.DEACTIVATED)){
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository5.save(transaction);
            throw new Exception("Card is invalid");
        }
        if(card.getBooks().size()>=max_allowed_books){
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository5.save(transaction);
            throw new Exception("Book limit has reached for this card");
        }
        book.setCard(card);
        book.setAvailable(false);
        List<Book> books=card.getBooks();
        books.add(book);
        card.setBooks(books);
        bookRepository5.updateBook(book);

        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository5.save(transaction);

       return transaction.getTransactionId(); //return transactionId instead
    }

    public Transaction returnBook(int cardId, int bookId) throws Exception{

        List<Transaction> transactions = transactionRepository5.find(cardId, bookId, TransactionStatus.SUCCESSFUL, true);
        Transaction transaction = transactions.get(transactions.size() - 1);
        Date issueDate= transaction.getTransactionDate();
        long timeIssueTime=Math.abs(System.currentTimeMillis() - issueDate.getTime());

        long noOfDays = TimeUnit.DAYS.convert(timeIssueTime, TimeUnit.MILLISECONDS);

        int fine=0;
        if(noOfDays > getMax_allowed_days)
            fine=(int)(noOfDays-getMax_allowed_days)*fine_per_day;

        Book book=transaction.getBook();
        book.setAvailable(true);
        book.setCard(null);

        bookRepository5.updateBook(book);


        Transaction returnBookTransaction  = new Transaction();
        returnBookTransaction.setFineAmount(fine);
        returnBookTransaction.setTransactionDate(new Date());
        returnBookTransaction.setBook(transaction.getBook());
        returnBookTransaction.setCard(transaction.getCard());
        returnBookTransaction.setIssueOperation(false);
        returnBookTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository5.save(returnBookTransaction);

        return returnBookTransaction; //return the transaction after updating all details
    }

}
