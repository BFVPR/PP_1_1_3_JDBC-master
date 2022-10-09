package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static volatile UserDaoJDBCImpl INSTANCE;
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS User (Id BIGINT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(30), LastName VARCHAR(30), Age TINYINT)";
    private static final String DROP_TABLE = "DROP TABLE User";
    private static final String INSERT_SQL = "INSERT INTO User (NAME, LASTNAME, AGE) VALUES (?,?,?)";

    private static final String DELETE_SQL_BY_ID = "DELETE FROM User WHERE Id = ?";
    private static final String SELECT_SQL = "select * from User";
    private static final String DELETE_SQL = "DELETE from User";

    private UserDaoJDBCImpl() {

    }

    public static UserDaoJDBCImpl getInstance() {
        UserDaoJDBCImpl localInstance = INSTANCE;
        if (localInstance == null) {
            synchronized (UserDaoJDBCImpl.class) {
                localInstance = INSTANCE;
                if (localInstance == null) {
                    INSTANCE = localInstance = new UserDaoJDBCImpl();
                }
            }
        }
        return localInstance;
    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //+
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //+
    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //+
    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // tut id null Norm?
    public List<User> getAllUsers() {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_SQL);
            List<User> list = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastname");
                Byte age = resultSet.getByte("age");
                User user = new User(name, lastName, age);
                list.add(user);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(DELETE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}