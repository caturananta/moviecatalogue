package co.id.dicoding.moviecatalogueapi.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.id.dicoding.moviecatalogueapi.BuildConfig;
import co.id.dicoding.moviecatalogueapi.R;
import co.id.dicoding.moviecatalogueapi.adapter.MovieAdapter;
import co.id.dicoding.moviecatalogueapi.api.ApiClient;
import co.id.dicoding.moviecatalogueapi.constant.Constant;
import co.id.dicoding.moviecatalogueapi.model.Movie;
import co.id.dicoding.moviecatalogueapi.rest.ListMovieResponse;
import co.id.dicoding.moviecatalogueapi.util.LanguageUtil;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment {

    private RecyclerView rvMovies;
    private ArrayList<Movie> arrMovie;
    private ProgressBar progressBar;
    private GifImageView gifImageView;
    private TextView queryParam;
    public static final String TAG = MovieFragment.class.getSimpleName();
    private LanguageUtil languageUtil = new LanguageUtil();
    private String lang;
    String apiKey = BuildConfig.TMDB_API_KEY;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> queryMovie;

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        queryParam = view.findViewById(R.id.txt_query_movie);
        queryParam.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int[] textLocation = new int[2];
                queryParam.getLocationOnScreen(textLocation);

                if(motionEvent.getRawX() >= textLocation[0] + queryParam.getWidth() - queryParam.getTotalPaddingRight()){

                    showMovieList(arrMovie);
                    hideFloatQuery();

                    return true;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBarMovie);
        gifImageView = view.findViewById(R.id.gifload);
        queryParam.setVisibility(View.GONE);
        rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setHasFixedSize(true);
        Log.d(TAG, "onCreate: " + languageUtil.getPrefLanguage(getActivity()));
        lang = languageUtil.getPrefLanguage(getActivity());
        if (savedInstanceState != null) {
            arrMovie = savedInstanceState.getParcelableArrayList(Constant.STATE_RESULT);
            showMovieList(arrMovie);
        } else {
            getMovies();
        }
        setHasOptionsMenu(true);
    }

    private void getMovies() {
        showLoading(true);
        Call<ListMovieResponse> movies = ApiClient.getApi().getMovies(
                apiKey,
                lang.equals(Constant.KEY_LANG_ID) ? Constant.API_LANG_INDONESIAN : Constant.API_LANG_ENGLISH);
        movies.enqueue(new Callback<ListMovieResponse>() {
            @Override
            public void onResponse(Call<ListMovieResponse> call, Response<ListMovieResponse> response) {
                showLoading(false);
                Log.d(TAG, "onResponse: first movie is " + response.body().getResults().get(0).getId());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        arrMovie = response.body().getResults();
                        showMovieList(arrMovie);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.notif_error_get_data, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ListMovieResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void getQueryMovie(String queryParam) {
        Log.e(TAG, "getQueryMovie: " + queryParam);
        rvMovies.setAlpha(0.5f);
        final Call<ListMovieResponse> movie = ApiClient.getQueryApi().getQueryMovies(
                apiKey,
                lang.equals(Constant.KEY_LANG_ID) ? Constant.API_LANG_INDONESIAN : Constant.API_LANG_ENGLISH,
                queryParam);
        movie.enqueue(new Callback<ListMovieResponse>() {
            @Override
            public void onResponse(Call<ListMovieResponse> call, Response<ListMovieResponse> response) {
                showLoading(false);
                rvMovies.setAlpha(1f);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        queryMovie = response.body().getResults();
                        showQueryMovie(queryMovie);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.notif_not_found, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ListMovieResponse> call, Throwable t) {
                showLoading(false);
                rvMovies.setAlpha(1f);
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
        showLoading(true);
    }

    private void showMovieList(ArrayList<Movie> movies) {
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        movieAdapter = new MovieAdapter(movies, getActivity());
        rvMovies.setAdapter(movieAdapter);
    }

    private void showQueryMovie(ArrayList<Movie> movies) {
        showFloatQuery();
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        movieAdapter = new MovieAdapter(movies, getActivity());
        rvMovies.setAdapter(movieAdapter);
    }

    private void showLoading(Boolean state) {
        if (state) {
//            progressBar.setVisibility(View.VISIBLE);
            gifImageView.setVisibility(View.VISIBLE);
        } else {
//            progressBar.setVisibility(View.GONE);
            gifImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constant.STATE_RESULT, arrMovie);
    }

    private String queryText;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (queryParam.getVisibility() == View.VISIBLE) {
                    hideFloatQuery();
                }
                queryText = query;
                getQueryMovie(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(TAG, "onQueryTextChange: "+newText );
//                filter movie in adapter
//                movieAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void hideFloatQuery(){
        queryParam.animate()
                .translationY(0)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        queryParam.setVisibility(View.GONE);
                    }
                });
    }

    private void showFloatQuery(){
        queryParam.animate()
                .translationY(0)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationCancel(animation);
                        queryParam.setVisibility(View.VISIBLE);
                        queryParam.setText(getString(R.string.content_query_result)+" "+queryText);
                    }
                });
    }
}
