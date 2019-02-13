package helo.niti.movies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import helo.niti.movies.model.Movie;
import helo.niti.movies.model.Review;
import helo.niti.movies.model.Trailer;
import helo.niti.movies.utilities.MoviesJsonUtils;
import helo.niti.movies.utilities.NetworkUtils;

/**
 * Responsible for loading data either form a database or via network
 */
public class DetailViewModel extends AndroidViewModel {

    private boolean inOfflineMode;
    private AppDatabase db;

    private LiveData<Movie> offlineMovie;
    private LiveData<List<Review>> offlineReviews;
    private LiveData<List<Trailer>> offlineTrailers;

    private MutableLiveData<Movie> movie;
    private MutableLiveData<List<Review>> reviews;
    private MutableLiveData<List<Trailer>> trailers;

    public DetailViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<Movie> getMovie(long movieId) {
        if (inOfflineMode) {
            if(offlineMovie == null){
                offlineMovie = db.moviesDao().findMovie(movieId);
            }

            return offlineMovie;
        } else {
            if (movie == null) {
                movie = new MutableLiveData<>();
                loadMovie(movieId);
            }

            return movie;
        }
    }

    // Get movie trailers
    public LiveData<List<Trailer>> getTrailers(long movieId) {
        if (inOfflineMode) {
            if (offlineTrailers == null) {
                offlineTrailers = db.moviesDao().findTrailers(movieId);
            }

            return offlineTrailers;
        } else {
            if (trailers == null) {
                trailers = new MutableLiveData<>();
                loadTrailers(movieId);
            }

            return trailers;
        }

    }

    // Get movie reviews
    public LiveData<List<Review>> getReviews(long movieId) {
        if (inOfflineMode) {
            if (offlineReviews == null) {
                offlineReviews = db.moviesDao().findReviews(movieId);
            }

            return offlineReviews;
        } else {
            if (reviews == null) {
                reviews = new MutableLiveData<>();
                loadReviews(movieId);
            }

            return reviews;
        }
    }


    private void loadMovie(long movieId) {
        URL movieUrl = NetworkUtils.buildMovieDetailsUrl(movieId);
        new GetMovieTask().execute(movieUrl);

    }

    private void loadTrailers(long movieId) {
        URL trailersUrl = NetworkUtils.buildTrailersUrl(movieId);
        new GetTrailersTask().execute(trailersUrl);
    }

    private void loadReviews(long movieId) {
        URL reviewsUrl = NetworkUtils.buildReviewsUrl(movieId);
        new GetReviewsTask().execute(reviewsUrl);
    }

    private class GetMovieTask extends AsyncTask<URL, Void, String> {
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
                // setValue method triggers change event on live data
                movie.setValue(MoviesJsonUtils.extractMovie(new JSONObject(response)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.v("heloniti", "Response " + response);
        }
    }

    private class GetTrailersTask extends AsyncTask<URL, Void, String> {
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
                trailers.setValue(MoviesJsonUtils.extractTrailers(response));

            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.v("heloniti", "Response " + response);
        }
    }

    private class GetReviewsTask extends AsyncTask<URL, Void, String> {
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
                reviews.setValue(MoviesJsonUtils.extractReviews(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.v("heloniti", "Response " + response);
        }
    }

    public void shouldLoadOffline(boolean inOfflineMode) {
        this.inOfflineMode = inOfflineMode;
    }
}
