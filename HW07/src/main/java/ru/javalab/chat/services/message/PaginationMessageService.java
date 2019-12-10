package ru.javalab.chat.services.message;

import ru.javalab.chat.dto.MessageDto;

import java.util.List;

public interface PaginationMessageService {
    List<MessageDto> find(int page);
}
