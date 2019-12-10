package ru.javalab.chat.repositories.cart;

import ru.javalab.chat.model.Cart;
import ru.javalab.chat.repositories.CrudRepositories;

public interface CartRepository extends CrudRepositories<Cart,Integer> {
    void save(Cart cart);
}
