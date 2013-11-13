package archon2.resources;

import archon2.data.Artist;
import archon2.data.MongoResource;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.json.JsonObject;
import javax.ws.rs.*;
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
        new Thread(new Runnable() {
            @Override
            public void run() {

                Artist art = new Artist(true);

                art.setName(jo.getString("name"));

                Query<Artist> q = ArtistResource.db.find(Artist.class);
                try {
                    q = art.createQuery(new String[]{"name"}, db);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                UpdateOperations<Artist> op = art.getUpdate(db);


                ArtistResource.db.findAndModify(q, op, false, true);

                asyncResponse.resume(art.toString());

            }
        }
        ).start();
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
