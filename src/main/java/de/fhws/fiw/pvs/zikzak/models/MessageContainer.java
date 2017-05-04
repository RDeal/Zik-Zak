package de.fhws.fiw.pvs.zikzak.models;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by Robin on 04.05.2017.
 */
public class MessageContainer {
    private static MessageContainer INSTANCE;
    private Map<Long, Message> data;

    private MessageContainer() {
        this.data = new HashMap<>();
    }

    public static MessageContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageContainer();
        }
        return INSTANCE;
    }

    public void insertMessage(Message newMessage) {
        data.put(newMessage.getId(), newMessage);
    }

    public Map<Long, Message> getAllMessages() {
        return this.data;
    }

    public Message getMessageById(Long id) {
        return data.get(id);
    }

    public void modifyMessage(Message modifiedMessage) {
        data.replace(modifiedMessage.getId(), modifiedMessage);
    }

    public void deleteMessage(Long id) {
        data.remove(id);
    }
}
