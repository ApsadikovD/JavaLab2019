package ru.javalab.chat.model;

public class MessageCommand {
    private String message;

    public MessageCommand() {
    }

    public MessageCommand(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
