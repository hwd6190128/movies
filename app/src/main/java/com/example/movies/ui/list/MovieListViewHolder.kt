package com.example.movies.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.ItemMovieBinding
import com.example.movies.ui.shared.MovieViewModel

class MovieListViewHolder(binding: ItemMovieBinding, viewModel: MovieViewModel) :
    RecyclerView.ViewHolder(binding.root) {

    internal var itemMoviesBinding: ItemMovieBinding? = binding

    init {
        itemMoviesBinding?.viewModel = viewModel
    }

    companion object {
        fun create(parent: ViewGroup, viewModel: MovieViewModel): MovieListViewHolder {
            val binding = ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MovieListViewHolder(binding, viewModel)
        }
    }
}