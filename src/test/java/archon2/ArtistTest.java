package archon2;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import archon2.data.Artist;
import archon2.data.MongoResource;
import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;

import static org.junit.Assert.assertEquals;

public class ArtistTest {

    private HttpServer server;
    private WebTarget target;
    private Datastore ds = MongoResource.INSTANCE.getDatastore("archon");

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);

        ds.delete(ds.createQuery(Artist.class));
    }

    @After
    public void tearDown() throws Exception {
        ds.delete(ds.createQuery(Artist.class));
        server.stop();
    }

    @Test
    public void artistCreate() {
        final String name = "epica";
        JsonObject js = Json.createObjectBuilder().add("name", name).build();
        JsonObject res1 = target.path("/artist/add").request().post(Entity.json(js)).readEntity(JsonObject.class);
        JsonObject res2 = target.path("/artist/add").request().post(Entity.json(js)).readEntity(JsonObject.class);

        assertEquals(res1.getString("name"), name);
        assertEquals(res2.getString("name"), name);

        long count = this.ds.find(Artist.class).field("name").equal(name).countAll();

        assertEquals(count, 1);
    }

    @Test
    public void artistCreateNew() {
        final String name = "wintersun";
        JsonObject js = Json.createObjectBuilder().add("name", name).build();
        JsonObject res1 = target.path("/artist/add/new").request().post(Entity.json(js)).readEntity(JsonObject.class);
        JsonObject res2 = target.path("/artist/add/new").request().post(Entity.json(js)).readEntity(JsonObject.class);

        assertEquals(res1.getString("name"), name);
        assertEquals(res2.getString("name"), name);

        long count = this.ds.find(Artist.class).field("name").equal(name).countAll();

        assertEquals(count, 2);

    }
}

