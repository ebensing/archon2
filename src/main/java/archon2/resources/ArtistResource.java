package archon2.resources;

import archon2.data.Artist;
import archon2.data.MongoResource;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

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
        Artist art = new Artist(true);

        art.setName(jo.getString("name"));


        Datastore db = this.mr.getDatastore("archon");

        Query<Artist> q = db.find(Artist.class);
        try {
            q = art.createQuery(new String[]{"name"}, db);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UpdateOperations<Artist> op = art.getUpdate(db);


        db.findAndModify(q, op, false, true);

        return art.toString();

    }
}
