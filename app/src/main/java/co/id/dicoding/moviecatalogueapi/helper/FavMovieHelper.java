package co.id.dicoding.moviecatalogueapi.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import co.id.dicoding.moviecatalogueapi.database.DatabaseHelper;
import co.id.dicoding.moviecatalogueapi.model.FavMovie;

import static android.provider.BaseColumns._ID;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.DESCRIPTION;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.ID;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.IMG;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.RELEASE;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.TABLE_NAME;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.TITLE;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.VOTE;

public class FavMovieHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private final DatabaseHelper dataBaseHelper;
    private static FavMovieHelper INSTANCE;

    private SQLiteDatabase database;

    public FavMovieHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static FavMovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavMovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen()) {
            database.close();
        }
    }

    public ArrayList<FavMovie> getAllFavMovie() {
        ArrayList<FavMovie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        FavMovie favMovie;
        if (cursor.getCount() > 0) {
            do {
                favMovie = new FavMovie();
                favMovie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                favMovie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                favMovie.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                favMovie.setRelease(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE)));
                favMovie.setVote(cursor.getString(cursor.getColumnIndexOrThrow(VOTE)));
                favMovie.setImg(cursor.getString(cursor.getColumnIndexOrThrow(IMG)));
                arrayList.add(favMovie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertFavMovie(FavMovie favMovie) {
        ContentValues args = new ContentValues();
        args.put(ID, favMovie.getId());
        args.put(TITLE, favMovie.getTitle());
        args.put(DESCRIPTION, favMovie.getDescription());
        args.put(RELEASE, favMovie.getRelease());
        args.put(VOTE, favMovie.getVote());
        args.put(IMG, favMovie.getImg());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteFavMovie(int id) {
        return database.delete(TABLE_NAME, _ID + " = '" + id + "'", null);
    }

    public boolean exist(String value) {
        String sql = "SELECT EXISTS (SELECT * FROM " + TABLE_NAME + " WHERE " + ID + "='" + value + "' LIMIT 1)";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();

        if (cursor.getInt(0) == 1) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }
}
