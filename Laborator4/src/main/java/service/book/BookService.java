package service.book;

import model.Book;

import java.time.LocalDate;
import java.util.List;

public interface BookService {
    List<Book> findAll();
    Book findById(Long id);
    boolean save(Book book);
    int getAgeOfBook(Long id); //e business logic
    boolean updateStock(Long id, int stock);
    boolean updateBook(Long bookId, String title, String author, LocalDate date, double price, int stock);
    boolean deleteBook(Long id);
}
