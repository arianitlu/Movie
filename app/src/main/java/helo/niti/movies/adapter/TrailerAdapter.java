package helo.niti.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import helo.niti.movies.R;
import helo.niti.movies.model.Trailer;

/** Provides views representing a clickable movie trailer */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerVh> {
    public class TrailerVh extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        public TrailerVh(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            // Start activity that knows wants to handle a video uri
            Trailer trailer = trailers.get(getAdapterPosition());
            Intent watchIntent = new Intent(Intent.ACTION_VIEW, trailer.getTrailerUri());
            context.startActivity(watchIntent);
        }
    }

    private List<Trailer> trailers;
    private Context context;

    public TrailerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TrailerVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View trailerView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trailer, parent, false);
        return new TrailerVh(trailerView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerVh holder, int position) {
        Trailer trailer = trailers.get(position);
        holder.nameTextView.setText(trailer.getName());

    }

    @Override
    public int getItemCount() {
        return trailers == null ? 0: trailers.size() ;
    }

    public void setTrailers(List<Trailer> trailers){
        this.trailers = trailers;
        notifyDataSetChanged();
    }
}
