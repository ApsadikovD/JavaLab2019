package ru.javalab.shop.repository.user;

import ru.javalab.di.Component;
import ru.javalab.shop.model.User;
import ru.javalab.shop.repository.RowMapper;
import ru.javalab.shop.util.DBConnection;
import ru.javalab.shop.util.PasswordEncrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository, Component {
    private Connection connection;
    private RowMapper<User> userRowMapper = rs -> new User(
            rs.getInt("id"),
            rs.getString("email"),
            rs.getString("hash")
    );

    public UserRepositoryImpl() {
        connection = DBConnection.getInstance();
    }

    @Override
    public Optional<User> findByName(String name) {
        String sqlQuery = "SELECT * FROM user WHERE email = ? LIMIT 1";
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

    @Override
    public void save(User user) {
        String sqlQuery = "INSERT INTO user(email, hash) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, PasswordEncrypt.generateHash(user.getHash()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<User> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public String getName() {
        return "userRepository";
    }
}
