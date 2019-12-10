package ru.javalab.chat.protocol;

public class CmdAddProduct {
    private String price;
    private String name;

    public CmdAddProduct(String price, String name) {
        this.price = price;
        this.name = name;
    }

    public CmdAddProduct() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
