package ru.javalab.chat.services.product;

import ru.javalab.chat.context.Component;
import ru.javalab.chat.dto.Dto;
import ru.javalab.chat.model.Product;
import ru.javalab.chat.repositories.product.ProductRepository;

public class AddProductServiceImpl implements AddProductService, Component {
    private ProductRepository productRepository;

    public AddProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Dto addProduct(String name, float price) {
        productRepository.save(new Product(name, price));
        return null;
    }

    @Override
    public String getName() {
        return "addProductService";
    }
}
