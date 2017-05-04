package de.fhws.fiw.pvs.zikzak.models;

/**
 * Created by Robin on 04.05.2017.
 */
public class Message {
    private static Long counter = Long.valueOf(99999);
    private Long id;
    private String text;
    private User user;

    public Message(String text, User user) {
        this.id = counter++;
        this.text = text;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User newUser) {
        this.user = newUser;
    }
}
