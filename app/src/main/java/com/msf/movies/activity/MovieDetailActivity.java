package com.msf.movies.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.msf.movies.db.MovieDatabase;
import com.msf.movies.fragment.MovieDetailFragment;
import com.msf.movies.R;
import com.msf.movies.model.Movie;
import com.msf.movies.util.AppExecutor;
import com.msf.movies.util.NetworkEndPoints;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieListActivity}.
 */
public class MovieDetailActivity extends AppCompatActivity implements MovieDetailFragment.CallBackDropImage {
    private static final String KEY_TITULO = "titulo";
    private static final String KEY_TITLE_MOVIE = "titleMovie";
    private static final String KEY_POSTER = "poster";
    private static final String KEY_MOVIE = "movie";

    private Movie movie;

    @BindView(R.id.toolbar_detail)
    CollapsingToolbarLayout appBarLayout;

    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.poster)
    ImageView poster;

    private String backImage;

    private MovieDatabase database;

    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                movie = extras.getParcelable(KEY_MOVIE);
            }
            arguments.putParcelable(KEY_MOVIE, movie);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (appBarLayout != null) {
                    ViewCompat.setTransitionName(appBarLayout, KEY_TITLE_MOVIE);
                    ViewCompat.setTransitionName(poster, KEY_POSTER);
                    if (movie != null) {
                        appBarLayout.setTitle(movie.getTitle());
                    }
                }
            }
            MovieDetailFragment fragment = MovieDetailFragment.getInstance(this);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container, fragment).commit();
        }
    }

    private void getMovieFromDb() {
        database = MovieDatabase.getInstance(this);
        LiveData<Movie> liveDataMovieSaved = database.movieDao().loadMovieById(movie.getId());
        liveDataMovieSaved.observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if(movie != null){
                    MovieDetailActivity.this.movie.setFavorited(true);
                    hasFavMovie(true);
                }
            }
        });
    }

    private void hasFavMovie(boolean yes){
        menuItem.setIcon(getDrawable(yes ?  R.drawable.ic_star_fill: R.drawable.ic_star_outline));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        menuItem = menu.findItem(R.id.action_favorite_movie);
        getMovieFromDb();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        } else if(id == R.id.action_favorite_movie){
            AppExecutor.getInstance().getDbIo().execute(new Runnable() {
                @Override
                public void run() {
                    if(movie.isFavorited()){
                        database.movieDao().deleteMovie(movie);
                        callChangeIconOnUiThred(false);
                        movie.setFavorited(false);
                    } else {
                        database.movieDao().insertFavMovie(movie);
                        callChangeIconOnUiThred(true);
                        movie.setFavorited(true);
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void callChangeIconOnUiThred(final boolean favMovie){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hasFavMovie(favMovie);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_TITULO, appBarLayout.getTitle().toString());
        outState.putString(KEY_POSTER,backImage);
        outState.putParcelable(KEY_MOVIE, movie);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        appBarLayout.setTitle(savedInstanceState.getString(KEY_TITULO));
        onRequestBackdrop(savedInstanceState.getString(KEY_POSTER));
        movie = savedInstanceState.getParcelable(KEY_MOVIE);
    }

    @Override
    public void onRequestBackdrop(String backDropPath) {
        backImage =  backDropPath;
        Picasso.get()
                .load(NetworkEndPoints.IMAGE_API.getUrl() + backDropPath)
                .into(poster, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError(Exception e) {
                        supportStartPostponedEnterTransition();
                    }
                });
    }
}
