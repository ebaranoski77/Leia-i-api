package com.elvisbaranoski.leiai.Leiai.v1.service.impl;

import com.elvisbaranoski.leiai.Leiai.v1.entity.LoanBook;
import com.elvisbaranoski.leiai.Leiai.v1.repository.LoanBookRepository;
import com.elvisbaranoski.leiai.Leiai.v1.service.LoanBookService;

public class LoanBookServiceImpl implements LoanBookService {

    private LoanBookRepository repository;

    public LoanBookServiceImpl(LoanBookRepository repository) {
        this.repository = repository;
    }

    @Override
    public LoanBook save(LoanBook loanBook) {
        return repository.save(loanBook);
    }
}
