package com.example.movies.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @field:SerializedName("page") val page: Int,
    @field:SerializedName("results") val results: List<Movie> = emptyList(),
    @field:SerializedName("total_pages") val totalPages: Int,
    @field:SerializedName("total_results") val totalResults: Int
)