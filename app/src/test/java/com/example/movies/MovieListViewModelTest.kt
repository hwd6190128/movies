package com.example.movies

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.movies.data.MovieRepository
import com.example.movies.ui.main.MovieAdapter
import com.example.movies.ui.main.MovieListViewModel
import com.example.movies.utils.FakeWebservice
import com.example.movies.utils.MovieFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

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
    fun `get movie list`() = runBlockingTest(testDispatcher) {

        val differ = AsyncPagingDataDiffer(
            diffCallback = MovieAdapter.MOVIE_COMPARATOR,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = testDispatcher,
            workerDispatcher = testDispatcher,
        )

        val fakeApi = FakeWebservice(fakeMovieList)
        val repo = MovieRepository(fakeApi)
        val movieViewModel = MovieListViewModel(repo)

        // submitData allows differ to receive data from PagingData, but suspends until
        // invalidation, so we must launch this in a separate job.
        val job = launch {
            movieViewModel.getMovieList().collectLatest { pagingData ->
                differ.submitData(pagingData)
            }
        }

        // Wait for initial load to finish.
        advanceUntilIdle()

        Assert.assertEquals(fakeMovieList, differ.snapshot())

        // runBlockingTest checks for leaking jobs, so we have to cancel the one we started.
        job.cancel()
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}