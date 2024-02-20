package repository.book;

import model.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator{
    private Cache<Book> cache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache <Book> cache){
        super(bookRepository);
        this.cache = cache;
    }
    @Override
    public List<Book> findAll(){
        if(cache.hasResult()){
            return cache.load();
        }
        List <Book> books = decoratedRepository.findAll();
        cache.save(books);
        return books;
    }
    @Override
    public Optional<Book> findById(Long id){

        return decoratedRepository.findById(id);
    }
    public boolean save(Book book){
        cache.invalidateCache();
        return decoratedRepository.save(book);
    }
    public void removeAll(){
        cache.invalidateCache();
        decoratedRepository.removeAll();
    }

    @Override
    public boolean updateStock(Long bookId, int newStock) {
        cache.invalidateCache();
        return decoratedRepository.updateStock(bookId, newStock);
    }

    @Override
    public boolean updateBook(Long bookId, String title, String author, LocalDate date, double price, int stock) {
        cache.invalidateCache();
        return decoratedRepository.updateBook(bookId, title, author, date, price, stock);
    }

    @Override
    public boolean deleteBook(Long id) {
        cache.invalidateCache();
        return decoratedRepository.deleteBook(id);
    }
}
