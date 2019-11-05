package ru.javalab.chat.db.dao;

import ru.javalab.chat.db.RowMapper;
import ru.javalab.chat.db.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDao {
    public static final int PAGE_SIZE = 5;

    private Connection connection;
    private RowMapper<Message> messageRowMapper = rs -> new Message(
            rs.getInt("id"),
            rs.getString("message"),
            rs.getInt("user_id"),
            rs.getDate("date")
    );

    public MessageDao(Connection connection) {
        this.connection = connection;
    }

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

    public Optional<List<Message>> find(int page) {
        String sqlQuery = "SELECT * FROM message ORDER BY `date` DESC LIMIT  ?, 5";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, (page - 1) * 5);
            ResultSet rs = stmt.executeQuery();
            List<Message> messagesList = new ArrayList<>();
            while (rs.next()) {
                messagesList.add(messageRowMapper.mapRow(rs));
            }
            return Optional.of(messagesList);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
