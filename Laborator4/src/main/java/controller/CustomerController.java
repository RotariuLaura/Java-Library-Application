package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Spinner;
import model.Book;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.order.OrderService;
import service.order.OrderServiceImpl;
import view.CustomerView;

import java.util.List;

public class CustomerController {

    private final CustomerView customerView;
    private final BookService bookService;
    private final Long customerId;
    private final OrderService orderService;

    public CustomerController(CustomerView customerView, BookServiceImpl bookService, OrderServiceImpl orderService, Long customerId) {
        this.customerView = customerView;
        this.bookService = bookService;
        this.customerId = customerId;
        this.orderService = orderService;

        this.customerView.addViewAllBooksButtonListener(new CustomerController.ViewAllBooksListener());
        this.customerView.addBuyABookButtonListener(new CustomerController.BuyABookListener());
        this.customerView.addConfirmPurchaseButtonListener(new ConfirmPurchaseListener());
    }

    private class ViewAllBooksListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            refreshBooksTable();
        }
    }
    private void refreshBooksTable() {
            List<Book> books = bookService.findAll();
            ObservableList<Book> observableBooks = FXCollections.observableArrayList(books);
            customerView.displayBooks(observableBooks);
    }

    private class BuyABookListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            customerView.openBuyABookWindow();
        }
    }
    private class ConfirmPurchaseListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = customerView.getSelectedBook();
            if (selectedBook.getStock() == 0){
                customerView.displayError("No stock for this book!");
                return;
            }
            Spinner<Integer> quantitySpinner = customerView.getQuantitySpinner();
            int quantity = quantitySpinner.getValue();
            double totalPrice = selectedBook.getPrice() * quantity;
            java.util.Date currentDate = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
            try{
                if((orderService.insertOrder(selectedBook.getId(), customerId, quantity, totalPrice, sqlDate)))
                {
                    int newStock = selectedBook.getStock() - quantity;
                    selectedBook.setStock(newStock);
                    bookService.updateStock(selectedBook.getId(), newStock);
                    refreshBooksTable();
                    customerView.displayMessage("Your order has been placed successfully!");
                } else {
                    customerView.displayError("The order was not processed!");
                }
            } catch (Exception e){
                e.printStackTrace();
                customerView.displayError("The order was not processed!");
            }
        }
    }

}