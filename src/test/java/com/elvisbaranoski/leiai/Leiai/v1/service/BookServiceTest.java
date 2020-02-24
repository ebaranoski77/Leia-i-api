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
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um LIVRO com sucesso!")
    public void saveBookTest() {

        //CENARIO
        Book book = createValidBook();
        when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        when(repository.save(book))
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
    public void shouldNotSaveABookWithDuplicateISBN() {

        //CENARIO
        Book book = createValidBook();
        when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //EXECUÇÃO
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        //VERIFICAÇÃO
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Esta ISBN já foi usada na criação de outro LIVRO!");
        Mockito.verify(repository, Mockito.never()).save(book);

    }

    @Test
    @DisplayName("Deve  obter um id.")
    public void getById() {

        //CENÁRIO (given)
        Long id = 1L;

        Book book = createValidBook();
        book.setId(id);
        BDDMockito.given(repository.findById(id)).willReturn(Optional.of(book));

        //EXECUÇÃO
        Optional<Book> foundBook = service.getById(id);

        //VERIFICAÇÃO
        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());


    }

    @Test
    @DisplayName("Deve  retornar vazio quando obter um livro que não existe na base de dados.")
    public void bookNotFoundByIdTest() {

        //CENÁRIO (given)
        Long id = 1L;

        Book book = Book.builder().id(1L).build();
        BDDMockito.given(repository.findById(id)).willReturn(Optional.of(book));

        //EXECUÇÃO
        service.delete(book);

        //VERIFICAÇÃO
        Mockito.verify(repository, Mockito.times(1)).delete(book);


    }

    @Test
    @DisplayName("Deve  deletar um livro que existe na base de dados.")
    public void deleteBookTest() {

        //CENÁRIO (given)
        Book book = Book.builder().id(1L).build();

        //EXECUÇÃO
        assertDoesNotThrow(() -> service.delete(book));

        //VERIFICAÇÃO
        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve  ocorrer um erro ao deletar um livro que não existe na base de dados.")
    public void deleteInvalidBookTest() {

        //CENÁRIO (given)
        Book book = new Book();

        //EXECUÇÃO
        assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        //VERIFICAÇÃO
        Mockito.verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve  atualizar um livro que existe na base de dados.")
    public void updateBookTest() {

        //CENÁRIO (given)
        Long id = 1L;

        //ATUALIZA LIVRO
        Book updatingBook = Book.builder().id(id).build();

        //SIMULAÇÃO
        Book updateBook = createValidBook();
        updateBook.setId(id);

        when(repository.save(updatingBook)).thenReturn(updateBook);

        //EXECUÇÃO
        Book book = service.update(updatingBook);

        //VERIFICAÇÃO
        assertThat(book.getId()).isEqualTo(updateBook.getId());
        assertThat(book.getTitle()).isEqualTo(updateBook.getTitle());
        assertThat(book.getAuthor()).isEqualTo(updateBook.getAuthor());
        assertThat(book.getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Deve  ocorrer um erro ao atualizar um livro que não existe na base de dados.")
    public void updateInvalidBookTest() {

        //CENÁRIO (given)
        Book book = new Book();

        //EXECUÇÃO
        assertThrows(IllegalArgumentException.class, () -> service.update(book));

        //VERIFICAÇÃO
        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve  filtrar livros pelas propriedades.")
    public void findBookTest() {

        //CENÁRIO (given)
        Book book = createValidBook();
        List<Book> list = Arrays.asList(book);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Book> page = new PageImpl<Book>(list, pageRequest, 1);

        when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        //EXECUÇÃO
        Page<Book> result = service.find(book, pageRequest);

        //VERIFICAÇÃO
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve  obter um livro pelo isbn.")
    public void getBookByIsbn() {

        //CENÁRIO (given)
        String isbn = "1234567";
        when(repository.findByIsbn(isbn)).thenReturn(Optional.of(Book.builder().id(1L).isbn(isbn).build()));

        //EXECUÇÃO
        Optional<Book> book = service.getBookByIsbn(isbn);

        //VERIFICAÇÃO
        assertThat(book.isPresent()).isTrue();
        assertThat(book.get().getId()).isEqualTo(1L);
        assertThat(book.get().getIsbn()).isEqualTo(isbn);

        verify(repository, times(1)).findByIsbn(isbn);
    }


}
