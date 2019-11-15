package co.id.dicoding.moviecatalogueapi.database;

import android.provider.BaseColumns;

public final class DatabaseContract {

    private DatabaseContract() {
    }

    public static final class MovieColumn implements BaseColumns {
        public static final String TABLE_NAME = "fav_movie";
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String RELEASE = "release";
        public static final String VOTE = "vote";
        public static final String IMG = "img";
    }
}