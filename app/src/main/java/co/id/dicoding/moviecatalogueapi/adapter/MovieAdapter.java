package co.id.dicoding.moviecatalogueapi.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import co.id.dicoding.moviecatalogueapi.R;
import co.id.dicoding.moviecatalogueapi.activity.DetailActivity;
import co.id.dicoding.moviecatalogueapi.constant.Constant;
import co.id.dicoding.moviecatalogueapi.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviesViewHolder> implements Filterable {

    private ArrayList<Movie> listMovie;
    private ArrayList<Movie> filterList;
    private Context mContext;

    public MovieAdapter(ArrayList<Movie> list, Context context) {
        this.listMovie = list;
        this.mContext = context;
        this.filterList = list;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder movieViewHolder, int i) {
        final Movie movie = filterList.get(i);
        String errTranslate = movieViewHolder.itemView.getContext().getString(R.string.notif_err_translate);
        movieViewHolder.movieTitle.setText(movie.getTitle());
        movieViewHolder.movieYear.setText(movie.getRelease_date().split("-")[0]);
        movieViewHolder.movieRating.setText(movie.getVote_average().toString());
        movieViewHolder.movieDescr.setText(movie.getOverview().equals("") ? errTranslate : movie.getOverview());
        Glide.with(movieViewHolder.itemView.getContext())
                .load(Constant.POSTER_URL + movie.getPoster_path())
                .into(movieViewHolder.moviePoster);

        movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailMovie = new Intent(mContext, DetailActivity.class);
                detailMovie.putExtra(Constant.EXTRA_DATA, movie);
                detailMovie.putExtra(Constant.EXTRA_FLAG, Constant.FLAG_MOVIE);
                mContext.startActivity(detailMovie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Movie> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = listMovie;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterList = (ArrayList<Movie>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieTitle, movieRating, movieDescr, movieYear;

        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.img_photo);
            movieTitle = itemView.findViewById(R.id.txt_name);
            movieYear = itemView.findViewById(R.id.txt_year);
            movieRating = itemView.findViewById(R.id.txt_rating);
            movieDescr = itemView.findViewById(R.id.txt_description);
        }
    }

    protected ArrayList<Movie> getFilteredResults(String constraint) {
        ArrayList<Movie> results = new ArrayList<>();

        for (Movie item : listMovie) {
            if (item.getTitle().toLowerCase().contains(constraint)) {
                results.add(item);
                Log.e("filtermovie", "getFilteredResults: " + item.getTitle());
            }
        }
        return results;
    }
}
