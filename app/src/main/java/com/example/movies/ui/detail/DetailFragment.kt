package com.example.movies.ui.detail

import androidx.navigation.fragment.navArgs
import com.example.movies.R
import com.example.movies.base.BaseFragment
import com.example.movies.databinding.DetailFragmentBinding

class DetailFragment : BaseFragment<DetailViewModel, DetailFragmentBinding>() {

    private val args: DetailFragmentArgs by navArgs()

    override fun getLayoutRes() = R.layout.detail_fragment

    override fun getViewModelClass() = DetailViewModel::class.java

    override fun init() {
        super.init()
        mBinding.vm = viewModel.apply {
            args.movie.also { setMovie(it) }
        }
    }
}