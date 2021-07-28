package com.example.movies.ui.main

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.base.BaseViewModel
import com.example.movies.base.LiveDataEvent
import com.example.movies.data.MovieRepository
import com.example.movies.model.API_QUERY_CATEGORY
import com.example.movies.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : BaseViewModel() {

    private var currentCategoryResult: Flow<PagingData<Movie>>? = null
    var currentCategory: String = API_QUERY_CATEGORY
    val movieDetail = MutableLiveData<LiveDataEvent<Movie>>()
    var state: Parcelable? = null
    var prevKey: Int? = null

    fun getMovieList(
        categoryStr: String = currentCategory
    ): Flow<PagingData<Movie>> {
        val lastResult = currentCategoryResult
        if (categoryStr == currentCategory && lastResult != null) {
            return lastResult
        }
        currentCategory = categoryStr
        val newResult: Flow<PagingData<Movie>> = repository.getMovieListStream(categoryStr, prevKey)
            .cachedIn(viewModelScope) // must call after map or filter operations to ensure trigger data
        currentCategoryResult = newResult
        return newResult
    }

    fun onClickMovie(movie: Movie) {
        movie.apply {
            movieDetail.value = LiveDataEvent(movie)
        }
    }
}