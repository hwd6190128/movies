package com.example.movies.ui.main

import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.ItemMovieBinding

/**
 * @ClassName: MovieViewHolder
 * @Description:
 * @Date: 2021/7/26 00:47
 * @History:
 * <date> <version> <desc>
 */
class MovieViewHolder(binding: ItemMovieBinding, viewModel: MovieListViewModel) :
    RecyclerView.ViewHolder(binding.root) {

    internal var itemMoviesBinding: ItemMovieBinding? = binding

    init {
        itemMoviesBinding?.viewModel = viewModel
    }
}