package ru.javalab.chat.services.message;

import ru.javalab.chat.context.Component;
import ru.javalab.chat.dto.Dto;
import ru.javalab.chat.model.Message;
import ru.javalab.chat.repositories.message.MessageRepository;

public class AddMessageServiceImpl implements AddMessageService, Component {
    private MessageRepository messageRepository;

    public AddMessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Dto save(String message, int id) {
        messageRepository.save(new Message(message, id));
        return null;
    }

    @Override
    public String getName() {
        return "addMessageService";
    }
}
