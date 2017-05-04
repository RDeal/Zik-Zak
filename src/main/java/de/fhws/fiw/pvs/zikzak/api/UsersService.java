package de.fhws.fiw.pvs.zikzak.api;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.fhws.fiw.pvs.zikzak.models.User;
import de.fhws.fiw.pvs.zikzak.models.UserContainer;

/**
 * Created by Robin on 04.05.2017.
 */
@Path("/user")
public class UsersService {
    @Context
    UriInfo uriInfo;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser() {
        User newUser = new User();
        UserContainer.getInstance().addUser(newUser);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(newUser.getId())).build()).build();
    }
}
