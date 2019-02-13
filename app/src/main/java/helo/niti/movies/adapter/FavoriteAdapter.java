package helo.niti.movies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import helo.niti.movies.R;
import helo.niti.movies.model.Movie;

/** Provides views representing a user favorite movie*/
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteVh> {
    public class FavoriteVh extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleTextView;
        TextView releaseDateTextView;

        public FavoriteVh(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            releaseDateTextView = itemView.findViewById(R.id.release_date_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            long movieId = movies.get(adapterPosition).getId();
            clickHandler.onClick(movieId);
        }
    }

    private FavoriteAdapterOnClickHandler clickHandler;

    public interface FavoriteAdapterOnClickHandler{
        void onClick(long movieId);
    }

    private List<Movie> movies;

    public FavoriteAdapter(FavoriteAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public FavoriteVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View favoriteView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new FavoriteVh(favoriteView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteVh holder, int position) {
        Movie movie = movies.get(position);
        holder.titleTextView.setText(movie.getTitle());
        holder.releaseDateTextView.setText(formatDate(movie.getReleaseDate()));
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0: movies.size();
    }

    public void setMovies(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    private String formatDate(String apiDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date sourceDate = null;

        try {
            sourceDate = dateFormat.parse(apiDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat("(y)", Locale.getDefault());
        return targetFormat.format(sourceDate);
    }
}
