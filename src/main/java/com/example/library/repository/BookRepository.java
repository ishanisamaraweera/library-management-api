package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ishani.s
 */
public interface BookRepository extends JpaRepository<Book, Long> {
}
