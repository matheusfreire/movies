package com.msf.moveis;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msf.moveis.model.Movie;
import com.msf.moveis.util.NetworkEndPoints;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private int total;
    private List<Movie> listMovies;
    private MoviesOnClickListener listener;

    public interface MoviesOnClickListener {
        void onClickMovie(Movie movie, View... views);
    }

    public MoviesAdapter(int total, List<Movie> movies, MoviesOnClickListener onClickListener){
        this.total = total;
        this.listMovies = movies;
        this.listener = onClickListener;
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
            Movie movie = listMovies.get(position);
            holder.title.setText(movie.getTitle());
            holder.rating.setText(String.valueOf(movie.getVoteAverage()));
            Picasso.get()
                    .load(NetworkEndPoints.IMAGE_API.getUrl() + getItem(position).getPoster())
                    .into(holder.thumbnail);
        } catch (IndexOutOfBoundsException e){
            Log.d("Adapter", "Exception");
        }
    }

    private Movie getItem(int position){
        return listMovies.get(position);
    }

    @Override
    public int getItemCount() {
        return total;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.rating)
        TextView rating;

        @BindView(R.id.thumbnail)
        ImageView thumbnail;

        MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            listener.onClickMovie(getItem(adapterPosition), title);
        }
    }
}
