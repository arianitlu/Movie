package helo.niti.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import helo.niti.movies.adapter.SwapAdapter;
import helo.niti.movies.model.Movie;
import helo.niti.movies.movies.R;

public class SwapActivity extends AppCompatActivity {

    RecyclerView swapRecyclerView;
    SwapAdapter swapAdapter;
    private AdView mAdView;
    private MainViewModel mainViewModel;
    private int swipeRight = 0;
    private int swipeLeft = 0;
    TextView txt_like,txt_dislike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txt_like = findViewById(R.id.txt_like);
        txt_dislike = findViewById(R.id.txt_dislike);

        // Add ads on our app
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest
                .Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        // RycyclerView
        swapRecyclerView = findViewById(R.id.swap_recyclerview);

        swapAdapter = new SwapAdapter();
        mainViewModel.getMovies("top_rated").observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                swapRecyclerView.setAdapter(swapAdapter);
                swapRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                swapAdapter.setMovies(movies);
            }
        });

        // Item touch helper attached to recyclerview , implementing swipe methods
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                swapAdapter.deleteMovie(viewHolder.getAdapterPosition());

                if (i == ItemTouchHelper.RIGHT) {
                    swipeRight++;

                    txt_dislike.setVisibility(View.VISIBLE);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txt_dislike.setVisibility(View.INVISIBLE);
                        }
                    }, 1000);

                    if (swipeRight == 5){
                    Intent intent = new Intent(SwapActivity.this, MainActivity.class);
                    startActivity(intent);
                    }
                }
                if (i == ItemTouchHelper.LEFT) {
                    swipeLeft++;
                    txt_like.setVisibility(View.VISIBLE);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txt_like.setVisibility(View.INVISIBLE);
                        }
                    }, 1000);

                    if (swipeLeft == 10){
                    Intent intent = new Intent(SwapActivity.this, MainActivity.class);
                    startActivity(intent);
                    }
                }
            }
        });
    }
}