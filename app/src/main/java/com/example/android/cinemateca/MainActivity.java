package com.example.android.cinemateca;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.cinemateca.adapter.MovieAdapter;
import com.example.android.cinemateca.data.MovieService;
import com.example.android.cinemateca.model.MovieResult;
import com.example.android.cinemateca.sqliteDb.MovieDbHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static String BASE_URL = "https://api.themoviedb.org/3/";
    public static int Page = 1;
    public static final String API_KEY = "Add you own API key from  themoviedb.org";
    public static String Language = "en-US";
    private RecyclerView recyclerView;

    private List<MovieResult.ResultsBean> movieList = new ArrayList<>();

    MovieDbHelper db = new MovieDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //at start of the app the popular movies will be shown
        loadPopularMovies();
    }


    public void loadPopularMovies() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final MovieAdapter movieAdapter = new MovieAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(movieAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //convert Gson into JSON
                .build();

        MovieService movieService = retrofit.create(MovieService.class);
        //make network call
        Call<MovieResult> callPopularMovies = movieService.getListOfPopularMovies(API_KEY, Language, Page);

        callPopularMovies.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                MovieResult result = response.body();
                List<MovieResult.ResultsBean> listOfMovies = result.getResults();

                String msg = "result null: " + (result == null) + ", getListOfPopularMovies null: " + (listOfMovies == null);
                if (listOfMovies != null) {
                }
                movieAdapter.setData(listOfMovies);
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                t.printStackTrace();
                String size = (call != null ? call.toString() : null);
                Log.e("eroare", "err: " + size + ", msg: " + t.getMessage());
            }
        });
    }

    public void loadTopRatedMovies() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final MovieAdapter movieAdapter = new MovieAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(movieAdapter);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //convert Gson into JSON
                .build();

        MovieService movieService = retrofit.create(MovieService.class);
        //make network call
        Call<MovieResult> callTopRatedMovies = movieService.getListOfHRatedMovies(API_KEY, Language, Page);

        callTopRatedMovies.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                MovieResult result = response.body();
                List<MovieResult.ResultsBean> listOfMovies = result.getResults();

                String msg = "result null: " + (result == null) + ", getListOfPopularMovies null: " + (listOfMovies == null);

                if (listOfMovies != null) {
                }

                movieAdapter.setData(listOfMovies);
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                t.printStackTrace();
                String size = (call != null ? call.toString() : null);
                Log.e("eroare", "err: " + size + ", msg: " + t.getMessage());
            }
        });
    }

    private void loadFavoriteMovies() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        MovieAdapter movieAdapter = new MovieAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(movieAdapter);
        List<MovieResult.ResultsBean> listOfMovies = db.getFavoriteMoviesList();
        movieAdapter.setData(listOfMovies);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_popular:
                loadPopularMovies();
                break;
            case R.id.action_rating:
                loadTopRatedMovies();
                break;
            case R.id.action_favorite:
                loadFavoriteMovies();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        checkList();
    }

    private void checkList() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = sharedPreferences.getString(
                this.getString(R.string.pref_favorite_key),
                this.getString(R.string.pref_most_popular));

        if (sortOrder.equals(this.getString(R.string.pref_most_popular))) {
            loadPopularMovies();
        } else {
            loadFavoriteMovies();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (movieList.isEmpty()) {
            checkList();
        } else {
            checkList();
        }
    }
}






