package de.fhws.fiw.pvs.zikzak.api;

import de.fhws.fiw.pvs.zikzak.filter.AuthFilter;

import com.owlike.genson.GensonBuilder;
import com.owlike.genson.ext.jaxrs.GensonJaxRSFeature;
import org.apache.catalina.filters.CorsFilter;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Robin on 21.06.2017.
 */
@ApplicationPath( "api" )
public class Application extends ResourceConfig
{
    public Application( )
    {
        super( );
        registerClasses( getServiceClasses( ) );
        packages( "org.glassfish.jersey.examples.linking" );
        register( DeclarativeLinkingFeature.class );
        register( MultiPartFeature.class );
        register( CorsFilter.class );
        register( AuthFilter.class );
        register( new GensonJaxRSFeature( ).use(
                new GensonBuilder( ).setSkipNull( true )
                        .useIndentation( true )
                        .useDateAsTimestamp( false )
                        .useDateFormat( new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" ) )
                        .create( ) ) );
    }

    private Set<Class<?>> getServiceClasses()
    {
        Set<Class<?>> returnValue = new HashSet<>( );

        returnValue.add( DispatcherService.class );
        returnValue.add( UserService.class );
        returnValue.add( MessageSecondaryService.class );
        returnValue.add( MessageService.class );
        returnValue.add( CommentService.class );

        return returnValue;
    }
}
