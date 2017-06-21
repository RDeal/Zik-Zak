package de.fhws.fiw.pvs.zikzak.api;

import de.fhws.fiw.pvs.zikzak.filter.UserAuthorization;
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
 * Created by Robin on 04.05.2017.
 */
@Path("messages")
public class MessageService
{
    @Context
    ContainerRequestContext context;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response listAllMessages( @QueryParam( "size" ) @DefaultValue( "10" ) int size,
                                     @QueryParam( "offset" ) @DefaultValue( "0" ) int offset,
                                     @QueryParam( "orderby" ) @DefaultValue( "date" ) String orderBy )
    {

        List<Message> messages = Storage.getInstance( ).getMessages( size, offset, orderBy );

        return Response.ok( messages )
                .header( "Link", createPostMessageHeader( ) )
                .header( "Link", createSelfUriForPage( ) )
                .header( "Link", createPrevUriForPage( size, offset, orderBy ) )
                .header( "Link", createNextUriForPage( size, offset, orderBy ) )
                .header( "X-totalnumberofresults", createTotalNumberHeader( ) )
                .header( "X-numberofresults", messages.size( ) )
                .cacheControl( CachingUtils.create2SecondsPublicCaching( ) )
                .build( );
    }

    private int createTotalNumberHeader( )
    {
        return Storage.getInstance( ).getTotalNumberOfMessages( );
    }

    private String createSelfUriForPage()
    {
        return Hyperlinks.linkHeader( uriInfo.getRequestUri( ).toString( ), "self", MediaType.APPLICATION_JSON );
    }

    private String createNextUriForPage(int size, int offset, String orderBy )
    {
        int totalNumber = Storage.getInstance( ).getTotalNumberOfMessages( );

        int nextOffset = Math.min( offset + size, totalNumber );

        URI location = uriInfo.getBaseUriBuilder( )
                .path( this.getClass( ) )
                .build( );

        String nextUri = location.toString() + "?size=" + size + "&offset=" + nextOffset + "&orderby=" + orderBy;

        return Hyperlinks.linkHeader( nextUri, "next", MediaType.APPLICATION_JSON );
    }

    private String createPrevUriForPage(int size, int offset, String orderBy)
    {
        int prevOffset = Math.max(offset - size, 0);

        URI location = uriInfo.getBaseUriBuilder( )
                .path( this.getClass( ) )
                .build( );

        String prevUri = location.toString() + "?size=" + size + "&offset=" + prevOffset + "&orderby=" + orderBy;

        return Hyperlinks.linkHeader( prevUri, "prev", MediaType.APPLICATION_JSON );
    }

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    @UserAuthorization
    public Response saveMessage( Message message )
    {
        final String userid = ( String ) this.context.getProperty( "userid" );

        try
        {
            message.setUserid( userid );
            Storage.getInstance( ).createMessage( message );
        }
        catch ( Exception e )
        {
            throw new WebApplicationException( Response.Status.INTERNAL_SERVER_ERROR );
        }

        URI location = uriInfo.getAbsolutePathBuilder( )
                .path( Long.toString( message.getId( ) ) )
                .build( );

        return Response.created( location )
                .header( "Link", createGetAllMessagesHeader( ) )
                .build( );
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path( "{id}" )
    public Response getMessage( @PathParam( "id" ) long id )
    {
        Message message = Storage.getInstance( ).getMessage( id );

        if ( message == null )
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }

        return Response.ok( message )
                .header( "Link", createGetAllMessagesHeader( ) )
                .header( "Link", createPutMessageHeader( ) )
                .header( "Link", createDeleteMessageHeader( ) )
                .cacheControl( CachingUtils.create30SecondsPublicCaching( ) )
                .build( );
    }

    @DELETE
    @Path( "{id}" )
    @UserAuthorization
    public Response deleteMessage( @PathParam( "id" ) long id )
    {
        final String userid = ( String ) this.context.getProperty( "userid" );

        try
        {
            if(Storage.getInstance().getMessage( id ).getUserid().equals( userid ))
            {
                Storage.getInstance( ).deleteMessage( id );
            } else
            {
                throw new WebApplicationException( Response.Status.FORBIDDEN );
            }
        }
        catch ( Exception e )
        {
            throw new WebApplicationException( Response.Status.INTERNAL_SERVER_ERROR );
        }

        return Response.noContent( )
                .header("Link", createGetAllMessagesHeader())
                .build( );
    }

    @PUT
    @Consumes( MediaType.APPLICATION_JSON )
    @Path( "{id}" )
    @UserAuthorization
    public Response updateMessage( @PathParam( "id" ) long id, Message message )
    {
        final String userid = ( String ) this.context.getProperty( "userid" );

        try
        {
            if(Storage.getInstance().getMessage( id ).getUserid().equals( userid ))
            {
                Storage.getInstance( ).updateMessage( id, message );
            }
            else
            {
                throw new WebApplicationException( Response.Status.FORBIDDEN );
            }
        }
        catch ( Exception e )
        {
            throw new WebApplicationException( Response.Status.INTERNAL_SERVER_ERROR );
        }

        return Response.noContent( )
                .header( "Link", createGetAllMessagesHeader( ) )
                .header( "Link", createGetSingleMessageHeader( ) )
                .build( );
    }

    @PUT
    @Path( "{id}/downvotes" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response doDownvote( @PathParam( "id" ) long id, String userid )
    {
        Message message = Storage.getInstance( ).getMessage( id );

        if(message == null)
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }

        message.addDownVote( userid );

        return Response.noContent( ).build( );
    }

    @PUT
    @Path( "{id}/upvotes" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response doUpvote( @PathParam( "id" ) long id, String userid )
    {
        Message message = Storage.getInstance( ).getMessage( id );

        if(message == null)
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }

        message.addUpVote( userid );

        return Response.noContent( ).build( );
    }

    @GET
    @Path( "ping" )
    public String ping( )
    {
        return "OK";
    }

    private String createGetAllMessagesHeader( )
    {
        URI location = uriInfo.getBaseUriBuilder( )
                .path( this.getClass( ) )
                .build( );

        String uri = location.toString() + "?size={SIZE}&offset={OFFSET}&orderby={ORDERBY}";

        return Hyperlinks.linkHeader( uri, "getAllMessages", MediaType.APPLICATION_JSON );
    }

    private String createPostMessageHeader( )
    {
        URI location = uriInfo.getBaseUriBuilder( )
                .path( this.getClass( ) )
                .build( );

        return Hyperlinks.linkHeader( location.toString( ), "createMessage", MediaType.APPLICATION_JSON );
    }

    private String createGetSingleMessageHeader()
    {
        URI location = uriInfo.getRequestUri( );

        return Hyperlinks.linkHeader( location.toString( ), "getSingleMessage", MediaType.APPLICATION_JSON );
    }

    private String createPutMessageHeader()
    {
        URI location = uriInfo.getRequestUri( );

        return Hyperlinks.linkHeader( location.toString( ), "updateMessage", MediaType.APPLICATION_JSON );
    }

    private String createDeleteMessageHeader()
    {
        URI location = uriInfo.getRequestUri( );

        return Hyperlinks.linkHeader( location.toString( ), "deleteMessage", MediaType.APPLICATION_JSON );
    }
}
