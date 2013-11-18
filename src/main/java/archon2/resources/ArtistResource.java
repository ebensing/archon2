package archon2.resources;

import archon2.data.Artist;
import archon2.data.MongoResource;
import org.glassfish.jersey.server.ManagedAsync;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import util.JsonHelper;

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

    private Datastore db = MongoResource.INSTANCE.getDatastore("archon");

    @POST
    @Path("/add")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void addArtist(final String jo, @Suspended final AsyncResponse asyncResponse) {
        Artist art = JsonHelper.parseJson(jo, Artist.class);
        Query<Artist> q = db.find(Artist.class);
        try {
            q = art.createQuery(new String[]{"name"}, db);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UpdateOperations<Artist> op = art.getUpdate(db);
        db.findAndModify(q, op, false, true);
        asyncResponse.resume(art.toString());
    }

    @POST
    @Path("/add/new")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void addNewArtist(final String req, @Suspended final AsyncResponse asyncResponse) {
        Artist art = JsonHelper.parseJson(req, Artist.class);
        db.save(art);
        asyncResponse.resume(art.toString());
    }
}
