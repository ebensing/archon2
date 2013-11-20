package archon2;

import ArchonData.server.DataServer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static org.junit.Assert.assertEquals;

public class ArtistTest {

    private HttpServer server;
    private WebTarget target;
    private Registry reg = null;

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
        reg = LocateRegistry.createRegistry(3809);
        DataServer svr =  new DataServer();
        reg.bind("DataService", svr);
    }

    @After
    public void tearDown() throws Exception {
        target.path("/artist/delete/all").request().delete().readEntity(JsonObject.class);
        server.stop();
        UnicastRemoteObject.unexportObject(reg, true);
    }

    @Test
    public void artistCreate() {
        final String name = "epica";
        JsonObject js = Json.createObjectBuilder().add("name", name).build();
        JsonObject res1 = target.path("/artist/add").request().post(Entity.json(js)).readEntity(JsonObject.class);
        JsonObject res2 = target.path("/artist/add").request().post(Entity.json(js)).readEntity(JsonObject.class);

        assertEquals(res1.getString("name"), name);
        assertEquals(res2.getString("name"), name);

    }

    @Test
    public void artistCreateNew() {
        final String name = "wintersun";
        JsonObject js = Json.createObjectBuilder().add("name", name).build();
        JsonObject res1 = target.path("/artist/add/new").request().post(Entity.json(js)).readEntity(JsonObject.class);
        JsonObject res2 = target.path("/artist/add/new").request().post(Entity.json(js)).readEntity(JsonObject.class);

        assertEquals(res1.getString("name"), name);
        assertEquals(res2.getString("name"), name);

    }

    @Test
    public void getArtistByName() {

        final String name = "wintersun";
        JsonObject js = Json.createObjectBuilder().add("name", name).build();
        target.path("/artist/add/new").request().post(Entity.json(js)).readEntity(JsonObject.class);

        JsonObject res = target.path("/artist/byName/" + name).request().get().readEntity(JsonObject.class);

        assertEquals(res.getString("name"), name);
    }


    @Test
    public void getArtistById() {

        final String name = "wintersun";
        JsonObject js = Json.createObjectBuilder().add("name", name).build();
        JsonObject res = target.path("/artist/add/new").request().post(Entity.json(js)).readEntity(JsonObject.class);

        JsonObject res2 = target.path("/artist/byId/" + res.getString("id")).request().get().readEntity(JsonObject.class);

        assertEquals(res2.getString("name"), name);
    }

    @Test
    public void getAllArtists() {
        final String name1 = "wintersun";
        final String name2 = "dark tranquility";
        JsonObject js1 = Json.createObjectBuilder().add("name", name1).build();
        JsonObject res1 = target.path("/artist/add/new").request().post(Entity.json(js1)).readEntity(JsonObject.class);

        JsonObject js2 = Json.createObjectBuilder().add("name", name2).build();
        JsonObject res2 = target.path("/artist/add/new").request().post(Entity.json(js2)).readEntity(JsonObject.class);


        assertEquals(res1.getString("name"), name1);
        assertEquals(res2.getString("name"), name2);

        JsonObject artists = target.path("/artist/all").request().get().readEntity(JsonObject.class);

        assertEquals(artists.getJsonArray("data").size(), 2);
        assertEquals(artists.getJsonArray("data").getJsonObject(0).getString("name"), name2);
        assertEquals(artists.getJsonArray("data").getJsonObject(1).getString("name"), name1);
    }


    @Test
    public void testDeleteAllArtists() {
        final String name1 = "wintersun";
        final String name2 = "dark tranquility";
        JsonObject js1 = Json.createObjectBuilder().add("name", name1).build();
        JsonObject res1 = target.path("/artist/add/new").request().post(Entity.json(js1)).readEntity(JsonObject.class);

        JsonObject js2 = Json.createObjectBuilder().add("name", name2).build();
        JsonObject res2 = target.path("/artist/add/new").request().post(Entity.json(js2)).readEntity(JsonObject.class);


        assertEquals(res1.getString("name"), name1);
        assertEquals(res2.getString("name"), name2);

        JsonObject status = target.path("/artist/delete/all").request().delete().readEntity(JsonObject.class);

        assertEquals(status.getString("operation"), "completed");

        JsonObject artists = target.path("/artist/all").request().get().readEntity(JsonObject.class);
        assertEquals(artists.getJsonArray("data").size(), 0);
    }

    @Test
    public void testDeleteArtist() {
        final String name1 = "wintersun";
        final String name2 = "dark tranquility";
        JsonObject js1 = Json.createObjectBuilder().add("name", name1).build();
        JsonObject res1 = target.path("/artist/add/new").request().post(Entity.json(js1)).readEntity(JsonObject.class);

        JsonObject js2 = Json.createObjectBuilder().add("name", name2).build();
        JsonObject res2 = target.path("/artist/add/new").request().post(Entity.json(js2)).readEntity(JsonObject.class);


        assertEquals(res1.getString("name"), name1);
        assertEquals(res2.getString("name"), name2);

        JsonObject status = target.path("/artist/delete/" + res1.getString("id")).request().delete().readEntity(JsonObject.class);

        assertEquals(status.getString("operation"), "completed");

        JsonObject artists = target.path("/artist/all").request().get().readEntity(JsonObject.class);
        assertEquals(artists.getJsonArray("data").size(), 1);
        assertEquals(artists.getJsonArray("data").getJsonObject(0).getString("name"), name2);
    }

    @Test
    public void testUpdateArtist() {

        final String name1 = "wintersun";
        final String name2 = "dark tranquility";
        JsonObject js1 = Json.createObjectBuilder().add("name", name1).build();
        JsonObject res1 = target.path("/artist/add/new").request().post(Entity.json(js1)).readEntity(JsonObject.class);

        assertEquals(res1.getString("name"), name1);

        JsonObject update = Json.createObjectBuilder()
                .add("id", res1.getString("id"))
                .add("name", name2)
                .add("seen", 3).build();

        JsonObject res2 = target.path("/artist/update").request().put(Entity.json(update)).readEntity(JsonObject.class);

        assertEquals(res2.getString("name"), name2);
        assertEquals(res2.getInt("seen"), 3);
    }
}

