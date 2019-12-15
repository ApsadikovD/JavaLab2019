package ru.javalab.shop.repository.product;

import ru.javalab.di.Component;
import ru.javalab.shop.model.Product;
import ru.javalab.shop.repository.RowMapper;
import ru.javalab.shop.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository, Component {
    private Connection connection;
    private RowMapper<Product> productRowMapper = new RowMapper<Product>() {
        @Override
        public Product mapRow(ResultSet row) throws SQLException {
            return new Product(row.getInt("id"),
                    row.getString("name"),
                    row.getDouble("price"), row.getString("desc")
            );
        }
    };

    public ProductRepositoryImpl() {
        connection = DBConnection.getInstance();
    }

    @Override
    public Optional<Product> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        String sqlQuery = "SELECT * FROM product";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            ResultSet rs = stmt.executeQuery();
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(productRowMapper.mapRow(rs));
            }
            return Optional.of(productList).orElse(new ArrayList<>());
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getName() {
        return "productRepository";
    }
}
