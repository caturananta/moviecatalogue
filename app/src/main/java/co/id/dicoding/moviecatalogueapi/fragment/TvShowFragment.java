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
import co.id.dicoding.moviecatalogueapi.adapter.TvShowAdapter;
import co.id.dicoding.moviecatalogueapi.api.ApiClient;
import co.id.dicoding.moviecatalogueapi.constant.Constant;
import co.id.dicoding.moviecatalogueapi.model.TvShow;
import co.id.dicoding.moviecatalogueapi.rest.ListTvShowResponse;
import co.id.dicoding.moviecatalogueapi.util.LanguageUtil;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowFragment extends Fragment {

    private RecyclerView rvTvsShow;
    private ArrayList<TvShow> tvShows;
    private ProgressBar progressBar;
    private GifImageView gifImageView;
    private TextView queryParam;
    public static final String TAG = TvShowFragment.class.getSimpleName();
    private LanguageUtil languageUtil = new LanguageUtil();
    private String lang;
    String apiKey = BuildConfig.TMDB_API_KEY;
    private TvShowAdapter tvShowAdapter;
    private ArrayList<TvShow> queryTvShows;

    public TvShowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_show, container, false);
        queryParam = view.findViewById(R.id.txt_query_tvshow);
        queryParam.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int[] textLocation = new int[2];
                queryParam.getLocationOnScreen(textLocation);

                if (motionEvent.getRawX() >= textLocation[0] + queryParam.getWidth() - queryParam.getTotalPaddingRight()) {

                    showTvShowList(tvShows);
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
        progressBar = view.findViewById(R.id.progressBarTvShow);
        gifImageView = view.findViewById(R.id.gifloadtv);
        queryParam.setVisibility(View.GONE);
        rvTvsShow = view.findViewById(R.id.rv_tv_show);
        rvTvsShow.setHasFixedSize(true);
        Log.d(TAG, "onCreate: " + languageUtil.getPrefLanguage(getActivity()));
        lang = languageUtil.getPrefLanguage(getActivity());
        if (savedInstanceState != null) {
            tvShows = savedInstanceState.getParcelableArrayList(Constant.STATE_RESULT);
            showTvShowList(tvShows);
        } else {
            getTvShows();
        }
        setHasOptionsMenu(true);
    }

    private void getTvShows() {
        showLoading(true);
        final Call<ListTvShowResponse> tvs = ApiClient.getApi().getTvShows(
                apiKey,
                lang.equals("en") ? Constant.API_LANG_ENGLISH : Constant.API_LANG_INDONESIAN);
        tvs.enqueue(new Callback<ListTvShowResponse>() {
            @Override
            public void onResponse(Call<ListTvShowResponse> call, Response<ListTvShowResponse> response) {
                Log.d(TAG, "onResponse: first tv show is " + response.body().getResults().get(0).getName());
                showLoading(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        tvShows = response.body().getResults();
                        showTvShowList(tvShows);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.notif_error_get_data, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ListTvShowResponse> call, Throwable t) {
                showLoading(false);
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void getQueryTvShows(String queryParam) {
        Log.e(TAG, "getQueryMovie: " + queryParam);
        rvTvsShow.setAlpha(0.5f);
        final Call<ListTvShowResponse> movie = ApiClient.getQueryApi().getQueryTvShows(
                apiKey,
                lang.equals(Constant.KEY_LANG_ID) ? Constant.API_LANG_INDONESIAN : Constant.API_LANG_ENGLISH,
                queryParam);
        movie.enqueue(new Callback<ListTvShowResponse>() {
            @Override
            public void onResponse(Call<ListTvShowResponse> call, Response<ListTvShowResponse> response) {
                showLoading(false);
                rvTvsShow.setAlpha(1f);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        queryTvShows = response.body().getResults();
                        showQueryTvShows(queryTvShows);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.notif_not_found, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ListTvShowResponse> call, Throwable t) {
                showLoading(false);
                rvTvsShow.setAlpha(1f);
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
        showLoading(true);
    }

    private void showTvShowList(ArrayList<TvShow> tvs) {
        rvTvsShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        tvShowAdapter = new TvShowAdapter(tvs, getActivity());
        rvTvsShow.setAdapter(tvShowAdapter);
    }

    private void showQueryTvShows(ArrayList<TvShow> tvs) {
        showFloatQuery();
        rvTvsShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        tvShowAdapter = new TvShowAdapter(tvs, getActivity());
        rvTvsShow.setAdapter(tvShowAdapter);
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
        outState.putParcelableArrayList(Constant.STATE_RESULT, tvShows);
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
                getQueryTvShows(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                filter tv show in adapter
//                tvShowAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void hideFloatQuery() {
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

    private void showFloatQuery() {
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
