package ru.javalab.chat.repositories.product;

import ru.javalab.chat.context.Component;
import ru.javalab.chat.dto.ProductDto;
import ru.javalab.chat.model.Product;
import ru.javalab.chat.repositories.RowMapper;
import ru.javalab.chat.repositories.cart.CartRepositoryImpl;
import ru.javalab.chat.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository, Component {
    private Connection connection;
    private RowMapper<ProductDto> productRowMapper = rs -> new ProductDto(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getFloat("price")
    );

    public ProductRepositoryImpl() {
        connection = DBConnection.getInstance();
    }

    @Override
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

    @Override
    public void delete(int id) {
        String sqlQuery = "DELETE FROM product WHERE  id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<List<ProductDto>> findByPage(int page) {
        String sqlQuery = "SELECT * FROM product LIMIT  ?, 5";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, (page - 1) * 5);
            ResultSet rs = stmt.executeQuery();
            List<ProductDto> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(productRowMapper.mapRow(rs));
            }
            return Optional.of(productList);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Product> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return null;
    }

    @Override
    public String getName() {
        return "productRepository";
    }
}
