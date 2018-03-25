package com.example.android.popular_movie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popular_movie.data.MovieContract;
import com.example.android.popular_movie.data.MovieDbHelper;
import com.example.android.popular_movie.utilities.MovieJsonUtils;
import com.example.android.popular_movie.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<MovieDetail[]> {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private String mOrderBy;


    private static final int MOVIE_LOADER_ID = 0;
    private static final int FAVORITE_MOVIE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        MovieDbHelper dbHelper = new MovieDbHelper(this);

        if (mOrderBy == null)
            mOrderBy = getString(R.string.popular);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(getString(R.string.lifecicle_callBack))) {
                mOrderBy = savedInstanceState.getString(getString(R.string.lifecicle_callBack));
            }
        }

        if (mOrderBy == getString(R.string.favorite_choice)) {
            getSupportLoaderManager().initLoader(FAVORITE_MOVIE_LOADER_ID, null, this);
        } else {
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        }

    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param moreDetails The detail for the movie that was clicked
     */
    @Override
    public void onClick(MovieDetail moreDetails) {
        Context context = this;
        Class destinationClass = MovieDetailsActivity.class;
        Intent intent = new Intent(context, destinationClass);
        intent.putExtra(getString(R.string.movie_detail), moreDetails);
        startActivity(intent);
    }


    @Override
    public Loader<MovieDetail[]> onCreateLoader(final int id, final Bundle args) {

        return new AsyncTaskLoader<MovieDetail[]>(this) {

            MovieDetail[] mMovieData = null;

            @Override
            protected void onStartLoading() {

                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public MovieDetail[] loadInBackground() {
                MovieDetail[] movie_detail = null;

                try {
                    if (id == MOVIE_LOADER_ID) {
                        URL movieRequestUrl = NetworkUtils.buildUrl(mOrderBy);
                        String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                        movie_detail = MovieJsonUtils.getMovieDetailFromJson(MainActivity.this, jsonMovieResponse);
                    }

                    if (id == FAVORITE_MOVIE_LOADER_ID) {
                        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                null);
                        movie_detail = getMovieDetailFromCursor(cursor);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return movie_detail;
            }

            public void deliverResult(MovieDetail[] data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieDetail[]> loader, MovieDetail[] movieDetails) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieAdapter.setMovieData(movieDetails);
        if (movieDetails != null) {
            showMovieView();
        } else {
            if (mOrderBy == getString(R.string.favorite_choice)) {
                showErrorMessage(getString(R.string.list_favorite_empty));
            } else {
                showErrorMessage(getString(R.string.error_message));
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieDetail[]> loader) {

    }

    private void invalidateData() {
        mMovieAdapter.setMovieData(null);
    }

    private void showMovieView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String mess) {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setText(mess);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_mostPopular) {
            mOrderBy = getString(R.string.popular);
            invalidateData();
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            return true;
        }

        if (id == R.id.action_topRated) {
            mOrderBy = getString(R.string.topRated);
            invalidateData();
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            return true;
        }

        if (id == R.id.action_favorite) {
            mOrderBy = getString(R.string.favorite_choice);
            invalidateData();
            getSupportLoaderManager().restartLoader(FAVORITE_MOVIE_LOADER_ID, null, this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(getString(R.string.lifecicle_callBack), mOrderBy);
        super.onSaveInstanceState(outState);
    }


    public MovieDetail[] getMovieDetailFromCursor(Cursor cursor) {
        MovieDetail[] movieDetails = null;

        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getCount();
            movieDetails = new MovieDetail[count];
            int i = 0;
            do {
                int idMovieCol = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID_MOVIE);
                int originalTitleCol = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
                int relativePosterPathCol = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELATIVE_POSTER_PATH);
                int voteAverageCol = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                int releaseDateCol = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
                int overviewCol = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);

                String idMovie = cursor.getString(idMovieCol);
                String originalTitle = cursor.getString(originalTitleCol);
                String relativePosterPath = cursor.getString(relativePosterPathCol);
                String voteAverage = cursor.getString(voteAverageCol);
                String releaseDate = cursor.getString(releaseDateCol);
                String overview = cursor.getString(overviewCol);

                MovieDetail movieDetail = new MovieDetail(idMovie, relativePosterPath, originalTitle, overview, voteAverage, releaseDate);
                movieDetails[i] = movieDetail;
                i++;

            } while (cursor.moveToNext()); //move to next row in the query result
            cursor.close();
        }

        return movieDetails;
    }


}
