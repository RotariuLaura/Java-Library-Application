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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Book;

import java.time.LocalDate;

public class CustomerView {

    private Button viewBooksButton;
    private Button buyBookButton;
    private TableView<Book> booksTable;
    private Button confirmPurchaseButton;
    private Book selectedBook;
    private Spinner<Integer> quantitySpinner;

    public CustomerView(Stage primaryStage) {
        primaryStage.setTitle("Customers");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        initializeSceneTitle(gridPane);

        initializeFields(gridPane);

        initializeBooksTable(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initializeSceneTitle(GridPane gridPane){
        Text sceneTitle = new Text("Welcome to our Book Store");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);
    }

    private void initializeFields(GridPane gridPane){

        viewBooksButton = new Button("View all books");
        HBox signInButtonHBox = new HBox(10);
        signInButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        signInButtonHBox.getChildren().add(viewBooksButton);
        gridPane.add(signInButtonHBox, 1, 1);

        buyBookButton = new Button("Buy a book");
        HBox logInButtonHBox = new HBox(10);
        logInButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        logInButtonHBox.getChildren().add(buyBookButton);
        gridPane.add(logInButtonHBox, 0, 1);

        confirmPurchaseButton = new Button("Confirm Purchase");
    }

    public void initializeBooksTable(GridPane gridPane){
        booksTable = new TableView<>();

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setMinWidth(120);

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setMinWidth(120);

        TableColumn<Book, LocalDate> publishedDateColumn = new TableColumn<>("Published Date");
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        publishedDateColumn.setMinWidth(120);

        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setMinWidth(120);

        TableColumn<Book, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockColumn.setMinWidth(120);

        booksTable.getColumns().addAll(titleColumn, authorColumn, publishedDateColumn, priceColumn, stockColumn);
        VBox booksTableBox = new VBox(booksTable);
        booksTableBox.setAlignment(Pos.CENTER);
        booksTableBox.setPadding(new Insets(20));

        gridPane.add(booksTableBox, 0, 2, 2, 1);
    }

    public void addViewAllBooksButtonListener(EventHandler<ActionEvent> viewBooksButtonListener) {
        viewBooksButton.setOnAction(viewBooksButtonListener);
    }

    public void addBuyABookButtonListener(EventHandler<ActionEvent> buyABookButtonListener) {
        buyBookButton.setOnAction(buyABookButtonListener);
    }
    public void addConfirmPurchaseButtonListener(EventHandler<ActionEvent> confirmPurchaseListener) {
        confirmPurchaseButton.setOnAction(confirmPurchaseListener);
    }

    public void displayBooks(ObservableList <Book> books){
        booksTable.setItems(books);
        booksTable.refresh();
    }

    public void openBuyABookWindow() {
        selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if(selectedBook != null) {
            if(selectedBook.getStock() == 0){
                displayError("No stock for this book!");
                return;
            }
            Stage buyStage = new Stage();
            VBox buyLayout = new VBox(10);
            buyLayout.setAlignment(Pos.CENTER);
            Scene buyScene = new Scene(buyLayout, 350, 150);
            buyStage.setTitle("Buy the book: " + selectedBook.getTitle());

            Label quantityLabel = new Label("Select Quantity:");
            quantitySpinner = new Spinner<>(1, selectedBook.getStock(), 1);

            buyLayout.getChildren().addAll(quantityLabel, quantitySpinner, confirmPurchaseButton);
            buyStage.setScene(buyScene);
            buyStage.show();
        } else {
            displayError("You must select a book to buy first!");
        }
    }
    public void displayError(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }

    public void displayMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Book getSelectedBook() {
        return selectedBook;
    }

    public Spinner<Integer> getQuantitySpinner() {
        return quantitySpinner;
    }
}