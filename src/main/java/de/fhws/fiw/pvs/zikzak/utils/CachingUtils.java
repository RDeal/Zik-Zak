package de.fhws.fiw.pvs.zikzak.utils;

import javax.ws.rs.core.CacheControl;

/**
 * Created by Robin on 21.06.2017.
 */
public class CachingUtils
{
    public static CacheControl create2SecondsPublicCaching()
    {
        CacheControl cacheControl = new CacheControl( );
        cacheControl.setPrivate( false );
        cacheControl.setMaxAge( 2 );

        return cacheControl;
    }

    public static CacheControl create30SecondsPublicCaching()
    {
        CacheControl cacheControl = new CacheControl( );
        cacheControl.setPrivate( false );
        cacheControl.setMaxAge( 30 );

        return cacheControl;
    }

    public static CacheControl create60SecondsPublicCaching()
    {
        CacheControl cacheControl = new CacheControl( );
        cacheControl.setPrivate( false );
        cacheControl.setMaxAge( 60 );

        return cacheControl;
    }

    public static CacheControl create30SecondsPrivateCaching()
    {
        CacheControl cacheControl = new CacheControl( );
        cacheControl.setPrivate( true );
        cacheControl.setMaxAge( 30 );

        return cacheControl;
    }
}
