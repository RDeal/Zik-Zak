package de.fhws.fiw.pvs.zikzak.api;

import de.fhws.fiw.pvs.zikzak.filter.UserAuthorization;
import de.fhws.fiw.pvs.zikzak.models.Comment;
import de.fhws.fiw.pvs.zikzak.models.Message;
import de.fhws.fiw.pvs.zikzak.storage.Storage;
import de.fhws.fiw.pvs.zikzak.utils.*;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by Robin on 21.06.2017.
 */
@Path("messages/{messageid}/comments")
public class CommentService
{
    @Context
    ContainerRequestContext context;

    @Context
    UriInfo uriInfo;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response listAllComments(@PathParam( "messageid" ) long messageid,
                                    @QueryParam( "size" ) @DefaultValue( "10" ) int size,
                                    @QueryParam( "offset" ) @DefaultValue( "0" ) int offset )
    {
        List<Comment> comments = Storage.getInstance( ).getCommentsOfMessage( messageid, size, offset );

        return Response.ok( comments )
                .header( "Link", createPostCommentHeader( ) )
                .header( "Link", createSelfUriForPage( ) )
                .header( "Link", createNextUriForPage( messageid, size, offset ) )
                .header( "Link", createPrevUriForPage( size, offset ) )
                .header( "X-totalnumberofresults", createTotalNumberHeader( messageid ) )
                .header( "X-numberofresults", comments.size( ) )
                .cacheControl( CachingUtils.create2SecondsPublicCaching( ) )
                .build( );
    }

    private int createTotalNumberHeader( long messageId )
    {
        return Storage.getInstance( ).getTotalNumberOfCommentsOfMessage( messageId );
    }

    private String createSelfUriForPage()
    {
        return Hyperlinks.linkHeader( uriInfo.getRequestUri( ).toString( ), "self", MediaType.APPLICATION_JSON );
    }

    private String createNextUriForPage(long messageId, int size, int offset)
    {
        int totalNumber = Storage.getInstance( ).getTotalNumberOfCommentsOfMessage( messageId );

        int nextOffset = Math.min( offset + size, totalNumber );

        URI location = uriInfo.getAbsolutePathBuilder( ).build( );

        String nextUri = location.toString() + "?size=" + size + "&offset=" + nextOffset;

        return Hyperlinks.linkHeader( nextUri, "next", MediaType.APPLICATION_JSON );
    }

    private String createPrevUriForPage(int size, int offset)
    {
        int prevOffset = Math.max(offset - size, 0);

        URI location = uriInfo.getAbsolutePathBuilder( ).build( );

        String prevUri = location.toString() + "?size=" + size + "&offset=" + prevOffset;

        return Hyperlinks.linkHeader( prevUri, "prev", MediaType.APPLICATION_JSON );
    }

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    @UserAuthorization
    public Response createComment( @PathParam( "messageid" ) long messageid, Comment comment )
    {
        final String userid = ( String ) this.context.getProperty( "userid" );

        try
        {
            Message message = Storage.getInstance( ).getMessage( messageid );

            if ( message == null )
            {
                throw new WebApplicationException( Response.Status.NOT_FOUND );
            }

            comment.setMessageid( messageid );
            comment.setUserid( userid );
            Storage.getInstance( ).createComment( messageid, comment );
        }
        catch ( Exception e )
        {
            throw new WebApplicationException( Response.Status.INTERNAL_SERVER_ERROR );
        }

        URI location = uriInfo.getAbsolutePathBuilder( )
                .path( Long.toString( comment.getId( ) ) )
                .build( );

        return Response.created( location )
                .header( "Link", createGetAllCommentsHeader( ) )
                .build( );
    }

    @DELETE
    @Path( "id" )
    @UserAuthorization
    public Response deleteComment( @PathParam( "id" ) long id,
                                   @PathParam( "messageid" ) long messageid )
    {
        final String userid = ( String ) this.context.getProperty( "userid" );

        try
        {
            Message message = Storage.getInstance( ).getMessage( messageid );

            if ( message != null )
            {
                if ( message.getUserid( ).equals( userid ) )
                {
                    Storage.getInstance( ).deleteComment( messageid, id );
                }
                else
                {
                    throw new WebApplicationException( Response.Status.FORBIDDEN );
                }
            }
        }
        catch ( Exception e )
        {
            throw new WebApplicationException( Response.Status.INTERNAL_SERVER_ERROR );
        }

        return Response.noContent( )
                .header( "Link", createGetAllCommentsHeader( ) )
                .build( );
    }

    @PUT
    @Path( "{id}/downvotes" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response doDownvote( @PathParam( "messageid" ) long messageid,
                                @PathParam( "id" ) long id, String userid )
    {
        Message message = Storage.getInstance( ).getMessage( id );

        if ( message == null )
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }

        for ( Comment comment : message.getComments( ) )
        {
            if ( comment.getId( ) == id )
            {
                comment.addDownVote( userid );
            }
        }

        return Response.noContent( ).build( );
    }

    @PUT
    @Path( "{id}/upvotes" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response doUpvote( @PathParam( "messageid" ) long messageid,
                              @PathParam("id") long id, String userid )
    {
        Message message = Storage.getInstance( ).getMessage( messageid );

        if(message == null)
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }

        for ( Comment comment : message.getComments( ) )
        {
            if ( comment.getId( ) == id )
            {
                comment.addUpVote( userid );
            }
        }

        return Response.noContent( ).build( );
    }

    private String createGetAllCommentsHeader()
    {
        URI location = uriInfo.getAbsolutePathBuilder( ).build( );

        String uri = location.toString( ) + "?size={SIZE}&offset={OFFSET}";

        return Hyperlinks.linkHeader( uri, "getAllCommentsOfMessage", MediaType.APPLICATION_JSON );
    }

    private String createPostCommentHeader( )
    {
        URI location = uriInfo.getAbsolutePathBuilder( ).build( );

        return Hyperlinks.linkHeader( location.toString( ), "createComment", MediaType.APPLICATION_JSON );
    }
}
