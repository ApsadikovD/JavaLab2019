package ru.javalab.chat.db.model;

import java.sql.Date;

public class Message {
    private int id;
    private String message;
    private int userId;
    private Date date;

    public Message(int id, String message, int userId, Date date) {
        this.id = id;
        this.message = message;
        this.userId = userId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
