package ru.javalab.chat.services.product;

import ru.javalab.chat.context.Component;
import ru.javalab.chat.dto.Dto;
import ru.javalab.chat.repositories.product.ProductRepository;

public class DeleteProductServiceImpl implements DeleteProductService, Component {
    private ProductRepository productRepository;

    public DeleteProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Dto deleteProduct(int id) {
        productRepository.delete(id);
        return null;
    }

    @Override
    public String getName() {
        return "deleteProductServiceImpl";
    }
}
