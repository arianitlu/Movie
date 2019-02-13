package helo.niti.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import helo.niti.movies.adapter.ReviewAdapter;
import helo.niti.movies.adapter.TrailerAdapter;
import helo.niti.movies.helper.AppExecutors;
import helo.niti.movies.model.Movie;
import helo.niti.movies.model.Review;
import helo.niti.movies.model.Trailer;
import helo.niti.movies.utilities.NetworkUtils;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    public static final String EXTRA_MOVIE_ID = "movieId";
    public static final String EXTRA_MODE_OFFLINE = "modeOffline";

    @BindView(R.id.backdrop)
    ImageView backdropImageView;
    @BindView(R.id.overview_text_view)
    TextView overviewTextView;
    @BindView(R.id.user_rating_text_view)
    TextView userRatingTextView;
    @BindView(R.id.release_date_text_view)
    TextView releaseDateTextView;
    @BindView(R.id.original_title_text_view)
    TextView originalTitleTextView;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private DetailViewModel detailViewModel;
    private AppDatabase db;

    private long movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        movieId = getIntent().getLongExtra(EXTRA_MOVIE_ID, 0);
        boolean isModeOffline = getIntent().getBooleanExtra(EXTRA_MODE_OFFLINE, false);

        // ViewModel that's responsible for getting movie together with their trailers and reviews
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.shouldLoadOffline(isModeOffline);


        // Get instance of the app database
        db = AppDatabase.getInstance(this);

        // Gets the movie details without trailers and reviews
        detailViewModel.getMovie(movieId).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                collapsingToolbar.setTitle("" + movie.getTitle());

                Picasso.get().load(NetworkUtils.buildPosterUrl(movie.getPosterPath()).toString())
                        .into(backdropImageView);

                overviewTextView.setText(movie.getOverview());
                originalTitleTextView.setText(movie.getOriginalTitle());
                userRatingTextView.setText(String.valueOf(movie.getRating()));
                releaseDateTextView.setText(movie.getReleaseDate());
            }
        });


        // Show trailers horizontally using a {@link RecyclerView}
        RecyclerView trailersCarousel = findViewById(R.id.trailers_carousel);
        trailersCarousel.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        trailerAdapter = new TrailerAdapter(this);
        trailersCarousel.setAdapter(trailerAdapter);

        // Refresh {@link TrailerAdapter} when trailers are returned
        detailViewModel.getTrailers(movieId).observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(@Nullable List<Trailer> trailers) {
                trailerAdapter.setTrailers(trailers);
            }
        });

        // Show reviews using a {@link RecyclerView}
        RecyclerView reviewsListView = findViewById(R.id.reviews_list_view);
        reviewsListView.setLayoutManager(new LinearLayoutManager(this));

        reviewAdapter = new ReviewAdapter(this);
        reviewsListView.setAdapter(reviewAdapter);

        // Refresh {@link ReviewAdapter} when reviews are returned
        detailViewModel.getReviews(movieId).observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviews) {
                reviewAdapter.setReviews(reviews);
            }
        });


    }

    public void toggleFavorite(boolean makeFavorite) {
        if (makeFavorite) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    db.moviesDao().insertMovies(detailViewModel.getMovie(movieId).getValue());
                    db.moviesDao().insertTrailers(detailViewModel.getTrailers(movieId).getValue());
                    db.moviesDao().insertReviews(detailViewModel.getReviews(movieId).getValue());
                }
            });

        } else {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    db.moviesDao().deleteMovies(detailViewModel.getMovie(movieId).getValue());
                }
            });
        }

        Toast.makeText(this, makeFavorite ? R.string.favorited : R.string.unfavorited, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);

        // Check if the movie is a favorite and indicate that by changing toggle icon
        final MenuItem toggleButton = menu.findItem(R.id.action_toggle_favorite);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.moviesDao().findMovie(movieId).observe(DetailActivity.this, new Observer<Movie>() {
                    @Override
                    public void onChanged(@Nullable Movie movie) {
                        boolean favorite = movie != null;
                        toggleButton.setChecked(favorite);
                        toggleButton.setIcon(favorite ? R.drawable.ic_star_white_24dp : R.drawable.ic_star_border_white_24dp);
                    }
                });
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle_favorite:
                boolean checked = !item.isChecked();
                item.setChecked(checked);
                item.setIcon(checked ? R.drawable.ic_star_white_24dp : R.drawable.ic_star_border_white_24dp);
                toggleFavorite(checked);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
