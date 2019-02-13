package helo.niti.movies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import helo.niti.movies.model.Movie;
import helo.niti.movies.model.Review;
import helo.niti.movies.model.Trailer;

public final class MoviesJsonUtils {

    private MoviesJsonUtils(){
    }

    /**
     * Extracts Json movies into a list {@link Movie} objects
     *
     * @param responseJson response from themoviedb.org
     * @return List of {@link Movie} objects
     * @throws JSONException
     */
    public static List<Movie> extractMovies(String responseJson) throws JSONException {
        List<Movie> movies = new ArrayList<>();

        JSONObject responseObject = new JSONObject(responseJson);

        JSONArray movieArray = responseObject.getJSONArray("results");
        for(int i = 0; i < movieArray.length(); i++){
            JSONObject movieObject = movieArray.getJSONObject(i);
            Movie movie = extractMovie(movieObject);
            movies.add(movie);
        }

        return movies;
    }

    public static Movie extractMovie(JSONObject movieObject) throws JSONException {

        String title = movieObject.getString("title");
        String originalTitle = movieObject.getString("original_title");
        String overview = movieObject.getString("overview");
        String posterPath = movieObject.getString("poster_path");
        double rating = movieObject.getInt("vote_average");
        String releaseDate = movieObject.getString("release_date");
        long id = movieObject.getLong("id");

        Movie movie = new Movie(id, title, originalTitle, overview, posterPath, rating, releaseDate);

        return movie;
    }

    public static List<Review> extractReviews(String responseJson) throws JSONException {
        List<Review> reviews = new ArrayList<>();

        JSONObject responseObject = new JSONObject(responseJson);

        JSONArray reviewArray = responseObject.getJSONArray("results");
        for(int i = 0; i < reviewArray.length(); i++){
            JSONObject reviewObject = reviewArray.getJSONObject(i);
            String author = reviewObject.getString("author");
            String content = reviewObject.getString("content");
            String reviewId = reviewObject.getString("id");
            long movieId = responseObject.getLong("id");
            reviews.add(new Review(author, content, movieId, reviewId));
        }

        return reviews;
    }

    public static List<Trailer> extractTrailers(String responseJson) throws JSONException {
        List<Trailer> trailers = new ArrayList<>();

        JSONObject responseObject = new JSONObject(responseJson);

        JSONArray trailerArray = responseObject.getJSONArray("results");
        for(int i = 0; i < trailerArray.length(); i++){
            JSONObject trailerObject = trailerArray.getJSONObject(i);
            String name = trailerObject.getString("name");
            String trailerId = trailerObject.getString("key");
            String site = trailerObject.getString("site");
            long movieId = responseObject.getLong("id");
            trailers.add(new Trailer(name, site, movieId, trailerId));
        }

        return trailers;
    }
}
