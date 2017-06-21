package de.fhws.fiw.pvs.zikzak.converter;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;
import javax.ws.rs.core.Link;

/**
 * Created by Robin on 21.06.2017.
 */
public class LinkConverter implements Converter<Link>
{
    public LinkConverter( )
    {
    }

    public void serialize(Link link, ObjectWriter objectWriter, Context context) throws Exception
    {
        objectWriter.writeName(link.getRel( ));
        objectWriter.beginObject( );
        objectWriter.writeString("href", this.replaceCharacters(link.getUri().toASCIIString( )));
        objectWriter.writeString("rel", link.getRel( ));
        if(link.getType( ) != null && !link.getType( ).isEmpty( ))
        {
            objectWriter.writeString("type", link.getType( ));
        }

        objectWriter.endObject( );
    }

    public Link deserialize(ObjectReader objectReader, Context context) throws Exception
    {
        Link returnValue = null;
        objectReader.beginObject( );

        while(objectReader.hasNext( ))
        {
            objectReader.next( );
            if("href".equals(objectReader.name( )))
            {
                String link = objectReader.valueAsString( );
                returnValue = Link.fromUri(link).build( );
            }
        }

        objectReader.endObject( );
        return returnValue;
    }

    private String replaceCharacters(String body)
    {
        return body.replace("%3F", "?").replaceAll("%7B", "{").replaceAll("%7D", "}");
    }
}
