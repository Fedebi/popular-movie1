package com.example.android.popular_movie;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popular_movie.data.MovieContract;
import com.example.android.popular_movie.utilities.MovieJsonUtils;
import com.example.android.popular_movie.utilities.NetworkUtils;
import com.example.android.popular_movie.utilities.ReviewDetail;
import com.example.android.popular_movie.utilities.TrailerDetail;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Object[]>, ReviewAdapter.ReviewAdapterOnClickHandler {

    private MovieDetail movieDetail;
    private Boolean movieAlreadyExistInFavoriteList;
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private static final int TRAILER_LOADER_ID = 15;

    private RecyclerView mRecyclerViewReview;
    private ReviewAdapter mReviewAdapter;
    private static final int REVIEW_LOADER_ID = 16;

    private static final String URL_YOUTUBE = "http://www.youtube.com/watch?v=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ImageView mImage = (ImageView) findViewById(R.id.iv_movie);
        TextView mOriginal_Title = (TextView) findViewById(R.id.tv_Title);
        TextView mOverview = (TextView) findViewById(R.id.tv_Synopsis);
        TextView mRelease_date = (TextView) findViewById(R.id.tv_release_date);
        TextView mVote_average = (TextView) findViewById(R.id.tv_vote);

        movieAlreadyExistInFavoriteList = false;

        final Button mButton = (Button) findViewById(R.id.button_favorite);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieAlreadyExistInFavoriteList) {
                    // Delete a single row of data using a ContentResolver
                    int moviesDeleted = getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                            MovieContract.MovieEntry.COLUMN_ID_MOVIE + "=?",
                            new String[]{movieDetail.getMovie_id()});

                    if (moviesDeleted != 0) {
                        movieAlreadyExistInFavoriteList = false;
                        mButton.setText(getString(R.string.movie_button_favorite));
                    }
                } else {

                    // Insert new movie data via a ContentResolver
                    // Create new empty ContentValues object
                    ContentValues cv = new ContentValues();
                    cv.put(MovieContract.MovieEntry.COLUMN_ID_MOVIE, movieDetail.getMovie_id());
                    cv.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movieDetail.getOriginal_title());
                    cv.put(MovieContract.MovieEntry.COLUMN_RELATIVE_POSTER_PATH, movieDetail.getPoster_path());
                    cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movieDetail.getVote_average());
                    cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieDetail.getRelease_date());
                    cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieDetail.getOverview());

                    // Insert the content values via a ContentResolver
                    Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);

                    movieAlreadyExistInFavoriteList = true;
                    mButton.setText(getString(R.string.movie_button_remove_favorite));
                }
            }
        });


        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(getString(R.string.movie_detail))) {
                movieDetail = (MovieDetail) intent.getParcelableExtra(getString(R.string.movie_detail));
                if (movieDetail != null) {

                    String moviePosters = movieDetail.getPoster_path();
                    String uri_base = "http://image.tmdb.org/t/p/w185";
                    Uri uri = Uri.parse(uri_base).buildUpon().appendEncodedPath(moviePosters).build();
                    Context context = mImage.getContext();
                    Picasso.with(context).load(uri).into(mImage);

                    String original_title = movieDetail.getOriginal_title();
                    mOriginal_Title.setText(original_title);
                    mOverview.setText(movieDetail.getOverview());
                    mRelease_date.setText(movieDetail.getRelease_date());
                    mVote_average.setText(movieDetail.getVote_average());
                    setTitle(original_title);

                    try {
                        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                                null,
                                MovieContract.MovieEntry.COLUMN_ID_MOVIE + "=?",
                                new String[]{movieDetail.getMovie_id()},
                                null);

                        if (cursor != null && cursor.getCount() > 0) {

                            movieAlreadyExistInFavoriteList = true;
                            mButton.setText(getString(R.string.movie_button_remove_favorite));
                        }
                        cursor.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }

                } else {
                    // Movie details unavailable
                    closeOnError();
                }
            } else {
                // Movie details unavailable
                closeOnError();
            }
        } else {
            // Movie details unavailable
            closeOnError();
        }

        //TRAILER
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailer);
        //mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(this);
        mRecyclerView.setAdapter(mTrailerAdapter);

        getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, null, MovieDetailsActivity.this).forceLoad();

        //REVIEW
        mRecyclerViewReview = (RecyclerView) findViewById(R.id.recyclerview_review);

        LinearLayoutManager layoutManagerReview = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewReview.setLayoutManager(layoutManagerReview);
        mRecyclerViewReview.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter(this);
        mRecyclerViewReview.setAdapter(mReviewAdapter);

        getSupportLoaderManager().initLoader(REVIEW_LOADER_ID, null, MovieDetailsActivity.this);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(TrailerDetail moreDetails) {

        //Uri.parse("http://www.youtube.com/watch?v=" + videoId)
        String url = URL_YOUTUBE + moreDetails.getSource();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public Loader<Object[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Object[]>(this) {

            TrailerDetail[] mTrailerData = null;
            ReviewDetail[] mReviewData = null;

            @Override
            protected void onStartLoading() {
                if (getId() == TRAILER_LOADER_ID) {
                    if (mTrailerData != null) {
                        deliverResult(mTrailerData);
                    } else {
                        forceLoad();
                    }
                }
                if (getId() == REVIEW_LOADER_ID) {
                    if (mReviewData != null) {
                        deliverResult(mReviewData);
                    } else {
                        forceLoad();
                    }
                }
            }

            @Override
            public Object[] loadInBackground() {

                Object[] detail = null;

                if (getId() == TRAILER_LOADER_ID) {
                    try {
                        String id = movieDetail.getMovie_id();
                        URL movieRequestUrl = NetworkUtils.buildUrlTrailerAndReview(id, getString(R.string.trailers));
                        String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                        detail = MovieJsonUtils.getTrailerDetailFromJson(MovieDetailsActivity.this, jsonMovieResponse);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                if (getId() == REVIEW_LOADER_ID) {

                    detail = null;
                    try {
                        String id = movieDetail.getMovie_id();
                        URL movieRequestUrl = NetworkUtils.buildUrlTrailerAndReview(id, getString(R.string.reviews));
                        String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                        detail = MovieJsonUtils.getReviewDetailFromJson(MovieDetailsActivity.this, jsonMovieResponse);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                return detail;
            }

            public void deliverResult(Object[] data) {
                if (getId() == TRAILER_LOADER_ID) {
                    mTrailerData = (TrailerDetail[]) data;
                }
                if (getId() == REVIEW_LOADER_ID) {
                    mReviewData = (ReviewDetail[]) data;
                }

                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Object[]> loader, Object[] details) {
        if (loader.getId() == TRAILER_LOADER_ID) {
            mTrailerAdapter.setTrailerData((TrailerDetail[]) details);
        }
        if (loader.getId() == REVIEW_LOADER_ID) {
            mReviewAdapter.setReviewData((ReviewDetail[]) details);
        }
    }

    @Override
    public void onLoaderReset(Loader<Object[]> loader) {
    }

    private void invalidateData() {
        mTrailerAdapter.setTrailerData(null);
        mReviewAdapter.setReviewData(null);
    }

    @Override
    public void onClick(ReviewDetail moreDetails) {
        String url = moreDetails.getUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
