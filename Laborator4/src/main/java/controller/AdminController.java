package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Order;
import model.User;
import model.validator.Notification;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.order.OrderService;
import service.order.OrderServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import view.AdminView;

import java.util.List;

public class AdminController {

    private final AdminView adminView;
    private final AuthenticationService authenticationService;
    private final BookService bookService;
    private final OrderService orderService;

    public AdminController(AdminView adminView, AuthenticationServiceImpl authenticationService, BookServiceImpl bookService, OrderServiceImpl orderService) {
        this.adminView = adminView;
        this.authenticationService = authenticationService;
        this.bookService = bookService;
        this.orderService = orderService;

        this.adminView.addViewAllEmployeesButtonListener(new AdminController.ViewAllEmployeesListener());
        this.adminView.addIntroduceAEmployeeButtonListener(new AdminController.IntroduceAnEmployeeListener());
        this.adminView.addUpdateEmployeeButtonListener(new AdminController.UpdateAnEmployeeListener());
        this.adminView.addDeleteEmployeeButtonListener(new AdminController.DeleteAnEmployeeListener());
        this.adminView.addViewOrdersButtonListener(new AdminController.ViewAllOrdersListener());
        this.adminView.addConfirmUpdateButtonListener(new AdminController.ConfirmUpdateListener());
        this.adminView.addConfirmIntroduceButtonListener(new AdminController.ConfirmIntroduceListener());
        this.adminView.addPDFReportButtonListener(new AdminController.PDFReportListener());
    }

    private class ViewAllEmployeesListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            adminView.initializeBooksTable(adminView.getGridPane());
            refreshEmployeesTable();
        }
    }
    private void refreshEmployeesTable() {
        List<User> employees = ((AuthenticationServiceImpl) authenticationService).getUserRepository().findAllEmployees();
        ObservableList<User> observableEmployees = FXCollections.observableArrayList(employees);
        adminView.displayEmployees(observableEmployees);
    }

    private class IntroduceAnEmployeeListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            adminView.openIntroduceAnEmployeeWindow();
        }
    }

    private class UpdateAnEmployeeListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            adminView.openUpdateAnEmployeeWindow();
        }
    }

    private class DeleteAnEmployeeListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            User selectedEmployee = adminView.getSelectedEmployee();
            if (selectedEmployee != null) {
                try{
                    if(((AuthenticationServiceImpl) authenticationService).getUserRepository().deleteEmployee(selectedEmployee.getId())){
                        refreshEmployeesTable();
                        adminView.displayMessage("The employee has been deleted successfully!");
                    } else {
                        adminView.displayError("The employee could not be deleted!");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    adminView.displayError("The employee could not be deleted!");
                }
            } else {
                adminView.displayError("You must select an employee to delete first!");
            }
        }
    }

    private class ViewAllOrdersListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            adminView.initializeOrdersTable(adminView.getGridPane());
            refreshOrdersTable();
        }
    }
    private void refreshOrdersTable() {
        List<Order> orders = orderService.findAll();
        ObservableList<Order> observableOrders= FXCollections.observableArrayList(orders);
        adminView.displayOrders(observableOrders);
    }


    private class PDFReportListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            PDFReportGenerator pdfReportGenerator = new PDFReportGenerator();
            try{
                List<Order> orders = orderService.findAll();
                pdfReportGenerator.generateEmployeesActivityReport(orders, bookService, ((AuthenticationServiceImpl)authenticationService).getUserRepository());
                adminView.displayMessage("PDF with employees activity generated!");
            }
            catch (Exception e){
                adminView.displayError("PDF could not be generated!");
            }
        }
    }

    private class ConfirmUpdateListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            User selectedEmployee = adminView.getSelectedEmployee();
            if (selectedEmployee != null) {
                String username = adminView.getUserField().getText();
                String password = adminView.getPasswordField().getText();
                if(!username.endsWith("@employee.com")){
                    adminView.displayError("The username must end with @employee.com!");
                }
                try{
                    if(!authenticationService.updateEmployeeDetails(selectedEmployee.getId(), username, password).hasErrors()){
                        refreshEmployeesTable();
                        adminView.setActionTargetText("");
                        adminView.displayMessage("The employee has been updated successfully!");
                    } else {
                        Notification<Boolean> updateNotification = authenticationService.updateEmployeeDetails(selectedEmployee.getId(), username, password);
                        adminView.setActionTargetText(updateNotification.getFormattedErrors());
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    adminView.displayError("The employee was not updated!");
                    adminView.setActionTargetText("");
                }
            } else {
                adminView.displayError("You must select an employee to update first!");
            }
          }
        }

        private class ConfirmIntroduceListener implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String username = adminView.getUserField().getText();
                    if(!username.endsWith("@employee.com")){
                        adminView.displayError("The username must end with @employee.com!");
                        return;
                    }
                    String password = adminView.getPasswordField().getText();
                    if (!authenticationService.register(username, password).hasErrors()) {
                        refreshEmployeesTable();
                        adminView.setActionTargetText("");
                        adminView.displayMessage("The employee has been added successfully!");
                    } else {
                        Notification<Boolean> registerNotification = authenticationService.register(username, password);
                        adminView.setActionTargetText(registerNotification.getFormattedErrors());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    adminView.setActionTargetText("");
                    adminView.displayError("The employee was not added!");
                }
            }
        }

    }