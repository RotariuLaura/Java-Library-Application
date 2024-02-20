package repository.user;

import model.User;
import model.validator.Notification;

import java.util.*;

public interface UserRepository {

    List<User> findAll();
    List<User> findAllEmployees();

    Notification<User> findByUsernameAndPassword(String username, String password);

    Notification<Boolean> save(User user);

    void removeAll();

    boolean existsByUsername(String username);
    User findByUsername(String username);
    boolean deleteEmployee(Long id);
    User findById(Long id);
    boolean updateEmployee(Long id, String newName, String newPassword, String newSalt);
}