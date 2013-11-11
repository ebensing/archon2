package archon2.resources;

import archon2.data.Artist;
import archon2.data.MongoResource;
import org.mongodb.morphia.Datastore;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * User: EJ
 * Date: 11/10/13
 * Time: 7:08 PM
 */
@Path("/artist")
public class ArtistResource {

    private MongoResource mr = MongoResource.INSTANCE;

    @POST
    @Path("/add")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public String addArtist(final JsonObject jo) {
        Artist art = new Artist();

        art.setName(jo.getString("name"));

        Datastore db = this.mr.getDatastore("archon");

        db.save(art);

        return art.toString();

    }
}
