package archon2.actors;

import akka.actor.UntypedActor;
import archon2.data.Artist;
import archon2.data.MongoResource;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * User: EJ
 * Date: 11/13/13
 * Time: 1:03 PM
 */
public class ArtistActor extends UntypedActor {

    private Datastore db = MongoResource.INSTANCE.getDatastore("archon");

    private static final HashMap<ArtistMessageTypes, Method> dispatch;
    static {
        HashMap<ArtistMessageTypes, Method> tmp = new HashMap<ArtistMessageTypes, Method>();
        Method[] methods = ArtistActor.class.getMethods();

        for(Method m : methods) {
            if (m.isAnnotationPresent(DispatchMethod.class)) {
                DispatchMethod dispatchMethod = m.getAnnotation(DispatchMethod.class);
                try {
                    tmp.put(dispatchMethod.value(), m);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        dispatch = tmp;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ActorMessage) {
            ActorMessage actorMessage = (ActorMessage) message;
            dispatch.get(actorMessage.type).invoke(this, actorMessage);
        } else {
            unhandled(message);
        }
    }

    @SuppressWarnings("unused")
    @DispatchMethod(ArtistMessageTypes.CREATE)
    public void createArtist(ActorMessage message) {
        Query<Artist> q = db.find(Artist.class);
        try {
            q = message.artist.createQuery(new String[]{"name"}, db);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UpdateOperations<Artist> op = message.artist.getUpdate(db);
        db.findAndModify(q, op, false, true);
        message.asyncResponse.resume(message.artist.toString());
    }

    @SuppressWarnings("unused")
    @DispatchMethod(ArtistMessageTypes.FORCECREATE)
    public void forceCreateArtist(ActorMessage message) {
        db.save(message.artist);
        message.asyncResponse.resume(message.artist.toString());
    }
}
