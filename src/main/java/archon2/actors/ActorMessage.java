package archon2.actors;

import archon2.data.Artist;

import javax.ws.rs.container.AsyncResponse;

/**
 * User: EJ
 * Date: 11/13/13
 * Time: 1:17 PM
 */


public class ActorMessage {

    public final ArtistMessageTypes type;
    public final AsyncResponse asyncResponse;
    public final Artist artist;

    public ActorMessage(ArtistMessageTypes type, Artist artist, AsyncResponse response) {
        this.type = type;
        this.artist = artist;
        this.asyncResponse = response;
    }

}
