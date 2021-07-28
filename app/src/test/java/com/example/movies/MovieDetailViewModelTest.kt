package com.example.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movies.ui.detail.MovieDetailViewModel
import com.example.movies.utils.MovieFactory
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class MovieDetailViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule() // for test LiveData

    @Test
    fun `set movie detail`() {
        val dummyMovie = MovieFactory().createDummyMovie()
        val movieViewModel = MovieDetailViewModel()
        movieViewModel.setMovie(dummyMovie)

        val expected = dummyMovie.id
        val actual = movieViewModel.movie.value?.id
        Assert.assertEquals(expected, actual)
    }

}