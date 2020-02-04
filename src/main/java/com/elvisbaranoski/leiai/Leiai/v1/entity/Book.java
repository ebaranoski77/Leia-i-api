package com.elvisbaranoski.leiai.Leiai.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
public class Book implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1189842909592101805L;
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column//(nullable = false)
    private String title;
    @Column//(nullable = false)
    private String author;
    @Column//(nullable = false)
    private String isbn;
}
