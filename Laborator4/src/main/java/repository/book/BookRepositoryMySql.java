package repository.book;

import model.Book;
import model.builder.BookBuilder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySql implements BookRepository {

    private final Connection connection;
    public BookRepositoryMySql(Connection connection) {

        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";
        List<Book> books = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                books.add(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Optional<Book> book = Optional.of(getBookFromResultSet(resultSet));
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    // ALWAYS use PreparedStatement when USER INPUT DATA is present
    // DON'T CONCATENATE Strings
    @Override
    public boolean save(Book book) {
        String sql = "INSERT INTO book VALUES(null, ?, ?, ?, ?, ?);";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
            preparedStatement.setDouble(4, book.getPrice());
            preparedStatement.setInt(5, book.getStock());
            int rowsInserted = preparedStatement.executeUpdate();
            return (rowsInserted != 1) ? false : true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        String sql = "TRUNCATE TABLE book;";
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean deleteBook(Long bookId) {
        String sql = "DELETE FROM book WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, bookId);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean updateStock(Long bookId, int newStock) {
        String sql = "UPDATE book SET stock = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, newStock);
            preparedStatement.setLong(2, bookId);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateBook(Long bookId, String title, String author, LocalDate date, double price, int stock){
        String sql = "UPDATE book SET title = ?, author = ?, publishedDate = ?, price = ?, stock = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setDate(3, Date.valueOf(date));
            preparedStatement.setDouble(4, price);
            preparedStatement.setInt(5, stock);
            preparedStatement.setLong(6, bookId);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException{
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setAuthor(resultSet.getString("author"))
                .setTitle(resultSet.getString("title"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .setPrice(resultSet.getDouble("price"))
                .setStock(resultSet.getInt("stock"))
                .build();
    }
}
