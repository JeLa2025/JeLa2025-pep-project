package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

import java.util.List;

/**
 * The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
 * persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
 * SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
 * actions undertaken by the API to a logging file.
 *
 * It's perfectly normal to have Service methods that only contain a single line that calls a DAO method. An
 * application that follows best practices will often have unnecessary code, but this makes the code more
 * readable and maintainable in the long run!
 */

public class MessageService {
    public MessageDAO messageDAO;
    public AccountDAO accountDAO;
    /**
     * No-args constructor that creates a MessageDAO
     */
    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    /*
     * Args constructor for MessageService when a MessageDAO is provided
     */
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
        accountDAO = new AccountDAO();

    }

    /*
     * Args constructor for MessageService when a MessageDAO and 
     * AccountDAO is provided
     */
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    /**
     * TODO: Use messageDAO to retrive all message
     */
    public List<Message> getAllMessage() {
        return messageDAO.getAllMessage();
    }

    /** Helper to validate message_text length 
     * message must not be blank or greater than 255 characters long
    */
    private boolean validateMessage(String message) {
        int len = message.length();
        if (len > 0 && len <= 255) {
            return true;
        }
        return false;
    }

    /** Helper to validate message_id
     * uses messageDAO to check if message_id exists in the message database
    */
    private boolean validateMessageId(int message_id) {
        Message message = messageDAO.getMessageById(message_id);
        return (message != null) ? true : false;
    }

    /**
     * TODO: Use messageDAO to presist a message to the db
     * message
     * will not contain a message_id.
     * message_text is not blank, is not over 255 characters
     * posted_by refers to a real, existing user.
     */
    public Message addMessage(Message message) {
        // get message_text of the message
        String message_text = message.getMessage_text();

        // get foreign key (posted_by) of the message
        int posted_by = message.getPosted_by();
        // get the foreign key from the Account table
        Account account = accountDAO.getAccountFromId(posted_by);

        // check if message_text is not blank or over 255 characters long
        // and check if posted_by refers to a real account
        if (validateMessage(message_text) &&
            account != null) {
                return messageDAO.insertMessage(message);
        }
        return null;
    }

    /**
     * TODO: Use messageDAO to retrive message by id
     */
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    /**
     * TODO: Use messageDAO to delete message by id
     * The deletion of an existing message should remove an existing message from the database. 
     * If the message existed, the response body should contain the now-deleted message.
     */
    public Message deleteMessageById(int id) {
        // get the message to be deleted if it exists
        Message output = messageDAO.getMessageById(id);

        // delete the message if it exists
        messageDAO.deleteMessageById(id);

        // return the deleted message
        return output;
    }

    /**
     * TODO: Use messageDAO to update message by id
     * the message id already exists
     * the new message_text is not blank and is not over 255 characters. 
     * If the update is successful, the response body should contain the full updated message
     * The message existing on the database should have the updated message_text.
     */
    public Message updateMessage(int id, String text) {

        // validate message_text and message_id
        if (validateMessage(text) && validateMessageId(id)) {
            // update the message
            messageDAO.updateMessage(id, text);
            // retrive the update message and return it
            return getMessageById(id);
        } else {
            return null;
        } 
    }
}
