package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.Borrower;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author ishani.s
 */
@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    public LibraryService(BookRepository bookRepository, BorrowerRepository borrowerRepository) {
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    public Borrower registerBorrower(Borrower borrower) {
        return borrowerRepository.save(borrower);
    }

    public Book registerBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public List<Book> registerBooks(List<Book> books) {
        return bookRepository.saveAll(books);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    @Transactional
    public Book borrowBook(Long bookId, Long borrowerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (book.isBorrowed()) {
            throw new RuntimeException("Book already borrowed");
        }
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));
        book.borrow(borrower);
        return bookRepository.save(book);
    }

    @Transactional
    public Book returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if(!book.isBorrowed()){
          throw new RuntimeException("Book has not borrowed");
        }
        book.returned();
        return bookRepository.save(book);
    }
}
