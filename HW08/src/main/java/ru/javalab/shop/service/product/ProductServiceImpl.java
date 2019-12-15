package ru.javalab.shop.service.product;

import ru.javalab.di.Component;
import ru.javalab.shop.model.Product;
import ru.javalab.shop.repository.product.ProductRepositoryImpl;

import java.util.List;

public class ProductServiceImpl implements ProductService, Component {
    private ProductRepositoryImpl productRepository;

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public String getName() {
        return "productService";
    }
}
