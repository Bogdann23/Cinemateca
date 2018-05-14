package com.example.android.cinemateca.data;

import com.example.android.cinemateca.model.MovieResult;
import com.example.android.cinemateca.model.ReviewResult;
import com.example.android.cinemateca.model.TrailerResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/popular")
        //calls asynchronous, on a background thread
    Call<MovieResult> getListOfPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page);

    @GET("movie/top_rated")
    Call<MovieResult> getListOfHRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page);

    //HTTP URI gets videos for a {movie_id) using api_key
    @GET("movie/{movie_id}/videos")
    Call<TrailerResult> getListOfVideos(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey);


    @GET("movie/{id}/reviews")
    Call<ReviewResult> getListOfReviews(
            @Path("id") int id,
            @Query("api_key") String apiKey);
}
