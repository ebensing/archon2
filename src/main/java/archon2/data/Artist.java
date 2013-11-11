package archon2.data;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.json.Json;
import javax.json.JsonObject;
import java.lang.reflect.Field;
import java.util.*;


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
    private HashMap<String, Boolean> changed = new HashMap<String, Boolean>();

    public Artist() {
       this(false);
    }

    public Artist(boolean markDirty) {
        this.name = "";
        this.seen = 0;
        this.description = "";
        this.genre = "";

        if (markDirty) {
            this.changed.put("name", true);
            this.changed.put("seen", true);
            this.changed.put("genre", true);
            this.changed.put("description", true);
        }
    }

    private void markClean() {
        this.changed.put("name", false);
        this.changed.put("seen", false);
        this.changed.put("genre", false);
        this.changed.put("description", false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.changed.put("name", true);
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.changed.put("seen", true);
        this.seen = seen;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.changed.put("description", true);
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.changed.put("genre", true);
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

    public Query<Artist> createQuery(String[] fields, Datastore db) throws NoSuchFieldException, IllegalAccessException {
        Query<Artist> q = db.find(Artist.class);

        Class<?> c = this.getClass();


        for(String f : fields) {
           Field field = c.getDeclaredField(f);
           field.setAccessible(true);

           q = q.field(f).equal(field.get(this));
        }

        return q;
    }

    public UpdateOperations<Artist> getUpdate(Datastore db) {
        UpdateOperations<Artist> op = db.createUpdateOperations(Artist.class);

        Class<?> c = this.getClass();


        try {
            for (Map.Entry<String, Boolean> item : this.changed.entrySet()) {
                if (item.getValue()) {
                    Field f = c.getDeclaredField(item.getKey());
                    f.setAccessible(true);

                    op = op.set(item.getKey(), f.get(this));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.markClean();

        return op;
    }
}
