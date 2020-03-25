package com.elvisbaranoski.leiai.Leiai.v1.service;

import com.elvisbaranoski.leiai.Leiai.v1.entity.LoanBook;

import java.util.Optional;

public interface LoanBookService {
    LoanBook save(LoanBook loanBook);

    Optional<LoanBook> getById(Long id);

    LoanBook update(LoanBook loanBook);
}
