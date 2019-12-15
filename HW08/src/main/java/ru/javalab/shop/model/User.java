package ru.javalab.shop.model;

public class User {
    private int id;
    private String email;
    private String hash;

    public User() {
    }

    public User(String email, String hash) {
        this.email = email;
        this.hash = hash;
    }

    public User(int id, String email, String hash) {
        this.id = id;
        this.email = email;
        this.hash = hash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
