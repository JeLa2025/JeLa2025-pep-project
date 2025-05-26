package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postVerifyLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::retriveAllMessageByAccountHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
        */

    /**
     * Handler to post a new account
     * Jackson ObjectMapper is used to convert the JSON of the POST request into
     * a Account object.
     * If AccountService returns a null, API will return a 400 (Client error) message
     * If sucessful, returns 200 with JSON of Account
     * @throws JsonProcessiongException if there is an issue converting JSON into an object
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
       
        // if AccountService sucessfully returns a account
        if (addedAccount != null) {
            ctx.json(addedAccount);
            ctx.status(200);
        } else {
            // if AccountService returns null
            ctx.status(400);
        }
    }

    /**
     * Handler to verify user logins
     * if sucessful: response body contains a JSON of the account including account_id
     *              200 respond status
     * if failed: respnse status 401
     * @param ctx
     * @throws JsonProcessingException
     */
    private void postVerifyLoginHandler (Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        
        // send them to verify log in
        Account verifyAccount = accountService.verifyLogin(account);
   
        // if AccountService sucessfully returns a account
        if (verifyAccount != null) {
            ctx.json(mapper.writeValueAsString(verifyAccount));
            ctx.status(200);
        } else {
            // if AccountService returns null
            ctx.status(401);
        }
    }

    /**
     * Handler to post new messages
     * If sucessful: response body contains JSON of the message including message_id
     *              response status is 200
     * If fail: response status is 400
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);

        // if MessageService sucessfully returns a account
        if (addedMessage != null) {
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        } else {
            // if MessageService returns null
            ctx.status(400);
        }

    }

    /**
     * Handler to get all messages
     * Response body contain a JSON representaion of list of all messages from db
     * Empty list if no messages
     * 200 status by default
     */
    private void getAllMessageHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessage();
        ctx.json(messages);
        ctx.status(200);
    }

    /**
     * Handler to retrieve a message by its ID
     * response should contain a JSON of message
     * empty body if no message
     * status 200 by default
     */
    private void getMessageByIdHandler(Context ctx) {
        // get the message_id, retrieve path parameter
        int id = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = messageService.getMessageById(id);
        // if message doesn't exist
        if (message == null) {
            ctx.json("");
        } else {
            ctx.json(message);
        }
        ctx.status(200);
    }

    /**
     * Handler to delete a message
     * If sucessful, response body has now-deleted message
     * if message not exist, resonse body should be empty
     * 200 status by default
     */
    private void deleteMessageByIdHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = messageService.deleteMessageById(id);
        // if the message didn't exist
        if (message == null) {
            ctx.json("");
        } else {
            ctx.json(message);
        }
        ctx.status(200);
    }

    /**
     * Handler to update a message
     * If sucessful: messgae is not blank and not over 255 characters
     *              response body contains full updated message
     *              200 status code
     * unsucessful: 400 status code
     */
    private void updateMessageByIdHandler(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        // get id and message_text
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        String message_text = message.getMessage_text();

        Message updatedMessage = messageService.updateMessage(id, message_text);
        
        // if MessageService sucessfully returns a account
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
            ctx.status(200);
        } else {
            // if MessageService returns null
            ctx.status(400);
        }
        
    }

    /**
     * Handler to retrieve all messages written by a particular user
     * response should JSON of a list of all messages
     * empty JSON if no messages
     * 200 status by default
     */
    private void retriveAllMessageByAccountHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("account_id"));

        List<Message> messages = accountService.getAllMessages(id);
        ctx.json(messages);
        ctx.status(200);
    }
}