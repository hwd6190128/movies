package com.example.movies.net

import com.example.movies.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Webservice {

    @GET("/3/search/movie")
    suspend fun getMovieList(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): MovieResponse

}