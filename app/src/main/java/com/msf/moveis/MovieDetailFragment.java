package com.msf.moveis;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msf.moveis.model.Movie;
import com.msf.moveis.util.MoviesApi;
import com.msf.moveis.util.RetrofitClientInstance;

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
        ButterKnife.bind(this,rootView);
        if (mItem != null) {
            progressLoading.setVisibility(View.VISIBLE);
            relativeLayoutDetail.setVisibility(View.INVISIBLE);
            MoviesApi service = RetrofitClientInstance.getRetrofitInstance().create(MoviesApi.class);
            Call<Movie> call = service.callDetail(mItem.getId(),BuildConfig.Api);
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
        } else {
            mErrorMessage.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    private void buildInterface(Movie movieResult) {
        mDescription.setText(movieResult.getOverview());
        mRating.setText(String.valueOf(movieResult.getVoteAverage()).concat("/10"));
        mDirectorTxt.setText(movieResult.getTagLine());
        mReleasedDate.setText(getDateReleased(movieResult.getReleaseDate()));
        mRatebar.setRating(getVoteAverage(movieResult));
        animateRatingbar(mRatebar);
    }

    private void animateRatingbar(RatingBar mRatebar) {
        float current = mRatebar.getRating();

        ObjectAnimator anim = ObjectAnimator.ofFloat(mRatebar, "rating", 0, current);
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
}
