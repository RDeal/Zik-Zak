package de.fhws.fiw.pvs.zikzak.models;

import de.fhws.fiw.pvs.zikzak.converter.LinkConverter;

import com.owlike.genson.annotation.JsonConverter;
import com.owlike.genson.annotation.JsonIgnore;
import org.glassfish.jersey.linking.InjectLink;

import javax.ws.rs.core.Link;

/**
 * Created by Robin on 04.05.2017.
 */
public class User {
    private String id;

    public User( )
    {

    }

    public String getId( )
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    @InjectLink(style = InjectLink.Style.ABSOLUTE,
            value = "users/${instance.id}/messages",
            type = "application/json",
            rel = "getMessagesOfUser")
    private Link messageUrl;

    @JsonConverter( LinkConverter.class )
    public Link getMessageUrl( )
    {
        return messageUrl;
    }

    @JsonIgnore
    public void setMessageUrl( Link messageUrl )
    {
        this.messageUrl = messageUrl;
    }

    @InjectLink(style = InjectLink.Style.ABSOLUTE,
            value = "users/${instance.id}",
            type = "application/json",
            rel= "self")
    private Link selfUri;

    @JsonConverter( LinkConverter.class )
    public Link getSelfUri( )
    {
        return selfUri;
    }

    @JsonIgnore
    public void setSelfUri( Link selfUri )
    {
        this.selfUri = selfUri;
    }
}
