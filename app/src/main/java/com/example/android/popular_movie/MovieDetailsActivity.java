package com.example.android.popular_movie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ImageView mImage = (ImageView) findViewById(R.id.iv_movie);
        TextView mOriginal_Title = (TextView) findViewById(R.id.tv_Title);
        TextView mOverview = (TextView) findViewById(R.id.tv_Synopsis);
        TextView mRelease_date = (TextView) findViewById(R.id.tv_release_date);
        TextView mVote_average = (TextView) findViewById(R.id.tv_vote);

        Intent intent = getIntent();

        if (intent != null) {
           if(intent.hasExtra(getString(R.string.movie_detail))){
              MovieDetail movieDetail = (MovieDetail)intent.getParcelableExtra(getString(R.string.movie_detail));
              if(movieDetail!= null){

                  String moviePosters = movieDetail.getPoster_path();
                  String uri_base = "http://image.tmdb.org/t/p/w185";
                  Uri uri = Uri.parse(uri_base).buildUpon().appendEncodedPath(moviePosters).build();
                  Context context = mImage.getContext();
                  Picasso.with(context).load(uri).into(mImage);

                  String original_title= movieDetail.getOriginal_title();
                  mOriginal_Title.setText(original_title);
                  mOverview.setText(movieDetail.getOverview());
                  mRelease_date.setText(movieDetail.getRelease_date());
                  mVote_average.setText(movieDetail.getVote_average());
                  setTitle(original_title);
              }
              else {
                  // Movie details unavailable
                  closeOnError();
              }
            }
           else {
               // Movie details unavailable
               closeOnError();
           }
        } else {
            // Movie details unavailable
            closeOnError();
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
