package repository.security;

import model.Right;
import model.Role;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.*;

public class RightsRolesRepositoryMySql implements RightsRolesRepository {

    private final Connection connection;

    public RightsRolesRepositoryMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addRole(String role) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + ROLE + " values (null, ?)");
            insertStatement.setString(1, role);
            insertStatement.executeUpdate();
        } catch (SQLException e) {

        }
    }

    @Override
    public void addRight(String right) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO `" + RIGHT + "` values (null, ?)");
            insertStatement.setString(1, right);
            insertStatement.executeUpdate();
        } catch (SQLException e) {

        }
    }

    @Override
    public Role findRoleByTitle(String roleTitle) {
        try {
            String fetchRoleSql = "SELECT * FROM " + ROLE + " WHERE `role` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchRoleSql);
            preparedStatement.setString(1, roleTitle);

            ResultSet roleResultSet = preparedStatement.executeQuery();
            if (roleResultSet.next()) {
                Long roleId = roleResultSet.getLong("id");
                String fetchedRoleTitle = roleResultSet.getString("role");
                return new Role(roleId, fetchedRoleTitle, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Role findRoleById(Long roleId) {
        try {
            String fetchRoleSql = "SELECT * FROM " + ROLE + " WHERE `id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchRoleSql);
            preparedStatement.setLong(1, roleId);

            ResultSet roleResultSet = preparedStatement.executeQuery();
            if (roleResultSet.next()) {
                String roleTitle = roleResultSet.getString("role");
                return new Role(roleId, roleTitle, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Right findRightByTitle(String rightTitle) {
        try {
            String fetchRightSql = "SELECT * FROM `" + RIGHT + "` WHERE `right` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchRightSql);
            preparedStatement.setString(1, rightTitle);

            ResultSet rightResultSet = preparedStatement.executeQuery();
            if (rightResultSet.next()) {
                Long rightId = rightResultSet.getLong("id");
                return new Right(rightId, rightTitle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addRolesToUser(User user, List<Role> roles) {
        try {
            for (Role role : roles) {
                PreparedStatement insertUserRoleStatement = connection
                        .prepareStatement("INSERT INTO `user_role` values (null, ?, ?)");
                insertUserRoleStatement.setLong(1, user.getId());
                insertUserRoleStatement.setLong(2, role.getId());
                insertUserRoleStatement.executeUpdate();
            }
        } catch (SQLException e) {

        }
    }

    @Override
    public List<Role> findRolesForUser(Long userId) {
        try {
            List<Role> roles = new ArrayList<>();
            String fetchRoleSql = "SELECT * FROM " + USER_ROLE + " WHERE `user_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchRoleSql);
            preparedStatement.setLong(1, userId);

            ResultSet userRoleResultSet = preparedStatement.executeQuery();
            while (userRoleResultSet.next()) {
                long roleId = userRoleResultSet.getLong("role_id");
                roles.add(findRoleById(roleId));
            }
            return roles;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addRoleRight(Long roleId, Long rightId) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + ROLE_RIGHT + " values (null, ?, ?)");
            insertStatement.setLong(1, roleId);
            insertStatement.setLong(2, rightId);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}