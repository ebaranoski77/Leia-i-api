package com.elvisbaranoski.leiai.Leiai.v1.service;

import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import com.elvisbaranoski.leiai.Leiai.v1.exception.BusinessException;
import com.elvisbaranoski.leiai.Leiai.v1.repository.BookRepository;
import com.elvisbaranoski.leiai.Leiai.v1.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um LIVRO com sucesso!")
    public void saveBookTest(){

        //CENARIO
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(book))
                               .thenReturn(Book.builder()
                                       .id(1L)
                                       .title("Drilax")
                                       .author("Elvis Baranoski")
                                       .isbn("123456")
                                       .build());

        //EXECUÇÃO
        Book saveBook = service.save(book);

        //VERIFICAÇÃO
        assertThat(saveBook.getId()).isNotNull();
        assertThat(saveBook.getTitle()).isEqualTo("Drilax");
        assertThat(saveBook.getAuthor()).isEqualTo("Elvis Baranoski");
        assertThat(saveBook.getIsbn()).isEqualTo("123456");

    }

    private Book createValidBook() {
        return Book.builder().title("Drilax").author("Elvis Baranoski").isbn("123456").build();
    }

    @Test
    @DisplayName("Deve lançar um erro de negocio ao tentar salvar um LIVRO com isbn duplicado!")
    public void shouldNotSaveABookWithDuplicateISBN(){

        //CENARIO
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //EXECUÇÃO
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        //VERIFICAÇÃO
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Esta ISBN já foi usada na criação de outro LIVRO!");
       Mockito.verify(repository,Mockito.never()).save(book);

    }


}
