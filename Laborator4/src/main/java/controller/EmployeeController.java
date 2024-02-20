package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Book;
import model.Order;
import model.builder.BookBuilder;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.order.OrderService;
import service.order.OrderServiceImpl;
import view.EmployeeView;

import java.time.LocalDate;
import java.util.List;

public class EmployeeController {

    private final EmployeeView employeeView;
    private final BookService bookService;
    private final OrderService orderService;
    private final Long employeeId;

    public EmployeeController(EmployeeView employeeView, BookServiceImpl bookService, OrderServiceImpl orderService, Long employeeId) {
        this.employeeView = employeeView;
        this.bookService = bookService;
        this.orderService = orderService;
        this.employeeId = employeeId;

        this.employeeView.addViewAllBooksButtonListener(new EmployeeController.ViewAllBooksListener());
        this.employeeView.addIntroduceABookButtonListener(new EmployeeController.IntroduceABookListener());
        this.employeeView.addUpdateBookButtonListener(new EmployeeController.UpdateABookListener());
        this.employeeView.addDeleteBookButtonListener(new EmployeeController.DeleteABookListener());
        this.employeeView.addViewOrdersButtonListener(new EmployeeController.ViewAllOrdersListener());
        this.employeeView.addConfirmOrderButtonListener(new EmployeeController.ConfirmOrderListener());
        this.employeeView.addConfirmUpdateButtonListener(new EmployeeController.ConfirmUpdateListener());
        this.employeeView.addConfirmIntroduceButtonListener(new EmployeeController.ConfirmIntroduceListener());
        this.employeeView.addPDFReportButtonListener(new EmployeeController.PDFReportListener());
    }

    private class ViewAllBooksListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            employeeView.initializeBooksTable(employeeView.getGridPane());
            refreshBooksTable();
        }
    }
    private void refreshBooksTable() {
        List<Book> books = bookService.findAll();
        ObservableList<Book> observableBooks = FXCollections.observableArrayList(books);
        employeeView.displayBooks(observableBooks);
    }

    private class IntroduceABookListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            employeeView.openIntroduceABookWindow();
        }
    }

    private class UpdateABookListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            employeeView.openUpdateABookWindow();
        }
    }

    private class DeleteABookListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = employeeView.getSelectedBook();
            if (selectedBook != null) {
                try{
                    if(bookService.deleteBook(selectedBook.getId()))
                    {
                        refreshBooksTable();
                        employeeView.displayMessage("The book has been deleted successfully!");
                    } else {
                        employeeView.displayError("The book could not be deleted!");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    employeeView.displayError("The book could not be deleted!");
                }
            } else {
                employeeView.displayError("You must select a book to delete first!");
            }
        }
    }

    private class ViewAllOrdersListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            employeeView.initializeOrdersTable(employeeView.getGridPane());
            refreshOrdersTable();
        }
    }
    private void refreshOrdersTable() {
        List<Order> orders = orderService.findAll();
        ObservableList<Order> observableOrders= FXCollections.observableArrayList(orders);
        employeeView.displayOrders(observableOrders);
    }

    private class ConfirmOrderListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            try {
                Order selectedOrder = employeeView.getOrdersTable().getSelectionModel().getSelectedItem();
                if (selectedOrder != null) {
                    if (selectedOrder.getEmployeeId() == 0) {
                        if (orderService.updateOrder(selectedOrder.getId(), employeeId)) {
                            refreshOrdersTable();
                            employeeView.displayMessage("This order has been confirmed successfully!");
                        } else {
                            employeeView.displayError("The order could not be confirmed!");
                        }
                    } else {
                        employeeView.displayError("This order has already been confirmed!");
                    }
                } else {
                    employeeView.displayError("You must select an order to complete first!");
                }
            } catch (Exception e) {
                employeeView.displayError("You must view the orders first!");
            }
        }
    }

    private class PDFReportListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            PDFReportGenerator pdfReportGenerator = new PDFReportGenerator();
            try{
                List<Order> orders = orderService.findAllBooksSoldByAnEmployee(employeeId);
                pdfReportGenerator.generateEmployeeBookReport(orders, bookService);
                employeeView.displayMessage("PDF with books sold generated!");
            }
            catch (Exception e){
                employeeView.displayError("PDF could not be generated!");
            }
        }
    }

    private class ConfirmUpdateListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = employeeView.getSelectedBook();
            if (selectedBook != null) {
                String title = employeeView.getTitleField().getText();
                String author = employeeView.getAuthorField().getText();
                LocalDate date = LocalDate.parse(employeeView.getPublishedDateField().getText());
                double price = Double.parseDouble(employeeView.getPriceField().getText());
                int stock = Integer.parseInt(employeeView.getStockField().getText());
                try{
                    if(bookService.updateBook(selectedBook.getId(), title, author, date, price, stock)){
                        refreshBooksTable();
                        employeeView.displayMessage("The book has been updated successfully!");
                    } else {
                        employeeView.displayError("The book was not updated!");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    employeeView.displayError("The order was not updated!");
                }
            } else {
                employeeView.displayError("You must select a book to update first!");
            }
        }
    }

    private class ConfirmIntroduceListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try{
            String title = employeeView.getTitleField().getText();
            String author = employeeView.getAuthorField().getText();
            LocalDate date = LocalDate.parse(employeeView.getPublishedDateField().getText());
            double price = Double.parseDouble(employeeView.getPriceField().getText());
            int stock = Integer.parseInt(employeeView.getStockField().getText());
            Book book = new BookBuilder()
                    .setTitle(title)
                    .setAuthor(author)
                    .setPublishedDate(date)
                    .setPrice(price)
                    .setStock(stock)
                    .build();
            if(bookService.save(book)){
                refreshBooksTable();
                employeeView.displayMessage("The book has been added successfully!");
            } else {
                employeeView.displayError("The book was not added!");
            }
            } catch (Exception e){
                e.printStackTrace();
                employeeView.displayError("You must fill the information!");
            }
        }
    }

}