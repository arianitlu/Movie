package helo.niti.movies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3";

    private static final String PATH_DISCOVER = "discover";

    private static final String PATH_MOVIES = "movie";
    private static final String PATH_TRAILERS = "videos";
    private static final String PATH_REVIEWS = "reviews";


    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";

    /** Values for the URI. Place where the API key must me inserted*/
    private static final String apiKey = "1afe9082c06f2e12037f4958f570ee76";

    /** Keys for URI*/
    private static final String API_KEY_PARAM = "api_key";

    private static final String WITH_GENRES = "with_genres";
    private static final String MOVIE_PAGE = "page";

    private NetworkUtils(){

    }

    //https://api.themoviedb.org/3/discover/movie?with_genres=18&api_key=1afe9082c06f2e12037f4958f570ee76
    //https://api.themoviedb.org/3/discover/movie?with_genres=18&api_key=1afe9082c06f2e12037f4958f570ee76&page=2

    // Search by genres
    public static URL buildMoviesGenreUrl(String genre){
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(PATH_DISCOVER)
                .appendPath(PATH_MOVIES)
                .appendQueryParameter(WITH_GENRES, genre)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URL is " + url);

        return url;
    }

    public static URL buildMoviesGenreUrlPage(String genre, String page){
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(PATH_DISCOVER)
                .appendPath(PATH_MOVIES)
                .appendQueryParameter(WITH_GENRES, genre)
                .appendQueryParameter(MOVIE_PAGE, page)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URL is " + url);

        return url;
    }

    /** Method for building request URL for movies*/
    public static URL buildMoviesUrl(String preferedEndpoint){
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIES)
                .appendPath(preferedEndpoint)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URL is " + url);

        return url;
    }

    public static URL buildMoviesUrlPage(String preferedEndpoint, String page){
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIES)
                .appendPath(preferedEndpoint)
                .appendQueryParameter(MOVIE_PAGE, page)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URL is " + url);

        return url;
    }

    /** Method for building request URL for a single movie*/
    public static URL buildMovieDetailsUrl(long movieId){
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIES)
                .appendPath(String.valueOf(movieId))
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built movie detail URL is " + url);

        return url;
    }

    /** Method for building request URL for movies*/
    public static URL buildPosterUrl(String posterPath){
        Uri builtUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendEncodedPath(posterPath)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URL is " + url);

        return url;
    }

    /** Method for building request URL for movie trilers*/
    public static URL buildTrailersUrl(long movieId){
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIES)
                .appendPath(String.valueOf(movieId))
                .appendPath(PATH_TRAILERS)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URL is " + url);

        return url;
    }

    /** Method for building request URL for movie reviews*/
    public static URL buildReviewsUrl(long movieId){
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIES)
                .appendPath(String.valueOf(movieId))
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URL is " + url);

        return url;
    }



    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
