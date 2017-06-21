package de.fhws.fiw.pvs.zikzak.models;

import de.fhws.fiw.pvs.zikzak.converter.LinkConverter;

import com.owlike.genson.annotation.JsonConverter;
import com.owlike.genson.annotation.JsonDateFormat;
import com.owlike.genson.annotation.JsonIgnore;
import org.glassfish.jersey.linking.InjectLink;

import javax.ws.rs.core.Link;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Robin on 21.06.2017.
 */
public class Comment
{
    private String userid;
    private long id;
    private long messageid;
    private String text;
    private Set<String> upVotes;
    private Set<String> downVotes;

    @JsonDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    private Date creationDate;

    public Comment( )
    {
        this.upVotes = new HashSet<>( );
        this.downVotes = new HashSet<>( );
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

    public long getMessageid( )
    {
        return messageid;
    }

    @JsonIgnore
    public void setMessageid( long messageid )
    {
        this.messageid = messageid;
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

    @InjectLink(style = InjectLink.Style.ABSOLUTE,
            value = "messages/${instance.messageid}/comments/${instance.id}/upvotes",
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
            value = "messages/${instance.messageid}/comments/${instance.id}/downvotes",
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
            value = "messages/${instance.messageid}/comments/${instance.id}",
            type = "application/json", rel = "deleteComment")
    private Link deleteComment;

    @JsonConverter( LinkConverter.class )
    public Link getDeleteComment( )
    {
        return deleteComment;
    }

    @JsonIgnore
    public void setDeleteComment( Link deleteComment )
    {
        this.deleteComment = deleteComment;
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
}
