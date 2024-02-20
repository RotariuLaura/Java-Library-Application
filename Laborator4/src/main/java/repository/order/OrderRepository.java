package repository.order;

import model.Order;

import java.sql.Date;
import java.util.List;

public interface OrderRepository {
    boolean insertOrder(Long bookId, Long customerId, int quantity, double totalPrice, Date date);
    List<Order> findAll();
    List<Order> findAllBooksSoldByAnEmployee(Long id);
    public boolean updateOrder(Long id, Long employeeId);

}
