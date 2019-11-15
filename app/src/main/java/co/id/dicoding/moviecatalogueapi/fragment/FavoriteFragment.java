package co.id.dicoding.moviecatalogueapi.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import co.id.dicoding.moviecatalogueapi.R;
import co.id.dicoding.moviecatalogueapi.activity.DetailActivity;
import co.id.dicoding.moviecatalogueapi.adapter.FavMovieAdapter;
import co.id.dicoding.moviecatalogueapi.constant.Constant;
import co.id.dicoding.moviecatalogueapi.helper.FavMovieHelper;
import co.id.dicoding.moviecatalogueapi.helper.LoadFavMovieCallback;
import co.id.dicoding.moviecatalogueapi.model.FavMovie;
import co.id.dicoding.moviecatalogueapi.util.LanguageUtil;

public class FavoriteFragment extends Fragment implements LoadFavMovieCallback {

    private RecyclerView rvFavMovies;
    private ArrayList<FavMovie> arrFavMovie;
    private ProgressBar progressBar;
    public static final String TAG = FavoriteFragment.class.getSimpleName();
    private LanguageUtil languageUtil = new LanguageUtil();
    private String lang;
    private FavMovieHelper favMovieHelper;
    private FavMovieAdapter favMovieAdapter;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBarFavMovie);
        rvFavMovies = view.findViewById(R.id.rv_fav_movie);
        rvFavMovies.setHasFixedSize(true);
        Log.d(TAG, "onCreate: " + languageUtil.getPrefLanguage(getActivity()));
        lang = languageUtil.getPrefLanguage(getActivity());

        favMovieHelper = FavMovieHelper.getInstance(getActivity().getApplicationContext());
        favMovieHelper.open();

        if (savedInstanceState == null) {
            new LoadFavMovieAsync(favMovieHelper, this).execute();
        } else {
            arrFavMovie = savedInstanceState.getParcelableArrayList(Constant.STATE_RESULT);
            showRecyclerGrid(arrFavMovie);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void preExecute() {
        showLoading(true);
    }

    @Override
    public void postExecute(ArrayList<FavMovie> favMovies) {
        showLoading(false);
        showRecyclerGrid(favMovies);
    }

    private static class LoadFavMovieAsync extends AsyncTask<Void, Void, ArrayList<FavMovie>> {

        private final WeakReference<FavMovieHelper> weakNoteHelper;
        private final WeakReference<LoadFavMovieCallback> weakCallback;

        private LoadFavMovieAsync(FavMovieHelper noteHelper, LoadFavMovieCallback callback) {
            weakNoteHelper = new WeakReference<>(noteHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<FavMovie> doInBackground(Void... voids) {
            return weakNoteHelper.get().getAllFavMovie();
        }

        @Override
        protected void onPostExecute(ArrayList<FavMovie> favMovies) {
            super.onPostExecute(favMovies);
            weakCallback.get().postExecute(favMovies);
        }
    }

    private void showRecyclerGrid(ArrayList<FavMovie> arr) {
        rvFavMovies.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        favMovieAdapter = new FavMovieAdapter(arr, getVisibleFragment(), getActivity());
        rvFavMovies.setAdapter(favMovieAdapter);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constant.STATE_RESULT, arrFavMovie);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: rrrrrrrrrrrrrrrrrr");
        if (data != null) {
            if (requestCode == DetailActivity.REQUEST_CODE) {
                if (resultCode == DetailActivity.RESULT_CODE) {
                    int position = data.getIntExtra(Constant.EXTRA_POSITION, 0);
                    favMovieAdapter.removeItem(position);
                    Toast.makeText(getActivity(), R.string.notif_scs_delete, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(TAG, "onQueryTextChange: "+newText );
                favMovieAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
