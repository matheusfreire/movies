package com.msf.moveis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.msf.moveis.model.Movie;
import com.msf.moveis.model.MovieList;
import com.msf.moveis.util.ListItemDecoration;
import com.msf.moveis.util.MoviesApi;
import com.msf.moveis.util.RetrofitClientInstance;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements MoviesAdapter.MoviesOnClickListener{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.movie_list)
    RecyclerView recyclerView;


    @BindView(R.id.progress_loading)
    ProgressBar progressLoading;

    @BindView(R.id.error_message)
    TextView mErrorMessage;

    private MoviesAdapter mMoviesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);


        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        progressLoading.setVisibility(View.VISIBLE);
        MoviesApi service = RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class);
        Call<MovieList> call = service.callListPopular(BuildConfig.Api);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                mMoviesAdapter = new MoviesAdapter(response.body().getTotalResults(), response.body().getResults(), MovieListActivity.this);
                setupRecyclerView(recyclerView);
                progressLoading.setVisibility(View.INVISIBLE);
                showRecyclerView(true);
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                progressLoading.setVisibility(View.INVISIBLE);
                showRecyclerView(false);
            }
        });
        buildRecycler();
//        recyclerView.setAdapter(adapter);
    }

    private void buildRecycler() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new ListItemDecoration(2, dpToPx(10)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
    }

    private void showRecyclerView(boolean showRecyclerView) {
        mErrorMessage.setVisibility(showRecyclerView ? View.INVISIBLE : View.VISIBLE);
        recyclerView.setVisibility(showRecyclerView ? View.VISIBLE : View.INVISIBLE);
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
                makeSceneTransitionAnimation(this, views[0], "titleMovie");
        startActivity(intent, options.toBundle());
    }
}
