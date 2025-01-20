package Controller;

import Model.Account;
import Service.AccountService;
import Model.Message;
import Service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.io.IOException;
import java.util.List;
import java.lang.Integer;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);

        app.get("/messages", this::getAllMessagesHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountIdHandler);

        return app;
    }

    private void registerHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(ctx.body(), Account.class);

            String username = account.getUsername();
            String password = account.getPassword();

            AccountService accountService = new AccountService();
            boolean usernameExists = accountService.checkForExistingUsername(username);

            if (username.length() == 0 || password.length() < 4 || usernameExists) {
                ctx.status(400);
            } else {
                Account newAccount = accountService.register(username, password);
                ctx.json(mapper.writeValueAsString(newAccount)).status(200);
            }
        } catch (IOException e) {
            ctx.status(400).result("Invalid request body: " + e.getMessage());
        }
    }

    private void loginHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(ctx.body(), Account.class);

            AccountService accountService = new AccountService();
            Account loginAccount = accountService.login(account);

            if (loginAccount == null) {
                ctx.status(401);
            } else {
                ctx.json(mapper.writeValueAsString(loginAccount)).status(200);
            }
        } catch (IOException e) {
            ctx.status(400).result("Invalid request body: " + e.getMessage());
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            MessageService messageService = new MessageService();
            List<Message> messages = messageService.getAllMessages();

            if (messages == null || messages.isEmpty()) {
                ctx.json("[]");
            } else {
                ctx.json(mapper.writeValueAsString(messages));
            }
        } catch (IOException e) {
            ctx.status(400).result("Invalid request body: " + e.getMessage());
        }
    }

    private void getMessageByIdHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            int message_id = Integer.parseInt(ctx.pathParam("message_id"));

            MessageService messageService = new MessageService();
            boolean messageExists = messageService.checkForExistingMessage(message_id);

            if (!messageExists) {
                ctx.status(200);
            } else {
                Message message = messageService.getMessageById(message_id);
                ctx.status(200).json(mapper.writeValueAsString(message));
            }
        } catch (Exception e) {
            ctx.status(500).result("Error fetching message: " + e.getMessage());
        }
    }

    private void getAllMessagesByAccountIdHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            int account_id = Integer.parseInt(ctx.pathParam("account_id"));

            MessageService messageService = new MessageService();
            List<Message> messages = messageService.getAllMessagesByAccountId(account_id);

            if (messages == null || messages.isEmpty()) {
                ctx.json("[]");
            } else {
                ctx.json(mapper.writeValueAsString(messages));
            }
        } catch (Exception e) {
            ctx.status(400).result("Error fetching messages: " + e.getMessage());
        }
    }

    private void postNewMessageHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);

            int posted_by = message.getPosted_by();
            String message_text = message.getMessage_text();
            long time_posted_epoch = message.getTime_posted_epoch();

            AccountService accountService = new AccountService();
            boolean accountIdExists = accountService.checkForExistingAccountId(posted_by);

            if (message_text.length() == 0 || message_text.length() > 255 || !accountIdExists) {
                ctx.status(400);
            } else {
                MessageService messageService = new MessageService();
                Message newMessage = messageService.postNewMessage(posted_by, message_text, time_posted_epoch);
                ctx.json(mapper.writeValueAsString(newMessage)).status(200);
            }
        } catch (IOException e) {
            ctx.status(400).result("Invalid request body: " + e.getMessage());
        }
    }

    private void updateMessageByIdHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);

            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            String message_text = message.getMessage_text();

            MessageService messageService = new MessageService();
            boolean messageExists = messageService.checkForExistingMessage(message_id);

            if (message_text.length() == 0 || message_text.length() > 255 || !messageExists) {
                ctx.status(400);
            } else {
                Message updatedMessage = messageService.updateMessageById(message_id, message_text);
                ctx.json(mapper.writeValueAsString(updatedMessage)).status(200);
            }
        } catch (IOException e) {
            ctx.status(400).result("Invalid request body: " + e.getMessage());
        }
    }

    private void deleteMessageHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            int message_id = Integer.parseInt(ctx.pathParam("message_id"));

            MessageService messageService = new MessageService();
            boolean messageExists = messageService.checkForExistingMessage(message_id);

            if (!messageExists) {
                ctx.status(200);
            } else {
                Message message = messageService.deleteMessageById(message_id);
                ctx.status(200).json(mapper.writeValueAsString(message));
            }
        } catch (Exception e) {
            ctx.status(400).result("Error fetching message: " + e.getMessage());
        }
    }
}