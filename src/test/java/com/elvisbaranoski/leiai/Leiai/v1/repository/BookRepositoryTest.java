package com.elvisbaranoski.leiai.Leiai.v1.repository;

import com.elvisbaranoski.leiai.Leiai.v1.dto.BookDTO;
import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;
    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com isbn informado")
    public  void returnTrueWhenIsbnExists(){
        //CENÁRIO
        String isbn = "123456";
        Book book = Book.builder().title("Drilax").author("Elvis Baranoski").isbn(isbn).build();
        entityManager.persist(book);

        //EXECUÇÃO
        Boolean exists = repository.existsByIsbn(isbn);

        //VERIFICAÇÃO
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com isbn informado")
    public  void returnFalseWhenIsbnDoesntExists(){
        //CENÁRIO
        String isbn = "123456";
        //AQUI NÃO PERSISTIR A VARIAVEL ISBN DA ENTITY BOOK
       // Book book = Book.builder().title("Drilax").author("Elvis Baranoski").isbn(isbn).build();
       // entityManager.persist(book);

        //EXECUÇÃO
        Boolean exists = repository.existsByIsbn(isbn);

        //VERIFICAÇÃO
        assertThat(exists).isFalse();
    }
}
