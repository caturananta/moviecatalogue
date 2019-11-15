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
import co.id.dicoding.moviecatalogueapi.model.TvShow;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewsHolder> implements Filterable {

    private ArrayList<TvShow> listTvShow;
    private ArrayList<TvShow> filterList;
    private Context mContext;

    public TvShowAdapter(ArrayList<TvShow> list, Context context) {
        this.listTvShow = list;
        this.mContext = context;
        this.filterList = list;
    }

    @NonNull
    @Override
    public TvShowViewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tv_show, viewGroup, false);
        return new TvShowAdapter.TvShowViewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewsHolder tvShowViewsHolder, int i) {
        final TvShow tvShow = filterList.get(i);
        String errTranslate = tvShowViewsHolder.itemView.getContext().getString(R.string.notif_err_translate);
        tvShowViewsHolder.tvTitle.setText(tvShow.getName());
        tvShowViewsHolder.tvYear.setText(tvShow.getFirst_air_date().split("-")[0]);
        tvShowViewsHolder.tvRating.setText(tvShow.getVote_average().toString());
        tvShowViewsHolder.tvDescr.setText(tvShow.getOverview().equals("") ? errTranslate : tvShow.getOverview());
        Glide.with(tvShowViewsHolder.itemView.getContext())
                .load(Constant.POSTER_URL + tvShow.getPoster_path())
                .into(tvShowViewsHolder.tvPoster);

        tvShowViewsHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailMovie = new Intent(mContext, DetailActivity.class);
                detailMovie.putExtra(Constant.EXTRA_DATA, tvShow);
                detailMovie.putExtra(Constant.EXTRA_FLAG, Constant.FLAG_TVSHOW);
                mContext.startActivity(detailMovie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class TvShowViewsHolder extends RecyclerView.ViewHolder {
        ImageView tvPoster;
        TextView tvTitle, tvRating, tvDescr, tvYear;

        public TvShowViewsHolder(@NonNull View itemView) {
            super(itemView);
            tvPoster = itemView.findViewById(R.id.img_tv_photo);
            tvTitle = itemView.findViewById(R.id.txt_tv_name);
            tvYear = itemView.findViewById(R.id.txt_tv_year);
            tvRating = itemView.findViewById(R.id.txt_tv_rating);
            tvDescr = itemView.findViewById(R.id.txt_tv_description);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<TvShow> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = listTvShow;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterList = (ArrayList<TvShow>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    protected ArrayList<TvShow> getFilteredResults(String constraint) {
        ArrayList<TvShow> results = new ArrayList<>();

        for (TvShow item : listTvShow) {
            if (item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
                Log.e("filtertvshow", "getFilteredResults: " + item.getName());
            }
        }
        return results;
    }
}
