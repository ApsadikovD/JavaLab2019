package ru.javalab.chat.services.product;

import ru.javalab.chat.context.Component;
import ru.javalab.chat.dto.ProductDto;
import ru.javalab.chat.repositories.product.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaginationProductServiceImpl implements PaginationProductService, Component {
    private ProductRepository productRepository;

    public PaginationProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDto> find(int page) {
        Optional<List<ProductDto>> productList = productRepository.findByPage(page);
        return productList.orElse(new ArrayList<>());
    }


    @Override
    public String getName() {
        return "paginationProductService";
    }
}
