package co.id.dicoding.moviecatalogueapi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.DESCRIPTION;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.ID;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.IMG;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.RELEASE;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.TABLE_NAME;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.TITLE;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.VOTE;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbmovie";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_NAME,
            _ID,
            ID,
            TITLE,
            DESCRIPTION,
            RELEASE,
            VOTE,
            IMG
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
