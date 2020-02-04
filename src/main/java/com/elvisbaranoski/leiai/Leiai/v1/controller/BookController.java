package com.elvisbaranoski.leiai.Leiai.v1.controller;

import com.elvisbaranoski.leiai.Leiai.v1.dto.BookDTO;
import com.elvisbaranoski.leiai.Leiai.v1.entity.Book;
import com.elvisbaranoski.leiai.Leiai.v1.exception.ApiErrors;
import com.elvisbaranoski.leiai.Leiai.v1.exception.BusinessException;
import com.elvisbaranoski.leiai.Leiai.v1.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private BookService service;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper mapper) {
        this.service = service;
        this.modelMapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO created(@RequestBody @Valid BookDTO dto){

        Book entity = modelMapper.map(dto,Book.class);

        entity= service.save(entity);

        return modelMapper.map(entity,BookDTO.class);
        }

        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(MethodArgumentNotValidException.class)
         public  ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        return  new ApiErrors(bindingResult);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public  ApiErrors handleBusinessExceptions(BusinessException ex){
        return  new ApiErrors(ex);
    }
}
