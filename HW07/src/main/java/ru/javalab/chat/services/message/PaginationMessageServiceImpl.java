package ru.javalab.chat.services.message;

import ru.javalab.chat.context.Component;
import ru.javalab.chat.dto.MessageDto;
import ru.javalab.chat.repositories.message.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaginationMessageServiceImpl implements PaginationMessageService, Component {
    private MessageRepository messageRepository;

    public PaginationMessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<MessageDto> find(int page) {
        Optional<List<MessageDto>> messageList = messageRepository.findByPage(page);
        return messageList.orElse(new ArrayList<>());
    }

    @Override
    public String getName() {
        return "paginationMessageService";
    }
}
