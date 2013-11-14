package archon2.actors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: EJ
 * Date: 11/13/13
 * Time: 8:53 PM
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DispatchMethod {
    ActorMessageTypes value();
}
