package com.msf.moveis;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msf.moveis.model.Movie;

import butterknife.BindView;

public class MovieDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    private Movie mItem;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout appBarLayout;
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            if (appBarLayout != null) {
//                appBarLayout.setTitle(mItem.);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(mItem.details);
        }

        return rootView;
    }
}
