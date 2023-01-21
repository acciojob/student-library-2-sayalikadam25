package com.driver.services;

import com.driver.models.Author;
import com.driver.models.Book;
import com.driver.repositories.AuthorRepository;
import com.driver.repositories.BookRepository;
import org.apache.commons.lang3.StringUtils;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository2;
    @Autowired
    private AuthorRepository authorRepository;

    public void createBook(Book book){
        if(!bookRepository2.existsById(book.getId())){
            int authorId=book.getAuthor().getId();
            Author author=authorRepository.findById(authorId).get();
            author.getBooksWritten().add(book);
            book.setAuthor(author);
            bookRepository2.save(book);
            authorRepository.save(author);
        }
    }

    public List<Book> getBooks(String genre, boolean available, String author){
        if(genre!=null && author!=null){
            return bookRepository2.findBooksByGenreAuthor(genre,author,available);
        }
        else if(genre!=null){
            return bookRepository2.findBooksByGenre(genre,available);
        }
        else if(author!=null){
            return bookRepository2.findBooksByAuthor(author,available);
        }
        else{
            return bookRepository2.findByAvailability(available);
        }
    }

}
