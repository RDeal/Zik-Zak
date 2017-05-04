package de.fhws.fiw.pvs.zikzak.api;

import com.sun.org.apache.regexp.internal.RE;
import jdk.nashorn.internal.objects.annotations.Getter;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.fhws.fiw.pvs.zikzak.models.Message;
import de.fhws.fiw.pvs.zikzak.models.MessageContainer;
import de.fhws.fiw.pvs.zikzak.models.User;

/**
 * Created by Robin on 04.05.2017.
 */
@Path("/message ")
public class MessagesService {
    @Context
    UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMessage(Message newMessage) {
        MessageContainer.getInstance().insertMessage(newMessage);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(newMessage.getId())).build()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessages() {
        return Response.ok(MessageContainer.getInstance().getAllMessages()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(@PathParam("id") long id) {
        return Response.ok(MessageContainer.getInstance().getMessageById(id)).build();
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyMessage(Message modifiedMessage) {
        MessageContainer.getInstance().modifyMessage(modifiedMessage);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(modifiedMessage.getId())).build()).build();
    }


    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMessage(@PathParam("id") long id) {
        MessageContainer.getInstance().deleteMessage(id);
        return Response.noContent().build();
    }
}
