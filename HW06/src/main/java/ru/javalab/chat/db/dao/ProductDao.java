package ru.javalab.chat.db.dao;

import ru.javalab.chat.db.RowMapper;
import ru.javalab.chat.db.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDao {

    private Connection connection;
    private RowMapper<Product> productRowMapper = rs -> new Product(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getFloat("price")
    );

    public ProductDao(Connection connection) {
        this.connection = connection;
    }

    public void save(Product product) {
        String sqlQuery = "INSERT INTO product(name, price) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, product.getName());
            stmt.setFloat(2, product.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void delete(int id) {
        String sqlQuery = "DELETE FROM product WHERE  id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Optional<List<Product>> find(int page) {
        String sqlQuery = "SELECT * FROM product LIMIT  ?, 5";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, (page - 1) * 5);
            ResultSet rs = stmt.executeQuery();
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(productRowMapper.mapRow(rs));
            }
            return Optional.of(productList);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}
