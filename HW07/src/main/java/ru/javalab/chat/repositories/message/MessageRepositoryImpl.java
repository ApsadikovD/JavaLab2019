package ru.javalab.chat.repositories.message;

import ru.javalab.chat.context.Component;
import ru.javalab.chat.dto.MessageDto;
import ru.javalab.chat.model.Message;
import ru.javalab.chat.repositories.RowMapper;
import ru.javalab.chat.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageRepositoryImpl implements MessageRepository, Component {
    private Connection connection;
    private RowMapper<MessageDto> messageRowMapper = rs -> new MessageDto(
            rs.getString("name"),
            rs.getString("message")
    );

    public MessageRepositoryImpl() {
        connection = DBConnection.getInstance();
    }

    @Override
    public void save(Message message) {
        String sqlQuery = "INSERT INTO message(message, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, message.getMessage());
            stmt.setInt(2, message.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<List<MessageDto>> findByPage(int page) {
        String sqlQuery = "SELECT message.id, message.date, message.message, message.user_id, user.name FROM message " +
                "INNER JOIN user ON message.user_id = user.id ORDER BY `date` DESC LIMIT  ?, 5;";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, (page - 1) * 5);
            ResultSet rs = stmt.executeQuery();
            List<MessageDto> messagesList = new ArrayList<>();
            while (rs.next()) {
                messagesList.add(messageRowMapper.mapRow(rs));
            }
            return Optional.of(messagesList);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Message> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Message> findAll() {
        return null;
    }

    @Override
    public String getName() {
        return "messageRepository";
    }
}
