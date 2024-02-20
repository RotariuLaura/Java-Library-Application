package repository.user;

import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.USER;

public class UserRepositoryMySql implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySql(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        return null;
    }
    @Override
    public List<User> findAllEmployees() {
        List<User> employees = new ArrayList<>();
        try {
            String fetchEmployeeSql =
                    "SELECT * FROM `" + USER + "` WHERE `username` LIKE '%@employee.com'";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchEmployeeSql);

            ResultSet employeeResultSet = preparedStatement.executeQuery();
            while (employeeResultSet.next()) {
                User employee = new UserBuilder()
                        .setId(employeeResultSet.getLong("id"))
                        .setUsername(employeeResultSet.getString("username"))
                        .setPassword(employeeResultSet.getString("password"))
                        .setSalt(employeeResultSet.getString("salt"))
                        .setRoles(rightsRolesRepository.findRolesForUser(employeeResultSet.getLong("id")))
                        .build();
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    //rotariu.laura@yahoo.com' and 1=1; --  should not work after rewriting function

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`= ? and `password`= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet userResultSet = preparedStatement.executeQuery();
            if(userResultSet.next()) {
                User user = new UserBuilder()
                        .setId(userResultSet.getLong("id"))
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setSalt(userResultSet.getString("salt"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
                findByUsernameAndPasswordNotification.setResult(user);
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password");
                return findByUsernameAndPasswordNotification;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database");
        }
        return findByUsernameAndPasswordNotification;
    }

    public User findByUsername(String username) {
        try {
            String fetchUserSql =
                    "SELECT * FROM `" + USER + "` WHERE `username` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setString(1, username);

            ResultSet userResultSet = preparedStatement.executeQuery();
            if (userResultSet.next()) {
                return new UserBuilder()
                        .setId(userResultSet.getLong("id"))
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setSalt(userResultSet.getString("salt"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findById(Long id) {
        try {
            String fetchUserSql =
                    "SELECT * FROM `" + USER + "` WHERE `id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setLong(1, id);

            ResultSet userResultSet = preparedStatement.executeQuery();
            if (userResultSet.next()) {
                return new UserBuilder()
                        .setId(userResultSet.getLong("id"))
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setSalt(userResultSet.getString("salt"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Notification<Boolean> save(User user) {
        Notification<Boolean> saveNotification = new Notification<>();
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?, ?)", 1);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.setString(3, user.getSalt());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            if(rs.next()){
                long userId = rs.getLong(1);
                user.setId(userId);

                rightsRolesRepository.addRolesToUser(user, user.getRoles());
                saveNotification.setResult(Boolean.TRUE);
            } else {
                saveNotification.addError("Failed to save user to the Database!");
                saveNotification.setResult(Boolean.FALSE);
                return saveNotification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            saveNotification.addError("Something is wrong with the Database");
        }
        return saveNotification;
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`= ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setString(1, email);
            ResultSet userResultSet = preparedStatement.executeQuery();
            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEmployee(Long id, String newName, String newPassword, String newSalt) {
        try {
            String updateEmployeeSql =
                    "UPDATE `" + USER + "` SET `username` = ?, `password` = ?, `salt` = ? WHERE `id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateEmployeeSql);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newPassword);
            preparedStatement.setString(3, newSalt);
            preparedStatement.setLong(4, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEmployee(Long id) {
        try {
            String sql = "DELETE from user where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}