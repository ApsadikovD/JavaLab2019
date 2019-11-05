package ru.javalab.chat.model;

public class LoginCommand {
    private String name;
    private String password;

    public LoginCommand() {
    }

    public LoginCommand(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
