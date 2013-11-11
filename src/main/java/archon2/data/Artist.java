package archon2.data;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.json.Json;
import javax.json.JsonObject;


/**
 * User: EJ
 * Date: 11/5/13
 * Time: 10:48 PM
 */

@Entity
public class Artist {

    @Id private ObjectId id;

    private String name;
    private int seen;
    private String description;
    private String genre;

    public Artist() {
        this.name = "";
        this.seen = 0;
        this.description = "";
        this.genre = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        JsonObject jart = Json.createObjectBuilder()
                .add("name", this.name)
                .add("seen", this.seen)
                .add("description", this.description)
                .add("genre", this.genre).build();

        return jart.toString();
    }
}
