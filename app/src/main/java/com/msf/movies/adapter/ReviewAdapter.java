package com.msf.movies.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.msf.movies.R;
import com.msf.movies.model.Review;
import com.msf.movies.model.Video;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends ArrayAdapter {

    private List<Review> mReview;
    private int mLayout;


    public ReviewAdapter(Context context, int layout, List<Review> listReview) {
        super(context, layout);
        this.mReview = listReview;
        this.mLayout = layout;
    }

    @Override
    public int getCount() {
        return mReview.size();
    }

    @Nullable
    @Override
    public Review getItem(int position) {
        return mReview.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);
        ReviewViewHolder reviewViewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(mLayout, parent, false);
            reviewViewHolder = new ReviewViewHolder(convertView);
            convertView.setTag(reviewViewHolder);
        } else {
            reviewViewHolder = (ReviewViewHolder) convertView.getTag();
        }
        reviewViewHolder.mContent.setText(review.getContent());
        reviewViewHolder.mAuthor.setText(review.getAuthor());
        return convertView;
    }

    class ReviewViewHolder {
        @BindView(R.id.txt_review_author)
        TextView mAuthor;

        @BindView(R.id.txt_review_content)
        TextView mContent;

        ReviewViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
