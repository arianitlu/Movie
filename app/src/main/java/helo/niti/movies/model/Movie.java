package helo.niti.movies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey
    private long id;
    private String title;
    @ColumnInfo(name = "original_title")
    private String originalTitle;
    private String overview;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    private double rating;
    @ColumnInfo(name = "release_date")
    private String releaseDate;


    public Movie(long id, String title, String originalTitle, String overview, String posterPath, double rating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }
}
