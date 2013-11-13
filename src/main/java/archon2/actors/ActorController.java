package archon2.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Created with IntelliJ IDEA.
 * User: EJ
 * Date: 11/13/13
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActorController {

    public final static ActorSystem system = ActorSystem.create("ActorSystem");
    public final static ActorRef artistActor = system.actorOf(Props.create(ArtistActor.class));
}
