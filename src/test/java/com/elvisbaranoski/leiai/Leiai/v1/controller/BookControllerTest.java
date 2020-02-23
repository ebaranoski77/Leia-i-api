package com.elvisbaranoski.leiai.Leiai.v1.controller;

import com.elvisbaranoski.leiai.Leiai.v1.dto.BookDTO;
import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import com.elvisbaranoski.leiai.Leiai.v1.exception.BusinessException;
import com.elvisbaranoski.leiai.Leiai.v1.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Optional.of;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {BookController.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest {
    //DEFININDO ROTA
    static String BOOK_API="/api/v1/books";


    @Autowired
    MockMvc mvc;
    @MockBean
    BookService service;

    @Test
    @DisplayName("Deve criar um LIVRO com sucesso!")
    public void createdBookTest()throws Exception{

        BookDTO dto = createNewBook();//CONVERTE DTO

        //MOKANDO SERVICE
        Book saveBook = Book.builder().id(1L).title("Drilax").author("Elvis Baranoski").isbn("123456").build();
        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(saveBook);

        String json = new ObjectMapper().writeValueAsString(dto);//TRANSFORMANDO QUALQUER OBJETO EM JSON
        //MONTANDO UMA REQUISIÇÃO
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders //REQUEST
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);//PASSANDO O CORPO DA REQUISIÇÃO

        //REQUISIÇÃO

        mvc
                .perform(request)
                .andExpect(status().isCreated())  //PASSANDO OS MATCHERS VERIFICADORES
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()))
        ;

    }

    private BookDTO createNewBook() {
        return BookDTO.builder().title("Drilax").author("Elvis Baranoski").isbn("123456").build();
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficiente para a criação do LIVRO.")
    public void createdInvalidBookTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new BookDTO());//TRANSFORMANDO QUALQUER OBJETO EM JSON
        //MONTANDO UMA REQUISIÇÃO
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders //REQUEST
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);//PASSANDO O CORPO DA REQUISIÇÃO

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())  //PASSANDO OS MATCHERS VERIFICADORES
                .andExpect(jsonPath("errors", hasSize(3)))
        ;
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando houver duplicidade de isbn para a criação do LIVRO.")
    public void createdBookWithDuplicateIsbnTest() throws Exception {
        BookDTO dto = createNewBook();//CONVERTE DTO
        String json = new ObjectMapper().writeValueAsString(dto);//TRANSFORMANDO QUALQUER OBJETO EM JSON
        String menssagenError = "Esta ISBN já foi usada na criação de outro LIVRO!";
        BDDMockito.given(service.save(Mockito.any(Book.class))).willThrow(new BusinessException(menssagenError));
        //MONTANDO UMA REQUISIÇÃO
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders //REQUEST
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);//PASSANDO O CORPO DA REQUISIÇÃO

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())  //PASSANDO OS MATCHERS VERIFICADORES
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(menssagenError))
        ;
    }

    @Test
    @DisplayName("Deve obter informações do LIVRO.")
    public void getBookDetailsTest()throws Exception {

        //CENÁRIO (given)
        Long id = 1L;

        Book book= Book.builder()
                .id(id)
                .title(createNewBook().getTitle())
                .author(createNewBook().getAuthor())
                .isbn(createNewBook().getIsbn())
                .build();

        BDDMockito.given(service.getById(id)).willReturn(of(book));

        //EXECUÇÃO(when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(createNewBook().getTitle()))
                .andExpect(jsonPath("author").value(createNewBook().getAuthor()))
                .andExpect(jsonPath("isbn").value(createNewBook().getIsbn()))
        ;


    }

    @Test
    @DisplayName("Deve retornar resource not found quando o LIVRO procurado não exister.")
    public void bookNotFoundTest()throws Exception {

        //CENÁRIO (given)

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

        //EXECUÇÃO(when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound())
        ;

    }

    @Test
    @DisplayName("Deve DELETAR um LIVRO .")
    public void deleteBookTest()throws Exception {

        //CENÁRIO (given)
        //MOCANDO O ID

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.of(Book.builder().id(1L).build()));

        //EXECUÇÃO(when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    @DisplayName("Deve retornar not found quando não encontrar LIVRO para DELETAR.")
    public void deleteInexistentBookTest()throws Exception {

        //CENÁRIO (given)

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

        //EXECUÇÃO(when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("Deve atualizar um LIVRO .")
    public void updateBookTest()throws Exception {
        //CENÁRIO (given)
        Long id = 1L;
        String json = new ObjectMapper().writeValueAsString(createNewBook());//TRANSFORMANDO QUALQUER OBJETO EM JSON
        Book updatingBook = Book.builder()
                .id(1L)
                .title("Drilax2")
                .author("Ebarann")
                .isbn("1234567")
                .build();
        BDDMockito.given(service.getById(id)).willReturn(Optional.of(updatingBook));

        Book updateBook = Book.builder()
                .id(id)
                .title("Drilax")
                .author("Elvis Baranoski")
                .isbn("1234567")
                .build();

        BDDMockito.given(service.update(updatingBook)).willReturn(updateBook);

        //EXECUÇÃO(when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + 1))
                .content(json)//PASSANDO O CORPO DA REQUISIÇÃO
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                ;

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(createNewBook().getTitle()))
                .andExpect(jsonPath("author").value(createNewBook().getAuthor()))
                .andExpect(jsonPath("isbn").value("1234567"))
        ;

    }

    @Test
    @DisplayName("Deve retornar 404 ao atualizar um LIVRO inexistent .")
    public void updateInexistentBookTest()throws Exception {

        //CENÁRIO (given)

        String json = new ObjectMapper().writeValueAsString(createNewBook());//TRANSFORMANDO QUALQUER OBJETO EM JSON

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

        //EXECUÇÃO(when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + 1))
                .content(json)//PASSANDO O CORPO DA REQUISIÇÃO
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("Deve filtrar LIVROS.")
    public void findBookTest() throws Exception {

        //CENÁRIO (given)
        Long id = 1L;

        Book book = Book.builder()
                .id(id)
                .title(createNewBook().getTitle())
                .author(createNewBook().getAuthor())
                .isbn(createNewBook().getIsbn())
                .build();


        BDDMockito.given(service.find(Mockito.any(Book.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Book>(Arrays.asList(book), PageRequest.of(0, 100), 1));
        //EXECUÇÃO(when)

        String queryString = String.format("?title=%s&author=%s&page=0&size=100",
                book.getTitle(), book.getAuthor());

        //REQUISIÇÃO
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0))
        ;
    }

}


