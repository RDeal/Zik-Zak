package de.fhws.fiw.pvs.zikzak.models;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Robin on 04.05.2017.
 */
public class UserContainer {

    private static UserContainer INSTANCE;
    private Map<UUID, User> data;

    private UserContainer() {
        this.data = new HashMap<>();
    }

    public static UserContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserContainer();
        }
        return INSTANCE;
    }

    public void addUser(User newUser){
        data.put(newUser.getId(), newUser);
    }
}
