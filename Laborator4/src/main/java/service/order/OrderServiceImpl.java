package service.order;

import model.Order;
import repository.order.OrderRepository;

import java.sql.Date;
import java.util.List;

public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }
    public boolean insertOrder(Long bookId, Long customerId, int quantity, double totalPrice, Date date){
        return orderRepository.insertOrder(bookId, customerId, quantity, totalPrice, date);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findAllBooksSoldByAnEmployee(Long id) {
        return orderRepository.findAllBooksSoldByAnEmployee(id);
    }

    public boolean updateOrder(Long id, Long employeeId){
        return orderRepository.updateOrder(id, employeeId);
    }
}

