package helo.niti.movies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/** Represents a movie trailer */
@Entity(foreignKeys = @ForeignKey(entity = Movie.class, parentColumns = "id", childColumns = "movie_id", onDelete = CASCADE),
        tableName = "trailers")
public class Trailer {
    private static final String TAG = Trailer.class.getSimpleName();

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com";
    private static final String ACTION_WATCH = "watch";
    private static final String PARAMETER_VIDEO = "v";

    public String name;
    public String site;
    @ColumnInfo(name = "movie_id")
    public long movieId;
    @PrimaryKey
    @NonNull
    public String trailerId;

    public Trailer(String name, String site, long movieId, String trailerId) {
        this.name = name;
        this.site = site;
        this.movieId = movieId;
        this.trailerId = trailerId;
    }

    public String getName() {
        return name;
    }

    public Uri getTrailerUri(){
        if(!"YouTube".equals(site)) return null;
        return Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendPath(ACTION_WATCH)
                .appendQueryParameter(PARAMETER_VIDEO, trailerId)
                .build();
    }
}
