package com.elvisbaranoski.leiai.Leiai.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnedLoanBookDTO {

  // @NotEmpty
  private Boolean returned;

}
