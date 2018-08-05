package com.msf.movies.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.msf.movies.R;
import com.msf.movies.adapter.MoviesAdapter;
import com.msf.movies.fragment.MoviesFragment;
import com.msf.movies.model.Movie;
import com.msf.movies.viewmodel.MovieListViewModel;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesOnClickListener {

    private MovieListViewModel mMovieListViewModel;

    private MoviesFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mMovieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.movie_popular:
                    replaceWithFragment(1);
                    return true;
                case R.id.action_more_voted:
                    replaceWithFragment(2);
                    return true;
                case R.id.fav_movies:
                    replaceWithFragment(3);
                    return true;
            }
            return false;
        }
    };

    private void replaceWithFragment(int movieType) {
        fragment = MoviesFragment.newInstance(mMovieListViewModel, movieType);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    public boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
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
