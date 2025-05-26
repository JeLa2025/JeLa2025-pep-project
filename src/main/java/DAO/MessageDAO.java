package DAO;

// import Message class + Connection Util
import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    /**
     * TODO: insert new message into Message table
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // sql logic
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // setString, setInt, setLong
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            // execute the query
            int numRowsAffected = preparedStatement.executeUpdate();
            ResultSet pkResultSet = preparedStatement.getGeneratedKeys();
            if (numRowsAffected > 0 && pkResultSet.next()) {
                int generated_message_id = pkResultSet.getInt(1);
                message.setMessage_id(generated_message_id);
                return message;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TODO: retrive all message
     */
    public List<Message> getAllMessage() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> output = new ArrayList<>();
        try {
            // sql logic
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // execute the query
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Message message = new Message(
                                    rs.getInt("message_id"),
                                    rs.getInt("posted_by"),
                                    rs.getString("message_text"),
                                    rs.getLong("time_posted_epoch"));
                output.add(message);
            }

            return output;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * TODO: retrive all message by ID
     */
    public Message getMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();
    
        try {
            // sql logic
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            // setInt
            preparedStatement.setInt(1, id);

            // execute the query
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Message message = new Message(
                                    rs.getInt("message_id"),
                                    rs.getInt("posted_by"),
                                    rs.getString("message_text"),
                                    rs.getLong("time_posted_epoch"));
                return message;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * TODO: delete message by ID
     */
    public void deleteMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            // sql logic
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            // setInt
            preparedStatement.setInt(1, id);

            // execute the query
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * TODO: update message by ID
     */
    public void updateMessage(int id, String text) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            // sql logic
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            // setString, setInt
            preparedStatement.setString(1, text);
            preparedStatement.setInt(2, id);

            // execute the query
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}