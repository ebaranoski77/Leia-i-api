package com.elvisbaranoski.leiai.Leiai.v1.repository;

import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long > {

    Boolean existsByIsbn(String isbn);
}
