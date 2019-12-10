package ru.javalab.chat.services.cart;

import ru.javalab.chat.context.Component;
import ru.javalab.chat.dto.Dto;
import ru.javalab.chat.model.Cart;
import ru.javalab.chat.repositories.cart.CartRepository;

public class AddProductToCartServiceImpl implements AddProductToCartService, Component {
    private CartRepository cartRepository;

    public AddProductToCartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Dto addProductToCart(Cart cart) {
        cartRepository.save(cart);
        return null;
    }

    @Override
    public String getName() {
        return "addProductToCartService";
    }
}
