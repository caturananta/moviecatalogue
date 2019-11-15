package co.id.dicoding.moviecatalogueapi.helper;

import android.database.Cursor;

import java.util.ArrayList;

import co.id.dicoding.moviecatalogueapi.model.FavMovie;

import static android.provider.BaseColumns._ID;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.DESCRIPTION;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.IMG;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.RELEASE;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.TITLE;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.VOTE;

public class MappingHelper {
    public static ArrayList<FavMovie> mapCursorToArrayList(Cursor cursor) {
        ArrayList<FavMovie> favMovieList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION));
            String img = cursor.getString(cursor.getColumnIndexOrThrow(IMG));
            String vote = cursor.getString(cursor.getColumnIndexOrThrow(VOTE));
            String release = cursor.getString(cursor.getColumnIndexOrThrow(RELEASE));
            favMovieList.add(new FavMovie(id, title, description, release, img, vote));
        }
        return favMovieList;
    }
}
