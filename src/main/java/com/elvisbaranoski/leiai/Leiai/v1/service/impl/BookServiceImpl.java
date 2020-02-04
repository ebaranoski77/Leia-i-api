package com.elvisbaranoski.leiai.Leiai.v1.service.impl;

import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import com.elvisbaranoski.leiai.Leiai.v1.exception.BusinessException;
import com.elvisbaranoski.leiai.Leiai.v1.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements com.elvisbaranoski.leiai.Leiai.v1.service.BookService {

    private BookRepository repository;

   public BookServiceImpl(BookRepository repository) {

        this.repository = repository;
   }

    @Override
    public Book save(Book book) {
       if(repository.existsByIsbn(book.getIsbn())){
           throw  new BusinessException("Esta ISBN já foi usada na criação de outro LIVRO!");
       }
        return repository.save(book);

    }
}

