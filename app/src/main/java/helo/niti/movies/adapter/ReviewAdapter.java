package helo.niti.movies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import helo.niti.movies.R;
import helo.niti.movies.model.Review;

/** Provides views representing a movie review*/
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewVh> {
    public static class ReviewVh extends RecyclerView.ViewHolder{

        TextView authorTextView;
        TextView commentTextView;

        public ReviewVh(View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.author_text_view);
            commentTextView = itemView.findViewById(R.id.comment_text_view);
        }
    }

    private List<Review> reviews;

    public ReviewAdapter(Context context) {
        this.reviews = new ArrayList<>();
        // Add an item for when there are no reviews for a movie
        this.reviews.add(new Review(context.getString(R.string.no_reviews_title),
                context.getString(R.string.no_reviews_message), 0, "0"));
    }

    @NonNull
    @Override
    public ReviewVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View reView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewVh(reView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewVh holder, int position) {
        Review review = reviews.get(position);
        holder.authorTextView.setText(review.getAuthor());
        holder.commentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0: reviews.size();
    }

    public void setReviews(List<Review> reviews){
        if(reviews.isEmpty()) return;
        this.reviews = reviews;
        notifyDataSetChanged();
    }


}
