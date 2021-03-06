package de.fhws.fiw.pvs.zikzak.models;

import de.fhws.fiw.pvs.zikzak.converter.LinkConverter;

import com.owlike.genson.annotation.JsonConverter;
import com.owlike.genson.annotation.JsonDateFormat;
import com.owlike.genson.annotation.JsonIgnore;
import org.glassfish.jersey.linking.InjectLink;
import javax.ws.rs.core.Link;
import java.util.*;

/**
 * Created by Robin on 04.05.2017.
 */
public class Message {
    private String userid;
    private long id;
    private String text;
    private Set<String> upVotes;
    private Set<String> downVotes;
    private List<Comment> comments;

    @JsonDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    private Date creationDate;

    public Message( )
    {
        this.upVotes = new HashSet<>( );
        this.downVotes = new HashSet<>( );
        this.comments = new ArrayList<>( );
        this.creationDate = new Date( );
    }

    public String getUserid( )
    {
        return userid;
    }

    public void setUserid( String userid )
    {
        this.userid = userid;
    }

    public long getId( )
    {
        return id;
    }

    @JsonIgnore
    public void setId( long id )
    {
        this.id = id;
    }

    public String getText( )
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    public int getUpVotes()
    {
        return upVotes.size( );
    }

    public void addUpVote( String userId )
    {
        upVotes.add( userId );
    }

    public int getDownVotes()
    {
        return downVotes.size( );
    }

    public void addDownVote( String userId )
    {
        downVotes.add( userId );
    }

    @JsonIgnore
    public List<Comment> getComments( )
    {
        return comments;
    }

    public void addComment( Comment comment )
    {
        this.comments.add( comment );
    }

    public int getNumberOfComments()
    {
        return this.comments.size( );
    }

    @InjectLink(style = InjectLink.Style.ABSOLUTE,
            value = "messages/${instance.id}/upvotes",
            type = "application/json", rel = "putUpvote")
    private Link upVoteLink;

    @JsonConverter( LinkConverter.class )
    public Link getUpVoteLink( )
    {
        return upVoteLink;
    }

    @JsonIgnore
    public void setUpVoteLink( Link upVoteLink )
    {
        this.upVoteLink = upVoteLink;
    }

    @InjectLink(style = InjectLink.Style.ABSOLUTE,
            value = "messages/${instance.id}/downvotes",
            type = "application/json", rel = "putDownvote")
    private Link downVoteLink;

    @JsonConverter( LinkConverter.class )
    public Link getDownVoteLink( )
    {
        return downVoteLink;
    }

    @JsonIgnore
    public void setDownVoteLink( Link downVoteLink )
    {
        this.downVoteLink = downVoteLink;
    }

    @InjectLink(style = InjectLink.Style.ABSOLUTE,
            value = "messages/${instance.id}/comments?offset=0&size=10",
            type = "application/json", rel = "getAllComments")
    private Link commentLink;

    @JsonConverter( LinkConverter.class )
    public Link getCommentLink( )
    {
        return commentLink;
    }

    @JsonIgnore
    public void setCommentLink( Link commentLink )
    {
        this.commentLink = commentLink;
    }

    public Date getCreationDate( )
    {
        return creationDate;
    }

    @JsonIgnore
    public void setCreationDate( Date creationDate )
    {
        this.creationDate = creationDate;
    }

    @InjectLink(style = InjectLink.Style.ABSOLUTE,
            value = "messages/${instance.id}",
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
