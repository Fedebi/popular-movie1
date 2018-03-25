
package com.example.android.popular_movie.utilities;

import android.content.Context;

import com.example.android.popular_movie.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions to handle the movie db JSON data.
 */
public final class MovieJsonUtils {

    /**
     * @param movieJsonStr JSON response from server
     * @return Array of MovieDetail
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static MovieDetail[] getMovieDetailFromJson(Context context, String movieJsonStr) throws JSONException {

        //I use it for check if there is an error(for example a wrong APIkey)
        final String SUCCESS = "success";

        /* Each movie info is an element of the "result" array */
        final String MOVIE_RESULTS = "results";
        final String MOVIE_ID = "id";
        final String MOVIE_ORIGINAL_TITLE = "original_title";
        final String MOVIE_RELATIVE_POSTER_PATH = "poster_path";
        final String MOVIE_VOTE_AVERAGE = "vote_average";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_OVERVIEW = "overview";

        /* MovieDetail array to hold each movie detail*/
        MovieDetail[] arrayMovieDetails = null;
        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        if (movieJson.has(SUCCESS)) {
            boolean isSuccess = movieJson.getBoolean(SUCCESS);
            if (!isSuccess)
                return null;
        }

        if (movieJson.has(MOVIE_RESULTS)) {
            JSONArray movieArray = movieJson.getJSONArray(MOVIE_RESULTS);
            if (movieArray != null) {
                arrayMovieDetails = new MovieDetail[movieArray.length()];

                for (int i = 0; i < movieArray.length(); i++) {

            /* Get the JSON object representing the movie */
                    JSONObject result = movieArray.getJSONObject(i);

                    String movie_id = "";
                    if (result.has(MOVIE_ID)) {
                        movie_id = result.optString(MOVIE_ID);
                    }
                    String poster_path = "";
                    if (result.has(MOVIE_RELATIVE_POSTER_PATH)) {
                        poster_path = result.optString(MOVIE_RELATIVE_POSTER_PATH);
                    }
                    String original_title = "";
                    if (result.has(MOVIE_ORIGINAL_TITLE)) {
                        original_title = result.optString(MOVIE_ORIGINAL_TITLE);
                    }
                    String release_date = "";
                    if (result.has(MOVIE_RELEASE_DATE)) {
                        release_date = result.optString(MOVIE_RELEASE_DATE);
                    }
                    String overview = "";
                    if (result.has(MOVIE_OVERVIEW)) {
                        overview = result.optString(MOVIE_OVERVIEW);
                    }
                    String vote_average = "";
                    if (result.has(MOVIE_VOTE_AVERAGE)) {
                        vote_average = result.optString(MOVIE_VOTE_AVERAGE);
                    }

                    MovieDetail movieDetail = new MovieDetail(movie_id, poster_path, original_title, overview, vote_average, release_date);

                    arrayMovieDetails[i] = movieDetail;
                }
            }
        }
        return arrayMovieDetails;
    }


    public static TrailerDetail[] getTrailerDetailFromJson(Context context, String trailerJsonStr) throws JSONException {

        final String STATUS_CODE = "status_code";
        final String TRAILER_YOUTUBE = "youtube";
        final String TRAILER_NAME = "name";
        final String TRAILER_SOURCE = "source";

        TrailerDetail[] arrayTrailerDetails = null;
        JSONObject trailerJson = new JSONObject(trailerJsonStr);

        /* Is there an error? */
        if (trailerJson.has(STATUS_CODE)) {
            int status = trailerJson.getInt(STATUS_CODE);
            if (status>0)
                return null;
        }

        if (trailerJson.has(TRAILER_YOUTUBE)) {
            JSONArray trailerArray = trailerJson.getJSONArray(TRAILER_YOUTUBE);
            if (trailerArray != null) {
                arrayTrailerDetails = new TrailerDetail[trailerArray.length()];

                for (int i = 0; i < trailerArray.length(); i++) {

                    JSONObject obj = trailerArray.getJSONObject(i);

                    String trailer_name = "";
                    if (obj.has(TRAILER_NAME)) {
                        trailer_name = obj.optString(TRAILER_NAME);
                    }
                    String trailer_source = "";
                    if (obj.has(TRAILER_SOURCE)) {
                        trailer_source = obj.optString(TRAILER_SOURCE);
                    }

                    TrailerDetail trailerDetail = new TrailerDetail(trailer_name, trailer_source);

                    arrayTrailerDetails[i] = trailerDetail;
                }
            }
        }

        return arrayTrailerDetails;
    }


    public static ReviewDetail[] getReviewDetailFromJson(Context context, String reviewJsonStr) throws JSONException {

        final String STATUS_CODE = "status_code";

        final String REVIEW_RESULTS = "results";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";
        final String REVIEW_URL = "url";

        ReviewDetail[] arrayReviewDetails = null;
        JSONObject reviewJson = new JSONObject(reviewJsonStr);

         /* Is there an error? */
        if (reviewJson.has(STATUS_CODE)) {
            int status = reviewJson.getInt(STATUS_CODE);
            if (status>0)
                return null;
        }

        if (reviewJson.has(REVIEW_RESULTS)) {
            JSONArray reviewArray = reviewJson.getJSONArray(REVIEW_RESULTS);
            if (reviewArray != null) {
                arrayReviewDetails = new ReviewDetail[reviewArray.length()];

                for (int i = 0; i < reviewArray.length(); i++) {

                    JSONObject obj = reviewArray.getJSONObject(i);

                    String review_author = "";
                    if (obj.has(REVIEW_AUTHOR)) {
                        review_author = obj.optString(REVIEW_AUTHOR);
                    }
                    String review_content = "";
                    if (obj.has(REVIEW_CONTENT)) {
                        review_content = obj.optString(REVIEW_CONTENT);
                    }

                    String review_url = "";
                    if (obj.has(REVIEW_URL)) {
                        review_url = obj.optString(REVIEW_URL);
                    }

                    ReviewDetail reviewDetail = new ReviewDetail(review_author, review_content, review_url);

                    arrayReviewDetails[i] = reviewDetail;
                }
            }
        }

        return arrayReviewDetails;
    }

}