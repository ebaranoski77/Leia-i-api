package com.elvisbaranoski.leiai.Leiai.v1.repository;

import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import com.elvisbaranoski.leiai.Leiai.v1.entity.LoanBook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static com.elvisbaranoski.leiai.Leiai.v1.repository.BookRepositoryTest.createNewBook;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanBookRepositoryTest {

    @Autowired
    LoanBookRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve verifivar se existe empréstimo para o livro não retornado")
    public void existsByBookAndNotReturnedBookTest() {

        //CENÁRIO
        Book book = createNewBook("123456");
        entityManager.persist(book);
        LoanBook loanbook = LoanBook.builder().book(book).customer("Jacqueline").loanBookDate(LocalDate.now()).build();
        entityManager.persist(loanbook);

        //EXECUÇÃO
        boolean exists = repository.existsByBookAndNotReturnedBook(book);

        //VERIFICAÇÃO
        assertThat(exists).isTrue();
    }

}

