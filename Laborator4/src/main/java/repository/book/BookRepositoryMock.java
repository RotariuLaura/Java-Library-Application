package repository.book;

import model.Book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository{

    private final List <Book> books;

    public BookRepositoryMock(){
        books = new ArrayList<>();
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional <Book> findById(Long id) {
        return books.parallelStream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public void removeAll() {
        books.clear();
    }

    @Override
    public boolean updateStock(Long bookId, int newStock) {
        Optional <Book> book = findById(bookId);
        if (book.isPresent()) {
            book.get().setStock(newStock);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateBook(Long bookId, String title, String author, LocalDate date, double price, int stock) {
        Optional <Book> book = findById(bookId);
        if (book.isPresent()) {
            book.get().setTitle(title);
            book.get().setAuthor(author);
            book.get().setPrice(price);
            book.get().setStock(stock);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteBook(Long id) {
        Optional <Book> book = findById(id);
        if (book.isPresent()) {
            books.remove(book.get());
            return true;
        }
        return false;
    }
}
