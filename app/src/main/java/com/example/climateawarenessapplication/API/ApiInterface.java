package com.example.climateawarenessapplication.API;

import com.example.climateawarenessapplication.Models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("articles")
    Call<News> getNews(

            @Query("country") String country
    );

    @GET("everything")
    Call<News> getNewsSearch(

        @Query("q") String keyword,
        @Query("language") String language,
        @Query("sortBy") String sortBy

    );

}
