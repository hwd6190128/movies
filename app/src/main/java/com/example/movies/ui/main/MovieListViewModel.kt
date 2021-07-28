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
import com.example.movies.net.API_QUERY_CATEGORY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : BaseViewModel() {

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<Movie>>? = null
    val movieDetail = MutableLiveData<LiveDataEvent<Movie>>()
    var state: Parcelable? = null
    var prevKey: Int? = null

    fun getMovieList(prevKey: Int?, queryStr: String = API_QUERY_CATEGORY): Flow<PagingData<Movie>> {
        val lastResult = currentSearchResult
        if (queryStr == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryStr
        showLoading()
        val newResult: Flow<PagingData<Movie>> = repository.getMovieListStream(queryStr, prevKey)
            .cachedIn(viewModelScope) // must call after map or filter operations to ensure trigger data
        currentSearchResult = newResult
        return newResult
    }

    fun onClickMovie(movie: Movie) {
        movie.apply {
            movieDetail.value = LiveDataEvent(movie)
        }
    }
}