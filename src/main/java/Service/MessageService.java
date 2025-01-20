package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public boolean checkForExistingMessage(int message_id) {
        return messageDAO.checkForExistingMessage(message_id);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    public List<Message> getAllMessagesByAccountId(int account_id) {
        return messageDAO.getAllMessagesByAccountId(account_id);
    }

    public Message postNewMessage(int posted_by, String message_text, long time_posted_epoch) {
        return messageDAO.postNewMessage(posted_by, message_text, time_posted_epoch);
    }

    public Message updateMessageById(int message_id, String message_text) {
        return messageDAO.updateMessageById(message_id, message_text);
    }

    public Message deleteMessageById(int message_id) {
        return messageDAO.deleteMessageById(message_id);
    }
}