package model.builder;

import model.Order;

import java.time.LocalDate;

public class OrderBuilder {
    private Order order;
    public OrderBuilder(){
        this.order = new Order();
    }

    public OrderBuilder setId(Long id) {
        order.setId(id);
        return this;
    }

    public OrderBuilder setBookId(Long bookId) {
        order.setBookId(bookId);
        return this;
    }

    public OrderBuilder setCustomerId(Long customerId) {
        order.setCustomerId(customerId);
        return this;
    }

    public OrderBuilder setEmployeeId(Long employeeId) {
        order.setEmployeeId(employeeId);
        return this;
    }

    public OrderBuilder setQuantity(int quantity) {
        order.setQuantity(quantity);
        return this;
    }

    public OrderBuilder setTotalPrice(Double totalPrice) {
        order.setTotalPrice(totalPrice);
        return this;
    }

    public OrderBuilder setOrderDate(LocalDate orderDate) {
        order.setOrderDate(orderDate);
        return this;
    }

    public Order build() {
        return order;
    }
}
