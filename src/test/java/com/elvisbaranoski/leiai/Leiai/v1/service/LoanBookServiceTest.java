package com.elvisbaranoski.leiai.Leiai.v1.service;


import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import com.elvisbaranoski.leiai.Leiai.v1.entity.LoanBook;
import com.elvisbaranoski.leiai.Leiai.v1.repository.LoanBookRepository;
import com.elvisbaranoski.leiai.Leiai.v1.service.impl.LoanBookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
        when(repository.save(savingloanBook)).thenReturn(savedLoanBook);


        //EXECUÇÃO
        LoanBook loanBook = service.save(savingloanBook);

        //VERIFICAÇÃO
        assertThat(loanBook.getId()).isEqualTo(savedLoanBook.getId());
        assertThat(loanBook.getBook().getId()).isEqualTo(savedLoanBook.getId());
        assertThat(loanBook.getCustomer()).isEqualTo(savedLoanBook.getCustomer());
        assertThat(loanBook.getLoanBookDate()).isEqualTo(savedLoanBook.getLoanBookDate());

    }

}
