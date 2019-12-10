package ru.javalab.chat.protocol;

public class CmdMessage {
    private String message;

    public CmdMessage() {
    }

    public CmdMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
