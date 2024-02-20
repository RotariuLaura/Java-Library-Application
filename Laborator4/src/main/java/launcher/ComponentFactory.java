package launcher;

import controller.LoginController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySql;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySql;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySql;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySql;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.order.OrderService;
import service.order.OrderServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import view.LoginView;

import java.sql.Connection;

public class ComponentFactory {
    private final LoginView loginView;
    private final LoginController loginController;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final Stage primaryStage;
    private static volatile ComponentFactory instance;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    public static ComponentFactory getInstance(Boolean componentsForTests, Stage stage){
        if(instance == null) {
            synchronized (ComponentFactory.class) {
                if (instance == null) {
                    instance = new ComponentFactory(componentsForTests, stage);
                }
            }
        }
        return instance;
    }
    public ComponentFactory(Boolean componentsForTests, Stage stage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTests).getConnection();
        this.rightsRolesRepository = new RightsRolesRepositoryMySql(connection);
        this.userRepository = new UserRepositoryMySql(connection, rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        this.primaryStage = stage;
        this.loginView = new LoginView(primaryStage);
        this.loginController = new LoginController(loginView, authenticationService, this);
        this.bookRepository = new BookRepositoryMySql(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        this.orderRepository = new OrderRepositoryMySql(connection);
        this.orderService = new OrderServiceImpl(orderRepository);
    }

    public AuthenticationService getAuthenticationService(){
        return authenticationService;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

}
