package helo.niti.movies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/** Represents a movie review */
@Entity(foreignKeys = @ForeignKey(entity = Movie.class, parentColumns = "id", childColumns = "movie_id", onDelete = CASCADE),
        tableName = "reviews")
public class Review {
    public String author;
    public String content;
    @ColumnInfo(name = "movie_id")
    public long movieId;
    @PrimaryKey
    @NonNull
    public String id;

    public Review(String author, String content, long movieId, String id) {
        this.author = author;
        this.content = content;
        this.movieId = movieId;
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
