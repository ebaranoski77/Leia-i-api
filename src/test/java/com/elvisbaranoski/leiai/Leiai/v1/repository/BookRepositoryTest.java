package com.elvisbaranoski.leiai.Leiai.v1.repository;

import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

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
        Book book = createNewBook(isbn);
        entityManager.persist(book);

        //EXECUÇÃO
        Boolean exists = repository.existsByIsbn(isbn);

        //VERIFICAÇÃO
        assertThat(exists).isTrue();
    }

    private Book createNewBook(String isbn) {
        return Book.builder().title("Drilax").author("Elvis Baranoski").isbn(isbn).build();
    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com isbn informado")
    public void returnFalseWhenIsbnDoesntExists() {
        //CENÁRIO
        String isbn = "123456";

        //EXECUÇÃO
        Boolean exists = repository.existsByIsbn(isbn);

        //VERIFICAÇÃO
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro por ID")
    public void findByIdTest() {
        //CENÁRIO

        Book book = createNewBook("123456");
        entityManager.persist(book);

        //EXECUÇÃO
        Optional<Book> foundBook = repository.findById(book.getId());

        //VERIFICAÇÃO
        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro ")
    public void saveBookTest() {
        //CENÁRIO

        Book book = createNewBook("123456");


        //EXECUÇÃO
        Book savedBook = repository.save(book);

        //VERIFICAÇÃO
        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro ")
    public void deleteBookTest() {
        //CENÁRIO

        Book book = createNewBook("123456");
        entityManager.persist(book);
        Book foundBook = entityManager.find(Book.class, book.getId());

        //EXECUÇÃO
        repository.delete(foundBook);
        Book deletedBook = entityManager.find(Book.class, book.getId());

        //VERIFICAÇÃO
        assertThat(deletedBook).isNull();
    }
}
