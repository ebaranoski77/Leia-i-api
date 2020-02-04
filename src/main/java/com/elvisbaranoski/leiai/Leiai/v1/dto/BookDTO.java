package com.elvisbaranoski.leiai.Leiai.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long id;
    @NotEmpty
    //@NotNull
    @Length(min=3,max=50, message="O titulo deve ter no mínimo 3 caracteres e no máximo 50!")
    private String title;
    @NotEmpty
    //@NotNull
    @Length(min=3,max=50, message="O endereço deve ter no mínimo 3 caracteres e no máximo 50!")
    private String author;
    @NotEmpty
    //@NotNull
    private String isbn;
}
