package helo.niti.movies.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import helo.niti.movies.model.Movie;
import helo.niti.movies.model.Review;
import helo.niti.movies.model.Trailer;

@Dao
public interface MoviesDao {
    @Insert
    void insertMovies(Movie... movies);

    @Insert
    void insertReviews(List<Review> reviews);

    @Insert
    void insertTrailers(List<Trailer> trailers);

    @Delete
    void deleteMovies(Movie... movies);

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> findMovies();

    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<Movie> findMovie(long id);

    @Query("SELECT * FROM trailers WHERE movie_id = :movieId")
    LiveData<List<Trailer>> findTrailers(long movieId);

    @Query("SELECT * FROM reviews WHERE movie_id = :movieId")
    LiveData<List<Review>> findReviews(long movieId);
}
