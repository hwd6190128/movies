package com.example.movies.ui.shared

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
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : BaseViewModel() {

    private var currentCategoryResult: Flow<PagingData<Movie>>? = null
    private var currentCategory: String = API_QUERY_CATEGORY
    val actionToDetail = MutableLiveData<LiveDataEvent<String>>()
    var selectedMovie = MutableLiveData<Movie>()

    fun getMovieList(categoryStr: String = currentCategory): Flow<PagingData<Movie>> {
        val lastResult = currentCategoryResult
        if (categoryStr == currentCategory && lastResult != null) {
            return lastResult
        }
        currentCategory = categoryStr

        // must call cachedIn after map or filter operations to ensure trigger data.
        return repository.getMovieListStream(categoryStr).cachedIn(viewModelScope).apply {
            currentCategoryResult = this
        }
    }

    fun onClickMovie(movie: Movie) {
        selectedMovie.value = movie
        actionToDetail.value = LiveDataEvent(currentCategory)
    }
}