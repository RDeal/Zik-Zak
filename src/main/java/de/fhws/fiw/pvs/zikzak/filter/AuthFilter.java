package de.fhws.fiw.pvs.zikzak.filter;

import de.fhws.fiw.pvs.zikzak.storage.Storage;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by Robin on 21.06.2017.
 */
@Provider
@UserAuthorization
public class AuthFilter implements ContainerRequestFilter
{
    @Override
    public void filter( ContainerRequestContext requestContext ) throws IOException
    {
        final String authHeader = requestContext.getHeaderString( HttpHeaders.AUTHORIZATION );
        if ( authHeader == null )
        {
            requestContext.abortWith( Response.status( Response.Status.UNAUTHORIZED )
                    .header( HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"zikzak.com\"" )
                    .entity( "Page requires login." )
                    .build( ) );
        }
        else
        {
            final String withoutBearer = authHeader.replaceFirst( "[Bb]earer ", "" );

            if ( Storage.getInstance( ).getUser( withoutBearer ) == null )
            {
                requestContext.abortWith( Response.status( Response.Status.UNAUTHORIZED )
                        .header( HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"zikzak.com\"" )
                        .entity( "Page requires login." )
                        .build( ) );
            }

            requestContext.setProperty( "userid", withoutBearer );
        }
    }
}
