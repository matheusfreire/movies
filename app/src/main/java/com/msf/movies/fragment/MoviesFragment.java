package com.msf.movies.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msf.movies.BuildConfig;
import com.msf.movies.R;
import com.msf.movies.activity.MainActivity;
import com.msf.movies.activity.MovieListActivity;
import com.msf.movies.adapter.MoviesAdapter;
import com.msf.movies.model.Movie;
import com.msf.movies.util.ListItemDecoration;
import com.msf.movies.util.MoviesApi;
import com.msf.movies.util.RetrofitClientInstance;
import com.msf.movies.viewmodel.MovieListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Setter;

public class MoviesFragment extends Fragment {

    private static final int MOVIE_POPULAR = 1;
    private static final int MOVIE_MORE_VOTED = 2;
    private static final int FAVORITES_MOVIES = 3;

    private int movieType;

    private MovieListViewModel movieListViewModel;

    private static final int NUM_COLUMNS = 2;

    @BindView(R.id.movie_list)
    RecyclerView mRecyclerView;


    @BindView(R.id.progress_loading)
    ProgressBar mProgressLoading;

    @BindView(R.id.error_message)
    TextView mErrorMessage;

    @BindView(R.id.relative_layout_list)
    RelativeLayout mRelativeLayoutList;

    private MoviesAdapter mMoviesAdapter;

    public MoviesFragment() {
    }

    public static MoviesFragment newInstance(MovieListViewModel mMovieListViewModel, int movieType) {
        MoviesFragment fragment = new MoviesFragment();
        fragment.movieType = movieType;
        fragment.movieListViewModel = mMovieListViewModel;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_movie_list, container, false);
        ButterKnife.bind(this, rootView);
        switch (movieType){
            case MOVIE_POPULAR:
                movieListViewModel.getMovies(RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class).callListPopular(BuildConfig.API_KEY));
                observerMultableLiveData();
                break;
            case MOVIE_MORE_VOTED:
                movieListViewModel.getMovies(RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class).callListTopRated(BuildConfig.API_KEY));
                observerMultableLiveData();
                break;
            case FAVORITES_MOVIES:
                movieListViewModel.getFavMovies();
                movieListViewModel.getLiveDataMovies().observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(@Nullable List<Movie> movies) {
                        mountRecyclerWithList(movies);
                    }
                });
                break;
        }
        buildRecycler();
        return rootView;
    }

    private void observerMultableLiveData() {
        movieListViewModel.getMultableLiveDataListMovie().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mountRecyclerWithList(movies);
            }
        });
    }

    private void buildRecycler() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), NUM_COLUMNS);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new ListItemDecoration(NUM_COLUMNS, dpToPx(10)));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
    }

    private void showRecyclerView(boolean showRecyclerView) {
        mErrorMessage.setVisibility(showRecyclerView ? View.INVISIBLE : View.VISIBLE);
        mRecyclerView.setVisibility(showRecyclerView ? View.VISIBLE : View.INVISIBLE);
    }

    private void mountRecyclerWithList(@Nullable List<Movie> movies) {
        if(movies != null){
            mMoviesAdapter = new MoviesAdapter(movies.size(), movies, (MainActivity) getActivity());
            setAdapterToRecycler(mRecyclerView);
            mProgressLoading.setVisibility(View.INVISIBLE);
            showRecyclerView(true);
        } else {
            mProgressLoading.setVisibility(View.INVISIBLE);
            showRecyclerView(false);
        }
    }

    private void setAdapterToRecycler(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(mMoviesAdapter);
    }

    private void buidlNetworkMessage() {
        mErrorMessage.setText(R.string.no_network);
        mRecyclerView.setAdapter(null);
        showRecyclerView(false);
        Snackbar mySnackbar = Snackbar.make(mRelativeLayoutList,R.string.try_again, Snackbar.LENGTH_SHORT);
//        mySnackbar.setAction(R.string.yes, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getMovies(RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class).callListPopular(BuildConfig.API_KEY));
//            }
//        });
        mySnackbar.show();
    }

    private int dpToPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

}
