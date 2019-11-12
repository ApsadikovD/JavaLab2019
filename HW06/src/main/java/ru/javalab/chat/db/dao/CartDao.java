package ru.javalab.chat.db.dao;

import ru.javalab.chat.db.model.Cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CartDao {
    private Connection connection;

    public CartDao(Connection connection) {
        this.connection = connection;
    }

    public void save(Cart cart) {
        String sqlQuery = "INSERT INTO cart(user_id, product_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, cart.getUserId());
            stmt.setInt(2, cart.getProductId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
