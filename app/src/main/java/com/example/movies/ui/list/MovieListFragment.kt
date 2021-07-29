package com.example.movies.ui.list

import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.movies.R
import com.example.movies.base.BaseFragment
import com.example.movies.databinding.MovieListFragmentBinding
import com.example.movies.model.API_QUERY_CATEGORY
import com.example.movies.ui.shared.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieListFragment : BaseFragment<MovieViewModel, MovieListFragmentBinding>() {

    override fun getLayoutRes() = R.layout.movie_list_fragment

    override fun getViewModelClass() = MovieViewModel::class.java

    override fun getViewModelStoreOwner() = requireActivity()

    private lateinit var adapter: MovieListAdapter

    private var job: Job? = null

    override fun initObserve() {
        super.initObserve()

        binding.vm = viewModel.apply {

            actionToDetail.observe(viewLifecycleOwner, { event ->
                event?.getContentIfNotHandled()?.also { title ->

                    val detailAction =
                        MovieListFragmentDirections.actionMovieListFragmentToDetailFragment(title)
                    navController.navigate(detailAction)
                }
            })
        }

        initSwipeRefresh()
        initAdapter()
        loadMovies()
    }

    private fun initSwipeRefresh() {
        binding.swipeRefresh.apply {
            setOnRefreshListener {
                adapter.refresh()
            }
        }
    }

    private fun initAdapter() {
        adapter = MovieListAdapter(viewModel)
        binding.apply {
            // add divider
            rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            // add state handling
            rv.adapter = adapter.apply {
                addLoadStateListener { state ->

                    val refreshState = state.source.refresh
                    rv.visibility =
                        if (refreshState is LoadState.NotLoading) View.VISIBLE else View.GONE
                    emptyList.visibility =
                        if (adapter.itemCount == 0 || refreshState is LoadState.Error) View.VISIBLE else View.GONE
                    swipeRefresh.isRefreshing = refreshState is LoadState.Loading

                    detectErrorToToast(state)
                }
            }.withLoadStateHeaderAndFooter(
                // add loading/error panel of header and footer
                header = MovieLoadStateAdapter { adapter.retry() },
                footer = MovieLoadStateAdapter { adapter.retry() }
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
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadMovies(categoryQuery: String = API_QUERY_CATEGORY) {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getMovieList(categoryQuery).collectLatest {
                binding.swipeRefresh.isRefreshing = false
                adapter.submitData(it)
            }
        }
    }

}