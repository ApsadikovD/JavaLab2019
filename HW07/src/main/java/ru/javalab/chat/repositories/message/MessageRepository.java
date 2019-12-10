package ru.javalab.chat.repositories.message;

import ru.javalab.chat.dto.MessageDto;
import ru.javalab.chat.model.Message;
import ru.javalab.chat.repositories.CrudRepositories;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends CrudRepositories<Message, Integer> {

    void save(Message message);

    Optional<List<MessageDto>> findByPage(int page);
}
