package model;

import java.time.LocalDate;

public class Order {
    private Long id;
    private Long bookId;
    private Long customerId;
    private Long employeeId;
    private int quantity;
    private Double totalPrice;
    private LocalDate orderDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return String.format("Order ID: %d | Book ID: %d | Customer ID: %d | Employee ID: %d | Quantity: %d | Total Price: %.2f | Order Date: %s",
                id, bookId, customerId, employeeId, quantity, totalPrice, orderDate.toString());
    }
}
