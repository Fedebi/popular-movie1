package com.example.android.popular_movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popular_movie.utilities.ReviewDetail;

/**
 * {@link ReviewAdapter} exposes a list of reviews to a
 * {@link RecyclerView}
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private ReviewDetail[] mReviewDetail;
    private final ReviewAdapterOnClickHandler mClickHandler;

    public interface ReviewAdapterOnClickHandler {
        void onClick(ReviewDetail moreDetails);
    }

    /**
     * Creates a ReviewAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public ReviewAdapter(ReviewAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final TextView mReviewView;

        public ReviewAdapterViewHolder(View view) {
            super(view);

            mReviewView = (TextView)view.findViewById(R.id.tv_review);
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
            ReviewDetail reviewDetail = mReviewDetail[adapterPosition];
            mClickHandler.onClick(reviewDetail);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout.
     * @return A new ReviewAdapterViewHolder that holds the View for each grid item
     */
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ReviewAdapterViewHolder(view);
    }

    /**
     *
     *
     * @param  reviewAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        String trailerName = mReviewDetail[position].getContent();
        reviewAdapterViewHolder.mReviewView.setText(trailerName);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mReviewDetail) return 0;
        return mReviewDetail.length;
    }

    public void setReviewData(ReviewDetail[] reviewDetails) {
        mReviewDetail = reviewDetails;
        notifyDataSetChanged();
    }
}