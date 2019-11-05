package ru.javalab.chat.model;

import ru.javalab.chat.db.model.Message;

import java.util.List;

public class PaginationResponse {
    private List<Message> data;

    public PaginationResponse() {
    }

    public PaginationResponse(List<Message> data) {
        this.data = data;
    }

    public List<Message> getData() {
        return data;
    }

    public void setData(List<Message> data) {
        this.data = data;
    }
}
