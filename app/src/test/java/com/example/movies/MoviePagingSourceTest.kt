package com.example.movies

import androidx.paging.PagingSource
import com.example.movies.data.MoviePagingSource
import com.example.movies.model.API_QUERY_CATEGORY
import com.example.movies.utils.FakeWebservice
import com.example.movies.utils.MovieFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MoviePagingSourceTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val movieFactory = MovieFactory()
    private val fakeMovieList = listOf(
        movieFactory.createDummyMovie(),
        movieFactory.createDummyMovie()
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load returns page when get movie list successful `() = runBlockingTest(testDispatcher) {
        val fakeApi = FakeWebservice(fakeMovieList)
        val pagingSource = MoviePagingSource(fakeApi, API_QUERY_CATEGORY)

        val expected = PagingSource.LoadResult.Page(
            data = listOf(fakeMovieList[0], fakeMovieList[1]),
            prevKey = null,
            nextKey = null
        )
        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = true
            )
        )
        assertEquals(expected, actual)
    }
}