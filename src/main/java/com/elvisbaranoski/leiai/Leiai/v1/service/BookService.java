package com.elvisbaranoski.leiai.Leiai.v1.service;

import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;

import java.util.Optional;

public interface BookService {

    Book save(Book book);

   Optional<Book> getById(Long id);

    void delete(Book book);

    Book update(Book book);
}