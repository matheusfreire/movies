package com.msf.movies.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.msf.movies.R;
import com.msf.movies.model.Video;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoAdapter extends ArrayAdapter {

    private List<Video> mVideos;
    private OnClickListenerVideo mClickListener;
    private int mLayout;

    public interface OnClickListenerVideo {
        void onClickVideo(Video video);
    }

    public VideoAdapter(Context context, int layout, List<Video> listVideos, OnClickListenerVideo onClickListenerVideo) {
        super(context, layout);
        this.mVideos = listVideos;
        this.mClickListener = onClickListenerVideo;
        this.mLayout = layout;
    }

    @Override
    public int getCount() {
        return mVideos.size();
    }

    @Nullable
    @Override
    public Video getItem(int position) {
        return mVideos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Video video = getItem(position);
        VideoViewHolder videoHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(mLayout, parent, false);
            videoHolder = new VideoViewHolder(convertView);
            convertView.setTag(videoHolder);
        } else {
            videoHolder = (VideoViewHolder) convertView.getTag();
        }
        videoHolder.mTitle.setText(video.getName());
        return convertView;
    }

    class VideoViewHolder implements View.OnClickListener {
        @BindView(R.id.txt_video_title)
        TextView mTitle;

        VideoViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
