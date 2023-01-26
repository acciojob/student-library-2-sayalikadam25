package com.driver.services;

import com.driver.models.Author;
import com.driver.models.Book;
import com.driver.repositories.AuthorRepository;
import com.driver.repositories.BookRepository;
import lombok.NonNull;
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
//        if(!bookRepository2.existsById(book.getId())){
//            Author author=book.getAuthor();
//            if(author!=null){
//                if(!authorRepository.findAll().contains(author)) {
//                    authorRepository.save(author);
//                }
//                author.getBooksWritten().add(book);
//                book.setAuthor(author);
//            }
//            bookRepository2.save(book);
//            authorRepository.save(author);
//        }
        bookRepository2.save(book);
    }

    public List<Book> getBooks(String genre, boolean available, String author){
        List<Book> books=new ArrayList<>();
        if(!StringUtils.isBlank(genre) && !StringUtils.isBlank(author)){
            books=bookRepository2.findBooksByGenreAuthor(genre,author,available);
            if(books==null)
                return new ArrayList<>();
            return books;
        }
        else if(!StringUtils.isBlank(genre)){
            books=bookRepository2.findBooksByGenre(genre,available);
            if(books==null)
                return new ArrayList<>();
            return books;
        }
        else if(!StringUtils.isBlank(author)){
            books=bookRepository2.findBooksByAuthor(author,available);
            if(books==null)
                return new ArrayList<>();
            return books;
        }
        else{
            books=bookRepository2.findByAvailability(available);
            if(books==null)
                return new ArrayList<>();
            return books;
        }
    }

}
