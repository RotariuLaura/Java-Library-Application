package database;


import java.util.*;

import static database.Constants.Rights.*;
import static database.Constants.Roles.*;
public class Constants {

    public static Map<String, List<String>> getRolesRights() {
        Map<String, List<String>> rolesRights = new HashMap<>();
        for (String role : ROLES) {
            rolesRights.put(role, new ArrayList<>());
        }
        rolesRights.get(ADMINISTRATOR).addAll(Arrays.asList(RIGHTS));

        rolesRights.get(EMPLOYEE).addAll(Arrays.asList(CREATE_BOOK, DELETE_BOOK, UPDATE_BOOK, SELL_BOOK));

        rolesRights.get(CUSTOMER).addAll(Arrays.asList(VIEW_BOOKS, BUY_BOOK));

        return rolesRights;
    }

    public static class Schemas {
        public static final String TEST = "test_library";
        public static final String PRODUCTION = "library";

        public static final String[] SCHEMAS = new String[]{TEST, PRODUCTION};
    }

    public static class Tables {
        public static final String BOOK = "book";
        public static final String USER = "user";
        public static final String ROLE = "role";
        public static final String RIGHT = "right";
        public static final String ROLE_RIGHT = "role_right";
        public static final String USER_ROLE = "user_role";
        public static final String ORDER_TABLE = "order_table";

        public static final String[] ORDERED_TABLES_FOR_CREATION = new String[]{USER, ROLE, RIGHT, ROLE_RIGHT, USER_ROLE,
                BOOK, ORDER_TABLE};
    }

    public static class Roles {
        public static final String ADMINISTRATOR = "administrator";
        public static final String EMPLOYEE = "employee";
        public static final String CUSTOMER = "customer";

        public static final String[] ROLES = new String[]{ADMINISTRATOR, EMPLOYEE, CUSTOMER};
    }

    public static class Rights {
        public static final String CREATE_USER = "create_user";
        public static final String DELETE_USER = "delete_user";
        public static final String UPDATE_USER = "update_user";

        public static final String VIEW_USERS = "view_users";

        public static final String CREATE_BOOK = "create_book";
        public static final String DELETE_BOOK = "delete_book";
        public static final String UPDATE_BOOK = "update_book";

        public static final String SELL_BOOK = "sell_book";
        public static final String BUY_BOOK = "buy_book";
        public static final String VIEW_BOOKS = "view_books";

        public static final String[] RIGHTS = new String[]{CREATE_USER, DELETE_USER, UPDATE_USER, VIEW_USERS,
                CREATE_BOOK, DELETE_BOOK, UPDATE_BOOK, SELL_BOOK, BUY_BOOK, VIEW_BOOKS};
    }
}