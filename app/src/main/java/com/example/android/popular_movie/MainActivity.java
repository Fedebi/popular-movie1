package com.example.android.popular_movie;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.example.android.popular_movie.utilities.MovieJsonUtils;
import com.example.android.popular_movie.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private String mOrderBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mOrderBy = getString(R.string.popular);

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovie();
    }

    private void loadMovie() {
        showMovieView();
        new FetchMovieTask().execute(mOrderBy);
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

    private void showMovieView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    public class FetchMovieTask extends AsyncTask<String, Void, MovieDetail[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieDetail[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String order = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(order);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                return  MovieJsonUtils.getMovieDetailFromJson(MainActivity.this, jsonMovieResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieDetail[] movieDetails) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieDetails != null) {
                showMovieView();
                mMovieAdapter.setMovieData(movieDetails);
            } else {
                showErrorMessage();
            }
        }
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
            mMovieAdapter.setMovieData(null);
            mOrderBy = getString(R.string.popular);
            loadMovie();
            return true;
        }

        if (id == R.id.action_topRated) {
            mMovieAdapter.setMovieData(null);
            mOrderBy = getString(R.string.topRated);
            loadMovie();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
