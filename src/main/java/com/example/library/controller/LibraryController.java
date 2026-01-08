package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.Borrower;
import com.example.library.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 *
 * @author ishani.s
 */
@RestController
@RequestMapping("library-management/api")
public class LibraryController {
    private final LibraryService service;

    public LibraryController(LibraryService service) {
        this.service = service;
    }

    @PostMapping("/borrowers")
    public Borrower registerBorrower(@Valid @RequestBody Borrower borrower) {
        return service.registerBorrower(borrower);
    }

    /**
     * Register a single book in the library.
     *
     * @param book the book to register
     * @return the saved book
     */
    @PostMapping("/books")
    public Book registerBook(@Valid @RequestBody Book book) {
        return service.registerBook(book);
    }

    /**
     * Register multiple books at once (bulk registration).
     * All books are saved transactionally.
     *
     * @param books the list of books to register
     * @return the list of saved books
     */
    @PostMapping("/books/bulk")
    public List<Book> registerBooks(@Valid @RequestBody List<Book> books) {
        return service.registerBooks(books);
    }

    @GetMapping("/books")
    public List<Book> listBooks() {
        return service.getAllBooks();
    }

    @PostMapping("/borrow/{bookId}/borrower/{borrowerId}")
    public Book borrowBook(@PathVariable Long bookId, @PathVariable Long borrowerId) {
        return service.borrowBook(bookId, borrowerId);
    }

    @PostMapping("/return/{bookId}")
    public Book returnBook(@PathVariable Long bookId) {
        return service.returnBook(bookId);
    }
}
