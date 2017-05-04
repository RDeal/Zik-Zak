package de.fhws.fiw.pvs.zikzak;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import de.fhws.fiw.pvs.zikzak.api.UsersService;
import de.fhws.fiw.pvs.zikzak.api.MessagesService;

/**
 * Created by Robin on 04.05.2017.
 */
@ApplicationPath("/zikzak")
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> returnValue = new HashSet<Class<?>>();
        returnValue.add(UsersService.class);
        returnValue.add(MessagesService.class);
        return returnValue;
    }
}