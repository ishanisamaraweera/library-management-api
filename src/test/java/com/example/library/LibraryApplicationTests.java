package com.example.library;

import com.example.library.model.Book;
import com.example.library.model.Borrower;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowerRepository;
import com.example.library.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LibraryApplicationTests {

  @Autowired
  private LibraryService service;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private BorrowerRepository borrowerRepository;

  @Autowired
  private MockMvc mockMvc;

  private Book book1, book2;
  private Borrower borrower1;

  @BeforeEach
  void setup() {
    bookRepository.deleteAll();
    borrowerRepository.deleteAll();

    book1 = Book.builder()
            .isbn("9780134685991")
            .title("Design Patterns")
            .author("Joshua Bloch")
            .build();

    book2= Book.builder()
            .isbn("9780201633610")
            .title("Effective Java")
            .author("Erich Gamma")
            .build();

    borrower1 = Borrower.builder()
            .name("Ishani")
            .email("ishani@example.com")
            .build();

  }

  // ---------------- SERVICE TESTS ----------------

  @Test
  void registerBorrower_success() {
    Borrower saved = service.registerBorrower(borrower1);
    assertThat(saved.getId()).isNotNull();
  }

  @Test
  void registerBook_success() {
    Book saved = service.registerBook(book1);
    assertThat(saved.getId()).isNotNull();
  }

  @Test
  void bulkRegisterBooks_success() {
    var saved = service.registerBooks(Arrays.asList(book1, book2));
    assertThat(saved).hasSize(2);
  }

  @Test
  void bulkRegisterBooks_emptyList() {
    var saved = service.registerBooks(Collections.emptyList());
    assertThat(saved).isEmpty();
  }

  @Test
  void getAllBooks_success() {
    service.registerBooks(Arrays.asList(book1, book2));
    assertThat(service.getAllBooks()).hasSize(2);
  }

  @Test
  void borrowBook_success() {
    Book savedBook = service.registerBook(book1);
    Borrower savedBorrower = service.registerBorrower(borrower1);

    Book borrowed = service.borrowBook(savedBook.getId(), savedBorrower.getId());
    assertThat(borrowed.isBorrowed()).isTrue();
  }

  @Test
  void borrowBook_bookNotFound() {
    Borrower savedBorrower = service.registerBorrower(borrower1);
    assertThrows(RuntimeException.class,
            () -> service.borrowBook(999L, savedBorrower.getId()));
  }

  @Test
  void borrowBook_borrowerNotFound() {
    Book savedBook = service.registerBook(book1);
    assertThrows(RuntimeException.class,
            () -> service.borrowBook(savedBook.getId(), 999L));
  }

  @Test
  void borrowBook_alreadyBorrowed() {
    Book savedBook = service.registerBook(book1);
    Borrower savedBorrower = service.registerBorrower(borrower1);

    service.borrowBook(savedBook.getId(), savedBorrower.getId());
    assertThrows(RuntimeException.class,
            () -> service.borrowBook(savedBook.getId(), savedBorrower.getId()));
  }

  @Test
  void returnBook_success() {
    Book savedBook = service.registerBook(book1);
    Borrower savedBorrower = service.registerBorrower(borrower1);

    service.borrowBook(savedBook.getId(), savedBorrower.getId());
    Book returned = service.returnBook(savedBook.getId());

    assertThat(returned.isBorrowed()).isFalse();
  }

  @Test
  void returnBook_notBorrowed() {
    Book savedBook = service.registerBook(book1);
    assertThrows(RuntimeException.class,
            () -> service.returnBook(savedBook.getId()));
  }

  @Test
  void returnBook_notFound() {
    assertThrows(RuntimeException.class,
            () -> service.returnBook(999L));
  }

  // ---------------- CONTROLLER TESTS ----------------

  @Test
  void registerBorrower_endpoint() throws Exception {
    String json = """
                {"name":"Ishani","email":"ishani@example.com"}
                """;

    mockMvc.perform(post("/library-management/api/borrowers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());
  }

  @Test
  void registerBook_endpoint() throws Exception {
    String json = """
                {"isbn":"9780134685991","title":"Effective Java","author":"Joshua Bloch"}
                """;

    mockMvc.perform(post("/library-management/api/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());
  }

  @Test
  void registerBooks_bulk_endpoint() throws Exception {
    String json = """
                [
                  {"isbn":"9780134685991","title":"Effective Java","author":"Joshua Bloch"},
                  {"isbn":"9780201633610","title":"Design Patterns","author":"GoF"}
                ]
                """;

    mockMvc.perform(post("/library-management/api/books/bulk")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  void getAllBooks_endpoint() throws Exception {
    service.registerBooks(Arrays.asList(book1, book2));

    mockMvc.perform(get("/library-management/api/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  void validationError_missingFields() throws Exception {
    String json = """
                {"isbn":"","title":"","author":""}
                """;

    mockMvc.perform(post("/library-management/api/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isBadRequest());
  }

//  @Test
//  void testBorrowBookNotFound() throws Exception {
//    when(service.borrowBook(anyLong(), anyLong()))
//            .thenThrow(new RuntimeException("Book not found"));
//
//    mockMvc.perform(post("/library-management/api/borrow/99/borrower/1")
//                    .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isInternalServerError())
//            .andExpect(content().string("Book not found"));
//  }
//
//  @Test
//  void testReturnBookNotBorrowed() throws Exception {
//    when(service.returnBook(anyLong()))
//            .thenThrow(new RuntimeException("Book has not borrowed"));
//
//    mockMvc.perform(post("/library-management/api/return/1")
//                    .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isInternalServerError())
//            .andExpect(content().string("Book has not borrowed"));
//  }
}
