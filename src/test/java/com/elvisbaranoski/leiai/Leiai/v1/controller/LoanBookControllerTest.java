package com.elvisbaranoski.leiai.Leiai.v1.controller;

import com.elvisbaranoski.leiai.Leiai.v1.dto.LoanBookDTO;
import com.elvisbaranoski.leiai.Leiai.v1.dto.ReturnedLoanBookDTO;
import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import com.elvisbaranoski.leiai.Leiai.v1.entity.LoanBook;
import com.elvisbaranoski.leiai.Leiai.v1.exception.BusinessException;
import com.elvisbaranoski.leiai.Leiai.v1.service.BookService;
import com.elvisbaranoski.leiai.Leiai.v1.service.LoanBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {LoanBookController.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoanBookControllerTest {

    //DEFININDO ROTA
    static String LOADBOOK_API = "/api/v1/loadBooks";

    @Autowired
    MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private LoanBookService loanBookservice;

    @Test
    @DisplayName("Deve realizar um emprestimo de LIVRO com sucesso!")
    public void createdLoanBookTest() throws Exception {
        //CENÁRIO
        LoanBookDTO dto = LoanBookDTO.builder()//CONVERTE DTO
                .isbn("123456")
                .customer("Jacqueline")
                .build();


        Book book = Book.builder()
                .id(1L)
                .isbn("123456")
                .build();
        BDDMockito.given(bookService.getBookByIsbn("123456")).willReturn(Optional.of(book));

        LoanBook loanBook = LoanBook.builder()
                .id(1L)
                .customer("Jacqueline")
                .book(book)
                .loanBookDate(LocalDate.now())
                .build();
        BDDMockito.given(loanBookservice.save(Mockito.any(LoanBook.class))).willReturn(loanBook);

        String json = new ObjectMapper().writeValueAsString(dto);//TRANSFORMANDO QUALQUER OBJETO EM JSON

        //MONTANDO UMA REQUISIÇÃO
        MockHttpServletRequestBuilder request = //REQUEST
                post(LOADBOOK_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json);//PASSANDO O CORPO DA REQUISIÇÃO

        //REQUISIÇÃO

        mvc
                .perform(request)
                .andExpect(status().isCreated())  //PASSANDO OS MATCHERS VERIFICADORES
                .andExpect(content().string("1"))
        ;
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar fazer rmprestimo de LIVRO inexistente.")
    public void invalideIsbnCreatedLoanBookTest() throws Exception {

        //CENÁRIO
        LoanBookDTO dto = LoanBookDTO.builder()//CONVERTE DTO
                .isbn("123456")
                .customer("Jacqueline")
                .build();

        String json = new ObjectMapper().writeValueAsString(dto);//TRANSFORMANDO QUALQUER OBJETO EM JSON
        BDDMockito.given(bookService.getBookByIsbn("123456")).willReturn(Optional.empty());

        //MONTANDO UMA REQUISIÇÃO
        MockHttpServletRequestBuilder request = //REQUEST
                post(LOADBOOK_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json);//PASSANDO O CORPO DA REQUISIÇÃO

        //REQUISIÇÃO

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())  //PASSANDO OS MATCHERS VERIFICADORES
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Book not found for passed isbn"))
        ;
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar fazer rmprestimo de LIVRO emprestado.")
    public void loanedBookErrorTest() throws Exception {

        //CENÁRIO
        LoanBookDTO dto = LoanBookDTO.builder()//CONVERTE DTO
                .isbn("123456")
                .customer("Jacqueline")
                .build();

        String json = new ObjectMapper().writeValueAsString(dto);//TRANSFORMANDO QUALQUER OBJETO EM JSON
        Book book = Book.builder()
                .id(1L)
                .isbn("123456")
                .build();
        BDDMockito.given(bookService.getBookByIsbn("123456")).willReturn(Optional.of(book));

        BDDMockito.given(loanBookservice.save(Mockito.any(LoanBook.class)))
                .willThrow(new BusinessException("Book allReady loaned"));

        //MONTANDO UMA REQUISIÇÃO
        MockHttpServletRequestBuilder request = //REQUEST
                post(LOADBOOK_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json);//PASSANDO O CORPO DA REQUISIÇÃO

        //REQUISIÇÃO

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())  //PASSANDO OS MATCHERS VERIFICADORES
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Book allReady loaned"))
        ;
    }

    @Test
    @DisplayName("Deve retornar um LIVRO.")
    public void returnedBookTest() throws Exception {

        //CENÁRIO
        ReturnedLoanBookDTO dto = ReturnedLoanBookDTO
                .builder()//CONVERTE DTO
                .returned(true)
                .build();
        LoanBook loanBook = LoanBook.builder()
                .id(1L)
                .build();
        BDDMockito.given(loanBookservice.getById(Mockito.anyLong()))
                .willReturn(Optional.of(loanBook));

        //MONTANDO UMA REQUISIÇÃO

        String json = new ObjectMapper().writeValueAsString(dto);//TRANSFORMANDO QUALQUER OBJETO EM JSON
        //REQUISIÇÃO

        mvc
                .perform(patch(LOADBOOK_API.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)//PASSANDO O CORPO DA REQUISIÇÃO
                ).andExpect(status().isOk())  //PASSANDO OS MATCHERS VERIFICADORES
        ;

        Mockito.verify(loanBookservice, Mockito.times(1)).update(loanBook);
    }

    @Test
    @DisplayName("Deve retornar 404 caso devolver um LIVRO inexistente.")
    public void returnedInexistentBookTest() throws Exception {

        //CENÁRIO
        ReturnedLoanBookDTO dto = ReturnedLoanBookDTO
                .builder()//CONVERTE DTO
                .returned(true)
                .build();

        //MONTANDO UMA REQUISIÇÃO
        String json = new ObjectMapper().writeValueAsString(dto);//TRANSFORMANDO QUALQUER OBJETO EM JSON

        BDDMockito.given(loanBookservice.getById(Mockito.anyLong()))
                .willReturn(Optional.empty());


        //REQUISIÇÃO

        mvc
                .perform(patch(LOADBOOK_API.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)//PASSANDO O CORPO DA REQUISIÇÃO
                ).andExpect(status().isNotFound())  //PASSANDO OS MATCHERS VERIFICADORES
        ;

    }

}
