package com.elvisbaranoski.leiai.Leiai.v1.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
public class LoanBook implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1189842909592101805L;
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String isbn;
    @Column(length = 100)
    private String customer;
    @JoinColumn(name = "id_book")
    @ManyToOne
    private Book book;
    @Column
    private LocalDate loanBookDate;
    @Column
    private Boolean returnedBook;


}
