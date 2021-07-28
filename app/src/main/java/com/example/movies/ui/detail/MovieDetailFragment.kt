package com.example.movies.ui.detail

import androidx.navigation.fragment.navArgs
import com.example.movies.R
import com.example.movies.base.BaseFragment
import com.example.movies.databinding.MovieDetailFragmentBinding

class MovieDetailFragment : BaseFragment<MovieDetailViewModel, MovieDetailFragmentBinding>() {

    private val args: MovieDetailFragmentArgs by navArgs()

    override fun getLayoutRes() = R.layout.movie_detail_fragment

    override fun getViewModelClass() = MovieDetailViewModel::class.java

    override fun init() {
        super.init()
        mBinding.vm = viewModel.apply {
            args.movie.also { setMovie(it) }
        }
    }
}