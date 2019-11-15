package co.id.dicoding.moviecatalogueapi.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/";
    private static final String QUERY_URL = "https://api.themoviedb.org/3/search/";

    public static PlaceholderAPI getApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PlaceholderAPI apiService = retrofit.create(PlaceholderAPI.class);

        return apiService;
    }

    public static PlaceholderAPI getQueryApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(QUERY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PlaceholderAPI apiService = retrofit.create(PlaceholderAPI.class);

        return apiService;
    }
}
