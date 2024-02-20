package repository.book;

import model.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    //doar functiile care manipuleaza baza de date direct
    List<Book> findAll();
    Optional <Book> findById(Long id);
    boolean save(Book book);
    void removeAll();
    boolean updateStock(Long bookId, int newStock);
    boolean updateBook(Long bookId, String title, String author, LocalDate date, double price, int stock);

    boolean deleteBook(Long id);
}
