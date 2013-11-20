package archon2.resources;

import ArchonData.main;
import ArchonData.server.DataService;
import archon2.responses.ArtistList;
import org.bson.types.ObjectId;
import org.glassfish.jersey.server.ManagedAsync;
import util.JsonHelper;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import ArchonData.data.Artist;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

/**
 * User: EJ
 * Date: 11/10/13
 * Time: 7:08 PM
 */
@Path("/artist")
public class ArtistResource {

    private DataService db;

    public ArtistResource() {
        try {
            Registry reg = LocateRegistry.getRegistry(main.port);
            this.db = (DataService) reg.lookup("DataService");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }

    @POST
    @Path("/add")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void addArtist(final String jo, @Suspended final AsyncResponse asyncResponse) {
        Artist art = JsonHelper.parseJson(jo, Artist.class);
        try {
            db.addArtist(art);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        asyncResponse.resume(JsonHelper.stringify(art));
    }

    @POST
    @Path("/add/new")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void addNewArtist(final String req, @Suspended final AsyncResponse asyncResponse) {
        Artist art = JsonHelper.parseJson(req, Artist.class);
        try {
            art = db.forceAddArtist(art);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        asyncResponse.resume(JsonHelper.stringify(art));
    }

    @GET
    @Path("/byName/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getArtistByName(@PathParam("name") String name, @Suspended final AsyncResponse asyncResponse) {
        Artist artist = null;

        try {
            artist = db.getArtist(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (artist == null) {
            asyncResponse.resume("{ \"error\" : \"Not Found\" }");
        } else {
            asyncResponse.resume(JsonHelper.stringify(artist));
        }
    }

    @GET
    @Path("/byId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getArtistById(@PathParam("id") String id, @Suspended final AsyncResponse asyncResponse) {
        Artist artist = null;

        try {
            ObjectId objectId = new ObjectId(id);
            artist = db.getArtist(objectId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (artist == null) {
            asyncResponse.resume("{ \"error\" : \"Not Found\" }");
        } else {
            asyncResponse.resume(JsonHelper.stringify(artist));
        }
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getAllArtists(@Suspended final AsyncResponse asyncResponse) {
        List<Artist> artists = null;

        try {
            artists = db.getArtists("name");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (artists == null) {
            asyncResponse.resume("{ \"error\" : \"Not Found\" }");
        } else {
            ArtistList artistList = new ArtistList(artists);
            asyncResponse.resume(JsonHelper.stringify(artistList));
        }
    }

    @DELETE
    @Path("/delete/all")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void deleteAllArtists(@Suspended final AsyncResponse asyncResponse) {

        try {
            db.deleteAllArtists();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        asyncResponse.resume("{ \"operation\" : \"completed\" }");
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void deleteArtist(@PathParam("id") String id, @Suspended final AsyncResponse asyncResponse) {

        ObjectId objectId = new ObjectId(id);
        Artist artist = new Artist(objectId);

        try {
            db.deleteArtist(artist);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        asyncResponse.resume("{ \"operation\" : \"completed\" }");
    }

    @PUT
    @Path("/update")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void updateArtist(final String rawJson, @Suspended final AsyncResponse asyncResponse) {
        Artist artist = JsonHelper.parseJson(rawJson, Artist.class);

        try {
            db.updateArtist(artist);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        asyncResponse.resume(JsonHelper.stringify(artist));
    }
}
