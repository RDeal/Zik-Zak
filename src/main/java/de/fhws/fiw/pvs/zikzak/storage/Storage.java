package de.fhws.fiw.pvs.zikzak.storage;

import de.fhws.fiw.pvs.zikzak.models.Comment;
import de.fhws.fiw.pvs.zikzak.models.Message;
import de.fhws.fiw.pvs.zikzak.models.User;

import java.util.*;

/**
 * Created by Robin on 21.06.2017.
 */
public class Storage {
    private static Storage INSTANCE;

    public static Storage getInstance()
    {
        if ( INSTANCE == null )
        {
            INSTANCE = new Storage( );
        }

        return INSTANCE;
    }

    private static long ID_COUNTER = 0L;

    private static long getNextID()
    {
        return ID_COUNTER++;
    }

    private Map<String, User> userStorage;

    private Map<Long, Message> messageStorage;

    private Map<String, Set<Long>> userToMessageMap;

    private Storage( )
    {
        userStorage = new HashMap<>( );
        messageStorage = new HashMap<>( );
        userToMessageMap = new HashMap<>( );
    }

    public int getTotalNumberOfUsers()
    {
        return userStorage.size( );
    }

    public int getTotalNumberOfMessages()
    {
        return Math.min( messageStorage.size( ), 100 );
    }

    public int getTotalNumberOfMessagesOfUser( String userid )
    {
        Set<Long> messageIds = userToMessageMap.get( userid );

        return messageIds == null ? 0 : messageIds.size( );
    }

    public int getTotalNumberOfCommentsOfMessage( long messageId )
    {
        Message message = messageStorage.get( messageId );

        int amount;
        if ( message != null )
        {
            amount = message.getComments( ).size( );
        }
        else
        {
            amount = 0;
        }

        return amount;
    }

    public String createUser( User user )
    {
        UUID uuid = UUID.randomUUID( );

        user.setId( uuid.toString( ) );

        userStorage.put( user.getId( ), user );

        return user.getId( );
    }

    public User getUser( String id )
    {
        return userStorage.get( id );
    }

    public List<User> getUsers(int size, int offset )
    {
        List<User> users = new ArrayList<>( );

        int count = 0;

        for ( User user : userStorage.values( ) )
        {
            if ( count >= offset && users.size( ) <= size )
            {
                users.add( user );
            }

            count++;
        }

        return users;
    }

    public void deleteUser( String id )
    {
        userStorage.remove( id );
    }

    public User updateUser( String id, User user )
    {
        user.setId( id );

        userStorage.put( id, user );

        return user;
    }

    public long createMessage( Message message )
    {
        long id = getNextID( );

        message.setId( id );

        messageStorage.put( id, message );

        return id;
    }

    public Message getMessage( long id )
    {
        return messageStorage.get( id );
    }

    public List<Message> getMessages( int size, int offset, String orderBy )
    {
        List<Message> messages = new ArrayList<>( );

        int count = 0;

        for ( Message message : getMessagesOrderedBy( orderBy ) )
        {
            if ( count >= offset && messages.size( ) <= size )
            {
                messages.add( message );
            }

            count++;

            if ( count == 100 )
            {
                break;
            }
        }

        return messages;
    }

    private List<Message> getMessagesOrderedBy( String orderBy )
    {
        List<Message> messages;

        switch ( orderBy )
        {
            case "upvotes":
                messages = getMessagesByUpvotes( );
                break;
            case "comments":
                messages = getMessagesByComments( );
                break;
            default:
                messages = getMessagesByDate( );
        }

        return messages;
    }

    private List<Message> getMessagesByUpvotes( )
    {
        List<Message> messages = new ArrayList<>( messageStorage.values( ) );

        messages.sort( Comparator.comparingInt( ( Message message ) -> ( message.getUpVotes( ) ) ).reversed( ) );

        return messages;
    }

    private List<Message> getMessagesByComments()
    {
        List<Message> messages = new ArrayList<>( messageStorage.values( ) );

        messages.sort(
                Comparator.comparingInt( ( Message message ) -> ( message.getComments( ).size( ) ) ).reversed( ) );

        return messages;
    }

    private List<Message> getMessagesByDate()
    {
        List<Message> messages = new ArrayList<>( messageStorage.values( ) );

        messages.sort( Comparator.comparing( ( Message message ) -> ( message.getCreationDate( ) ) ).reversed( ) );

        return messages;
    }

    public void deleteMessage( long id )
    {
        messageStorage.remove( id );
    }

    public Message updateMessage( long id, Message message )
    {
        message.setId( id );

        messageStorage.put( id, message );

        return message;
    }

    public void addMessageToUser( String userId, long messageId )
    {
        Set<Long> messages = userToMessageMap.get( userId );

        if ( messages == null )
        {
            messages = new HashSet<>( );
        }

        messages.add( messageId );

        userToMessageMap.put( userId, messages );
    }

    public void removeMessageFromUser( String userId, long messageId )
    {
        Set<Long> messages = userToMessageMap.get( userId );

        if ( messages != null )
        {
            messages.remove( messageId );
        }

    }

    public boolean ownsUserMessage( String userId, long id )
    {
        Set<Long> messageIds = userToMessageMap.get( userId );

        return messageIds.contains( id );
    }

    public List<Message> getMessagesOfUser( String userId, int size, int offset )
    {
        List<Message> messages = new ArrayList<>( );
        Set<Long> messageIds = userToMessageMap.get( userId );

        if ( messageIds != null )
        {
            int count = 0;

            for ( Long messageId : messageIds )
            {
                if ( count >= offset && messages.size( ) <= size )
                {
                    messages.add( messageStorage.get( messageId ) );
                }
                count++;
            }
        }

        return messages;
    }

    public List<Comment> getCommentsOfMessage( long id, int size, int offset )
    {
        List<Comment> comments;

        Message message = messageStorage.get( id );

        if ( message != null )
        {
            comments = message.getComments( );
        }
        else
        {
            comments = new ArrayList<>( );
        }

        List<Comment> filteredComments = new ArrayList<>( );

        int count = 0;
        for ( Comment comment : comments )
        {
            if ( count >= offset && filteredComments.size( ) <= size )
            {
                filteredComments.add( comment );
            }
            count++;
        }

        return filteredComments;
    }

    public long createComment( long id, Comment comment )
    {
        long commentId = getNextID( );

        comment.setId( id );

        messageStorage.get( id ).addComment( comment );

        return commentId;
    }

    public void deleteComment( long messageid, long id )
    {
        Message message = messageStorage.get( messageid );

        message.getComments( ).removeIf( comment -> comment.getId( ) == id );
    }
}
