package com.example.android.cinemateca.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.cinemateca.MovieDetails;
import com.example.android.cinemateca.R;
import com.example.android.cinemateca.model.MovieResult;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context mContext;
    private List<MovieResult.ResultsBean> networkResultList;
    private LayoutInflater mInflater;

    public MovieAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<MovieResult.ResultsBean> networkResultList) {
        this.networkResultList = networkResultList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = mInflater.inflate(R.layout.movie_list_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        MovieResult.ResultsBean mNetworkResult = networkResultList.get(position);
        String path = "http://image.tmdb.org/t/p/w500" + mNetworkResult.getPoster_path();

        // Load images using Picasso library
        Picasso.with(mContext)
                .load(path)
                .into(viewHolder.poster);
    }

    @Override
    public int getItemCount() {
        if (networkResultList != null) {
            return networkResultList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView poster;

        public MyViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poster);

            //add onClick for Recyclerview items
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        MovieResult.ResultsBean itemClick = networkResultList.get(position);
                        Intent intent = new Intent(mContext, MovieDetails.class);
                        intent.putExtra("original_title", networkResultList.get(position).getOriginal_title());
                        intent.putExtra("poster_path", networkResultList.get(position).getPoster_path());
                        intent.putExtra("overview", networkResultList.get(position).getOverview());
                        intent.putExtra("vote_average", Double.toString(networkResultList.get(position).getVote_average()));
                        intent.putExtra("release_date", networkResultList.get(position).getRelease_date());
                        intent.putExtra("id", networkResultList.get(position).getId());

                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}

