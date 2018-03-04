package com.example.android.popular_movie.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;



/**
 * These utilities will be used to communicate with the server.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static String my_api_key = "";


    /**
     * Builds the URL used to talk to the server.
     *
     * @param orderBy The order that will be queried for.
     * @return The URL to use to query the movie db server.
     */

    public static URL buildUrl(String orderBy) {

        String movie_base_url ="http://api.themoviedb.org/3/movie";
        String api_key = "api_key";

        Uri builtUri = Uri.parse(movie_base_url).buildUpon()
                .appendPath(orderBy)
                .appendQueryParameter(api_key, my_api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI" + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}