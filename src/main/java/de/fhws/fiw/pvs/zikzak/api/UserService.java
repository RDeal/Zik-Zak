package de.fhws.fiw.pvs.zikzak.api;

import de.fhws.fiw.pvs.zikzak.models.User;
import de.fhws.fiw.pvs.zikzak.storage.Storage;
import de.fhws.fiw.pvs.zikzak.utils.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by Robin on 04.05.2017.
 */
@Path("users")
public class UserService
{
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response listAllUsers( @QueryParam( "size" ) @DefaultValue( "10" ) int size,
                                  @QueryParam( "offset" ) @DefaultValue( "0" ) int offset )
    {
        List<User> users = Storage.getInstance( ).getUsers( size, offset );

        return Response.ok( users )
                .header( "Link", createPostUserHeader( ) )
                .header( "Link", createSelfUriForPage( ) )
                .header( "Link", createPrevUriForPage( size, offset ) )
                .header( "Link", createNextUriForPage( size, offset ) )
                .header( "X-totalnumberofresults", createTotalNumberHeader( ) )
                .header( "X-numberofresults", users.size( ) )
                .cacheControl( CachingUtils.create2SecondsPublicCaching( ) )
                .build( );
    }

    private int createTotalNumberHeader( )
    {
        return Storage.getInstance( ).getTotalNumberOfUsers( );
    }

    private String createSelfUriForPage()
    {
        return Hyperlinks.linkHeader( uriInfo.getRequestUri( ).toString( ), "self", MediaType.APPLICATION_JSON );
    }

    private String createNextUriForPage(int size, int offset)
    {
        int totalNumber = Storage.getInstance( ).getTotalNumberOfUsers( );

        int nextOffset = Math.min( offset + size, totalNumber );

        URI location = uriInfo.getBaseUriBuilder( )
                .path( this.getClass( ) )
                .build( );

        String nextUri = location.toString() + "?size=" + size + "&offset=" + nextOffset;

        return Hyperlinks.linkHeader( nextUri, "next", MediaType.APPLICATION_JSON );
    }

    private String createPrevUriForPage(int size, int offset)
    {
        int prevOffset = Math.max(offset - size, 0);

        URI location = uriInfo.getBaseUriBuilder( )
                .path( this.getClass( ) )
                .build( );

        String prevUri = location.toString() + "?size=" + size + "&offset=" + prevOffset;

        return Hyperlinks.linkHeader( prevUri, "prev", MediaType.APPLICATION_JSON );
    }

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    public Response saveUser( User user )
    {
        try
        {
            Storage.getInstance( ).createUser( user );
        }
        catch ( Exception e )
        {
            throw new WebApplicationException( Response.Status.INTERNAL_SERVER_ERROR );
        }

        URI location = uriInfo.getAbsolutePathBuilder( )
                .path( user.getId( ) )
                .build( );

        return Response.created( location )
                .header( "Link", createGetAllUsersHeader( ) )
                .build( );
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path( "{id}" )
    public Response getUser( @PathParam( "id" ) String id )
    {
        User user = Storage.getInstance( ).getUser( id );

        if ( user == null )
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }

        return Response.ok( user )
                .header( "Link", createGetAllUsersHeader( ) )
                .header( "Link", createPutUserHeader( ) )
                .header( "Link", createDeleteUserHeader( ) )
                .cacheControl( CachingUtils.create30SecondsPrivateCaching( ) )
                .build( );
    }

    @DELETE
    @Path( "{id}" )
    public Response deleteUser( @PathParam( "id" ) String id )
    {
        try
        {
            Storage.getInstance( ).deleteUser( id );
        }
        catch ( Exception e )
        {
            throw new WebApplicationException( Response.Status.INTERNAL_SERVER_ERROR );
        }

        return Response.noContent( )
                .header( "Link", createGetAllUsersHeader( ) )
                .build( );
    }

    @PUT
    @Consumes( MediaType.APPLICATION_JSON )
    @Path( "{id}" )
    public Response updateUser( @PathParam( "id" ) String id, User user )
    {
        try
        {
            Storage.getInstance( ).updateUser( id, user );
        }
        catch ( Exception e )
        {
            throw new WebApplicationException( Response.Status.INTERNAL_SERVER_ERROR );
        }

        return Response.noContent( )
                .header( "Link", createGetAllUsersHeader( ) )
                .header( "Link", createGetSingleUserHeader( ) )
                .build( );
    }

    @GET
    @Path( "ping" )
    public String ping( )
    {
        return "OK";
    }

    private String createGetAllUsersHeader( )
    {
        URI location = uriInfo.getBaseUriBuilder( )
                .path( this.getClass( ) )
                .build( );

        String uri = location.toString() + "?size={SIZE}&offset={OFFSET}";

        return Hyperlinks.linkHeader( uri, "getAllUsers", MediaType.APPLICATION_JSON );
    }

    private String createPostUserHeader( )
    {
        URI location = uriInfo.getBaseUriBuilder( )
                .path( this.getClass( ) )
                .build( );

        return Hyperlinks.linkHeader( location.toString( ), "createUser", MediaType.APPLICATION_JSON );
    }

    private String createGetSingleUserHeader()
    {
        URI location = uriInfo.getRequestUri( );

        return Hyperlinks.linkHeader( location.toString( ), "getSingleUser", MediaType.APPLICATION_JSON );
    }

    private String createPutUserHeader()
    {
        URI location = uriInfo.getRequestUri( );

        return Hyperlinks.linkHeader( location.toString( ), "updateUser", MediaType.APPLICATION_JSON );
    }

    private String createDeleteUserHeader()
    {
        URI location = uriInfo.getRequestUri( );

        return Hyperlinks.linkHeader( location.toString( ), "deleteUser", MediaType.APPLICATION_JSON );
    }
}
