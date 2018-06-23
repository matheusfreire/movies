package com.msf.moveis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.msf.moveis.model.Movie;
import com.msf.moveis.model.MovieList;
import com.msf.moveis.utilities.MoviesApi;
import com.msf.moveis.utilities.RetrofitClientInstance;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity {

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
                Toast.makeText(MovieListActivity.this, "Teste", Toast.LENGTH_SHORT).show();
                progressLoading.setVisibility(View.INVISIBLE);
                showRecyclerView(true);
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                progressLoading.setVisibility(View.INVISIBLE);
                showRecyclerView(false);
            }
        });

        assert recyclerView != null;
        setupRecyclerView(recyclerView);
    }

    private void showRecyclerView(boolean showRecyclerView) {
        /* First, make sure the error is invisible */
        mErrorMessage.setVisibility(showRecyclerView ? View.INVISIBLE : View.VISIBLE);
        /* Then, make sure the weather data is visible */
        recyclerView.setVisibility(showRecyclerView ? View.VISIBLE : View.INVISIBLE);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
//        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
    }

}
