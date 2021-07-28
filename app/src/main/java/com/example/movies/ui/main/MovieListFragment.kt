package com.example.movies.ui.main

import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.base.BaseFragment
import com.example.movies.databinding.MovieListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieListFragment : BaseFragment<MovieListViewModel, MovieListFragmentBinding>() {

    override fun getLayoutRes() = R.layout.movie_list_fragment

    override fun getViewModelClass() = MovieListViewModel::class.java

    private lateinit var mAdapter: MovieAdapter

    private var job: Job? = null

    override fun init() {
        super.init()

        mBinding.vm = viewModel.apply {

            state?.let {
                (mBinding.rv.layoutManager as LinearLayoutManager).onRestoreInstanceState(it)
            }

            movieDetail.observe(viewLifecycleOwner, { event ->
                event?.getContentIfNotHandled()?.also { movie ->

                    val detailAction =
                        MovieListFragmentDirections.actionMovieListFragmentToDetailFragment(
                            title = movie.title,
                            movie = movie
                        )
                    navController.navigate(detailAction)
                }
            })
        }

        initSwipeRefresh()
        initAdapter()
        loadMovies(viewModel.prevKey)
    }

    private fun initSwipeRefresh() {
        mBinding.swipeRefresh.apply {
            setOnRefreshListener {
                mAdapter.refresh()
            }
        }
    }

    private fun initAdapter() {
        mAdapter = MovieAdapter(viewModel)
        mBinding.apply {
            rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            mAdapter.addLoadStateListener { state ->

                rv.visibility =
                    if (state.source.refresh is LoadState.NotLoading) View.VISIBLE else View.GONE
                emptyList.visibility = if (mAdapter.itemCount == 0) View.VISIBLE else View.GONE
                swipeRefresh.isRefreshing = state.source.refresh is LoadState.Loading

                detectErrorToToast(state)
            }
            rv.adapter = mAdapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter { mAdapter.retry() },
                footer = MovieLoadStateAdapter { mAdapter.retry() }
            )
        }
    }

    private fun detectErrorToToast(state: CombinedLoadStates) {
        val errorState = state.source.append as? LoadState.Error
            ?: state.source.prepend as? LoadState.Error
            ?: state.append as? LoadState.Error
            ?: state.prepend as? LoadState.Error
            ?: state.refresh as? LoadState.Error
        errorState?.let {
            Toast.makeText(
                context,
                "${it.error}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun loadMovies(prevKey: Int?) {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getMovieList(prevKey).collectLatest {
                mBinding.swipeRefresh.isRefreshing = false
                mAdapter.submitData(it)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.state = (mBinding.rv.layoutManager as LinearLayoutManager).onSaveInstanceState()
    }

}