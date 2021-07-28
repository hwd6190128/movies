package com.example.movies.utils

import com.example.movies.model.Movie
import com.example.movies.model.MovieResponse
import com.example.movies.net.Webservice


class FakeWebservice(private val results: List<Movie>) : Webservice {

    override suspend fun getMovieList(apiKey: String, query: String, page: Int): MovieResponse {
        return MovieResponse(
            page = 1,
            results = results,
            totalPages = 1,
            totalResults = 1,
        )
    }
}