package com.example.movies.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.base.BaseViewModel
import com.example.movies.model.Movie

class MovieDetailViewModel : BaseViewModel() {

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    internal fun setMovie(movie: Movie) {
        _movie.value = movie
    }
}