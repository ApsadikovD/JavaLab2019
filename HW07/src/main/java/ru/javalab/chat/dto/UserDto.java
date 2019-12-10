package ru.javalab.chat.dto;

public class UserDto implements Dto{
    private int id;
    private String name;
    private String token;

    public UserDto(int id, String name, String token) {
        this.id = id;
        this.name = name;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
