package com.example.library.repository;

import com.example.library.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ishani.s
 */
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
}
