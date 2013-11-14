package archon2.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.Creator;

/**
 * User: EJ
 * Date: 11/13/13
 * Time: 1:13 PM
 */

public class ActorController {

    public final static ActorSystem system = ActorSystem.create("ActorSystem");

    public final static ActorRef artistActor = system.actorOf(Props.create(new ArtistActorCreator()));
}

class ArtistActorCreator implements Creator<ArtistActor> {
    @Override
    public ArtistActor create() throws Exception {
        return new ArtistActor();
    }
}
