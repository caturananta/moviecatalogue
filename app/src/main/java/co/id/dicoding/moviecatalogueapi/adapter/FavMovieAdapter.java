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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import co.id.dicoding.moviecatalogueapi.R;
import co.id.dicoding.moviecatalogueapi.activity.DetailActivity;
import co.id.dicoding.moviecatalogueapi.constant.Constant;
import co.id.dicoding.moviecatalogueapi.model.FavMovie;
import co.id.dicoding.moviecatalogueapi.util.CustomOnItemClickListener;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.FavMovieHolder> implements Filterable {

    private ArrayList<FavMovie> listFavMovie;
    private ArrayList<FavMovie> filterList;
    private Fragment mFragment;
    private Context mContext;

    public FavMovieAdapter(ArrayList<FavMovie> list, Fragment mFragment, Context context) {
        this.listFavMovie = list;
        this.filterList = list;
        this.mFragment = mFragment;
        this.mContext = context;
    }

    @NonNull
    @Override
    public FavMovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_fav_movie, viewGroup, false);
        return new FavMovieAdapter.FavMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavMovieHolder favMovieHolder, int position) {
        final FavMovie favMovie = filterList.get(position);
        Glide.with(favMovieHolder.itemView.getContext())
                .load(Constant.POSTER_URL + favMovie.getImg())
                .into(favMovieHolder.poster);

        favMovieHolder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent detailMovie = new Intent(mContext, DetailActivity.class);
                detailMovie.putExtra(Constant.EXTRA_DATA, favMovie);
                detailMovie.putExtra(Constant.EXTRA_FLAG, Constant.FLAG_FAVORITE);
                detailMovie.putExtra(Constant.EXTRA_POSITION, position);
                mFragment.startActivityForResult(detailMovie, DetailActivity.REQUEST_CODE);
            }
        }));
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
                ArrayList<FavMovie> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = listFavMovie;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterList = (ArrayList<FavMovie>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class FavMovieHolder extends RecyclerView.ViewHolder {
        ImageView poster;

        public FavMovieHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.img_fav_movie);
        }
    }

    public void removeItem(int position) {
        this.listFavMovie.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listFavMovie.size());
    }

    protected ArrayList<FavMovie> getFilteredResults(String constraint) {
        ArrayList<FavMovie> results = new ArrayList<>();

        for (FavMovie item : listFavMovie) {
            if (item.getTitle().toLowerCase().contains(constraint)) {
                results.add(item);
                Log.e("filtermovie", "getFilteredResults: " + item.getTitle());
            }
        }
        return results;
    }
}
