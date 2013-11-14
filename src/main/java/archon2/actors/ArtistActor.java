package archon2.actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import archon2.data.Artist;
import archon2.data.MongoResource;
import archon2.resources.ArtistResource;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.lang.reflect.Method;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: EJ
 * Date: 11/13/13
 * Time: 1:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArtistActor extends UntypedActor {

    private Datastore db = MongoResource.INSTANCE.getDatastore("archon");

    private static final HashMap<ActorMessageTypes, Method> dispatch;
    static {
        HashMap<ActorMessageTypes, Method> tmp = new HashMap<ActorMessageTypes, Method>();

        Class[] params = new Class[] { ActorMessage.class};

        Method[] methods = ArtistActor.class.getMethods();

        for(Method m : methods) {
            if (m.isAnnotationPresent(DispatchMethod.class)) {
                DispatchMethod dispatchMethod = m.getAnnotation(DispatchMethod.class);
                try {
                    tmp.put(dispatchMethod.type(), m);
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

    @DispatchMethod(type=ActorMessageTypes.CREATE)
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
}
