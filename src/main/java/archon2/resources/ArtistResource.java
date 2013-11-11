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

    private Datastore db = MongoResource.INSTANCE.getDatastore("archon");

    @POST
    @Path("/add")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public String addArtist(final JsonObject jo) {
        Artist art = new Artist(true);

        art.setName(jo.getString("name"));

        Query<Artist> q = this.db.find(Artist.class);
        try {
            q = art.createQuery(new String[]{"name"}, db);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UpdateOperations<Artist> op = art.getUpdate(db);


        this.db.findAndModify(q, op, false, true);

        return art.toString();

    }

    @POST
    @Path("/add/new")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public String addNewArtist(final JsonObject req) {
        Artist art = new Artist();

        art.setName(req.getString("name"));
        this.db.save(art);

        return art.toString();
    }
}
