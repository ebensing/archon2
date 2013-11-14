package archon2.resources;

import archon2.actors.ActorController;
import archon2.actors.ActorMessage;
import archon2.actors.ArtistMessageTypes;
import archon2.data.Artist;
import org.codehaus.jackson.map.ObjectMapper;

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

    private ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Path("/add")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public void addArtist(final String jo, @Suspended final AsyncResponse asyncResponse) {
        Artist art = null;
        try {
            art = objectMapper.readValue(jo, Artist.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // create the message and send it to our actor
        ActorMessage msg = new ActorMessage(ArtistMessageTypes.CREATE, art, asyncResponse);
        ActorController.artistActor.tell(msg, ActorController.artistActor);
    }

    @POST
    @Path("/add/new")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public void addNewArtist(final String req, @Suspended final AsyncResponse asyncResponse) {
        Artist art = null;
        try {
            art = objectMapper.readValue(req, Artist.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ActorMessage msg = new ActorMessage(ArtistMessageTypes.FORCECREATE, art, asyncResponse);
        ActorController.artistActor.tell(msg, ActorController.artistActor);
    }
}
