package archon2;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "https://localhost:8080/api/";
    public static final String KEYSTORE_SERVER_FILE = "./keystore_server";
    public static final String KEYSTORE_SERVER_PWD = "testing";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {

        SSLContextConfigurator sslContextConfigurator = new SSLContextConfigurator();
        sslContextConfigurator.setKeyStoreFile(KEYSTORE_SERVER_FILE);
        sslContextConfigurator.setKeyStorePass(KEYSTORE_SERVER_PWD);

        // create a resource config that scans for JAX-RS resources and providers
        // in archon2 package
        final ResourceConfig rc = new ResourceConfig()
                .packages("archon2");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI),
                rc,
                true,
                new SSLEngineConfigurator(sslContextConfigurator).setClientMode(false).setNeedClientAuth(false));
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}

