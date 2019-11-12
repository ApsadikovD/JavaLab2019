package ru.javalab.chat.model.command;

public class CmdDeleteProduct {
    private int id;

    public CmdDeleteProduct(int id) {
        this.id = id;
    }

    public CmdDeleteProduct() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
