package com.elvisbaranoski.leiai.Leiai.v1.controller;

import com.elvisbaranoski.leiai.Leiai.v1.dto.LoanBookDTO;
import com.elvisbaranoski.leiai.Leiai.v1.dto.ReturnedLoanBookDTO;
import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import com.elvisbaranoski.leiai.Leiai.v1.entity.LoanBook;
import com.elvisbaranoski.leiai.Leiai.v1.service.BookService;
import com.elvisbaranoski.leiai.Leiai.v1.service.LoanBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/loadBooks")
@RequiredArgsConstructor
@Api("LoanBook API")
public class LoanBookController {

    private final LoanBookService service;
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(" creates a loanBooks")
    public long created(@RequestBody LoanBookDTO dto) {
        Book book = bookService
                .getBookByIsbn(dto.getIsbn())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));

        LoanBook entity = LoanBook.builder()
                .book(book)
                .customer(dto.getCustomer())
                .loanBookDate(LocalDate.now())
                .build();
        entity = service.save(entity);
        return entity.getId();
    }


    @PatchMapping("{id}")
    public void returnLoanBook(@PathVariable Long id,
                               @RequestBody ReturnedLoanBookDTO dto) {

        LoanBook loanBook = service.getById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found for passed isbn"));
        loanBook.setReturnedBook(dto.getReturned());

        service.update(loanBook);
    }

}
