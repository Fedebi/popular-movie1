package com.example.android.popular_movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popular_movie.utilities.TrailerDetail;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private TrailerDetail[] mTrailerDetail;
    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(TrailerDetail moreDetails);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final TextView mTrailerView;

        public TrailerAdapterViewHolder(View view) {
            super(view);

            mTrailerView = (TextView)view.findViewById(R.id.tv_trailer);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            TrailerDetail trailerDetail = mTrailerDetail[adapterPosition];
            mClickHandler.onClick(trailerDetail);
        }
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        String trailerName = mTrailerDetail[position].getName();
        trailerAdapterViewHolder.mTrailerView.setText(trailerName);
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerDetail) return 0;
        return mTrailerDetail.length;
    }

    public void setTrailerData(TrailerDetail[] trailerDetails) {
        mTrailerDetail = trailerDetails;
        notifyDataSetChanged();
    }
}