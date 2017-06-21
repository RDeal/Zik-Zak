package de.fhws.fiw.pvs.zikzak.utils;

/**
 * Created by Robin on 21.06.2017.
 */
public class Hyperlinks
{
    public static String linkHeader( String uri, String rel, String mediaType )
    {
        StringBuilder sb = new StringBuilder( );
        sb.append( '<' ).append( uri ).append( ">;" );
        sb.append( "rel" ).append( "=\"" ).append( rel ).append( "\"" );
        if ( mediaType != null && !mediaType.isEmpty( ) )
        {
            sb.append( ";" );
            sb.append( "type" ).append( "=\"" ).append( mediaType ).append( "\"" );
        }

        return sb.toString( );
    }
}
