package ru.javalab.chat.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CmdAddCart {
    @JsonProperty("product_id")
    private int productId;

    public CmdAddCart(int productId) {
        this.productId = productId;
    }

    public CmdAddCart() {
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
