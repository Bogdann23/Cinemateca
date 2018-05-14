package com.example.android.cinemateca;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cinemateca.adapter.ReviewAdapter;
import com.example.android.cinemateca.adapter.TrailerAdapter;
import com.example.android.cinemateca.sqliteDb.MovieContract;
import com.example.android.cinemateca.sqliteDb.MovieDbHelper;
import com.example.android.cinemateca.data.MovieService;
import com.example.android.cinemateca.model.MovieResult;
import com.example.android.cinemateca.model.ReviewResult;
import com.example.android.cinemateca.model.TrailerResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.cinemateca.MainActivity.API_KEY;
import static com.example.android.cinemateca.MainActivity.BASE_URL;

public class MovieDetails extends AppCompatActivity {
    ImageView imageView;
    TextView movieName, plotSynopsis, voteRating, releaseDate;
    CheckBox favoriteCheckBox;
    String path = "http://image.tmdb.org/t/p/w185";
    private RecyclerView trailerRecyclerView, reviewRecyclerView;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private MovieDbHelper movieDbHelper;
    private RecyclerView.LayoutManager trailerLayoutManager;
    private RecyclerView.LayoutManager reviewLayoutManager;
    List<TrailerResult.ResultsBean> trailerList;
    List<ReviewResult.ResultsBean> reviewList;
    int movie_id;
    private MovieResult.ResultsBean favoriteMovie;
    String poster, name, plot, rating, date;
    private SQLiteDatabase mSQLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_movie);

        //access writeable method for our db
        MovieDbHelper dbHelper = new MovieDbHelper(this);
        mSQLiteDatabase = dbHelper.getWritableDatabase();

        //set data to views
        imageView = (ImageView) findViewById(R.id.movie_image);
        movieName = (TextView) findViewById(R.id.titleTemplate);
        plotSynopsis = (TextView) findViewById(R.id.DescriptionText);
        voteRating = (TextView) findViewById(R.id.ratingScore);
        releaseDate = (TextView) findViewById(R.id.releaseDate);
        favoriteCheckBox = (CheckBox) findViewById(R.id.fav);

        //The Intent object can be retrieved via the getIntent() method.
        //these is the extras needed
        Intent intent = getIntent();
        if (intent.hasExtra("original_title")) {

            poster = getIntent().getExtras().getString("poster_path");
            name = getIntent().getExtras().getString("original_title");
            plot = getIntent().getExtras().getString("overview");
            rating = getIntent().getExtras().getString("vote_average");
            date = getIntent().getExtras().getString("release_date");
            movie_id = getIntent().getExtras().getInt("id");

            //hook up data to views
            Picasso.with(this).load(path + poster).into(imageView);
            movieName.setText(name);
            plotSynopsis.setText(plot);
            voteRating.setText(rating);
            releaseDate.setText(date);
        }
        trailerRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_trailer);
        reviewRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_review);

        // use a linear layout manager
        trailerLayoutManager = new LinearLayoutManager(this);
        reviewLayoutManager = new LinearLayoutManager(this);
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);

        trailerList = new ArrayList<>();
        reviewList = new ArrayList<>();

        // specify an adapter
        trailerAdapter = new TrailerAdapter(this, trailerList);
        reviewAdapter = new ReviewAdapter(this, reviewList);
        trailerRecyclerView.setAdapter(trailerAdapter);
        reviewRecyclerView.setAdapter(reviewAdapter);

        trailerAdapter.notifyDataSetChanged();
        reviewAdapter.notifyDataSetChanged();

        //check if the movie exists in the db, if it is a favorite movie
        if (markedFavorite(name)) {
            favoriteCheckBox.setChecked(true);
            favoriteCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (favoriteCheckBox.isChecked()) {
                        saveMovieToDb();
                    } else {
                        movieDbHelper = new MovieDbHelper(MovieDetails.this);
                        movieDbHelper.deleteFavorite(movie_id);
                    }
                }
            });

        } else {
            //if movie is not in the db
            favoriteCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (favoriteCheckBox.isChecked()) {
                        saveMovieToDb();
                    } else {
                        int movie_id = getIntent().getExtras().getInt("id");
                        movieDbHelper = new MovieDbHelper(MovieDetails.this);
                        movieDbHelper.deleteFavorite(movie_id);
                    }
                }
            });

        }

        loadTrailerJSON();
        loadReviewJSON();
    }

    public boolean markedFavorite(String item) {
        //calling the columns needed from each record
        String[] projection = {
                MovieContract.FavoriteEntry._ID,
                MovieContract.FavoriteEntry.COLUMN_NAME_MOVIEID,
                MovieContract.FavoriteEntry.COLUMN_NAME_POSTER,
                MovieContract.FavoriteEntry.COLUMN_NAME_TITLE,
                MovieContract.FavoriteEntry.COLUMN_NAME_PLOT,
                MovieContract.FavoriteEntry.COLUMN_NAME_RELEASEDATE,
                MovieContract.FavoriteEntry.COLUMN_NAME_RATING
        };
        String selection = MovieContract.FavoriteEntry.COLUMN_NAME_TITLE + " =?";
        String[] selectionArgs = {item};
        String limit = "1";

        //query the db
        Cursor cursor = mSQLiteDatabase.query(MovieContract.FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null, limit);
        //if the favorite is > 0 then the movie is in the db
        boolean favorite = (cursor.getCount() > 0);
        cursor.close();
        return favorite;
    }

    //call API for the mResult videos and pass it to the mTrailerAdapter to display data
    private void loadTrailerJSON() {

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //convert Gson into JSON
                    .build();
            MovieService service = retrofit.create(MovieService.class);
            Call<TrailerResult> callTrailer = service.getListOfVideos(movie_id, API_KEY);
            callTrailer.enqueue(new Callback<TrailerResult>() {
                @Override
                public void onResponse(Call<TrailerResult> call, Response<TrailerResult> response) {
                    //populate recycler list
                    List<TrailerResult.ResultsBean> trailer = response.body().getResults();
                    trailerRecyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), trailer));
                }

                @Override
                public void onFailure(Call<TrailerResult> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MovieDetails.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    private void loadReviewJSON() {

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //convert Gson into JSON
                    .build();
            MovieService service = retrofit.create(MovieService.class);
            Call<ReviewResult> callReview = service.getListOfReviews(movie_id, API_KEY);
            callReview.enqueue(new Callback<ReviewResult>() {
                @Override
                public void onResponse(Call<ReviewResult> call, Response<ReviewResult> response) {
                    //populate recycler list
                    List<ReviewResult.ResultsBean> review = response.body().getResults();
                    reviewRecyclerView.setAdapter(new ReviewAdapter(getApplicationContext(), review));
                }

                @Override
                public void onFailure(Call<ReviewResult> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MovieDetails.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    public void saveMovieToDb() {

        MovieDbHelper movieDbHelper = new MovieDbHelper(MovieDetails.this);
        favoriteMovie = new MovieResult.ResultsBean();
        Double rating = favoriteMovie.getVote_average();
        favoriteMovie.setId(movie_id);
        favoriteMovie.setOriginal_title(name);
        favoriteMovie.setPoster_path(poster);
        favoriteMovie.setVote_average(rating);
        favoriteMovie.setRelease_date(date);
        favoriteMovie.setOverview(plot);

        movieDbHelper.addFavoriteMovie(favoriteMovie);
    }
}
