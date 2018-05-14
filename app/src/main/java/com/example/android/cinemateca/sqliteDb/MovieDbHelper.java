
package com.example.android.cinemateca.sqliteDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.cinemateca.model.MovieResult;

import java.util.ArrayList;
import java.util.List;

//This class helps us to create, open and manage database connections(allows also read/write of db)
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritemovies.db";

    //Database version. If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    //Constructs a new instance of InventoryDbHelper
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the products table
        String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + MovieContract.FavoriteEntry.TABLE_NAME + " ("
                + MovieContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.FavoriteEntry.COLUMN_NAME_MOVIEID + " INTEGER, "
                + MovieContract.FavoriteEntry.COLUMN_NAME_POSTER + " TEXT NOT NULL, "
                + MovieContract.FavoriteEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, "
                + MovieContract.FavoriteEntry.COLUMN_NAME_PLOT + " TEXT NOT NULL, "
                + MovieContract.FavoriteEntry.COLUMN_NAME_RELEASEDATE + " TEXT NOT NULL, "
                + MovieContract.FavoriteEntry.COLUMN_NAME_RATING + " REAL NOT NULL);";

        // Execute the SQL statement (the sql onCreate table)
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // The database is still at version 1, so there's nothing to do be done here.
    }

    public void addFavoriteMovie(MovieResult.ResultsBean movie) {
        // Get writable database
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MovieContract.FavoriteEntry.COLUMN_NAME_MOVIEID, movie.getId());
        values.put(MovieContract.FavoriteEntry.COLUMN_NAME_POSTER, movie.getPoster_path());
        values.put(MovieContract.FavoriteEntry.COLUMN_NAME_TITLE, movie.getOriginal_title());
        values.put(MovieContract.FavoriteEntry.COLUMN_NAME_PLOT, movie.getOverview());
        values.put(MovieContract.FavoriteEntry.COLUMN_NAME_RELEASEDATE, movie.getRelease_date());
        values.put(MovieContract.FavoriteEntry.COLUMN_NAME_RATING, movie.getVote_average());

        database.insert(MovieContract.FavoriteEntry.TABLE_NAME, null, values);
        database.close();
    }

    public void deleteFavorite(int id) {
        // Get writable database
        SQLiteDatabase database = this.getWritableDatabase();
        // Delete all rows that match the selection and selection args
        database.delete(MovieContract.FavoriteEntry.TABLE_NAME, MovieContract.FavoriteEntry.COLUMN_NAME_MOVIEID + "=" + id, null);
    }

    public List<MovieResult.ResultsBean> getFavoriteMoviesList() {
        String[] columns = {
                MovieContract.FavoriteEntry._ID,
                MovieContract.FavoriteEntry.COLUMN_NAME_MOVIEID,
                MovieContract.FavoriteEntry.COLUMN_NAME_POSTER,
                MovieContract.FavoriteEntry.COLUMN_NAME_TITLE,
                MovieContract.FavoriteEntry.COLUMN_NAME_PLOT,
                MovieContract.FavoriteEntry.COLUMN_NAME_RELEASEDATE,
                MovieContract.FavoriteEntry.COLUMN_NAME_RATING
        };

        String sortOrder = MovieContract.FavoriteEntry._ID + " ASC";

        List<MovieResult.ResultsBean> favoriteMoviesList = new ArrayList<>();

        // Get readable database
        SQLiteDatabase database = this.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = database.query(MovieContract.FavoriteEntry.TABLE_NAME,
                columns, null, null, null, null, sortOrder);

        if (cursor.moveToFirst()) {
            do {
                MovieResult.ResultsBean movie = new MovieResult.ResultsBean();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_NAME_MOVIEID))));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_NAME_POSTER)));
                movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_NAME_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_NAME_PLOT)));
                movie.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_NAME_RELEASEDATE)));
                movie.setVote_average(Double.parseDouble(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_NAME_RATING))));

                favoriteMoviesList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return favoriteMoviesList;
    }
}

