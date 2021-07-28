package com.example.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.movies.data.MoviePagingSource
import com.example.movies.data.MovieRepository
import com.example.movies.data.PAGE_SIZE
import com.example.movies.model.API_QUERY_CATEGORY
import com.example.movies.model.MovieResponse
import com.example.movies.net.API_KEY
import com.example.movies.net.Webservice
import com.example.movies.ui.main.MovieAdapter
import com.example.movies.ui.main.MovieListViewModel
import com.example.movies.utils.MovieFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule() // for test LiveData

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
    fun `click movie detail`() {
        val dummyMovie = MovieFactory().createDummyMovie()
        val movieViewModel = MovieListViewModel(Mockito.mock(MovieRepository::class.java))
        movieViewModel.onClickMovie(dummyMovie)

        val expected = dummyMovie.id
        val actual = movieViewModel.movieDetail.value?.getContentIfNotHandled()?.id
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `get movie list`() = runBlockingTest(testDispatcher) {

        // mock Webservice for create MoviePagingSource.
        val mockWebservice = Mockito.mock(Webservice::class.java)
        Mockito.`when`(mockWebservice.getMovieList(API_KEY, API_QUERY_CATEGORY)).thenReturn(
            MovieResponse(
                page = 1,
                results = fakeMovieList,
                totalPages = 1,
                totalResults = 1,
            )
        )

        // create moviePagingSource for mock MovieRepository.
        val pagingSource = MoviePagingSource(mockWebservice, API_QUERY_CATEGORY)

        // mock movieRepository for create MovieListViewModel.
        val mockRepo = Mockito.mock(MovieRepository::class.java)
        Mockito.`when`(mockRepo.getMovieListStream(API_QUERY_CATEGORY, null)).thenReturn(
            Pager(
                config = PagingConfig(
                    pageSize = PAGE_SIZE,
                    enablePlaceholders = true
                ),
                pagingSourceFactory = { pagingSource },
            ).flow
        )

        val movieViewModel = MovieListViewModel(mockRepo)

        val differ = AsyncPagingDataDiffer(
            diffCallback = MovieAdapter.MOVIE_COMPARATOR,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = testDispatcher,
            workerDispatcher = testDispatcher,
        )

        // submitData allows differ to receive data from PagingData
        val job = launch {
            movieViewModel.getMovieList().collectLatest { pagingData ->
                differ.submitData(pagingData)
            }
        }

        // Wait for initial load to finish.
        advanceUntilIdle()

        Assert.assertEquals(fakeMovieList, differ.snapshot())

        job.cancel()
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}