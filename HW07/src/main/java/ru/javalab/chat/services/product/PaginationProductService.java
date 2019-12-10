package ru.javalab.chat.services.product;

import ru.javalab.chat.dto.ProductDto;

import java.util.List;

public interface PaginationProductService {
    List<ProductDto> find(int page);
}
