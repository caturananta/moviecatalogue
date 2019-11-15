package co.id.dicoding.moviecatalogueapi.widgets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import co.id.dicoding.moviecatalogueapi.R;
import co.id.dicoding.moviecatalogueapi.constant.Constant;
import co.id.dicoding.moviecatalogueapi.database.DatabaseHelper;
import co.id.dicoding.moviecatalogueapi.model.FavMovie;

import static android.provider.BaseColumns._ID;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.DESCRIPTION;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.IMG;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.RELEASE;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.TABLE_NAME;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.TITLE;
import static co.id.dicoding.moviecatalogueapi.database.DatabaseContract.MovieColumn.VOTE;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final String TAG = StackRemoteViewsFactory.class.getSimpleName();
    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private final Context mContext;

    private SQLiteDatabase database;
    private static final String DATABASE_TABLE = TABLE_NAME;
    private ArrayList<FavMovie> favMovies;
    private final DatabaseHelper dataBaseHelper;

    public StackRemoteViewsFactory(Context context) {
        mContext = context;
        dataBaseHelper = new DatabaseHelper(context);
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: start");
        open();
        favMovies = getAllFavMovie();
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    @Override
    public void onDataSetChanged() {
        for (FavMovie fav : favMovies) {
            try {
                mWidgetItems.add(Glide.with(mContext).asBitmap().load(Constant.POSTER_URL + fav.getImg()).submit().get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        if (mWidgetItems.isEmpty()) {
            rv.setEmptyView(R.id.stack_view, R.id.empty_view);
        } else {
            rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(i));
        }
        Bundle extras = new Bundle();
        extras.putInt(FavMovieWidget.EXTRA_ITEM, i);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
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

}
