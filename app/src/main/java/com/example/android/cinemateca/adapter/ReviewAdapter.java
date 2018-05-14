package com.example.android.cinemateca.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.cinemateca.R;
import com.example.android.cinemateca.model.ReviewResult;

import java.util.List;

//To feed all your data to the list, you must extend the RecyclerView.Adapter class
    public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

        // context private member variable
        private Context context;
        //declare reviewList of object type TrailerResult.ResultsBean
        private List<ReviewResult.ResultsBean> reviewList;


        /**
         * TrailerAdapter constructor
         *
         * @param context, assign context
         */

        public ReviewAdapter(Context context, List<ReviewResult.ResultsBean> reviewList) {
            this.context = context;
            this.reviewList = reviewList;
        }

        //This method is called right when the adapter is created and is used to initialize the ViewHolder(s).
        //Create new views (invoked by the layout manager)
        @Override
        public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        //This method is called for each ViewHolder to bind it to the adapter. This is where we will pass our data to our ViewHolder.
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.reviewContent.setText(reviewList.get(position).getContent());
        }

        //This method returns the size of the collection that contains the items we want to display
        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            if (reviewList != null) {
                return reviewList.size();
            } else {
                return 0;
            }
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView reviewContent;

            //constructor. It takes the parent viewof the item layout allowing us to
            // setup any views we will need to use when displaying our data. Here I have just the TextView
            public ViewHolder(View itemView) {
                super(itemView);
                //de verificat in layout!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //trailerTitle nu e bine
                reviewContent = (TextView) itemView.findViewById(R.id.reviewLine);


            }
        }
    }


