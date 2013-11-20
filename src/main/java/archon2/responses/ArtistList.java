package archon2.responses;

import ArchonData.data.Artist;

import java.util.List;

/**
 * User: EJ
 * Date: 11/19/13
 * Time: 12:15 AM
 */
public class ArtistList {

    public List<Artist> data;

    public ArtistList(List<Artist> data) {
        this.data = data;
    }
}
