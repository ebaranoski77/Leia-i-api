package com.elvisbaranoski.leiai.Leiai.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long id;
    @NotEmpty
    @Length(min = 3, max = 50, message = "O titulo deve ter no mínimo 3 caracteres e no máximo 50!")
    private String title;
    @NotEmpty
    @Length(min = 3, max = 50, message = "O endereço deve ter no mínimo 3 caracteres e no máximo 50!")
    private String author;
    @NotEmpty
    private String isbn;
}
