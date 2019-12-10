package ru.javalab.chat.protocol;

public class CmdLogin {
    private String name;
    private String password;

    public CmdLogin() {
    }

    public CmdLogin(String name, String password) {
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
