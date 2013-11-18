package archon2.resources;

import ArchonData.main;
import ArchonData.server.DataService;
import org.glassfish.jersey.server.ManagedAsync;
import util.JsonHelper;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import ArchonData.data.Artist;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
}
