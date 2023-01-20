package com.driver.services;

import com.driver.models.Book;
import com.driver.repositories.BookRepository;
import org.apache.commons.lang3.StringUtils;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository2;

    public void createBook(Book book){
        bookRepository2.save(book);
    }

    public List<Book> getBooks(String genre, boolean available, String author){
        List<Book> books;

        if(!StringUtils.isBlank(genre) && !StringUtils.isBlank(author)){
            books=getBooksWhenNoInputNull(genre,available,author);
        }
        else if(StringUtils.isBlank(author) && !StringUtils.isBlank(genre)){
            books=getBooksWhenAuthorNull(genre,available);
        }
        else if(StringUtils.isBlank(genre) && !StringUtils.isBlank(author)){
            books=getBooksWhenGenreNull(author,available);
        }
        else{
            books=getBooksWhenNotAvailable(available);
        }
        return books;
    }
    public List<Book> getBooksWhenNoInputNull(String genre,boolean available,String author){
        return bookRepository2.findBooksByGenreAuthor(genre,author,available);
    }
    public List<Book> getBooksWhenAuthorNull(String genre,boolean available){
        return bookRepository2.findBooksByGenre(genre,available);
    }
    public List<Book> getBooksWhenGenreNull(String author,boolean available){
        return bookRepository2.findBooksByAuthor(author,available);
    }
    public List<Book> getBooksWhenNotAvailable(boolean available){
        return bookRepository2.findByAvailability(available);
    }

}
