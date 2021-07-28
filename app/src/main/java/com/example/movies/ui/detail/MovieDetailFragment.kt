package com.example.movies.ui.detail

import com.example.movies.R
import com.example.movies.base.BaseFragment
import com.example.movies.databinding.MovieDetailFragmentBinding
import com.example.movies.ui.shared.MovieViewModel

class MovieDetailFragment : BaseFragment<MovieViewModel, MovieDetailFragmentBinding>() {

    override fun getLayoutRes() = R.layout.movie_detail_fragment

    override fun getViewModelClass() = MovieViewModel::class.java

    override fun initObserve() {
        super.initObserve()
        binding.vm = viewModel
    }
}