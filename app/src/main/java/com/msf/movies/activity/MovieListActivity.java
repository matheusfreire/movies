package com.msf.movies.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msf.movies.BuildConfig;
import com.msf.movies.adapter.MoviesAdapter;
import com.msf.movies.R;
import com.msf.movies.model.Movie;
import com.msf.movies.model.MovieList;
import com.msf.movies.util.ListItemDecoration;
import com.msf.movies.util.MoviesApi;
import com.msf.movies.util.RetrofitClientInstance;
import com.msf.movies.viewmodel.MovieListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class MovieListActivity extends AppCompatActivity implements MoviesAdapter.MoviesOnClickListener {

    public static final String KEY_ADAPTER = "adapter";
    public static final String KEY_TYPE = "type";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.movie_list)
    RecyclerView mRecyclerView;


    @BindView(R.id.progress_loading)
    ProgressBar mProgressLoading;

    @BindView(R.id.error_message)
    TextView mErrorMessage;

    @BindView(R.id.relative_layout_list)
    RelativeLayout mRelativeLayoutList;

    private MoviesAdapter mMoviesAdapter;

    private static final int NUM_COLUMNS = 2;

    private MovieListViewModel mMovieListViewModel;

    private String typeMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);

        mMovieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        observerFromViewModel();
        if(savedInstanceState != null){
            typeMovies = savedInstanceState.getString(KEY_TYPE, "");
            switch (typeMovies){
                case "voted":
                    getMovies(RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class).callListTopRated(BuildConfig.API_KEY));
                    break;
                case "fav":
                    mMovieListViewModel.getFavMovies();
                    break;
                default:
                    getMovies(RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class).callListPopular(BuildConfig.API_KEY));
                    break;
            }
        } else {
            getMovies(RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class).callListPopular(BuildConfig.API_KEY));
        }
        buildRecycler();
    }

    private void observerFromViewModel() {
        mMovieListViewModel.getMultableLiveDataListMovie().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mountRecyclerWithList(movies);
            }
        });
        mMovieListViewModel.getLiveDataMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mountRecyclerWithList(movies);
            }
        });
    }

    private void mountRecyclerWithList(@Nullable List<Movie> movies) {
        if(movies != null){
            mMoviesAdapter = new MoviesAdapter(movies.size(), movies, MovieListActivity.this);
            setAdapterToRecycler(mRecyclerView);
            mProgressLoading.setVisibility(View.INVISIBLE);
            showRecyclerView(true);
        } else {
            mProgressLoading.setVisibility(View.INVISIBLE);
            showRecyclerView(false);
        }
    }

    private void getMovies(Call<MovieList> request) {
        if(isOnline()){
            mProgressLoading.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(null);
            mMovieListViewModel.getMovies(request);
        } else {
            buidlNetworkMessage();
        }
    }

    private void buidlNetworkMessage() {
        mErrorMessage.setText(R.string.no_network);
        mRecyclerView.setAdapter(null);
        showRecyclerView(false);
        Snackbar mySnackbar = Snackbar.make(mRelativeLayoutList,R.string.try_again, Snackbar.LENGTH_SHORT);
        mySnackbar.setAction(R.string.yes, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovies(RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class).callListPopular(BuildConfig.API_KEY));
            }
        });
        mySnackbar.show();
    }

    public boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_popular:
                getMovies(RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class).callListPopular(BuildConfig.API_KEY));
                setTypeMovies("popular");
                updateTitleToolbar(getString(R.string.movies_popular));
                break;
            case R.id.action_by_voted:
                getMovies(RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class).callListTopRated(BuildConfig.API_KEY));
                setTypeMovies("voted");
                updateTitleToolbar(getString(R.string.movies_more_voted));
                break;
            case R.id.action_favorites_movie:
                getFavoritesMovie();
                setTypeMovies("fav");
                updateTitleToolbar(getString(R.string.title_fav_movies));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateTitleToolbar(String title){
        mToolbar.setTitle(title);
    }

    public void setTypeMovies(String typeMovies) {
        this.typeMovies = typeMovies;
    }

    private void getFavoritesMovie() {
        if(isOnline()){
            mProgressLoading.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(null);
            mMovieListViewModel.getFavMovies();
        } else {
            buidlNetworkMessage();
        }
    }

    private void buildRecycler() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUM_COLUMNS);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new ListItemDecoration(NUM_COLUMNS, dpToPx(10)));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
    }

    private void showRecyclerView(boolean showRecyclerView) {
        mErrorMessage.setVisibility(showRecyclerView ? View.INVISIBLE : View.VISIBLE);
        mRecyclerView.setVisibility(showRecyclerView ? View.VISIBLE : View.INVISIBLE);
    }

    private void setAdapterToRecycler(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(mMoviesAdapter);
    }

    public int dpToPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onClickMovie(Movie movie, View... views) {
        Intent intent = new Intent(this,MovieDetailActivity.class);
        intent.putExtra("movie", movie);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, Pair.create(views[0], "poster"),Pair.create(views[1], "titleMovie"));
        startActivity(intent, options.toBundle());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_TYPE, typeMovies);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        typeMovies = savedInstanceState.getString(KEY_TYPE, "");
    }
}
