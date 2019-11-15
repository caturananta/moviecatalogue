package co.id.dicoding.moviecatalogueapi.api;

import co.id.dicoding.moviecatalogueapi.model.Movie;
import co.id.dicoding.moviecatalogueapi.model.TvShow;
import co.id.dicoding.moviecatalogueapi.rest.ListMovieResponse;
import co.id.dicoding.moviecatalogueapi.rest.ListTvShowResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceholderAPI {

    @GET("movie")
    Call<ListMovieResponse> getMovies(@Query("api_key") String api, @Query("language") String lang);

    @GET("tv")
    Call<ListTvShowResponse> getTvShows(@Query("api_key") String api, @Query("language") String lang);

    @GET("movie")
    Call<ListMovieResponse> getReleaseToday(@Query("api_key") String api, @Query("primary_release_date.gte") String gte, @Query("primary_release_date.lte") String lte);

    @GET("movie")
    Call<ListMovieResponse> getQueryMovies(@Query("api_key") String api, @Query("language") String lang, @Query("query") String query);

    @GET("tv")
    Call<ListTvShowResponse> getQueryTvShows(@Query("api_key") String api, @Query("language") String lang, @Query("query") String query);
}
