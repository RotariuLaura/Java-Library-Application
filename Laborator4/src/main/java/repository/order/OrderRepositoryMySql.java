package repository.order;

import model.Order;
import model.builder.OrderBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryMySql implements OrderRepository {

    private final Connection connection;

    public OrderRepositoryMySql(Connection connection) {

        this.connection = connection;
    }

    @Override
    public boolean insertOrder(Long bookId, Long customerId, int quantity, double totalPrice, Date date) {
        String sql = "INSERT INTO order_table (book_id, customer_id, quantity, total_price, order_date) VALUES(?, ?, ?, ?, ?);";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, bookId);
            preparedStatement.setLong(2, customerId);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setDouble(4, totalPrice);
            preparedStatement.setDate(5, date);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted == 1) {
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateOrder(Long id, Long employeeId){
        String sql = "UPDATE order_table SET employee_id = ? WHERE id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, employeeId);
            preparedStatement.setLong(2, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Order> findAll(){
        String sql = "SELECT * FROM order_table;";
        List<Order> orders = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    @Override
    public List<Order> findAllBooksSoldByAnEmployee(Long id){
        String sql = "SELECT * FROM order_table where employee_id = ?;";
        List<Order> orders = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException{
        return new OrderBuilder()
                .setId(resultSet.getLong("id"))
                .setBookId(resultSet.getLong("book_id"))
                .setCustomerId(resultSet.getLong("customer_id"))
                .setEmployeeId(resultSet.getLong("employee_id"))
                .setQuantity(resultSet.getInt("quantity"))
                .setTotalPrice(resultSet.getDouble("total_price"))
                .setOrderDate(new java.sql.Date(resultSet.getDate("order_date").getTime()).toLocalDate())
                .build();
    }
}
