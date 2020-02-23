package com.elvisbaranoski.leiai.Leiai.v1.service.impl;

import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import com.elvisbaranoski.leiai.Leiai.v1.exception.BusinessException;
import com.elvisbaranoski.leiai.Leiai.v1.repository.BookRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements com.elvisbaranoski.leiai.Leiai.v1.service.BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {

        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if (repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("Esta ISBN já foi usada na criação de outro LIVRO!");
        }
        return repository.save(book);

    }

    @Override
    public Optional<Book> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void delete(Book book) {
        if (book == null || book.getId() == null) {

            throw new IllegalArgumentException("Book id can not ID null.");
        }
        this.repository.delete(book);
    }

    @Override
    public Book update(Book book) {
        if (book == null || book.getId() == null) {

            throw new IllegalArgumentException("Book id can not ID null.");
        }
        return this.repository.save(book);
    }

    @Override
    public Page<Book> find(Book filter, Pageable pageRequest) {

        Example<Book> exemple = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(exemple, pageRequest);
    }

    @Override
    public Optional<Book> getBookByIdIsbn(String isbn) {
        return null;
    }


}

