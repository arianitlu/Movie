package helo.niti.movies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import helo.niti.movies.model.Movie;
import helo.niti.movies.utilities.MoviesJsonUtils;
import helo.niti.movies.utilities.NetworkUtils;

public class MainViewModel extends AndroidViewModel {

    private AppDatabase db;

    private LiveData<List<Movie>> offlineMovies;

    private MutableLiveData<List<Movie>> movies;

    /** Movie category could be popular, top-rated or favorites*/
    private String movieCategory;

    public MainViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    // Get movies from API
    public LiveData<List<Movie>> getMovies(String category) {
        if (category.equals(getApplication().getString(R.string.pref_category_favorites_value))) {
            if(offlineMovies == null){
                offlineMovies = db.moviesDao().findMovies();
            }

            return offlineMovies;
        } else {
            if (movies == null && !category.equals(movieCategory)) {
                movies = new MutableLiveData<>();
                movieCategory = category;
                loadMovies(category);
            }

            return movies;
        }
    }

    // Get movies by genre
    public LiveData<List<Movie>> getMoviesWithGenres(String category) {
            if (movies == null && !category.equals(movieCategory)) {
                movies = new MutableLiveData<>();
                movieCategory = category;
                loadMoviesWithGenres(category);
            }

            return movies;
    }

    // Get movies by genres and pages
    public LiveData<List<Movie>> getMoviesWithGenresPage(String category, String[] pages) {
        if (movies == null && !category.equals(movieCategory)) {
            movies = new MutableLiveData<>();
            movieCategory = category;

            // load three pages of 20 movies each
            loadMoviesWithGenresPage(category,pages[0]);
            loadMoviesWithGenresPage(category,pages[1]);
            loadMoviesWithGenresPage(category,pages[2]);
        }

        return movies;
    }

    // Get movies by page
    public LiveData<List<Movie>> getMoviesWithPage(String category, String[] pages) {
        if (movies == null && !category.equals(movieCategory)) {
            movies = new MutableLiveData<>();
            movieCategory = category;
            loadMoviesWithPage(category,pages[0]);
            loadMoviesWithPage(category,pages[1]);
            loadMoviesWithPage(category,pages[2]);

        }

        return movies;
    }

    private void loadMoviesWithGenres(String category){
        URL searchUrl = NetworkUtils.buildMoviesGenreUrl(category);
        new GetMoviesWithGenresTask().execute(searchUrl);
    }

    private void loadMovies(String category){
        URL searchUrl = NetworkUtils.buildMoviesUrl(category);
        new GetMoviesTask().execute(searchUrl);
    }
    private void loadMoviesWithGenresPage(String category, String page){
        URL searchUrl = NetworkUtils.buildMoviesGenreUrlPage(category,page);
        new GetMoviesWithGenresPagesTask().execute(searchUrl);
    }

    private void loadMoviesWithPage(String category, String page){
        URL searchUrl = NetworkUtils.buildMoviesUrlPage(category,page);
        new GetMoviesPagesTask().execute(searchUrl);
    }

    private class GetMoviesWithGenresTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                List<Movie> nMovies = MoviesJsonUtils.extractMovies(response);
                movies.setValue(nMovies);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetMoviesWithGenresPagesTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                List<Movie> nMovies = MoviesJsonUtils.extractMovies(response);
                if (movies.getValue() != null){
                    nMovies.addAll(movies.getValue());
                }
                movies.setValue(nMovies);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetMoviesPagesTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                List<Movie> nMovies = MoviesJsonUtils.extractMovies(response);
                if (movies.getValue() != null){
                    nMovies.addAll(movies.getValue());
                }
                movies.setValue(nMovies);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetMoviesTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                List<Movie> nMovies = MoviesJsonUtils.extractMovies(response);
                movies.setValue(nMovies);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
