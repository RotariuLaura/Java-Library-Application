package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Order;
import model.User;

import java.time.LocalDate;

public class AdminView {

    private Button viewEmployeesButton;
    private Button introduceEmployeeButton;
    private Button updateEmployeeButton;
    private Button deleteEmployeeButton;
    private Button viewOrdersButton;
    private Button PDFReportButton;
    private TableView<User> employeesTable;
    private TableView<Order> ordersTable;
    private Button confirmUpdateButton;
    private Button confirmIntroduceButton;
    private User selectedEmployee;
    private TextField userField;
    private TextField passwordField;
    private GridPane gridPane;
    private Text actiontarget;

    public AdminView(Stage primaryStage) {
        primaryStage.setTitle("Employees");

        gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 850, 480);
        primaryStage.setScene(scene);

        initializeSceneTitle(gridPane);

        initializeFields(gridPane);

        initializeBooksTable(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initializeSceneTitle(GridPane gridPane) {
        Text sceneTitle = new Text("Welcome to our Book Store");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);
    }

    private void initializeFields(GridPane gridPane) {

        HBox buttonsRow = new HBox(10);
        buttonsRow.setAlignment(Pos.BOTTOM_CENTER);

        introduceEmployeeButton = new Button("Add employee");
        viewEmployeesButton = new Button("View employees");
        updateEmployeeButton = new Button("Update employee");
        deleteEmployeeButton = new Button("Delete employee");
        viewOrdersButton = new Button("View orders");
        PDFReportButton = new Button("PDF report");

        buttonsRow.getChildren().addAll(introduceEmployeeButton, viewEmployeesButton, updateEmployeeButton,
                deleteEmployeeButton, viewOrdersButton, PDFReportButton);

        gridPane.add(buttonsRow, 0, 1, 2, 1);

        confirmUpdateButton = new Button("Confirm Update");
        confirmIntroduceButton = new Button("Confirm");
    }

