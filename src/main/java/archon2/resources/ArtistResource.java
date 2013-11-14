package archon2.resources;

import archon2.actors.ActorController;
import archon2.actors.ActorMessage;
import archon2.actors.ActorMessageTypes;
import archon2.data.Artist;
import archon2.data.MongoResource;
import org.mongodb.morphia.Datastore;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

/**
 * User: EJ
 * Date: 11/10/13
 * Time: 7:08 PM
 */
@Path("/artist")
public class ArtistResource {

    private static Datastore db = MongoResource.INSTANCE.getDatastore("archon");


    @POST
    @Path("/add")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public void addArtist(final JsonObject jo, @Suspended final AsyncResponse asyncResponse) {
        // json parsing, this can be factored out for an actual Json lib
        Artist art = new Artist(true);
        art.setName(jo.getString("name"));

        // create the message and send it to our actor
        ActorMessage msg = new ActorMessage(ActorMessageTypes.CREATE, art, asyncResponse);
        ActorController.artistActor.tell(msg, ActorController.artistActor);
    }

    @POST
    @Path("/add/new")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public String addNewArtist(final JsonObject req) {
        Artist art = new Artist();

        art.setName(req.getString("name"));
        db.save(art);

        return art.toString();
    }
}
