package com.example.android.popular_movie.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 2;

    // Constructor
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold movie data
        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.COLUMN_ID_MOVIE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_RELATIVE_POSTER_PATH + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE+ " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                " UNIQUE (" + MovieContract.MovieEntry.COLUMN_ID_MOVIE + ") ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
    }

    // Override the onUpgrade method
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.

       sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
       onCreate(sqLiteDatabase);
    }
}