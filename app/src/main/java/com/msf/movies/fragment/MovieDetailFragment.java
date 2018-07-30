package com.msf.movies.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msf.movies.BuildConfig;
import com.msf.movies.model.ReviewList;
import com.msf.movies.model.VideoList;
import com.msf.movies.viewmodel.MovieDetailViewModel;
import com.msf.movies.R;
import com.msf.movies.model.Movie;
import com.msf.movies.util.MoviesApi;
import com.msf.movies.util.RetrofitClientInstance;
import com.msf.movies.viewmodel.MovieDetailViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment {

    private Movie mItem;

    @BindView(R.id.progress_loading)
    ProgressBar progressLoading;

    @BindView(R.id.error_message)
    TextView mErrorMessage;

    @BindView(R.id.relative_detail)
    RelativeLayout relativeLayoutDetail;

    @BindView(R.id.director_movie)
    TextView mDirectorTxt;

    @BindView(R.id.released_date_movie)
    TextView mReleasedDate;

    @BindView(R.id.rating)
    TextView mRating;

    @BindView(R.id.ratebar)
    RatingBar mRatebar;

    @BindView(R.id.txt_description)
    TextView mDescription;

    private CallBackDropImage mListener;
    private MovieDetailViewModel movieDetailViewModel;


    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("movie")) {
            mItem = getArguments().getParcelable("movie");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        ButterKnife.bind(this, rootView);
        if (mItem != null) {
            progressLoading.setVisibility(View.VISIBLE);
            relativeLayoutDetail.setVisibility(View.INVISIBLE);
            MoviesApi service = RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class);
            Call<Movie> call = service.callDetail(mItem.getId(), BuildConfig.API_KEY);
            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    buildInterface(response.body());
                    progressLoading.setVisibility(View.INVISIBLE);
                    relativeLayoutDetail.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    progressLoading.setVisibility(View.INVISIBLE);
                    mErrorMessage.setVisibility(View.VISIBLE);
                }
            });
            MovieDetailViewModelFactory movieDetailViewModelFactory = new MovieDetailViewModelFactory(service.callListVideos(mItem.getId(), BuildConfig.API_KEY),
                    service.callListReview(mItem.getId(), BuildConfig.API_KEY));
            movieDetailViewModel = ViewModelProviders.of(this, movieDetailViewModelFactory).get(MovieDetailViewModel.class);
            getVideos();
            getReviews();
        } else {
            mErrorMessage.setVisibility(View.VISIBLE);
        }
        return rootView;
    }


    private void buildInterface(Movie movieResult) {
        mListener.onRequestBackdrop(movieResult.getBackDropImage());
        mDescription.setText(movieResult.getOverview());
        mRating.setText(String.valueOf(movieResult.getVoteAverage()).concat("/10"));
        mDirectorTxt.setText(movieResult.getTagLine());
        mReleasedDate.setText(getDateReleased(movieResult.getReleaseDate()));
        mRatebar.setRating(getVoteAverage(movieResult));
        animateRatingbar(mRatebar);
    }

    @SuppressLint("ObjectAnimatorBinding")
    private void animateRatingbar(RatingBar mRatebar) {
        float current = mRatebar.getRating();
        ObjectAnimator anim = ObjectAnimator.ofFloat(mRatebar, "mRating", 0, current);
        anim.setDuration(1800);
        anim.start();
    }

    private float getVoteAverage(Movie movieResult) {
        return (float) movieResult.getVoteAverage()/2;
    }

    private String getDateReleased(Date releaseDate) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(releaseDate);
    }

    public static MovieDetailFragment getInstance(CallBackDropImage callBackDropImage) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.mListener = callBackDropImage;
        return movieDetailFragment;
    }

    public void getVideos() {
        movieDetailViewModel.getLiveDataVideos().observe(this, new Observer<VideoList>() {
            @Override
            public void onChanged(@Nullable VideoList videoList) {
                Log.d("e","teste");
            }
        });
    }

    private void getReviews() {
        movieDetailViewModel.getLiveDataReview().observe(this, new Observer<ReviewList>() {
            @Override
            public void onChanged(@Nullable ReviewList reviewList) {
                Log.d("e","teste");
            }
        });
    }


    public interface CallBackDropImage{
        void onRequestBackdrop(String backDropPath);
    }
}
