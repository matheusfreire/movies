package com.msf.movies.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements MoviesAdapter.MoviesOnClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);


        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        getMovies(RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class).callListPopular(BuildConfig.API_KEY));
        buildRecycler();
    }

    private void getMovies(Call<MovieList> request) {
        if(isOnline()){
            mProgressLoading.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(null);
            request.enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                    mMoviesAdapter = new MoviesAdapter(response.body().getResults().size(), response.body().getResults(), MovieListActivity.this);
                    setupRecyclerView(mRecyclerView);
                    mProgressLoading.setVisibility(View.INVISIBLE);
                    showRecyclerView(true);
                }

                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {
                    mProgressLoading.setVisibility(View.INVISIBLE);
                    showRecyclerView(false);
                }

            });
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
                break;
            case R.id.action_by_voted:
                getMovies(RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class).callListTopRated(BuildConfig.API_KEY));
                break;
        }
        return super.onOptionsItemSelected(item);
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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
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
}
