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
        //check whether bookId and cardId already exist
        //conditions required for successful transaction of issue book:
        //1. book is present and available
        // If it fails: throw new Exception("Book is either unavailable or not present");
        //2. card is present and activated
        // If it fails: throw new Exception("Card is invalid");
        //3. number of books issued against the card is strictly less than max_allowed_books
        // If it fails: throw new Exception("Book limit has reached for this card");
        //If the transaction is successful, save the transaction to the list of transactions and return the id

        //Note that the error message should match exactly in all cases
        if(!cardRepository5.existsById(cardId))
            throw new Exception("Card is invalid");
        Card card=cardRepository5.findById(cardId).get();
        if(card.getCardStatus()== CardStatus.DEACTIVATED)
            throw new Exception("Card is invalid");
        if(!bookRepository5.existsById(bookId))
            throw new Exception("Book is either unavailable or not present");
        Book book=bookRepository5.findById(bookId).get();
        if(!book.isAvailable())
            throw new Exception("Book is either unavailable or not present");
        List<Book> books=card.getBooks();
        if(books.size()>=max_allowed_books)
            throw new Exception("Book limit has reached for this card");
        books.add(book);
        card.setBooks(books);
        book.setAvailable(false);
        cardRepository5.save(card);
        Transaction transaction=new Transaction();
        transaction.setBook(book);
        transaction.setCard(card);
        transaction.setTransactionDate(new Date());
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository5.save(transaction);

       return transaction.getTransactionId(); //return transactionId instead
    }

    public Transaction returnBook(int cardId, int bookId) throws Exception{

        //for the given transaction calculate the fine amount considering the book has been returned exactly when this function is called
        //make the book available for other users
        //make a new transaction for return book which contains the fine amount as well
        List<Transaction> transactions = transactionRepository5.find(cardId, bookId, TransactionStatus.SUCCESSFUL, true);
        Transaction transaction = transactions.get(transactions.size() - 1);
        Date transDate= transaction.getTransactionDate();
        LocalDate transactionDate=new java.sql.Date(transDate.getTime()).toLocalDate();
        LocalDate returnDate= LocalDate.now();
        Period period = Period.between(returnDate,transactionDate);
        int days= period.getDays();

        int fine=0;
        if(days>getMax_allowed_days)
            fine=(days-getMax_allowed_days)*fine_per_day;
        Transaction returnBookTransaction  = new Transaction();
        returnBookTransaction.setFineAmount(fine);
        returnBookTransaction.setTransactionDate(new Date());
        Book book=bookRepository5.findById(bookId).get();
        Card card=cardRepository5.findById(cardId).get();
        List<Book> books=card.getBooks();
        books.remove(book);
        card.setBooks(books);
        book.setAvailable(true);
        return returnBookTransaction; //return the transaction after updating all details
    }
}