    public void initializeBooksTable(GridPane gridPane) {
        employeesTable = new TableView<>();

        TableColumn<User, String> userColumn = new TableColumn<>("username");
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        userColumn.setMinWidth(200);

        TableColumn<User, String> passwordColumn = new TableColumn<>("password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setMinWidth(240);

        TableColumn<User, LocalDate> saltColumn = new TableColumn<>("salt");
        saltColumn.setCellValueFactory(new PropertyValueFactory<>("salt"));
        saltColumn.setMinWidth(190);

        employeesTable.getColumns().addAll(userColumn, passwordColumn, saltColumn);
        VBox employeesTableBox = new VBox(employeesTable);
        employeesTableBox.setAlignment(Pos.CENTER);
        employeesTableBox.setPadding(new Insets(20));

        gridPane.add(employeesTableBox, 0, 2, 2, 1);
    }

    public void initializeOrdersTable(GridPane gridPane) {
        ordersTable = new TableView<>();

        TableColumn<Order, Long> bookIdColumn = new TableColumn<>("Book nr");
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookIdColumn.setMinWidth(110);

        TableColumn<Order, Long> customerIdColumn = new TableColumn<>("Customer nr");
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerIdColumn.setMinWidth(110);

        TableColumn<Order, Long> employeeIdColumn = new TableColumn<>("Employee nr");
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employeeIdColumn.setMinWidth(110);

        TableColumn<Order, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setMinWidth(110);

        TableColumn<Order, Double> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        totalPriceColumn.setMinWidth(110);

        TableColumn<Order, LocalDate> orderDateColumn = new TableColumn<>("Order Date");
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        orderDateColumn.setMinWidth(100);

        ordersTable.getColumns().addAll(bookIdColumn, customerIdColumn, employeeIdColumn, quantityColumn, totalPriceColumn, orderDateColumn);
        VBox ordersTableBox = new VBox(ordersTable);
        ordersTableBox.setAlignment(Pos.CENTER);
        ordersTableBox.setPadding(new Insets(20));

        gridPane.add(ordersTableBox, 0, 2, 2, 1);
    }


    public void addViewAllEmployeesButtonListener(EventHandler<ActionEvent> viewEmployeesButtonListener) {
        viewEmployeesButton.setOnAction(viewEmployeesButtonListener);
    }

    public void addIntroduceAEmployeeButtonListener(EventHandler<ActionEvent> introduceEmployeeButtonListener) {
        introduceEmployeeButton.setOnAction(introduceEmployeeButtonListener);
    }

    public void addUpdateEmployeeButtonListener(EventHandler<ActionEvent> updateEmployeeButtonListener) {
        updateEmployeeButton.setOnAction(updateEmployeeButtonListener);
    }

    public void addDeleteEmployeeButtonListener(EventHandler<ActionEvent> deleteEmployeeButtonListener) {
        deleteEmployeeButton.setOnAction(deleteEmployeeButtonListener);
    }

    public void addViewOrdersButtonListener(EventHandler<ActionEvent> viewOrdersListener) {
        viewOrdersButton.setOnAction(viewOrdersListener);
    }

    public void addConfirmUpdateButtonListener(EventHandler<ActionEvent> confirmUpdateListener) {
        confirmUpdateButton.setOnAction(confirmUpdateListener);
    }

    public void addConfirmIntroduceButtonListener(EventHandler<ActionEvent> confirmIntroduceListener) {
        confirmIntroduceButton.setOnAction(confirmIntroduceListener);
    }

    public void addPDFReportButtonListener(EventHandler<ActionEvent> PDFReportListener) {
        PDFReportButton.setOnAction(PDFReportListener);
    }

    public void displayEmployees(ObservableList<User> employees) {
        employeesTable.setItems(employees);
        employeesTable.refresh();
    }

    public void displayOrders(ObservableList<Order> orders) {
        ordersTable.setItems(orders);
        ordersTable.refresh();
    }

    public void openUpdateAnEmployeeWindow() {
        selectedEmployee = employeesTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            Stage updateStage = new Stage();
            VBox updateLayout = new VBox(10);
            updateLayout.setAlignment(Pos.CENTER);
            Scene updateScene = new Scene(updateLayout, 300, 300);
            updateStage.setTitle("Update Employee");

            userField = new TextField(selectedEmployee.getUsername());
            passwordField = new PasswordField();
            updateLayout.getChildren().addAll(
                    new Label("Username:"), userField,
                    new Label("Password:"), passwordField,
                    confirmUpdateButton
            );
            actiontarget = new Text();
            actiontarget.setFill(Color.FIREBRICK);
            gridPane.add(actiontarget, 1, 6);

            updateStage.setScene(updateScene);
            updateStage.show();
        } else {
            displayError("You must select an employee to update first!");
        }
    }

    public void openIntroduceAnEmployeeWindow() {
        Stage updateStage = new Stage();
        VBox updateLayout = new VBox(10);
        updateLayout.setAlignment(Pos.CENTER);
        Scene updateScene = new Scene(updateLayout, 300, 300);
        updateStage.setTitle("Introduce employee");

        userField = new TextField();
        passwordField = new PasswordField();
        updateLayout.getChildren().addAll(
                new Label("Username:"), userField,
                new Label("Password:"), passwordField,
                confirmIntroduceButton
        );

        actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        gridPane.add(actiontarget, 1, 6);

        updateStage.setScene(updateScene);
        updateStage.show();
    }

    public void displayError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }

    public void displayMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public User getSelectedEmployee() {
        selectedEmployee = employeesTable.getSelectionModel().getSelectedItem();
        return selectedEmployee;
    }

    public TextField getUserField() {
        return userField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public TableView<Order> getOrdersTable() {
        return ordersTable;
    }

    public void setActionTargetText(String text){
        this.actiontarget.setText(text);
    }

}
