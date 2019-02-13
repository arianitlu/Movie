package helo.niti.movies.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import java.util.List;

import helo.niti.movies.DetailActivity;
import helo.niti.movies.MainViewModel;
import helo.niti.movies.R;
import helo.niti.movies.adapter.MovieAdapter;
import helo.niti.movies.model.Movie;

import static android.support.constraint.Constraints.TAG;

public class FragmentGenreAction extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler {

    private MovieAdapter movieAdapter;
    private MainViewModel mainViewModel;
    public List<Movie> movies1;
    public String movieCategoryChange = "28";
    RecyclerView moviesRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_popular, container, false);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        setHasOptionsMenu(true);

        moviesRecyclerView = view.findViewById(R.id.popular_list_view);
        moviesRecyclerView.setLayoutManager(getGridLayoutManager());

        movieAdapter = new MovieAdapter(this);

        moviesRecyclerView.setAdapter(movieAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] pages = {"3","2","1"};

        mainViewModel.getMoviesWithGenresPage("28",pages).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                showMovies(movies);
            }
        });

    }

    private String getMoviesCategory() {
        return movieCategoryChange;
    }

    private void showMovies(List<Movie> movies) {
        moviesRecyclerView.setAdapter(movieAdapter);
        moviesRecyclerView.setLayoutManager(getGridLayoutManager());
        movieAdapter.setMovies(movies);
    }

    private boolean isCategoryFavorites() {
        return getString(R.string.pref_category_favorites_value).equals(getMoviesCategory());
    }

    private RecyclerView.LayoutManager getGridLayoutManager() {
        return new GridLayoutManager(getActivity(), 3);
    }

    private RecyclerView.LayoutManager getLinerLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    public void onClick(long movieId) {
        // Show details of the selected movie
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE_ID, movieId);
        intent.putExtra(DetailActivity.EXTRA_MODE_OFFLINE, isCategoryFavorites());
        startActivity(intent);

        Log.v(TAG, "Id is " + movieId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                movieAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
