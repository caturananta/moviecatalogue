package co.id.dicoding.moviecatalogueapi.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import co.id.dicoding.moviecatalogueapi.R;
import co.id.dicoding.moviecatalogueapi.constant.Constant;
import co.id.dicoding.moviecatalogueapi.helper.FavMovieHelper;
import co.id.dicoding.moviecatalogueapi.model.FavMovie;
import co.id.dicoding.moviecatalogueapi.model.Movie;
import co.id.dicoding.moviecatalogueapi.model.TvShow;
import co.id.dicoding.moviecatalogueapi.widgets.FavMovieWidget;
import pl.droidsonroids.gif.GifImageView;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 100;
    public static final int RESULT_CODE = 101;
    private FavMovieHelper favMovieHelper;

    private ImageView imgPoster, imgPosterBg;
    private TextView txtTitle, txtYear, txtRating, txtDescription;
    private Movie movie;
    private TvShow tvShow;
    private Button btnAddToFav, btnShare, btnDelete;
    private FavMovie favMovie;
    private Boolean isMovie, isFavorite, isUpdate;
    private int position;
    private GifImageView gifImageViewBg, gifImageView;
    private static final String TOAST_ACTION = "co.id.dicoding.moviecatalogueapi.TOAST_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.header_detail_title));
        }

        favMovieHelper = FavMovieHelper.getInstance(this);
        favMovieHelper.open();

        if (getIntent().getStringExtra(Constant.EXTRA_FLAG).equals(Constant.FLAG_MOVIE)) {
            movie = getIntent().getParcelableExtra(Constant.EXTRA_DATA);
            isMovie = true;
            isUpdate = false;
            isFavorite = favMovieHelper.exist(String.valueOf(movie.getId())) == true ? true : false;
            Log.e("IsFavorite", String.valueOf(isFavorite));

        } else if (getIntent().getStringExtra(Constant.EXTRA_FLAG).equals(Constant.FLAG_TVSHOW)) {
            tvShow = getIntent().getParcelableExtra(Constant.EXTRA_DATA);
            isMovie = false;
            isUpdate = false;
            isFavorite = favMovieHelper.exist(String.valueOf(tvShow.getId())) == true ? true : false;
            Log.e("IsFavorite", String.valueOf(isFavorite));
        } else {
            favMovie = getIntent().getParcelableExtra(Constant.EXTRA_DATA);
            position = getIntent().getIntExtra(Constant.EXTRA_POSITION, 0);
            isFavorite = true;
            isUpdate = true;
        }

        imgPosterBg = findViewById(R.id.img_photo_bg);
        imgPoster = findViewById(R.id.img_photo_detail);
        txtTitle = findViewById(R.id.txt_name);
        txtYear = findViewById(R.id.txt_year);
        txtRating = findViewById(R.id.txt_rating);
        txtDescription = findViewById(R.id.txt_description);
        btnAddToFav = findViewById(R.id.btnFavorite);
        btnAddToFav.setOnClickListener(this);
        btnDelete = findViewById(R.id.btnRemove);
        btnDelete.setOnClickListener(this);
        gifImageView = findViewById(R.id.gifloaddetail);
        gifImageViewBg = findViewById(R.id.gifloaddetailbg);

        init();

    }

    private void init() {

        String errTranslate = getString(R.string.notif_err_translate);

        if (isUpdate) {
            btnAddToFav.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
            btnAddToFav.setVisibility(View.VISIBLE);
            if (isFavorite) {
                btnAddToFav.setBackgroundResource(R.drawable.btn_active);
            } else {
                btnAddToFav.setBackgroundResource(R.drawable.btn_innactive);
            }
        }

        if (getIntent().getStringExtra(Constant.EXTRA_FLAG).equals(Constant.FLAG_MOVIE)) {
            txtTitle.setText(movie.getTitle());
            txtYear.setText(movie.getRelease_date().split("-")[0]);
            txtRating.setText(movie.getVote_average().toString());
            txtDescription.setText(movie.getOverview().equals("") ? errTranslate : movie.getOverview());
            Glide.with(this)
                    .load(Constant.POSTER_URL + movie.getPoster_path())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            showLoading(true);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            showLoading(false);
                            return false;
                        }
                    })
                    .into(imgPoster);
            Glide.with(this)
                    .load(Constant.POSTER_URL + movie.getPoster_path())
                    .centerCrop()
                    .into(imgPosterBg);
        } else if (getIntent().getStringExtra(Constant.EXTRA_FLAG).equals(Constant.FLAG_TVSHOW)) {
            txtTitle.setText(tvShow.getName());
            txtYear.setText(tvShow.getFirst_air_date().split("-")[0]);
            txtRating.setText(tvShow.getVote_average().toString());
            txtDescription.setText(tvShow.getOverview().equals("") ? errTranslate : tvShow.getOverview());
            Glide.with(this)
                    .load(Constant.POSTER_URL + tvShow.getPoster_path())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            showLoading(true);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            showLoading(false);
                            return false;
                        }
                    })
                    .into(imgPoster);
            Glide.with(this)
                    .load(Constant.POSTER_URL + tvShow.getPoster_path())
                    .centerCrop()
                    .into(imgPosterBg);
        } else {
            txtTitle.setText(favMovie.getTitle());
            txtYear.setText(favMovie.getRelease().split("-")[0]);
            txtRating.setText(favMovie.getVote().toString());
            txtDescription.setText(favMovie.getDescription().equals("") ? errTranslate : favMovie.getDescription());
            Glide.with(this)
                    .load(Constant.POSTER_URL + favMovie.getImg())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            showLoading(true);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            showLoading(false);
                            return false;
                        }
                    })
                    .into(imgPoster);
            Glide.with(this)
                    .load(Constant.POSTER_URL + favMovie.getImg())
                    .centerCrop()
                    .into(imgPosterBg);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFavorite:
                if (isFavorite) {
                    Toast.makeText(DetailActivity.this, R.string.notif_err_already_insert, Toast.LENGTH_SHORT).show();
                } else {

                    btnAddToFav.setBackgroundResource(R.drawable.btn_active);
                    isFavorite = true;
                    favMovie = new FavMovie();
                    if (isMovie) {
                        favMovie.setId(movie.getId());
                        favMovie.setTitle(movie.getTitle());
                        favMovie.setVote(movie.getVote_average().toString());
                        favMovie.setRelease(movie.getRelease_date());
                        favMovie.setDescription(movie.getOverview());
                        favMovie.setImg(movie.getPoster_path());
                    } else {
                        favMovie.setId(tvShow.getId());
                        favMovie.setTitle(tvShow.getName());
                        favMovie.setVote(tvShow.getVote_average().toString());
                        favMovie.setRelease(tvShow.getFirst_air_date());
                        favMovie.setDescription(tvShow.getOverview());
                        favMovie.setImg(tvShow.getPoster_path());
                    }

                    long result = favMovieHelper.insertFavMovie(favMovie);

                    if (result > 0) {
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                        Intent intent = new Intent(this, FavMovieWidget.class);
                        intent.setAction(appWidgetManager.ACTION_APPWIDGET_UPDATE);
                        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(getApplication(), FavMovieWidget.class));
                        intent.putExtra(appWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                        sendBroadcast(intent);

                        Toast.makeText(DetailActivity.this, R.string.notif_scs_insert, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, R.string.notif_err_insert, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnRemove:
                showAlertDialog();
                break;
        }
    }

    private void showAlertDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(R.string.notif_delete_favorite);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.choose_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        long result = favMovieHelper.deleteFavMovie(favMovie.getId());
                        if (result > 0) {
                            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                            Intent widgetIntent = new Intent(DetailActivity.this, FavMovieWidget.class);
                            widgetIntent.setAction(appWidgetManager.ACTION_APPWIDGET_UPDATE);
                            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(getApplication(), FavMovieWidget.class));
                            widgetIntent.putExtra(appWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                            sendBroadcast(widgetIntent);

                            Intent intent = new Intent();
                            intent.putExtra(Constant.EXTRA_POSITION, position);
                            setResult(RESULT_CODE, intent);
                            finish();
                        } else {
                            Toast.makeText(DetailActivity.this, R.string.notif_err_delete, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.choose_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void showLoading(Boolean state) {
        if (state) {
            gifImageView.setVisibility(View.VISIBLE);
            gifImageViewBg.setVisibility(View.VISIBLE);
        } else {
            gifImageView.setVisibility(View.GONE);
            gifImageViewBg.setVisibility(View.GONE);
        }
    }

    private class RemoveMovies extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {


            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }
    }
}
