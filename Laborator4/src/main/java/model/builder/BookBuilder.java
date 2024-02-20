package model.builder;

import model.Book;

import java.time.LocalDate;

public class BookBuilder {
    private Book book;
    public BookBuilder(){
        book = new Book(); //am voie pt ca sunt in design pattern creational
    }
    public BookBuilder setId(Long id){
        book.setId(id);
        return this;
    }
    public BookBuilder setAuthor(String author){
        book.setAuthor(author);
        return this;
    }
    public BookBuilder setTitle(String title){
        book.setTitle(title);
        return this;
    }
    public BookBuilder setPublishedDate(LocalDate date){
        book.setPublishedDate(date);
        return this;
    }
    public BookBuilder setPrice(Double price){
        book.setPrice(price);
        return this;
    }
    public BookBuilder setStock(int stock){
        book.setStock(stock);
        return this;
    }
    public Book build(){
        return book;
    }

}
