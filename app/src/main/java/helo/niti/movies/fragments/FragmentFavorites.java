package helo.niti.movies.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import helo.niti.movies.DetailActivity;
import helo.niti.movies.MainViewModel;
import helo.niti.movies.R;
import helo.niti.movies.adapter.FavoriteAdapter;
import helo.niti.movies.model.Movie;

import static android.support.constraint.Constraints.TAG;

public class FragmentFavorites extends Fragment implements FavoriteAdapter.FavoriteAdapterOnClickHandler {

    private FavoriteAdapter favoriteAdapter;
    private MainViewModel mainViewModel;
    public List<Movie> movies1;
    public String movieCategoryChange = "favorites";
    private DrawerLayout drawer;
    RecyclerView moviesRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        moviesRecyclerView = view.findViewById(R.id.favorite_list_view);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.getMovies("favorites");
        setHasOptionsMenu(true);

        favoriteAdapter = new FavoriteAdapter(this);

        moviesRecyclerView.setLayoutManager(getGridLayoutManager());
        moviesRecyclerView.setAdapter(favoriteAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            mainViewModel.getMovies("favorites").observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    movies1 = movies;
                    showMovies(getMoviesCategory(), movies1);
                }
            });

    }

    private String getMoviesCategory() {
        return movieCategoryChange;
    }

    private void showMovies(String category, List<Movie> movies) {
        if (category.equals(getString(R.string.pref_category_favorites_value))) {
            moviesRecyclerView.setAdapter(favoriteAdapter);
            moviesRecyclerView.setLayoutManager(getLinerLayoutManager());
            favoriteAdapter.setMovies(movies);
        }
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
}
