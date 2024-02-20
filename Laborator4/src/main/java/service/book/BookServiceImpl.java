package service.book;

import model.Book;
import repository.book.BookRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id: %d not found".formatted(id)));
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public int getAgeOfBook(Long id) {
        Book book = this.findById(id);
        LocalDate now = LocalDate.now();
        return (int) ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }

    @Override
    public boolean updateStock(Long id, int stock) {
        return bookRepository.updateStock(id, stock);
    }

    @Override
    public boolean updateBook(Long bookId, String title, String author, LocalDate date, double price, int stock) {
        return bookRepository.updateBook(bookId, title, author, date, price, stock);
    }

    @Override
    public boolean deleteBook(Long id) {
        return bookRepository.deleteBook(id);
    }
}
