package com.example.movies.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movies.base.BaseRepository
import com.example.movies.model.Movie
import com.example.movies.model.PAGE_SIZE
import com.example.movies.net.Webservice
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val webservice: Webservice,
) : BaseRepository() {

    fun getMovieListStream(queryStr: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { MoviePagingSource(webservice, queryStr) },
        ).flow
    }
}