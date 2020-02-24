package com.elvisbaranoski.leiai.Leiai.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanBookDTO {


    @NotEmpty
    private String isbn;

    @NotEmpty
    private String customer;
}
