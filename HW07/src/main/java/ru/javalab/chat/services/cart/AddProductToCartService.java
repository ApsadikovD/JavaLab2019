package ru.javalab.chat.services.cart;

import ru.javalab.chat.dto.Dto;
import ru.javalab.chat.model.Cart;

public interface AddProductToCartService {

    Dto addProductToCart(Cart cart);
}
