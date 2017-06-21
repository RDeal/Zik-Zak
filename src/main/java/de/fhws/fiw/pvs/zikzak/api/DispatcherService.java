package de.fhws.fiw.pvs.zikzak.api;

import de.fhws.fiw.pvs.zikzak.utils.CachingUtils;
import de.fhws.fiw.pvs.zikzak.utils.Hyperlinks;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by Robin on 21.06.2017.
 */
public class DispatcherService {
    @Context
    UriInfo uriInfo;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response getBase( )
    {
        String userUri = uriInfo.getAbsolutePathBuilder( ).path( "users" ).build( ).toString( );
        String messageUri = uriInfo.getAbsolutePathBuilder( ).path( "messages" ).build( ).toString( );

        return Response.ok( )
                .header( "Link", Hyperlinks.linkHeader( userUri, "createUser", MediaType.APPLICATION_JSON ) )
                .header( "Link", Hyperlinks.linkHeader( messageUri, "getAllMessages", MediaType.APPLICATION_JSON ) )
                .cacheControl( CachingUtils.create60SecondsPublicCaching( ) )
                .build( );
    }
}
