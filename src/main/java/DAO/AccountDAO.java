package DAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

// imports Account class
import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A DAO is a class that mediates the transformation of data between the format of objects in Java to rows in a
 * database.
 */

public class AccountDAO {
    /**
     * TODO: insert a new Account into the Account table 
     */
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // sql logic
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // setString, setInt, setLong
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            // execute the query
            int numRowsAffected = preparedStatement.executeUpdate();
            ResultSet pkResultSet = preparedStatement.getGeneratedKeys();
            if (numRowsAffected > 0 && pkResultSet.next()) {
                int generated_account_id = pkResultSet.getInt(1);
                account.setAccount_id(generated_account_id);
                return account;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TODO: retrive account from account_id 
     * 
     */
    public Account getAccountFromId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // sql logic
            String sql = "SELECT * FROM account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // setString, setInt, setLong
            preparedStatement.setInt(1, account_id);

            // execute the query
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("account_id"),
                                            rs.getString("username"),
                                            rs.getString("password"));
                return account;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; 
    }

    /**
     * TODO: retrive account from username 
     * 
     */
    public Account getAccountFromUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // sql logic
            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // setString, setInt, setLong
            preparedStatement.setString(1, username);

            // execute the query
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("account_id"),
                                            rs.getString("username"),
                                            rs.getString("password"));
                return account;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; 
    }

    /**
     * TODO: retrive account from username and Password 
     * 
     */
    public Account getAccountFromUsernameAndPassword(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // sql logic
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // setString
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);


            // execute the query
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("account_id"),
                                            rs.getString("username"),
                                            rs.getString("password"));
                return account;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; 
    }

    /**
     * TODO: retrive all messages written by a particular user
     */
    public List<Message> getAllMessagesByAccount(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> output = new ArrayList<>();

        try {
            // sql logic
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // setString, setInt, setLong
            preparedStatement.setInt(1, account_id);


            // execute the query
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
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
    
}
