package com.example.movies.utils

import com.example.movies.model.Movie
import java.util.concurrent.atomic.AtomicInteger

class MovieFactory {

    private val counter = AtomicInteger(0)

    fun createDummyMovie() = Movie(
        adult = false, backdropPath = "/A2KqRrQ0cVCFEl7Qf0YttMa3QkS.jpg",
        genreIds = listOf(28, 16, 878, 10751),
        id = counter.toInt(),
        originalLanguage = "en",
        originalTitle = "Superman ${counter.toInt()}",
        overview = "When LexCorp accidentally unleashes a murderous creature.",
        popularity = counter.toDouble(),
        posterPath = "/itvuWm7DFWWzWgW0xgiaKzzWszP.jpg",
        releaseDate = "2007-09-18",
        title = "Superman ${counter.toInt()}",
        video = false,
        voteAverage = counter.toDouble(),
        voteCount = counter.toInt()
    )
}