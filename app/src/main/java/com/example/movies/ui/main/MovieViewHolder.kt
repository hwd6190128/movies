package com.example.movies.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.ItemFooterProgressBinding
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

    companion object {
        fun create(parent: ViewGroup, viewModel: MovieListViewModel): MovieViewHolder {
            val binding = ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MovieViewHolder(binding, viewModel)
        }
    }
}