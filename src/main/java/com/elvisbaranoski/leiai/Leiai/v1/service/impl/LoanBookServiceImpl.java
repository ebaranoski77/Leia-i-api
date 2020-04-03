package com.elvisbaranoski.leiai.Leiai.v1.service.impl;

import com.elvisbaranoski.leiai.Leiai.v1.entity.LoanBook;
import com.elvisbaranoski.leiai.Leiai.v1.exception.BusinessException;
import com.elvisbaranoski.leiai.Leiai.v1.repository.LoanBookRepository;
import com.elvisbaranoski.leiai.Leiai.v1.service.LoanBookService;

import java.util.Optional;

public class LoanBookServiceImpl implements LoanBookService {

    private LoanBookRepository repository;

    public LoanBookServiceImpl(LoanBookRepository repository) {
        this.repository = repository;
    }

    @Override
    public LoanBook save(LoanBook loanBook) {
        if (repository.existsByBookAndNotReturnedBook(loanBook.getBook())) {
            throw new BusinessException("Book allReady loaned");
        }
        return repository.save(loanBook);
    }

    @Override
    public Optional<LoanBook> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public LoanBook update(LoanBook loanBook) {
        return repository.save(loanBook);
    }
}
