package com.elvisbaranoski.leiai.Leiai.v1.service;


import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import com.elvisbaranoski.leiai.Leiai.v1.entity.LoanBook;
import com.elvisbaranoski.leiai.Leiai.v1.exception.BusinessException;
import com.elvisbaranoski.leiai.Leiai.v1.repository.LoanBookRepository;
import com.elvisbaranoski.leiai.Leiai.v1.service.impl.LoanBookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanBookServiceTest {

    LoanBookService service;

    @MockBean
    LoanBookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new LoanBookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um emprestimo!")
    public void saveLoanBookTest() {

        //CENARIO
        Book book = Book.builder().id(1L).build();
        String customer = "Jacqueline";
        LoanBook savingloanBook = LoanBook.builder()
                .book(book)
                .customer(customer)
                .loanBookDate(LocalDate.now())
                .build();
        LoanBook savedLoanBook = LoanBook.builder()
                .id(1L)
                .book(book)
                .customer(customer)
                .loanBookDate(LocalDate.now())
                .build();
        when(repository.existsByBookAndNotReturnedBook(book)).thenReturn(false);
        when(repository.save(savingloanBook)).thenReturn(savedLoanBook);


        //EXECUÇÃO
        LoanBook loanBook = service.save(savingloanBook);

        //VERIFICAÇÃO
        assertThat(loanBook.getId()).isEqualTo(savedLoanBook.getId());
        assertThat(loanBook.getBook().getId()).isEqualTo(savedLoanBook.getId());
        assertThat(loanBook.getCustomer()).isEqualTo(savedLoanBook.getCustomer());
        assertThat(loanBook.getLoanBookDate()).isEqualTo(savedLoanBook.getLoanBookDate());

    }

    @Test
    @DisplayName("Deve lançar um erro de negócio ao fazer um empréstimo de um livro já emprestado!")
    public void LoanedBookSaveTest() {

        //CENARIO
        Book book = Book.builder().id(1L).build();
        String customer = "Jacqueline";
        LoanBook savingloanBook = LoanBook.builder()
                .book(book)
                .customer(customer)
                .loanBookDate(LocalDate.now())
                .build();
        when(repository.existsByBookAndNotReturnedBook(book)).thenReturn(true);
        //EXECUÇÃO
        Throwable exception = catchThrowable(() -> service.save(savingloanBook));

        //VERIFICAÇÃO
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Book allReady loaned")
        ;
        verify(repository, never()).save(savingloanBook);
    }

    @Test
    @DisplayName("Deve obter as informações de um empréstimo de um livro pela ID!")
    public void getLoanedBookDetailsTest() {
        //CENARIO
        Long id = 1L;
        LoanBook loanBook = createLoanBook();
        loanBook.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(loanBook));

        //EXECUÇÃO
        Optional<LoanBook> result = service.getById(id);

        //VERIFICAÇÃO
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getCustomer()).isEqualTo(loanBook.getCustomer());
        assertThat(result.get().getBook()).isEqualTo(loanBook.getBook());
        assertThat(result.get().getLoanBookDate()).isEqualTo(loanBook.getLoanBookDate());

        verify(repository).findById(id);

    }

    public LoanBook createLoanBook() {
        Book book = Book.builder().id(1L).build();
        String customer = "Jacqueline";
        return LoanBook.builder()
                .book(book)
                .customer(customer)
                .loanBookDate(LocalDate.now())
                .build();
    }


}
