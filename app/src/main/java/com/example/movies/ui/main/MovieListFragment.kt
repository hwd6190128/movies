package com.example.movies.ui.main

import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
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

            isLoading.observe(viewLifecycleOwner, { event ->
                event?.getContentIfNotHandled()?.takeIf { it }?.apply {
                    // show
                } ?:{
                    // dismiss
                }//todo

            })

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

        initAdapter()
        loadMovies(viewModel.prevKey)
    }

    private fun initAdapter() {
        mAdapter = MovieAdapter(viewModel)
        mBinding.rv.apply {
            adapter = mAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun loadMovies(prevKey: Int?) {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getMovieList(prevKey).collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.state = (mBinding.rv.layoutManager as LinearLayoutManager).onSaveInstanceState()
    }

}