package com.msf.movies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msf.movies.R;
import com.msf.movies.model.Movie;
import com.msf.movies.util.NetworkEndPoints;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private int mTotal;
    private List<Movie> mListMovies;
    private MoviesOnClickListener mListener;

    public interface MoviesOnClickListener {
        void onClickMovie(Movie movie, View... views);
    }

    public MoviesAdapter(int mTotal, List<Movie> movies, MoviesOnClickListener onClickListener){
        this.mTotal = mTotal;
        this.mListMovies = movies;
        this.mListener = onClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_content, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        try{
            Movie movie = mListMovies.get(position);
            holder.mTitle.setText(movie.getTitle());
            holder.mRating.setText(String.valueOf(movie.getVoteAverage()));
            Picasso.get()
                    .load(NetworkEndPoints.IMAGE_API.getUrl() + getItem(position).getPoster())
                    .into(holder.mThumbnail);
        } catch (IndexOutOfBoundsException e){
            Log.d("Adapter", "Exception");
        }
    }

    private Movie getItem(int position){
        return mListMovies.get(position);
    }

    @Override
    public int getItemCount() {
        return mTotal;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.title)
        TextView mTitle;

        @BindView(R.id.rating)
        TextView mRating;

        @BindView(R.id.thumbnail)
        ImageView mThumbnail;

        MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mListener.onClickMovie(getItem(adapterPosition), mTitle, mThumbnail);
        }
    }
}
