package repositories;

import database.DatabaseConnectionFactory;
import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySql;
import repository.book.Cache;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryMySqlTest {
    private static BookRepository bookRepository;

    @BeforeAll
    public static void setUp() {
        bookRepository = new BookRepositoryCacheDecorator(
                new BookRepositoryMySql(
                        DatabaseConnectionFactory.getConnectionWrapper(true).getConnection()
                ),
                new Cache<>()
        );
    }

//    @BeforeEach
//    public void cleanUp(){
//        bookRepository.removeAll();
//    }

    @Test
    public void testFindAll(){
        List <Book> books = bookRepository.findAll();
        assertNotNull(books);
    }

    @Test
    public void testFindById(){
        Optional <Book> book = bookRepository.findById(1L);
        assertTrue(book.isPresent());
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
        bookRepository.removeAll();
        List <Book> books = bookRepository.findAll();
        assertTrue(books.isEmpty());
    }
}
