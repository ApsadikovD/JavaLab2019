package ru.javalab.chat.db.dao;

import ru.javalab.chat.db.RowMapper;
import ru.javalab.chat.db.model.User;
import ru.javalab.chat.util.PasswordEncrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDao {
    private Connection connection;
    private RowMapper<User> userRowMapper = rs -> new User(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("password"),
            rs.getInt("role")
    );

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) {
        String sqlQuery = "INSERT INTO user(name, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, PasswordEncrypt.generateHash(user.getPassword()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Optional<User> find(String name) {
        String sqlQuery = "SELECT * FROM user WHERE name = ? LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            User user = null;
            if (rs.next()) {
                user = userRowMapper.mapRow(rs);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
