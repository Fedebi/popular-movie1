
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
     *
     * @param movieJsonStr JSON response from server
     *
     * @return Array of MovieDetail
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static MovieDetail[] getMovieDetailFromJson(Context context, String movieJsonStr)throws JSONException {

        //I use it for check if there is an error(for example a wrong APIkey)
        final String SUCCESS = "success";

        /* Each movie info is an element of the "result" array */
        final String MOVIE_RESULTS = "results";
        final String MOVIE_ORIGINAL_TITLE = "original_title";
        final String MOVIE_RELATIVE_POSTER_PATH= "poster_path";
        final String MOVIE_VOTE_AVERAGE= "vote_average";
        final String MOVIE_RELEASE_DATE= "release_date";
        final String MOVIE_OVERVIEW= "overview";

        /* MovieDetail array to hold each movie detail*/
        MovieDetail[] arrayMovieDetails;
        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        if (movieJson.has(SUCCESS)) {
            boolean isSuccess = movieJson.getBoolean(SUCCESS);
            if(!isSuccess)
                return null;
        }

        JSONArray movieArray = movieJson.getJSONArray(MOVIE_RESULTS);
        arrayMovieDetails = new MovieDetail[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            /* Get the JSON object representing the movie */
            JSONObject result = movieArray.getJSONObject(i);

            String poster_path ="";
            if(result.has(MOVIE_RELATIVE_POSTER_PATH)){
                poster_path = result.optString(MOVIE_RELATIVE_POSTER_PATH);
            }
            String original_title ="";
            if(result.has(MOVIE_ORIGINAL_TITLE)){
                original_title = result.optString(MOVIE_ORIGINAL_TITLE);
            }
            String release_date ="";
            if(result.has(MOVIE_RELEASE_DATE)){
                release_date = result.optString(MOVIE_RELEASE_DATE);
            }
            String overview ="";
            if(result.has(MOVIE_OVERVIEW)){
                overview = result.optString(MOVIE_OVERVIEW);
            }
            String vote_average = "";
            if(result.has(MOVIE_VOTE_AVERAGE)){
                vote_average = result.optString(MOVIE_VOTE_AVERAGE);
            }

            MovieDetail movieDetail = new MovieDetail(poster_path,original_title,overview, vote_average, release_date);

            arrayMovieDetails[i] = movieDetail;
        }

        return arrayMovieDetails;
    }

}