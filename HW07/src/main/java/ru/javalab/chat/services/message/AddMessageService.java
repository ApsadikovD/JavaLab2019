package ru.javalab.chat.services.message;

import ru.javalab.chat.dto.Dto;
import ru.javalab.chat.dto.MessageDto;

public interface AddMessageService {
    Dto save(String message, int id);
}
