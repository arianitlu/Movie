package helo.niti.movies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import helo.niti.movies.R;
import helo.niti.movies.model.Movie;
import helo.niti.movies.utilities.NetworkUtils;

public class SwapAdapter extends RecyclerView.Adapter<SwapAdapter.MovieViewHolder> {

    private List<Movie> movies;

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int itemLayout = R.layout.item_movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParent = false;

        View view = inflater.inflate(itemLayout, parent, shouldAttachToParent);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        String posterPath = movie.getPosterPath();

        Picasso.get().load(NetworkUtils.buildPosterUrl(posterPath).toString())
                .into(holder.posterImageView);

    }

    @Override
    public int getItemCount() {
        if(movies == null)
            return 0;
        else
            return movies.size();
    }

    public void setMovies(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }
    public void deleteMovie(int position){
        movies.remove(position);
        notifyItemRemoved(position);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster_image_view)
        ImageView posterImageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
