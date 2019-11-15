package co.id.dicoding.moviecatalogueapi.helper;

import java.util.ArrayList;

import co.id.dicoding.moviecatalogueapi.model.FavMovie;

public interface LoadFavMovieCallback {
    void preExecute();
    void postExecute(ArrayList<FavMovie> notes);
}
