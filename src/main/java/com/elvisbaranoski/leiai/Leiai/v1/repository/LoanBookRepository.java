package com.elvisbaranoski.leiai.Leiai.v1.repository;

import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import com.elvisbaranoski.leiai.Leiai.v1.entity.LoanBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoanBookRepository extends JpaRepository<LoanBook, Long> {


    @Query(value = "select case when" +
            "(count(l.id)>0)then true else false " +
            "end from LoanBook l where l.book = :book " +
            "and (l.returnedBook is null or l.returnedBook is not false)")
    boolean existsByBookAndNotReturnedBook(@Param("book") Book book);
}
