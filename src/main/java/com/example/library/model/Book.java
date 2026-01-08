package com.example.library.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 *
 * @author ishani.s
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String isbn;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private boolean borrowed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    private Borrower borrower;

    // Convenience constructor (used in tests and object creation)
    public Book(String isbn, String title, String author, boolean borrowed) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.borrowed = borrowed;
    }

    public void borrow(Borrower borrower) {
        this.borrowed = true;
        this.borrower = borrower;
    }

    public void returned() {
        this.borrowed = false;
        this.borrower = null;
    }
}