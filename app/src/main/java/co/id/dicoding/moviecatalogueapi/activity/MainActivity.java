package co.id.dicoding.moviecatalogueapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import co.id.dicoding.moviecatalogueapi.R;
import co.id.dicoding.moviecatalogueapi.fragment.FavoriteFragment;
import co.id.dicoding.moviecatalogueapi.fragment.MovieFragment;
import co.id.dicoding.moviecatalogueapi.fragment.TvShowFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    fragment = new MovieFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(getString(R.string.tab_movie_title));
                    }

                    return true;

                case R.id.navigation_tvshow:
                    fragment = new TvShowFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(getString(R.string.tab_tv_title));
                    }

                    return true;

                case R.id.navigation_favorite:
                    fragment = new FavoriteFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(getString(R.string.tab_favorite));
                    }

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.tab_movie_title));
        }
        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_movie);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_language:
                Intent languageIntent = new Intent(this, LanguageActivity.class);
                startActivity(languageIntent);
                return true;

            case R.id.menu_setting:
                Intent configIntent = new Intent(this, ConfigActivity.class);
                startActivity(configIntent);
                return true;

        }
        return true;
    }
}
