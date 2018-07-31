package com.msf.movies.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.msf.movies.fragment.MovieDetailFragment;
import com.msf.movies.R;
import com.msf.movies.model.Movie;
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
    public static final String KEY_TITULO = "titulo";
    public static final String KEY_TITLE_MOVIE = "titleMovie";
    public static final String KEY_POSTER = "poster";
    public static final String KEY_MOVIE = "movie";
    @BindView(R.id.toolbar_detail)
    CollapsingToolbarLayout appBarLayout;

    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.poster)
    ImageView poster;

    private String backImage;

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

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            Bundle extras = getIntent().getExtras();
            Movie movie = null;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_TITULO, appBarLayout.getTitle().toString());
        outState.putString(KEY_POSTER,backImage);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        appBarLayout.setTitle(savedInstanceState.getString(KEY_TITULO));
        onRequestBackdrop(savedInstanceState.getString(KEY_POSTER));
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
