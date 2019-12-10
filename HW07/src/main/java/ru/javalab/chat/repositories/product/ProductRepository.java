package ru.javalab.chat.repositories.product;

import ru.javalab.chat.dto.ProductDto;
import ru.javalab.chat.model.Product;
import ru.javalab.chat.repositories.CrudRepositories;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepositories<Product, Integer> {
    void save(Product product);

    void delete(int id);

    Optional<List<ProductDto>> findByPage(int page);
}
