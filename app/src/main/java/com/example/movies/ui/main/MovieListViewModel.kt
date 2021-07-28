package com.example.movies.ui.main

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.base.BaseViewModel
import com.example.movies.base.LiveDataEvent
import com.example.movies.data.MovieRepository
import com.example.movies.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : BaseViewModel() {

    val movieDetail = MutableLiveData<LiveDataEvent<Movie>>()
    var state: Parcelable? = null
    var prevKey: Int? = null

    fun getMovieList(prevKey: Int?): Flow<PagingData<Movie>> {
        showLoading()
        return repository.getMovieList(prevKey).cachedIn(viewModelScope)
    }

    fun onClickMovie(movie: Movie) {
        movie.apply {
            movieDetail.value = LiveDataEvent(movie)
        }
    }
}