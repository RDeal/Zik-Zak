package de.fhws.fiw.pvs.zikzak.models;

import java.util.UUID;

/**
 * Created by Robin on 04.05.2017.
 */
public class User {
    private UUID id;

    public User() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
}
