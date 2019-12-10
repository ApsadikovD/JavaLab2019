package ru.javalab.chat.repositories.cart;

import ru.javalab.chat.context.Component;
import ru.javalab.chat.model.Cart;
import ru.javalab.chat.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CartRepositoryImpl implements CartRepository, Component {
    private Connection connection;

    public CartRepositoryImpl() {
        connection = DBConnection.getInstance();
    }

    @Override
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

    @Override
    public Optional<Cart> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Cart> findAll() {
        return null;
    }

    @Override
    public String getName() {
        return "cartRepository";
    }
}
