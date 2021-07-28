package com.example.movies.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movies.model.Movie
import com.example.movies.net.API_KEY
import com.example.movies.net.Webservice
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

const val INIT_PAGE_INDEX = 1
const val PAGE_SIZE = 20

class MoviePagingSource(private val service: Webservice, private val queryStr: String) :
    PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: INIT_PAGE_INDEX
        Timber.d("hc, initkey: $position")
        return try {
            val response = service.getMovieList(API_KEY, queryStr, position)
            val results = response.results
            val nextKey = if (response.page >= response.totalPages) {
                null
            } else {
                position + 1
            }
            val prevKey = if (position == INIT_PAGE_INDEX) null else position - 1

            Timber.d("hc, loadSize: ${params.key}")
            Timber.d("hc, nextKey: $nextKey, prevKey: $prevKey")
            LoadResult.Page(data = results, prevKey = prevKey, nextKey = nextKey)
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}