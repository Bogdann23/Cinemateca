
package com.example.android.cinemateca.sqliteDb;

import android.provider.BaseColumns;

public final class MovieContract {
    // To prevent someone from accidentally instantiating the contract class give it an empty constructor.
    private MovieContract() {
    }

    // Inner class that defines the table contents
    public static class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_NAME_MOVIEID = "movieid";
        public static final String COLUMN_NAME_POSTER = "poster";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PLOT = "description";
        public static final String COLUMN_NAME_RELEASEDATE = "releasedate";
        public static final String COLUMN_NAME_RATING = "rating";

    }
}