package helo.niti.movies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import helo.niti.movies.R;
import helo.niti.movies.model.Movie;
import helo.niti.movies.utilities.NetworkUtils;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> implements Filterable {

    private List<Movie> movies;
    private List<Movie> moviesFull;

    private final MovieAdapterOnClickHandler clickHandler;

    public interface MovieAdapterOnClickHandler{
        void onClick(long movieId);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

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
        String movieTitle = movie.getTitle();
        String posterPath = movie.getPosterPath();

        //holder.titleTextView.setText(movieTitle);
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
            moviesFull = new ArrayList<>(movies);
            notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.title_text_view)
        TextView titleTextView;
        @BindView(R.id.poster_image_view)
        ImageView posterImageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            long movieId = movies.get(adapterPosition).getId();
            clickHandler.onClick(movieId);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    // Search implementation
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Movie> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(moviesFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Movie item : moviesFull) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            movies.clear();
            movies.addAll((List) results.values);

            notifyDataSetChanged();
        }
    };
}
