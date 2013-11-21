package archon2.resources;

import ArchonData.data.User;
import ArchonData.server.DataService;
import archon2.remote.DataResource;
import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.server.ContainerRequest;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 * User: EJ
 * Date: 11/20/13
 * Time: 8:13 PM
 */

@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    javax.inject.Provider<UriInfo> uriInfo;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        authenticate(containerRequestContext.getRequest());
    }

    private User authenticate(Request request) {
        String auth = ((ContainerRequest) request).getHeaderString(HttpHeaders.AUTHORIZATION);

        if (auth == null) {
            throw new WebApplicationException(401);
        }

        if (!auth.startsWith("Basic ")) {
            throw new WebApplicationException(400);
        }

        auth = auth.substring("Basic ".length());
        String[] values = Base64.decodeAsString(auth).split(":");

        if (values.length < 2) {
            throw new WebApplicationException(400);
        }

        String username = values[0];
        String pwd = values[1];

        if (pwd == null || username == null) {
            throw new WebApplicationException(400);
        }

        // do credential validation here
        DataService client = DataResource.getClient();

        User user = null;
        try {
            user = client.getUser(username, pwd);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (user == null) {
            throw new WebApplicationException(400);
        }

        return user;
    }
}
