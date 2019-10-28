package ru.javalab.chat.db.dao;

import ru.javalab.chat.db.RowMapper;
import ru.javalab.chat.db.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessageDao {
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
}
