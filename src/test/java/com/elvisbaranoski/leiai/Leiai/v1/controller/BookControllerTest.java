package com.elvisbaranoski.leiai.Leiai.v1.controller;

import com.elvisbaranoski.leiai.Leiai.v1.dto.BookDTO;
import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import com.elvisbaranoski.leiai.Leiai.v1.exception.BusinessException;
import com.elvisbaranoski.leiai.Leiai.v1.service.BookService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
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

    }


