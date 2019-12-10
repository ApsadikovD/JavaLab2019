package ru.javalab.chat.services.product;

import ru.javalab.chat.dto.Dto;

public interface AddProductService {
    Dto addProduct(String name, float price);
}
