package repositories;

import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMock;
import repository.book.Cache;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookRepositoryMockTest {
    private static BookRepository bookRepository;
    @BeforeAll
    public static void setUp(){
        bookRepository = new BookRepositoryCacheDecorator(
                new BookRepositoryMock(),
                new Cache<>()
        );
    }

    @Test
    public void testFindAll(){
        assertEquals(0, bookRepository.findAll().size());
        Book book = new BookBuilder()
                .setTitle("Pinocchio")
                .setAuthor("Carlo Colodi")
                .setPublishedDate(LocalDate.of(2010, 6,2))
                .build();
        Book book2 = new BookBuilder()
                .setTitle("The Christmas Holiday")
                .setAuthor("Phillipa Ashley")
                .setPublishedDate(LocalDate.of(2010, 6,2))
                .build();
        bookRepository.save(book);
        bookRepository.save(book2);
        List<Book> books = bookRepository.findAll();
        assertEquals(books.get(0), book);
        assertEquals(books.get(1), book2);
        assertEquals(2, books.size());
    }

    @Test
    public void testFindById(){
        bookRepository.removeAll();
        final Optional<Book> books = bookRepository.findById(1L);
        assertTrue(books.isEmpty());
    }

    @Test
    public void testSave(){
        Book book = new BookBuilder()
                .setTitle("Pinocchio")
                .setAuthor("Carlo Colodi")
                .setPublishedDate(LocalDate.of(2010, 6,2))
                .build();
        assertTrue(bookRepository.save(book));
    }

    @Test
    public void testRemoveAll(){
        Book book = new BookBuilder()
                .setTitle("Pinocchio")
                .setAuthor("Carlo Colodi")
                .setPublishedDate(LocalDate.of(2010, 6,2))
                .build();
        bookRepository.save(book);
        bookRepository.removeAll();
        List <Book> books = bookRepository.findAll();
        assertTrue(books.isEmpty());
    }

}
